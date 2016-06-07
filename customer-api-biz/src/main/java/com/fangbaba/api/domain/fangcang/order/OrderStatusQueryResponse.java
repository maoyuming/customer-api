package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class OrderStatusQueryResponse extends Response {

	private static final long serialVersionUID = 8171843103454844386L;
	@XStreamAlias("GetOrderStatusResponse")
	private GetOrderStatusResponse getOrderStatusResponse;

	public GetOrderStatusResponse getGetOrderStatusResponse() {
		return getOrderStatusResponse;
	}

	public void setGetOrderStatusResponse(GetOrderStatusResponse getOrderStatusResponse) {
		this.getOrderStatusResponse = getOrderStatusResponse;
	}

}
