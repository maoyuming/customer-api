package com.duantuke.api.exception;

import com.duantuke.api.enums.ErrorEnum;

public class OpenException extends RuntimeException {
	private static final long serialVersionUID = 7613568775002494686L;
	public String message = "";
	public String code = "";

	public OpenException(String message) {
		this.message = message;
	}
	public OpenException(String message,String code) {
		this.message = message;
		this.code = code;
	}
	public OpenException(ErrorEnum enum1) {
		this.message = enum1.getName();
		this.code = enum1.getId();
	}

	
	
	
	public OpenException() {
		super();
		// TODO Auto-generated constructor stub
	}




	public OpenException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}




	public OpenException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}




	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String getMessage() {
		return this.message;
	}
}
