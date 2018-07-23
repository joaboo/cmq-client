package com.cmq.client.core.bean;

import com.cmq.client.common.util.ObjectUtils;
import com.cmq.client.remoting.RemotingResponse;

import lombok.Data;

@Data
public abstract class Response implements RemotingResponse {
	protected static final int SUCCESS = 0;

	protected static final String TOPIC_NOT_EXISTS = "10600"; // Topic不存在，或者已经被删除了
	protected static final String TOPIC_ALREADY_EXISTS = "10550"; // 同一帐号下存在同名Topic

	protected static final String SUBSCRIPTION_NOT_EXISTS = "10560"; // 订阅不存在
	protected static final String SUBSCRIPTION_ALREADY_EXISTS = "10470"; // 同一个账户的同一个Topic下同名订阅已经存在

	protected static final String QUEUE_NOT_EXISTS = "10100"; // Queue不存在，或者已经被删除了
	protected static final String QUEUE_ALREADY_EXISTS = "10210"; // 同一帐号下存在同名Queue

	protected static final String NO_MESSAGE = "10200"; // 队列没有消息
	protected static final String TOO_MANY_UNACKED = "10690"; // 队列中有太多不可见或者延时消息

	// 错误码
	private int code;
	// 错误信息
	private String message;
	// 模块错误码
	private String errorCode;
	// 服务器请求Id
	private String requestId;

	public boolean isSuccess() {
		return getCode() == SUCCESS;
	}

	public void resolveErrorCode() {
		if (ObjectUtils.isEmpty(message)) {
			return;
		}
		int begin = message.indexOf('(');
		if (begin < 0) {
			return;
		}
		int end = message.indexOf(')', begin);
		if (end < 0) {
			return;
		}
		errorCode = message.substring(begin + 1, end);
	}

}
