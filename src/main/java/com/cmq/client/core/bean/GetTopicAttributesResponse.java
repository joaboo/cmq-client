package com.cmq.client.core.bean;

import java.util.Objects;

import com.cmq.client.common.Constants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetTopicAttributesResponse extends Response {

	private Integer msgCount;
	private Integer maxMsgSize;
	private Integer msgRetentionSeconds;
	private Long createTime;
	private Long lastModifyTime;
	private Integer filterType;

	// 主题不存在
	public boolean notExists() {
		return Objects.equals(getErrorCode(), TOPIC_NOT_EXISTS);
	}

	// 属性不匹配
	public boolean isMatch() {
		return Objects.equals(filterType, Constants.PROPERTY_FILTERTYPE_BINDINGKEY);
	}
}
