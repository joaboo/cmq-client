package com.cmq.client.remoting;

public interface RemotingCallback {

	void onFailure(final Throwable t);

	void onResponse(final RemotingResponse response);
}
