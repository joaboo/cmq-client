package com.cmq.client.core.bean;

import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreateTopicResponse extends Response {

	// 主题的唯一标识 Id
	private String topicId;

	public boolean alreadyExist() {
		return Objects.equals(getErrorCode(), TOPIC_ALREADY_EXISTS);
	}

	public boolean notExists() {
		return Objects.equals(getErrorCode(), TOPIC_NOT_EXISTS);
	}
}
