package com.fangbaba.api.domain.fangcang.hotel;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;


@XmlRootElement
public class HotelInfoRequest extends Request{
	@XStreamAlias("GetHotelInfoRequest")
    private GetHotelInfoRequest getHotelInfoRequest;

	public GetHotelInfoRequest getGetHotelInfoRequest() {
		return getHotelInfoRequest;
	}

	public void setGetHotelInfoRequest(GetHotelInfoRequest getHotelInfoRequest) {
		this.getHotelInfoRequest = getHotelInfoRequest;
	}
}
