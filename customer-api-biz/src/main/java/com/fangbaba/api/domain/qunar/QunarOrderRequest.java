package com.fangbaba.api.domain.qunar;

public class QunarOrderRequest extends QunarRequest<Object>{

	private String  fromDate;//": "20120703123630",
	private String  toDate;//": "20120703123730",
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	
	
}
