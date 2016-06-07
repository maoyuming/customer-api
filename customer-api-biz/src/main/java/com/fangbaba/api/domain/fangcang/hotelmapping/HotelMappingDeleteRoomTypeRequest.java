package com.fangbaba.api.domain.fangcang.hotelmapping;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;
@XmlRootElement
public class HotelMappingDeleteRoomTypeRequest extends Request{
	@XStreamAlias("DeleteRoomTypeMappingRequest")
    private RoomTypeMappingDeleteRequest roomTypeMappingDeleteRequest;

	public RoomTypeMappingDeleteRequest getRoomTypeMappingDeleteRequest() {
		return roomTypeMappingDeleteRequest;
	}

	public void setRoomTypeMappingDeleteRequest(RoomTypeMappingDeleteRequest roomTypeMappingDeleteRequest) {
		this.roomTypeMappingDeleteRequest = roomTypeMappingDeleteRequest;
	}

	


}
