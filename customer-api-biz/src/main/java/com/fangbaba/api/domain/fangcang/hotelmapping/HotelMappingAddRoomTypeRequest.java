package com.fangbaba.api.domain.fangcang.hotelmapping;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;
@XmlRootElement
public class HotelMappingAddRoomTypeRequest extends Request{
	@XStreamAlias("AddRoomTypeMappingRequest")
    private HotelMappingRoomtypeRequest hotelMappingRoomtypeRequest;

	public HotelMappingRoomtypeRequest getHotelMappingRoomtypeRequest() {
		return hotelMappingRoomtypeRequest;
	}

	public void setHotelMappingRoomtypeRequest(HotelMappingRoomtypeRequest hotelMappingRoomtypeRequest) {
		this.hotelMappingRoomtypeRequest = hotelMappingRoomtypeRequest;
	}

	

}
