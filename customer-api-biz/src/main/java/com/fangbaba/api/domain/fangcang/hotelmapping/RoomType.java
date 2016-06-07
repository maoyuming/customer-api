package com.fangbaba.api.domain.fangcang.hotelmapping;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("RoomType")
public class RoomType implements Serializable{


	private static final long serialVersionUID = -3345482870857975727L;
	@XStreamAlias("FcRoomTypeId")
	private Long FcRoomTypeId;//房仓酒店房型id
	@XStreamAlias("FcRoomTypeName")
	private String FcRoomTypeName;//房仓酒店房型名称

	@XStreamAlias("SpRoomTypeId")
	private String  SpRoomTypeId;//供应商酒店房型id
	
	@XStreamAlias("SpRoomTypeName")
	private String  SpRoomTypeName;//供应商酒店房型名称

	public Long getFcRoomTypeId() {
		return FcRoomTypeId;
	}

	public void setFcRoomTypeId(Long fcRoomTypeId) {
		FcRoomTypeId = fcRoomTypeId;
	}

	public String getFcRoomTypeName() {
		return FcRoomTypeName;
	}

	public void setFcRoomTypeName(String fcRoomTypeName) {
		FcRoomTypeName = fcRoomTypeName;
	}

	public String getSpRoomTypeId() {
		return SpRoomTypeId;
	}

	public void setSpRoomTypeId(String spRoomTypeId) {
		SpRoomTypeId = spRoomTypeId;
	}

	public String getSpRoomTypeName() {
		return SpRoomTypeName;
	}

	public void setSpRoomTypeName(String spRoomTypeName) {
		SpRoomTypeName = spRoomTypeName;
	}
	


}
