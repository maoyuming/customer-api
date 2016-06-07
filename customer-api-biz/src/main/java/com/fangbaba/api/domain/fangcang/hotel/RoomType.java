package com.fangbaba.api.domain.fangcang.hotel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("RoomType")
public class RoomType  implements Serializable{
	@XStreamAlias("FcRoomtypeId")
	private Long roomtypeId;//>481951</RoomtypeId>
	@XStreamAlias("FcRoomTypeName")
    private String roomTypeName;//>标准间</RoomTypeName>
	@XStreamAlias("FcBedType")
    private String bedType;//>1000000</BedType>
	public Long getRoomtypeId() {
		return roomtypeId;
	}
	public void setRoomtypeId(Long roomtypeId) {
		this.roomtypeId = roomtypeId;
	}
	public String getRoomTypeName() {
		return roomTypeName;
	}
	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}
	public String getBedType() {
		return bedType;
	}
	public void setBedType(String bedType) {
		this.bedType = bedType;
	}
	
	
	
      
      
}
