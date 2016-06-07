package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class CreateOrderResponse extends Response {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 213728735826822111L;
	@XStreamAlias("CreateHotelOrderResponse")
	private CreateHotelOrderResponse createHotelOrderResponse;
	public CreateHotelOrderResponse getCreateHotelOrderResponse() {
		return createHotelOrderResponse;
	}
	public void setCreateHotelOrderResponse(CreateHotelOrderResponse createHotelOrderResponse) {
		this.createHotelOrderResponse = createHotelOrderResponse;
	}

	

}
