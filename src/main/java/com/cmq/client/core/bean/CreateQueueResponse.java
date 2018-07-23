package com.cmq.client.core.bean;

import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreateQueueResponse extends Response {

	// 队列的唯一标识 Id
	private String queueId;

	public boolean alreadyExist() {
		return Objects.equals(getErrorCode(), QUEUE_ALREADY_EXISTS);
	}

	public boolean notExists() {
		return Objects.equals(getErrorCode(), QUEUE_NOT_EXISTS);
	}

}
