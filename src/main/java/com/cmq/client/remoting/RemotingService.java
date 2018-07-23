package com.cmq.client.remoting;

import com.cmq.client.exception.CMQException;

public interface RemotingService {

	RemotingResponse call(final RemotingRequest request) throws CMQException;

	void asyncCall(final RemotingRequest request, final RemotingCallback callback);
}
