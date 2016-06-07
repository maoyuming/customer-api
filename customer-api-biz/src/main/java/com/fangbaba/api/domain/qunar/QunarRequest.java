package com.fangbaba.api.domain.qunar;

public class QunarRequest<T> {

	
	private String version;//": "1.0",
	private String hmac;//": "fa465f3fa07dc5641f8588f44c87ff49"
	private T data;
	public QunarRequest() {
		// TODO Auto-generated constructor stub
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

}
