package com.duantuke.api.domain.common;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class OpenResponse<T> implements Serializable {
	
	@JSONField(name = "result")
	private String result; // :" "true,false ",
	@JSONField(name = "errorCode")
	private String errorCode; // ":"",
	@JSONField(name = "errorMessage")
	private String errorMessage; // ":"",

	@JSONField(name = "data")
	private T data;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
	
	
}
