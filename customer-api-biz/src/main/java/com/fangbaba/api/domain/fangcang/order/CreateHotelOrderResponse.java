package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class CreateHotelOrderResponse extends Response {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 370235323838247355L;
	@XStreamAlias("SpOrderId")
	private String spOrderId;
	@XStreamAlias("OrderStatus")
	private Integer OrderStatus;
	@XStreamAlias("FcOrderId")
	private String FcOrderId;
	
	
	
	public String getSpOrderId() {
		return spOrderId;
	}
	public void setSpOrderId(String spOrderId) {
		this.spOrderId = spOrderId;
	}
	
	public Integer getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		OrderStatus = orderStatus;
	}
	public String getFcOrderId() {
		return FcOrderId;
	}
	public void setFcOrderId(String fcOrderId) {
		FcOrderId = fcOrderId;
	}

	

}
