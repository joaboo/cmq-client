package com.cmq.client.exception;

public class ClientException extends CMQException {
	private static final long serialVersionUID = -5758410930844185841L;
	private int errorCode;
	private String errorMessage;
	private String requestId;

	public ClientException(String message) {
		super(message);
		this.errorCode = -1;
		this.errorMessage = message;
		this.requestId = "";
	}

	public ClientException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = -1;
		this.errorMessage = message;
		this.requestId = "";
	}

	public ClientException(int errorCode, String errorMessage, String requestId) {
		super("errorCode:" + errorCode + ", errorMessage:" + errorMessage + ", requestId:" + requestId);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
