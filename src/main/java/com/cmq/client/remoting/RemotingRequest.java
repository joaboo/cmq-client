package com.cmq.client.remoting;

import java.util.Map;

public interface RemotingRequest {

	String getUrl();

	Map<String, String> getParameters();

	Class<? extends RemotingResponse> getResponseType();
}
