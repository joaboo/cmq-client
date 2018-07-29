package com.cmq.client.config;

import java.io.Serializable;
import java.util.List;

import com.cmq.client.common.Constants;
import com.cmq.client.common.Constants.MQModel;
import com.cmq.client.consumer.MessageListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class ClientConfig implements Serializable {
	private static final long serialVersionUID = -5229501132453986291L;

	private String secretId;
	private String secretKey;
	private String signatureMethod;
	private Endpoint queueEndpoint;
	private Endpoint topicEndpoint;

	private List<ProducerConfig> producerConfigs;
	private List<ConsumerConfig> consumerConfigs;

	private long httpTimeoutInMillis = Constants.DEFAULT_HTTP_TIMEOUT;
	private int httpPoolSize = Constants.DEFAULT_HTTP_POOL_SIZE;

	public Endpoint getEndpoint(MQModel mqModel) {
		switch (mqModel) {
		case QUEUE:
			return queueEndpoint;
		case TOPIC:
			return topicEndpoint;
		default:
			return null;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class Endpoint implements Serializable {
		private static final long serialVersionUID = -5949161306916234257L;

		private String url;
		private String host;
		private String path;
		private String region;
		private String httpMethod;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class ProducerConfig implements Serializable {
		private static final long serialVersionUID = 5887421849061890108L;

		private String topicName;
		private boolean autoCreate;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class ConsumerConfig implements Serializable {
		private static final long serialVersionUID = 3187298188984768980L;

		private String queueName;
		private boolean autoCreateQueue;
		private List<Subscription> subscriptions;
		private Class<? extends MessageListener> messageListenerClass;

		private int threads = Constants.DEFAULT_CONSUMER_THREADS; // 默认单线程消费
		private int queues = Constants.DEFAULT_CONSUMER_QUEUES;
		private long pullIntervalInMillis = Constants.DEFAULT_PULL_INTERVAL; // 拉取消息间隔时间
		private int receiveNumOfMsg = Constants.DEFAULT_RECEIVE_NUM_OF_MSG; // 消费的消息数量
		private int pollingWaitSeconds = Constants.DEFAULT_POLLING_WAIT_SECONDS; // 请求的长轮询等待时间

		public ConsumerConfig(String queueName, boolean autoCreateQueue, List<Subscription> subscriptions, Class<? extends MessageListener> messageListenerClass) {
			this.queueName = queueName;
			this.autoCreateQueue = autoCreateQueue;
			this.subscriptions = subscriptions;
			this.messageListenerClass = messageListenerClass;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class Subscription implements Serializable {
		private static final long serialVersionUID = -5216243760038859990L;

		private String subscriptionName;
		private String topicName;
		private List<String> bindingKeys;
		private boolean autoCreateTopic;
		private boolean autoSubscribe;
	}

}
