package com.fangbaba.api.domain.fangcang.order;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class CancelHotelOrderRequest implements Serializable{
	private static final long serialVersionUID = 4684411078587842205L;
	
	@XStreamAlias("SpOrderId")
	private String spOrderId;
	@XStreamAlias("CancelReason")
	private String cancelReason;
	
	
	
	public String getSpOrderId() {
		return spOrderId;
	}
	public void setSpOrderId(String spOrderId) {
		this.spOrderId = spOrderId;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	
	
	

}
