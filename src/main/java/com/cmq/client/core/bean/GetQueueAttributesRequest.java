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
public class GetQueueAttributesRequest extends Request {

	private String queueName;

	public GetQueueAttributesRequest(ClientConfig clientConfig) {
		super(clientConfig, MQModel.QUEUE);
	}

	@Override
	protected void valid() {
		Assert.notEmpty(queueName, "queueName == null");
	}

	@Override
	protected void addParams(TreeMap<String, String> params) {
		put(params, "queueName", queueName);
	}
}
