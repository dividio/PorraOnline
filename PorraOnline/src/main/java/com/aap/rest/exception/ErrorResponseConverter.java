package com.aap.rest.exception;


/**
 * Clase para encapsular los mensajes de error producidos por un wer service de tipo REST.
 * 
 */
public class ErrorResponseConverter {
	
	public static enum ErrorCode {
		EXITO, AVISO, ERR0R
	};

	private String message;
	private String reason;
	private ErrorCode errorCode;
	
	public ErrorResponseConverter() {
		this.message = null;
		this.reason = null;
		this.errorCode = ErrorCode.EXITO;
	}

	public ErrorResponseConverter(String message, String reason, int errorCode) {
		this.message = message;
		this.reason = reason;
		this.errorCode = ErrorCode.values()[errorCode - 1];
	}
	
	public ErrorResponseConverter(String message, String reason, ErrorCode errorCode) {
		this.message = message;
		this.reason = reason;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
}
