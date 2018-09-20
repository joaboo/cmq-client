package com.cmq.client.common.http;

import com.cmq.client.exception.CMQException;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HttpResponse {

	private String content;
	private CMQException exception;

	public HttpResponse(String content) {
		this.content = content;
	}

	public HttpResponse(CMQException exception) {
		this.exception = exception;
	}

}
