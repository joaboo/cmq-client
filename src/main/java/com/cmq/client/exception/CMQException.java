package com.cmq.client.exception;

public class CMQException extends RuntimeException {
	private static final long serialVersionUID = 9123665563781018491L;

	public CMQException(String message) {
		super(message);
	}

	public CMQException(Throwable cause) {
		super(cause);
	}

	public CMQException(String message, Throwable cause) {
		super(message, cause);
	}
}
