package com.fangbaba.api.domain.fangcang.order;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.Contacter;
import com.fangbaba.api.domain.GuestInfo;
import com.fangbaba.api.domain.OccupancyInfo;
import com.fangbaba.api.domain.SalePriceItem;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class CreateHotelOrderRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7777477559630425965L;
	@XStreamAlias("SpHotelId")
	private String SpHotelId;
	@XStreamAlias("SpRoomTypeId")
	private String SpRoomTypeId;
	@XStreamAlias("SpRatePlanId")
	private String SpRatePlanId;
	
	@XStreamAlias("BedType")
	private String BedType;
	
	@XStreamAlias("CheckInDate")
	private String CheckInDate;
	@XStreamAlias("CheckOutDate")
	private String CheckOutDate;
	
	@XStreamAlias("RoomNum")
	private Integer RoomNum;
	
	@XStreamAlias("ArrivalTime")
	private String ArrivalTime;
	@XStreamAlias("LatestArrivalTime")
	private String LatestArrivalTime;
	@XStreamAlias("TotalAmount")
	private String TotalAmount;
	
	@XStreamAlias("Currency")
	private String Currency;
	
	@XStreamAlias("FcOrderId")
	private String FcOrderId;
	@XStreamAlias("SalePriceList")
	private List<SalePriceItem> SalePriceList;
	@XStreamAlias("ConfirmType")
	private Integer ConfirmType;
	
	@XStreamAlias("GuestInfoList")
	private List<GuestInfo> GuestInfoList;
	
	@XStreamAlias("Contacter")
	private  Contacter contacter;
	@XStreamAlias("Remark")
	private String Remark;
	
	@XStreamAlias("SpecialRequirement")
	private String SpecialRequirement;
	@XStreamAlias("ReservedItem")
	private String ReservedItem;
	@XStreamAlias("OccupancyInfoList")
	private List<OccupancyInfo> OccupancyInfoList;
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
	public String getSpRatePlanId() {
		return SpRatePlanId;
	}
	public void setSpRatePlanId(String spRatePlanId) {
		SpRatePlanId = spRatePlanId;
	}
	
	public String getBedType() {
		return BedType;
	}
	public void setBedType(String bedType) {
		BedType = bedType;
	}
	public String getCheckInDate() {
		return CheckInDate;
	}
	public void setCheckInDate(String checkInDate) {
		CheckInDate = checkInDate;
	}
	public String getCheckOutDate() {
		return CheckOutDate;
	}
	public void setCheckOutDate(String checkOutDate) {
		CheckOutDate = checkOutDate;
	}
	public Integer getRoomNum() {
		return RoomNum;
	}
	public void setRoomNum(Integer roomNum) {
		RoomNum = roomNum;
	}
	public String getArrivalTime() {
		return ArrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		ArrivalTime = arrivalTime;
	}
	public String getLatestArrivalTime() {
		return LatestArrivalTime;
	}
	public void setLatestArrivalTime(String latestArrivalTime) {
		LatestArrivalTime = latestArrivalTime;
	}
	public String getTotalAmount() {
		return TotalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		TotalAmount = totalAmount;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	public String getFcOrderId() {
		return FcOrderId;
	}
	public void setFcOrderId(String fcOrderId) {
		FcOrderId = fcOrderId;
	}
	public List<SalePriceItem> getSalePriceList() {
		return SalePriceList;
	}
	public void setSalePriceList(List<SalePriceItem> salePriceList) {
		SalePriceList = salePriceList;
	}
	public Integer getConfirmType() {
		return ConfirmType;
	}
	public void setConfirmType(Integer confirmType) {
		ConfirmType = confirmType;
	}
	public List<GuestInfo> getGuestInfoList() {
		return GuestInfoList;
	}
	public void setGuestInfoList(List<GuestInfo> guestInfoList) {
		GuestInfoList = guestInfoList;
	}
	public Contacter getContacter() {
		return contacter;
	}
	public void setContacter(Contacter contacter) {
		this.contacter = contacter;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getSpecialRequirement() {
		return SpecialRequirement;
	}
	public void setSpecialRequirement(String specialRequirement) {
		SpecialRequirement = specialRequirement;
	}
	public String getReservedItem() {
		return ReservedItem;
	}
	public void setReservedItem(String reservedItem) {
		ReservedItem = reservedItem;
	}
	public List<OccupancyInfo> getOccupancyInfoList() {
		return OccupancyInfoList;
	}
	public void setOccupancyInfoList(List<OccupancyInfo> occupancyInfoList) {
		OccupancyInfoList = occupancyInfoList;
	}
	
	

}
