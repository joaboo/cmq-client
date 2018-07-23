package com.cmq.client.consumer;

import lombok.Data;

@Data
public class Message {

	// private String topicName;
	// private String queueName;
	// private String routeKey;

	// 服务器返回的消息ID
	public String msgId;
	// 消息体
	public String msgBody;
	// 每次消费唯一的消息句柄，用于删除等操作
	public String receiptHandle;
	// 消息发送到队列的时间，从 1970年1月1日 00:00:00 000 开始的毫秒数
	public long enqueueTime;
	// 消息下次可见的时间，从 1970年1月1日 00:00:00 000 开始的毫秒数
	public long nextVisibleTime;
	// 消息第一次出队列的时间，从 1970年1月1日 00:00:00 000 开始的毫秒数
	public long firstDequeueTime;
	// 出队列次数
	public int dequeueCount;
}
