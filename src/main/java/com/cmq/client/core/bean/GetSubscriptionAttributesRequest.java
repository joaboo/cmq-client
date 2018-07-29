package com.cmq.client.core.bean;

import java.util.TreeMap;

import com.cmq.client.common.Constants.MQModel;
import com.cmq.client.common.util.Assert;
import com.cmq.client.config.ClientConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetSubscriptionAttributesRequest extends Request {

	private String topicName;
	private String subscriptionName;

	public GetSubscriptionAttributesRequest(ClientConfig clientConfig) {
		super(clientConfig, MQModel.TOPIC);
	}

	@Override
	protected void valid() {
		Assert.notEmpty(topicName, "topicName == null");
		Assert.notEmpty(subscriptionName, "subscriptionName == null");
	}

	@Override
	protected void addParams(final TreeMap<String, String> params) {
		put(params, "topicName", topicName);
		put(params, "subscriptionName", subscriptionName);
	}
}
