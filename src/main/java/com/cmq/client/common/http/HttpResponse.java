package com.cmq.client.common.http;

import com.cmq.client.exception.CMQException;

import lombok.Getter;
import lombok.ToString;

@ToString
public class HttpResponse {

	@Getter
	private String content;
	@Getter
	private CMQException exception;

	public HttpResponse(String content) {
		this.content = content;
	}

	public HttpResponse(CMQException exception) {
		this.exception = exception;
	}

}
