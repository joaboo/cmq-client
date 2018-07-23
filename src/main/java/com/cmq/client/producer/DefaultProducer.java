package com.cmq.client.producer;

import java.util.concurrent.atomic.AtomicBoolean;

import com.cmq.client.core.CMQAdmin;
import com.cmq.client.core.CMQClient;
import com.cmq.client.exception.CMQException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultProducer extends CMQAdmin implements Producer {
	private final AtomicBoolean running = new AtomicBoolean(false);

	public DefaultProducer(CMQClient cmqClient) {
		super(cmqClient);
	}

	@Override
	public void start() throws CMQException {
		if (!running.compareAndSet(false, true)) {
			log.warn("Producer already running..");
			return;
		}

		// 调用父类初始化方法
		super.init();
	}

	@Override
	public void shutdown() {
		if (!running.compareAndSet(true, false)) {
			log.warn("Producer already shutdown..");
			return;
		}
	}

	@Override
	public void send(String topicName, String routingKey, String message) throws CMQException {
		cmqClient.publishMessage(topicName, routingKey, message);
	}

}
