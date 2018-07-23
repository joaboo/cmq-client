package com.cmq.client.core.bean;

import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubscribeResponse extends Response {

	public boolean alreadyExist() {
		return Objects.equals(getErrorCode(), SUBSCRIPTION_ALREADY_EXISTS);
	}

	public boolean notExists() {
		return Objects.equals(getErrorCode(), SUBSCRIPTION_NOT_EXISTS);
	}
}
