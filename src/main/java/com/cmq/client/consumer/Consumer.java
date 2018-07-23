package com.cmq.client.consumer;

import com.cmq.client.exception.CMQException;

public interface Consumer {

	void start() throws CMQException;

	void shutdown();

	void registerMessageListener(final MessageListener messageListener);

}
