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
public class DeleteMessageRequest extends Request {

	// 队列名
	private String queueName;
	// 上次消费返回唯一的消息句柄
	private String receiptHandle;

	public DeleteMessageRequest(ClientConfig clientConfig) {
		super(clientConfig, MQModel.QUEUE);
	}

	@Override
	protected void valid() {
		Assert.notNull(queueName, "queueName == null");
		Assert.notNull(receiptHandle, "receiptHandle == null");
	}

	@Override
	protected void addParams(final TreeMap<String, String> params) {
		put(params, "queueName", queueName);
		put(params, "receiptHandle", receiptHandle);
	}

}
