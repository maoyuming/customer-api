package com.fangbaba.api.face.bean;

import java.io.Serializable;


public class HotelMappingInfo implements Serializable{


	private static final long serialVersionUID = -3345482870857975727L;
	private Long FcHotelId;//房仓酒店id
	private String FcHotelName;//房仓酒店名称
	private Long FcRoomTypeId;//房仓酒店房型id
	private String FcRoomTypeName;//房仓酒店房型名称
	private String  SpHotelId;//供应商酒店id
	private String  SpRoomTypeId;//供应商酒店房型id
	private String  SpRoomTypeName;//供应商酒店房型名称
	public Long getFcHotelId() {
		return FcHotelId;
	}
	public void setFcHotelId(Long fcHotelId) {
		FcHotelId = fcHotelId;
	}
	public String getFcHotelName() {
		return FcHotelName;
	}
	public void setFcHotelName(String fcHotelName) {
		FcHotelName = fcHotelName;
	}
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
	public String getSpHotelId() {
		return SpHotelId;
	}
	public void setSpHotelId(String spHotelId) {
		SpHotelId = spHotelId;
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
