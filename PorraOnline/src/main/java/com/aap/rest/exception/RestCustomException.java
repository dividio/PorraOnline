package com.aap.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;


public class RestCustomException extends WebApplicationException {
	
	private static final long serialVersionUID = -5539032927185889300L;
	public static final int INFO = 1;
	public static final int WARNING = 2;
	public static final int ERROR = 3;
	
	private String reason;
	private Status status;
	private int errorCode;

	public RestCustomException(String message, String reason, Status status, int errorCode) {
		super(message);
		this.reason = reason;
		this.status = status;
		this.errorCode = errorCode;
	}

	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}