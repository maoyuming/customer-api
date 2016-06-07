package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class CancelOrderResponse extends Response {
	private static final long serialVersionUID = 4684411078587842205L;
	
	@XStreamAlias("CancelHotelOrderResponse")
	private CancelHotelOrderResponse CancelHotelOrderResponse;

	public CancelHotelOrderResponse getCancelHotelOrderResponse() {
		return CancelHotelOrderResponse;
	}

	public void setCancelHotelOrderResponse(
			CancelHotelOrderResponse cancelHotelOrderResponse) {
		CancelHotelOrderResponse = cancelHotelOrderResponse;
	}
	
	
	

}
