package com.fangbaba.api.domain.fangcang.hotelmapping;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XmlRootElement
public class RoomTypeMappingDeleteRequest {
	@XStreamAlias("SpHotelId")
	private String SpHotelId;
	
	@XStreamAlias("RoomTypeIdList")
	private SpRoomtype SpRoomTypeId;

	public String getSpHotelId() {
		return SpHotelId;
	}

	public SpRoomtype getSpRoomTypeId() {
		return SpRoomTypeId;
	}





	public void setSpRoomTypeId(SpRoomtype spRoomTypeId) {
		SpRoomTypeId = spRoomTypeId;
	}





	public void setSpHotelId(String spHotelId) {
		SpHotelId = spHotelId;
	}


	
	
}
