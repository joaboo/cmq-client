package com.cmq.client.consumer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cmq.client.common.util.NamedThreadFactory;
import com.cmq.client.config.ClientConfig.ConsumerConfig;
import com.cmq.client.core.CMQAdmin;
import com.cmq.client.core.CMQClient;
import com.cmq.client.core.ClientCallback;
import com.cmq.client.core.bean.DeleteMessageResponse;
import com.cmq.client.core.bean.ReceiveMessageResponse;
import com.cmq.client.core.bean.ReceiveMessageResponse.MsgInfo;
import com.cmq.client.exception.CMQException;
import com.cmq.client.exception.ClientException;
import com.cmq.client.remoting.RemotingResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultConsumer extends CMQAdmin implements Consumer {
	private final AtomicBoolean running = new AtomicBoolean(false);

	private ConsumerConfig consumerConfig;
	private MessageListener messageListener;

	private ScheduledExecutorService scheduledExecutor;
	private ExecutorService taskExecutor;

	public DefaultConsumer(ConsumerConfig consumerConfig, CMQClient cmqClient) {
		super(cmqClient);
		this.consumerConfig = consumerConfig;
	}

	@Override
	public void registerMessageListener(MessageListener messageListener) {
		if (running.get()) {
			log.warn("Consumer already running..");
			return;
		}
		this.messageListener = messageListener;
	}

	@Override
	public void start() throws CMQException {
		if (!running.compareAndSet(false, true)) {
			log.warn("Consumer already running..");
			return;
		}

		// 调用父类初始化方法
		super.init();

		// 获取消息任务
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutor.scheduleWithFixedDelay(new ReceiveTask(consumerConfig.getQueueName(), 16, 1), 10 * 1000L, consumerConfig.getPullIntervalInMillis(), TimeUnit.MILLISECONDS);

		// 消费消息执行
		taskExecutor = new ThreadPoolExecutor(
				consumerConfig.getThreads(),
				consumerConfig.getThreads(),
				0L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(consumerConfig.getQueues()),
				new NamedThreadFactory("CMQ-" + consumerConfig.getQueueName()),
				new CallerRunsPolicy());
	}

	@Override
	public void shutdown() {
		if (!running.compareAndSet(true, false)) {
			log.warn("Consumer already shutdown..");
			return;
		}

		scheduledExecutor.shutdown();
		taskExecutor.shutdown();
	}

	// 从 MQ Server获取消息
	class ReceiveTask implements Runnable {
		final String queueName;
		final int numOfMsg;
		final int pollingWaitSeconds;

		ReceiveTask(String queueName, int numOfMsg, int pollingWaitSeconds) {
			this.queueName = queueName;
			this.numOfMsg = numOfMsg;
			this.pollingWaitSeconds = pollingWaitSeconds;
		}

		@Override
		public void run() {
			int queues = consumerConfig.getQueues();
			int curSize = (int) ((ThreadPoolExecutor) taskExecutor).getQueue().size();
			int freeSize = queues - curSize;
			if (freeSize == 0) {
				log.debug("BlockingQueue is full");
				return;
			}
			int tmpNumOfMsg = freeSize > numOfMsg ? numOfMsg : freeSize;
			receiveMessage(queueName, tmpNumOfMsg, pollingWaitSeconds);
		}
	}

	private void execute(final String queueName, final Message message) {
		taskExecutor.execute(new ExecuteTask(queueName, message));
	}

	class ExecuteTask implements Runnable {
		final String queueName;
		final Message message;

		ExecuteTask(String queueName, Message message) {
			this.queueName = queueName;
			this.message = message;
		}

		@Override
		public void run() {
			try {
				messageListener.onMessage(message);
				deleteMessage(queueName, message.getReceiptHandle());
			} catch (Exception e) {
				log.error(String.format("Message consume error -> message:%s", message), e);
			}
		}
	}

	private void receiveMessage(final String queueName, final int numOfMsg, final int pollingWaitSeconds) {
		cmqClient.batchReceiveMessage(queueName, numOfMsg, pollingWaitSeconds, new ClientCallback() {
			@Override
			public void onFailure(Throwable t) {
				log.error(String.format("Queue(%s) receiveMessage error", queueName), t);
			}

			@Override
			public void onResponse(RemotingResponse remotingResponse) {
				ReceiveMessageResponse response = (ReceiveMessageResponse) remotingResponse;
				// 队列中没有消息
				if (response.noMessage()) {
					log.debug("No message -> Queue:{},ReceiveMessageResponse:{}", queueName, response);
					return;
				}

				// 错误返回
				if (!response.isSuccess()) {
					onFailure(new ClientException(response.getCode(), response.getMessage(), response.getRequestId()));
					return;
				}

				List<MsgInfo> msgInfoList = response.getMsgInfoList();
				for (MsgInfo msgInfo : msgInfoList) {
					execute(queueName, handleMessage(msgInfo));
				}
			}

			private Message handleMessage(final MsgInfo msgInfo) {
				final Message message = new Message();
				message.setMsgId(msgInfo.getMsgId());
				message.setMsgBody(msgInfo.getMsgBody());
				message.setReceiptHandle(msgInfo.getReceiptHandle());
				message.setEnqueueTime(msgInfo.getEnqueueTime());
				message.setNextVisibleTime(msgInfo.getNextVisibleTime());
				message.setFirstDequeueTime(msgInfo.getFirstDequeueTime());
				message.setDequeueCount(msgInfo.getDequeueCount());
				return message;
			}
		});
	}

	private void deleteMessage(String queueName, String receiptHandle) {
		cmqClient.deleteMessage(queueName, receiptHandle, new ClientCallback() {
			@Override
			public void onFailure(Throwable t) {
				log.error(String.format("Message ack error -> queueName:{},receiptHandle:{}", queueName, receiptHandle), t);
			}

			@Override
			public void onResponse(RemotingResponse remotingResponse) {
				DeleteMessageResponse response = (DeleteMessageResponse) remotingResponse;
				// 错误返回
				if (!response.isSuccess()) {
					onFailure(new ClientException(response.getCode(), response.getMessage(), response.getRequestId()));
					return;
				}
				log.debug("Message acked -> queueName:{},receiptHandle:{}", queueName, receiptHandle);
			}
		});
	}

}
