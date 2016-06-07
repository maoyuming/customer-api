package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class GetOrderStatusResponse extends Response {

	private static final long serialVersionUID = 3745247984239356668L;
	@XStreamAlias("SpOrderId")
	private String spOrderId;
	@XStreamAlias("OrderStatus")
	private String orderStatus;

	public String getSpOrderId() {
		return spOrderId;
	}

	public void setSpOrderId(String spOrderId) {
		this.spOrderId = spOrderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
