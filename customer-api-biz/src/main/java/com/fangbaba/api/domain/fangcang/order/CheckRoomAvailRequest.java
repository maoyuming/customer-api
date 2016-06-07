package com.fangbaba.api.domain.fangcang.order;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author he
 */
@XmlRootElement
public class CheckRoomAvailRequest implements Serializable{
	private static final long serialVersionUID = -4189890527246412977L;
	@XStreamAlias("SpHotelId")
    private String spHotelId;
	@XStreamAlias("SpRoomTypeId")
	private String spRoomTypeId;
	@XStreamAlias("SpRatePlanId")
	private String spRatePlanId;
	@XStreamAlias("CheckInDate")
	private String checkInDate;
	@XStreamAlias("CheckOutDate")
	private String checkOutDate;
	@XStreamAlias("RoomNum")
	private String roomNum;
	public String getSpHotelId() {
		return spHotelId;
	}
	public void setSpHotelId(String spHotelId) {
		this.spHotelId = spHotelId;
	}
	public String getSpRoomTypeId() {
		return spRoomTypeId;
	}
	public void setSpRoomTypeId(String spRoomTypeId) {
		this.spRoomTypeId = spRoomTypeId;
	}
	public String getSpRatePlanId() {
		return spRatePlanId;
	}
	public void setSpRatePlanId(String spRatePlanId) {
		this.spRatePlanId = spRatePlanId;
	}
	public String getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}
	public String getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
}
