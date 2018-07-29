package com.cmq.client.core.bean;

import java.util.List;
import java.util.TreeMap;

import com.cmq.client.common.Constants;
import com.cmq.client.common.Constants.MQModel;
import com.cmq.client.common.util.Assert;
import com.cmq.client.config.ClientConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubscribeRequest extends Request {

	private String topicName;
	private String subscriptionName;
	private String protocol = Constants.PROPERTY_PROTOCOL_QUEUE;
	private String endpoint;
	private String notifyStrategy = Constants.PROPERTY_NOTIFYSTRATEGY;
	private String notifyContentFormat = Constants.PROPERTY_NOTIFYCONTENTFORMAT;
	private List<String> bindingKeys;

	public SubscribeRequest(ClientConfig clientConfig) {
		super(clientConfig, MQModel.TOPIC);
	}

	@Override
	protected void valid() {
		Assert.notNull(topicName, "topicName == null");
		Assert.notNull(subscriptionName, "subscriptionName == null");
		Assert.notNull(protocol, "protocol == null");
		Assert.notNull(endpoint, "endpoint == null");
		Assert.notNull(notifyStrategy, "notifyStrategy == null");
		Assert.notNull(notifyContentFormat, "notifyContentFormat == null");
		Assert.notEmpty(bindingKeys, "bindingKeys == null");
		Assert.noNullElements(bindingKeys, "bindingKeys contain null element");
	}

	@Override
	protected void addParams(final TreeMap<String, String> params) {
		put(params, "topicName", topicName);
		put(params, "subscriptionName", subscriptionName);
		put(params, "protocol", protocol);
		put(params, "endpoint", endpoint);
		put(params, "notifyStrategy", notifyStrategy);
		put(params, "notifyContentFormat", notifyContentFormat);
		for (int i = 0; i < bindingKeys.size(); i++) {
			put(params, String.format(Constants.PROPERTY_BINDINGKEY_FORMAT, i + 1), bindingKeys.get(i));
		}
	}
}
