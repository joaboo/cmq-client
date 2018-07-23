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
public class ReceiveMessageRequest extends Request {

	// 队列名
	private String queueName;
	// 本次消费的消息数量,取值范围 1-16
	private Integer numOfMsg;
	// 本次请求的长轮询等待时间,取值范围 0-30 秒
	private Integer pollingWaitSeconds;

	public ReceiveMessageRequest(ClientConfig clientConfig) {
		super(clientConfig, MQModel.QUEUE);
	}

	@Override
	protected void valid() {
		Assert.notNull(queueName, "queueName == null");
		Assert.notNull(numOfMsg, "numOfMsg == null");
	}

	@Override
	protected void addParams(TreeMap<String, String> params) {
		put(params, "queueName", queueName);
		put(params, "numOfMsg", numOfMsg);
		put(params, "pollingWaitSeconds", pollingWaitSeconds);
	}

}
