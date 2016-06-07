package com.fangbaba.api.domain.fangcang.hotelmapping;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class HotelMappingRoomtypeRequest {
	@XStreamAlias("FcHotelId")
	private  String FcHotelId;
	@XStreamAlias("SpHotelId")
	private String SpHotelId;
	@XStreamAlias("RoomTypeList")
	private List<RoomType> RoomTypeList;

	public List<RoomType> getRoomTypeList() {
		return RoomTypeList;
	}

	public void setRoomTypeList(List<RoomType> roomTypeList) {
		RoomTypeList = roomTypeList;
	}

	public String getSpHotelId() {
		return SpHotelId;
	}

	public void setSpHotelId(String spHotelId) {
		SpHotelId = spHotelId;
	}

	public String getFcHotelId() {
		return FcHotelId;
	}

	public void setFcHotelId(String fcHotelId) {
		FcHotelId = fcHotelId;
	}
	
	

}
