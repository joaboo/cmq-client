package com.cmq.client.remoting;

public interface RemotingResponse {

	int getCode();

	String getMessage();

	String getRequestId();

	String getErrorCode();

	void resolveErrorCode();
}
