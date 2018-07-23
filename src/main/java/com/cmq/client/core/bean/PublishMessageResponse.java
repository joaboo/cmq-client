package com.cmq.client.core.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PublishMessageResponse extends Response {

	// 服务器生成消息的唯一标识 Id
	private String msgId;

}
