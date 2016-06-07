package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("Request")
public class CancelOrderRequest extends Request {
	private static final long serialVersionUID = 4684411078587842205L;
	
	@XStreamAlias("CancelHotelOrderRequest")
	private CancelHotelOrderRequest CancelHotelOrderRequest;

	public CancelHotelOrderRequest getCancelHotelOrderRequest() {
		return CancelHotelOrderRequest;
	}

	public void setCancelHotelOrderRequest(
			CancelHotelOrderRequest cancelHotelOrderRequest) {
		CancelHotelOrderRequest = cancelHotelOrderRequest;
	}

	
	
	
	

}
