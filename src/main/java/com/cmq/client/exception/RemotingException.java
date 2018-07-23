package com.cmq.client.exception;

public class RemotingException extends CMQException {
	private static final long serialVersionUID = -5690687334570505110L;

	public RemotingException(String message) {
		super(message);
	}

	public RemotingException(Throwable cause) {
		super(cause);
	}

	public RemotingException(String message, Throwable cause) {
		super(message, cause);
	}
}
