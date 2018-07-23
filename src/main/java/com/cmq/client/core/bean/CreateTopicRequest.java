package com.cmq.client.core.bean;

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
public class CreateTopicRequest extends Request {

	private String topicName;
	private Integer maxMsgSize;
	private Integer filterType = Constants.PROPERTY_FILTERTYPE_BINDINGKEY;

	public CreateTopicRequest(ClientConfig clientConfig) {
		super(clientConfig, MQModel.TOPIC);
	}

	@Override
	protected void valid() {
		Assert.notEmpty(topicName, "topicName == null");
	}

	@Override
	protected void addParams(TreeMap<String, String> params) {
		put(params, "topicName", topicName);
		put(params, "maxMsgSize", maxMsgSize);
		put(params, "filterType", filterType);
	}
}
