package com.cmq.client.producer;

import com.cmq.client.exception.CMQException;

public interface Producer {

	void start() throws CMQException;

	void shutdown();

	void send(String topicName, String routingKey, String message) throws CMQException;

}
