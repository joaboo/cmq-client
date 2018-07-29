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
public class GetTopicAttributesRequest extends Request {

	private String topicName;

	public GetTopicAttributesRequest(ClientConfig clientConfig) {
		super(clientConfig, MQModel.TOPIC);
	}

	@Override
	protected void valid() {
		Assert.notEmpty(topicName, "topicName == null");
	}

	@Override
	protected void addParams(final TreeMap<String, String> params) {
		put(params, "topicName", topicName);
	}
}
