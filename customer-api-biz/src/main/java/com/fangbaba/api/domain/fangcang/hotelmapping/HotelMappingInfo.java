package com.fangbaba.api.domain.fangcang.hotelmapping;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("HotelInfo")
public class HotelMappingInfo implements Serializable{


	private static final long serialVersionUID = -3345482870857975727L;
	@XStreamAlias("FcHotelId")
	private Long FcHotelId;//房仓酒店id
	
	@XStreamAlias("FcHotelName")
	private String FcHotelName;//房仓酒店名称
	
	@XStreamAlias("FcRoomTypeId")
	private Long FcRoomTypeId;//房仓酒店房型id
	
	@XStreamAlias("FcRoomTypeName")
	private String FcRoomTypeName;//房仓酒店房型名称
	
	@XStreamAlias("SpHotelId")
	private String  SpHotelId;//供应商酒店id
	
	@XStreamAlias("SpRoomTypeId")
	private String  SpRoomTypeId;//供应商酒店房型id
	
	@XStreamAlias("SpRoomTypeName")
	private String  SpRoomTypeName;//供应商酒店房型名称
	
	
	public String getSpHotelId() {
		return SpHotelId;
	}
	public void setSpHotelId(String spHotelId) {
		SpHotelId = spHotelId;
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
	public Long getFcHotelId() {
		return FcHotelId;
	}
	public void setFcHotelId(Long fcHotelId) {
		FcHotelId = fcHotelId;
	}
	public void setFcRoomTypeId(Long fcRoomTypeId) {
		FcRoomTypeId = fcRoomTypeId;
	}
	

}
