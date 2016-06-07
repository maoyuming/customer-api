package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("Request")
public class CreateOrderRequest extends Request {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6790460881622871211L;
	@XStreamAlias("CreateHotelOrderRequest")
	private CreateHotelOrderRequest createHotelOrderRequest;

	public CreateHotelOrderRequest getCreateHotelOrderRequest() {
		return createHotelOrderRequest;
	}

	public void setCreateHotelOrderRequest(CreateHotelOrderRequest createHotelOrderRequest) {
		this.createHotelOrderRequest = createHotelOrderRequest;
	}

}
