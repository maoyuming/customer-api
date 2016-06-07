package com.fangbaba.api.domain.fangcang.hotelmapping;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;
@XmlRootElement
public class HotelMappingAddRequest extends Request{
	@XStreamAlias("AddHotelMappingRequest")
    private HotelMappingHotelRequest hotelMappingAllRequest;

	public HotelMappingHotelRequest getHotelMappingAllRequest() {
		return hotelMappingAllRequest;
	}

	public void setHotelMappingAllRequest(HotelMappingHotelRequest hotelMappingAllRequest) {
		this.hotelMappingAllRequest = hotelMappingAllRequest;
	}
	
}
