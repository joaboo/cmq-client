package com.cmq.client.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cmq.client.common.Constants;
import com.cmq.client.common.util.Assert;
import com.cmq.client.common.util.ObjectUtils;
import com.cmq.client.config.ClientConfig;
import com.cmq.client.config.ClientConfig.ConsumerConfig;
import com.cmq.client.config.ClientConfig.ProducerConfig;
import com.cmq.client.config.ClientConfig.Subscription;
import com.cmq.client.consumer.MessageListener;
import com.cmq.client.exception.CMQException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CMQConfigBean {

	private String secretId;
	private String secretKey;
	private String signatureMethod = Constants.SIGN_METHOD_SHA1;
	private URLConfigBean urlConfigBean;
	private List<ProducerConfigBean> producerConfigBeans;
	private List<ConsumerConfigBean> consumerConfigBeans;

	public void setSignatureMethod(String signatureMethod) {
		if (Constants.SIGN_METHOD_SHA1.equalsIgnoreCase(signatureMethod)) {
			this.signatureMethod = Constants.SIGN_METHOD_SHA1;
		} else if (Constants.SIGN_METHOD_SHA256.equalsIgnoreCase(signatureMethod)) {
			this.signatureMethod = Constants.SIGN_METHOD_SHA256;
		} else {
			this.signatureMethod = signatureMethod;
		}
	}

	public void addProducerConfigBean(ProducerConfigBean producerConfigBean) {
		if (producerConfigBeans == null) {
			producerConfigBeans = new ArrayList<>();
		}
		producerConfigBeans.add(producerConfigBean);
	}

	public void addConsumerConfigBean(ConsumerConfigBean consumerConfigBean) {
		if (consumerConfigBeans == null) {
			consumerConfigBeans = new ArrayList<>();
		}
		consumerConfigBeans.add(consumerConfigBean);
	}

	public void valid() throws CMQException {
		Assert.notEmpty(secretId, "secretId == null");
		Assert.notEmpty(secretKey, "secretKey == null");
		Assert.notEmpty(signatureMethod, "signatureMethod == null");
		Assert.isTrue(Constants.SIGN_METHOD_SHA1.equals(signatureMethod) || Constants.SIGN_METHOD_SHA256.equals(signatureMethod), "Unsupported signatureMethod : " + signatureMethod);

		Assert.notNull(urlConfigBean, "urlConfigBean == null");
		urlConfigBean.valid();

		if (ObjectUtils.isNotEmpty(producerConfigBeans)) {
			for (ProducerConfigBean producerConfigBean : producerConfigBeans) {
				Assert.notNull(producerConfigBean, "producerConfigBean == null");
				producerConfigBean.valid();
			}
		}

		if (ObjectUtils.isNotEmpty(consumerConfigBeans)) {
			for (ConsumerConfigBean consumerConfigBean : consumerConfigBeans) {
				Assert.notNull(consumerConfigBean, "consumerConfigBean == null");
				consumerConfigBean.valid();
			}
		}
	}

	public ClientConfig getClientConfig() throws CMQException {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setSecretId(secretId);
		clientConfig.setSecretKey(secretKey);
		clientConfig.setSignatureMethod(signatureMethod);
		clientConfig.setQueueEndpoint(urlConfigBean.getQueueEndpoint());
		clientConfig.setTopicEndpoint(urlConfigBean.getTopicEndpoint());
		if (ObjectUtils.isNotEmpty(producerConfigBeans)) {
			List<ProducerConfig> producerConfigs = producerConfigBeans.stream().map(ProducerConfigBean::getProducerConfig).collect(Collectors.toList());
			clientConfig.setProducerConfigs(producerConfigs);
		}
		if (ObjectUtils.isNotEmpty(consumerConfigBeans)) {
			List<ConsumerConfig> consumerConfigs = consumerConfigBeans.stream().map(ConsumerConfigBean::getConsumerConfig).collect(Collectors.toList());
			clientConfig.setConsumerConfigs(consumerConfigs);
		}
		return clientConfig;
	}

	@Data
	@NoArgsConstructor
	public static class URLConfigBean {
		private String protocol = Constants.PROTOCOL_HTTP; // 协议
		private String region; // 地域
		private boolean outerNet = false; // 是否外网
		private String path = Constants.ENDPOINT_PATH; // 请求路径

		public URLConfigBean(String region) {
			this.region = region;
		}

		public URLConfigBean(String region, boolean outerNet) {
			this.region = region;
			this.outerNet = outerNet;
		}

		public URLConfigBean(String protocol, String region) {
			this.region = region;
			this.setProtocol(protocol);
		}

		public void setProtocol(String protocol) {
			if (Constants.PROTOCOL_HTTP.equalsIgnoreCase(protocol)) {
				this.protocol = Constants.PROTOCOL_HTTP;
			} else if (Constants.PROTOCOL_HTTPS.equalsIgnoreCase(protocol)) {
				this.protocol = Constants.PROTOCOL_HTTPS;
			} else {
				this.protocol = protocol;
			}
		}

		public ClientConfig.Endpoint getTopicEndpoint() throws CMQException {
			return getEndpoint(outerNet ? Constants.ENDPOINT_TOPIC_OUTER : Constants.ENDPOINT_TOPIC_INNER);
		}

		public ClientConfig.Endpoint getQueueEndpoint() throws CMQException {
			return getEndpoint(outerNet ? Constants.ENDPOINT_QUEUE_OUTER : Constants.ENDPOINT_QUEUE_INNER);
		}

		private ClientConfig.Endpoint getEndpoint(String hostFormat) {
			String host = String.format(hostFormat, region);
			String url = protocol + "://" + host + path;
			ClientConfig.Endpoint endpoint = new ClientConfig.Endpoint();
			endpoint.setHost(host);
			endpoint.setPath(path);
			endpoint.setUrl(url);
			endpoint.setRegion(region);
			endpoint.setHttpMethod(Constants.HTTP_METHOD_POST);
			return endpoint;
		}

		public void valid() throws CMQException {
			Assert.notEmpty(protocol, "protocol == null");
			Assert.isTrue(Constants.PROTOCOL_HTTP.equals(protocol) || Constants.PROTOCOL_HTTPS.equals(protocol), "Unsupported protocol : " + protocol);
			Assert.notEmpty(region, "region == null");
			Assert.notNull(path, "path == null");
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProducerConfigBean {
		private String topicName;
		private boolean autoCreate = false;

		public ProducerConfigBean(String topicName) {
			this.topicName = topicName;
		}

		public ClientConfig.ProducerConfig getProducerConfig() throws CMQException {
			return new ClientConfig.ProducerConfig(topicName, autoCreate);
		}

		public void valid() throws CMQException {
			Assert.notEmpty(topicName, "topicName == null");
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ConsumerConfigBean {
		private String queueName;
		private List<SubscriptionBean> subscriptionBeans;
		private boolean autoCreateQueue = false;
		private Class<? extends MessageListener> messageListenerClass;

		public ConsumerConfigBean(String queueName, Class<? extends MessageListener> messageListenerClass) {
			this.queueName = queueName;
			this.messageListenerClass = messageListenerClass;
		}

		public ClientConfig.ConsumerConfig getConsumerConfig() throws CMQException {
			List<Subscription> subscriptions = null;
			if (ObjectUtils.isNotEmpty(subscriptionBeans)) {
				subscriptions = subscriptionBeans.stream().map(SubscriptionBean::getSubscription).collect(Collectors.toList());
			}
			return new ClientConfig.ConsumerConfig(queueName, autoCreateQueue, subscriptions, messageListenerClass);
		}

		public void valid() throws CMQException {
			Assert.notEmpty(queueName, "queueName == null");
			if (ObjectUtils.isNotEmpty(subscriptionBeans)) {
				for (SubscriptionBean subscriptionBean : subscriptionBeans) {
					Assert.notNull(subscriptionBean, "subscriptionBean == null");
					subscriptionBean.valid();
				}
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SubscriptionBean {
		private String subscriptionName;
		private String topicName;
		private List<String> bindingKeys;

		private boolean autoCreateTopic;
		private boolean autoSubscribe;

		public SubscriptionBean(String subscriptionName, String topicName, List<String> bindingKeys) {
			this.subscriptionName = subscriptionName;
			this.topicName = topicName;
			this.bindingKeys = bindingKeys;
		}

		public ClientConfig.Subscription getSubscription() throws CMQException {
			return new ClientConfig.Subscription(subscriptionName, topicName, bindingKeys, autoCreateTopic, autoSubscribe);
		}

		public void valid() throws CMQException {
			Assert.notEmpty(subscriptionName, "subscriptionName == null");
			Assert.notEmpty(topicName, "topicName == null");
			Assert.notEmpty(bindingKeys, "bindingKeys == null");
		}
	}
}