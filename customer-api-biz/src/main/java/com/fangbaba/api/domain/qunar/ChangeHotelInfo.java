package com.fangbaba.api.domain.qunar;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XmlRootElement
public class ChangeHotelInfo implements Serializable{

	private static final long serialVersionUID = 544886478965751033L;
	@XStreamAsAttribute
	private List<HotelChange> hotels;

	public List<HotelChange> getHotels() {
		return hotels;
	}

	public void setHotels(List<HotelChange> hotels) {
		this.hotels = hotels;
	}
	
}
