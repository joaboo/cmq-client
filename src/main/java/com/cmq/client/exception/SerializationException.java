package com.cmq.client.exception;

public class SerializationException extends CMQException {
	private static final long serialVersionUID = -7548853016904720141L;

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(Throwable cause) {
		super(cause);
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
