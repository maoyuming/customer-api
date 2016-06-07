package com.fangbaba.api.domain.fangcang.hotel;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class HotelList  implements Serializable{


	@XStreamAlias("HotelInfo")
	private List<HotelInfo> hotelInfos;

	public List<HotelInfo> getHotelInfos() {
		return hotelInfos;
	}

	public void setHotelInfos(List<HotelInfo> hotelInfos) {
		this.hotelInfos = hotelInfos;
	}


}
