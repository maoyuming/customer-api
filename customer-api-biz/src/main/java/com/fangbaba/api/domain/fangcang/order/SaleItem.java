package com.fangbaba.api.domain.fangcang.order;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("SaleItem")
public class SaleItem  implements Serializable{
	private static final long serialVersionUID = -790284899743538620L;
	@XStreamAlias("SaleDate")
	private String saleDate;
	@XStreamAlias("DayCanBook")
	private String dayCanBook;
	@XStreamAlias("PriceNeedCheck")
	private String priceNeedCheck;
	@XStreamAlias("SalePrice")
	private String salePrice;
	@XStreamAlias("Currency")
	private String currency;
	@XStreamAlias("BreakfastType")
	private String breakfastType;
	@XStreamAlias("BreakfastNum")
	private String breakfastNum;
	@XStreamAlias("AvailableQuotaNum")
	private String availableQuotaNum;
	@XStreamAlias("RoomStatus")
	private String roomStatus;
	@XStreamAlias("OverDraft")
	private String overDraft;
	public String getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	public String getDayCanBook() {
		return dayCanBook;
	}
	public void setDayCanBook(String dayCanBook) {
		this.dayCanBook = dayCanBook;
	}
	public String getPriceNeedCheck() {
		return priceNeedCheck;
	}
	public void setPriceNeedCheck(String priceNeedCheck) {
		this.priceNeedCheck = priceNeedCheck;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBreakfastType() {
		return breakfastType;
	}
	public void setBreakfastType(String breakfastType) {
		this.breakfastType = breakfastType;
	}
	public String getBreakfastNum() {
		return breakfastNum;
	}
	public void setBreakfastNum(String breakfastNum) {
		this.breakfastNum = breakfastNum;
	}
	public String getAvailableQuotaNum() {
		return availableQuotaNum;
	}
	public void setAvailableQuotaNum(String availableQuotaNum) {
		this.availableQuotaNum = availableQuotaNum;
	}
	public String getRoomStatus() {
		return roomStatus;
	}
	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}
	public String getOverDraft() {
		return overDraft;
	}
	public void setOverDraft(String overDraft) {
		this.overDraft = overDraft;
	}
	
	
	
	
}
