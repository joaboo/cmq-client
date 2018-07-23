package com.cmq.client.core.bean;

import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReceiveMessageResponse extends Response {

	// 信息列表
	private List<MsgInfo> msgInfoList;

	public boolean noMessage() {
		return Objects.equals(getErrorCode(), NO_MESSAGE);
	}

	public boolean tooManyUnacked() {
		return Objects.equals(getErrorCode(), TOO_MANY_UNACKED);
	}

	@Data
	@ToString
	public static class MsgInfo {
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
}
