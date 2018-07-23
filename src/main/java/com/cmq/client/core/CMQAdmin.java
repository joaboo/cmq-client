package com.cmq.client.core;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cmq.client.config.ClientConfig;
import com.cmq.client.config.ClientConfig.ConsumerConfig;
import com.cmq.client.config.ClientConfig.ProducerConfig;
import com.cmq.client.config.ClientConfig.Subscription;
import com.cmq.client.core.bean.CreateQueueResponse;
import com.cmq.client.core.bean.CreateTopicResponse;
import com.cmq.client.core.bean.GetQueueAttributesResponse;
import com.cmq.client.core.bean.GetSubscriptionAttributesResponse;
import com.cmq.client.core.bean.GetTopicAttributesResponse;
import com.cmq.client.core.bean.SubscribeResponse;
import com.cmq.client.exception.CMQException;
import com.cmq.client.exception.ClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CMQAdmin {
	private static final AtomicBoolean inited = new AtomicBoolean(false);

	protected final ClientConfig clientConfig;
	protected final CMQClient cmqClient;

	protected CMQAdmin(CMQClient cmqClient) {
		this.cmqClient = cmqClient;
		this.clientConfig = cmqClient.getClientConfig();
	}

	protected void init() throws CMQException {
		if (!inited.compareAndSet(false, true)) {
			log.warn("Admin already inited..");
			return;
		}

		List<ProducerConfig> producerConfigs = clientConfig.getProducerConfigs();
		if (producerConfigs != null) {
			for (ProducerConfig producerConfig : producerConfigs) {
				// 创建主题
				if (producerConfig.isAutoCreate()) {
					initTopic(producerConfig.getTopicName());
				}
			}
		}

		List<ConsumerConfig> consumerConfigs = clientConfig.getConsumerConfigs();
		if (consumerConfigs != null) {
			for (ConsumerConfig consumerConfig : consumerConfigs) {
				// 创建队列
				if (consumerConfig.isAutoCreateQueue()) {
					initQueue(consumerConfig.getQueueName());
				}

				List<Subscription> subscriptions = consumerConfig.getSubscriptions();
				if (subscriptions != null) {
					for (Subscription subscription : subscriptions) {
						// 创建主题
						if (subscription.isAutoCreateTopic()) {
							initTopic(subscription.getTopicName());
						}
						// 创建订阅
						if (subscription.isAutoSubscribe()) {
							initSubscribe(subscription.getSubscriptionName(), subscription.getTopicName(), consumerConfig.getQueueName(), subscription.getBindingKeys());
						}
					}
				}
			}
		}
	}

	private void initQueue(String queueName) {
		GetQueueAttributesResponse response = getQueueAttributes(queueName);
		if (response == null) {
			createQueue(queueName);
			return;
		}
		// 判断属性匹配
		if (response.isMatch()) {
			log.info("Queue is already existed -> GetQueueAttributesResponse:{}", response);
		} else {
			throw new ClientException(String.format("Queue already exists, but the attributes not match -> GetQueueAttributesResponse:%s", response));
		}
	}

	private void initTopic(String topicName) {
		GetTopicAttributesResponse response = getTopicAttributes(topicName);
		if (response == null) {
			createTopic(topicName);
			return;
		}
		// 判断属性匹配
		if (response.isMatch()) {
			log.info("Topic is already existed -> GetTopicAttributesResponse:{}", response);
		} else {
			throw new ClientException(String.format("Topic already exists, but the attributes not match -> GetTopicAttributesResponse:%s", response));
		}
	}

	private void initSubscribe(String subscriptionName, String topicName, String queueName, List<String> bindingKeys) {
		GetSubscriptionAttributesResponse response = getSubscriptionAttributes(topicName, subscriptionName);
		if (response == null) {
			subscribe(subscriptionName, topicName, queueName, bindingKeys);
			return;
		}
		// 判断属性匹配
		if (response.isMatch(queueName, bindingKeys)) {
			log.info("Subscription is already existed -> GetSubscriptionAttributesResponse:{}", response);
		} else {
			throw new ClientException(String.format("Subscription already exists, but the attributes not match -> GetSubscriptionAttributesResponse:%s", response));
		}
	}

	protected GetQueueAttributesResponse getQueueAttributes(String queueName) throws CMQException {
		GetQueueAttributesResponse response = cmqClient.getQueueAttributes(queueName);
		if (response.isSuccess()) {
			log.info("Get queue attributes success -> GetQueueAttributesResponse:{}", response);
			return response;
		} else if (response.notExists()) {
			log.info("Queue not exists -> GetQueueAttributesResponse:{}", response);
			return null;
		} else {
			throw new ClientException(response.getCode(), response.getMessage(), response.getRequestId());
		}
	}

	protected GetTopicAttributesResponse getTopicAttributes(String topicName) throws CMQException {
		GetTopicAttributesResponse response = cmqClient.getTopicAttributes(topicName);
		if (response.isSuccess()) {
			log.info("Get topic attributes success -> GetTopicAttributesResponse:{}", response);
			return response;
		} else if (response.notExists()) {
			log.info("Topic not exists -> GetTopicAttributesResponse:{}", response);
			return null;
		} else {
			throw new ClientException(response.getCode(), response.getMessage(), response.getRequestId());
		}
	}

	protected GetSubscriptionAttributesResponse getSubscriptionAttributes(String topicName, String subscriptionName) throws CMQException {
		GetSubscriptionAttributesResponse response = cmqClient.getSubscriptionAttributes(topicName, subscriptionName);
		if (response.isSuccess()) {
			log.info("Get subscription attributes success -> GetSubscriptionAttributesResponse:{}", response);
			return response;
		} else if (response.notExists()) {
			log.info("Subscription not exists -> GetSubscriptionAttributesResponse:{}", response);
			return null;
		} else {
			throw new ClientException(response.getCode(), response.getMessage(), response.getRequestId());
		}
	}

	protected void createQueue(String queueName) throws CMQException {
		CreateQueueResponse response = cmqClient.createQueue(queueName);
		if (response.isSuccess()) {
			log.info("Queue create success -> CreateQueueResponse:{}", response);
		} else if (response.alreadyExist()) {
			log.info("Queue already exists -> CreateQueueResponse:{}", response);
		} else {
			throw new ClientException(response.getCode(), response.getMessage(), response.getRequestId());
		}
	}

	protected void createTopic(String topicName) throws CMQException {
		CreateTopicResponse response = cmqClient.createTopic(topicName);
		if (response.isSuccess()) {
			log.info("Topic create success -> CreateTopicResponse:{}", response);
		} else if (response.alreadyExist()) {
			log.info("Topic already exists -> CreateTopicResponse:{}", response);
		} else {
			throw new ClientException(response.getCode(), response.getMessage(), response.getRequestId());
		}
	}

	protected void subscribe(String subscriptionName, String topicName, String queueName, List<String> bindingKeys) throws CMQException {
		SubscribeResponse response = cmqClient.subscribe(subscriptionName, queueName, topicName, bindingKeys);
		if (response.isSuccess()) {
			log.info("Subscription create success -> SubscribeResponse:{}", response);
		} else if (response.alreadyExist()) {
			log.info("Subscription already exists -> SubscribeResponse:{}", response);
		} else {
			throw new ClientException(response.getCode(), response.getMessage(), response.getRequestId());
		}
	}
}
