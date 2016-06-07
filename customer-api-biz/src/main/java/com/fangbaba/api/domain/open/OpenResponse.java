package com.fangbaba.api.domain.open;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class OpenResponse<T> implements Serializable {
	
	@JSONField(name = "result")
	private String result; // :" "true,false ",
	@JSONField(name = "errorcode")
	private String errorcode; // ":"",
	@JSONField(name = "errormessage")
	private String errormessage; // ":"",

	@JSONField(name = "data")
	private T data;

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrormessage() {
		return errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
	
	
}
