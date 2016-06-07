package com.fangbaba.api.face.bean;

import java.io.Serializable;
import java.util.List;

public class RoomTypeMappingBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4032453535123624583L;

	private String SpHotelId;
	
	private List<SpRoomtype> RoomTypeList;

	public String getSpHotelId() {
		return SpHotelId;
	}

	public void setSpHotelId(String spHotelId) {
		SpHotelId = spHotelId;
	}

	public List<SpRoomtype> getRoomTypeList() {
		return RoomTypeList;
	}

	public void setRoomTypeList(List<SpRoomtype> roomTypeList) {
		RoomTypeList = roomTypeList;
	}
	
}
