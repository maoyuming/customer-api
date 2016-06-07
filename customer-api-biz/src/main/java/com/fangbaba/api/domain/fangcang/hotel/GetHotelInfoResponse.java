package com.fangbaba.api.domain.fangcang.hotel;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XmlRootElement
public class GetHotelInfoResponse {
	
	@XStreamAlias("HotelList")
	private List<HotelInfo> hotelList;

	public List<HotelInfo> getHotelList() {
		return hotelList;
	}

	public void setHotelList(List<HotelInfo> hotelList) {
		this.hotelList = hotelList;
	}

	
	
}
