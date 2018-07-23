package com.cmq.client.core.bean;

import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetQueueAttributesResponse extends Response {

	private Integer maxMsgHeapNum;
	private Integer pollingWaitSeconds;
	private Integer visibilityTimeout;
	private Integer maxMsgSize;
	private Integer msgRetentionSeconds;
	private Long createTime;
	private Long lastModifyTime;
	private Integer activeMsgNum;
	private Integer inactiveMsgNum;
	private Integer rewindSeconds;
	private Long rewindmsgNum;
	private Long minMsgTime;

	// 主题不存在
	public boolean notExists() {
		return Objects.equals(getErrorCode(), QUEUE_NOT_EXISTS);
	}

	// 属性不匹配
	public boolean isMatch() {
		return true;
	}
}
