package com.cmq.client.remoting;

import java.util.Map;

public interface RemotingRequest {

	String getUrl();

	Class<? extends RemotingResponse> getResponseType();

	Map<String, String> getParameters();
}
