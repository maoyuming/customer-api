package com.fangbaba.api.domain.fangcang.hotelmapping;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;
@XmlRootElement
public class DeteleHotelMappingRequest extends Request{
	@XStreamAlias("DeleteHotelMappingRequest")
    private HotelMappingHotelRequest addHotelMappingRequest;

	public HotelMappingHotelRequest getAddHotelMappingRequest() {
		return addHotelMappingRequest;
	}

	public void setAddHotelMappingRequest(HotelMappingHotelRequest addHotelMappingRequest) {
		this.addHotelMappingRequest = addHotelMappingRequest;
	}
}
