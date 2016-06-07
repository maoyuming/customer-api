package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.fangbaba.api.domain.fangcang.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class CancelHotelOrderResponse extends Response {
	private static final long serialVersionUID = 4684411078587842205L;
	
	@XStreamAlias("SpOrderId")
	private String spOrderId;
	@XStreamAlias("CancelStatus")
	private String CancelStatus;
	
	
	
	public String getSpOrderId() {
		return spOrderId;
	}
	public void setSpOrderId(String spOrderId) {
		this.spOrderId = spOrderId;
	}
	public String getCancelStatus() {
		return CancelStatus;
	}
	public void setCancelStatus(String cancelStatus) {
		CancelStatus = cancelStatus;
	}
	

}
