package com.fangbaba.api.domain.fangcang.hotel;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XmlRootElement
public class GetHotelInfoRequest {
	@XStreamAlias("FcHotelIds")
    private String fcHotelIds;

	public String getFcHotelIds() {
		return fcHotelIds;
	}

	public void setFcHotelIds(String fcHotelIds) {
		this.fcHotelIds = fcHotelIds;
	}
	
	
}
