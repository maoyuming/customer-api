package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("Request")
public class OrderStatusQueryRequest extends Request {
	private static final long serialVersionUID = 4684411078587842205L;
	
	@XStreamAlias("GetOrderStatusRequest")
	private GetOrderStatusRequest getOrderStatusRequest;

	public GetOrderStatusRequest getGetOrderStatusRequest() {
		return getOrderStatusRequest;
	}

	public void setGetOrderStatusRequest(GetOrderStatusRequest getOrderStatusRequest) {
		this.getOrderStatusRequest = getOrderStatusRequest;
	}


}
