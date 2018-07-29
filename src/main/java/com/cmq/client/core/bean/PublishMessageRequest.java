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
public class PublishMessageRequest extends Request {

	private String topicName; // 主题名
	private String routingKey; // 消息路由键
	private String msgBody; // 消息体

	public PublishMessageRequest(ClientConfig clientConfig) {
		super(clientConfig, MQModel.TOPIC);
	}

	@Override
	protected void valid() {
		Assert.notNull(topicName, "topicName == null");
		Assert.notNull(msgBody, "msgBody == null");
		Assert.notNull(routingKey, "routingKey == null");
	}

	@Override
	protected void addParams(final TreeMap<String, String> params) {
		put(params, "topicName", topicName);
		put(params, "msgBody", msgBody);
		put(params, "routingKey", routingKey);
	}
}
