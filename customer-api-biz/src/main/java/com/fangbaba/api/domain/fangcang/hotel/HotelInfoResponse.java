package com.fangbaba.api.domain.fangcang.hotel;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class HotelInfoResponse extends Response {
	@XStreamAlias("GetHotelInfoResponse")
	private GetHotelInfoResponse getHotelInfoResponse;

	public GetHotelInfoResponse getGetHotelInfoResponse() {
		return getHotelInfoResponse;
	}

	public void setGetHotelInfoResponse(GetHotelInfoResponse getHotelInfoResponse) {
		this.getHotelInfoResponse = getHotelInfoResponse;
	}
	
	
}
