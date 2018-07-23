package com.cmq.client.core.bean;

import java.util.List;
import java.util.Objects;

import com.cmq.client.common.Constants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetSubscriptionAttributesResponse extends Response {

	private String topicOwner;
	private Integer msgCount;
	private String protocol;
	private String endpoint;
	private String notifyStrategy;
	private String notifyContentFormat;
	private Long createTime;
	private Long lastModifyTime;
	private List<String> bindingKey;
	private List<String> filterTags;

	// 主题不存在
	public boolean notExists() {
		return Objects.equals(getErrorCode(), SUBSCRIPTION_NOT_EXISTS);
	}

	// 属性不匹配
	public boolean isMatch(String queueName, List<String> bindingKeys) {
		return Objects.equals(protocol, Constants.PROPERTY_PROTOCOL_QUEUE)
				|| Objects.equals(notifyStrategy, Constants.PROPERTY_NOTIFYSTRATEGY)
				|| Objects.equals(notifyContentFormat, Constants.PROPERTY_NOTIFYCONTENTFORMAT)
				|| Objects.equals(endpoint, queueName);
	}
}
