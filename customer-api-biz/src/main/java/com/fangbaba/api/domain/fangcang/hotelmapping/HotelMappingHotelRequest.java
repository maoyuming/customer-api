package com.fangbaba.api.domain.fangcang.hotelmapping;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class HotelMappingHotelRequest {
	@XStreamAlias("HotelList")
	private List<HotelMappingInfo> hotelMappingInfos;
	public List<HotelMappingInfo> getHotelMappingInfos() {
		return hotelMappingInfos;
	}
	public void setHotelMappingInfos(List<HotelMappingInfo> hotelMappingInfos) {
		this.hotelMappingInfos = hotelMappingInfos;
	}

}
