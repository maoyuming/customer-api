package com.fangbaba.api.face.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderDetail implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3647949081365411181L;
	// 人数
	private Integer num;
	// 房型
	private Long roomTypeId;
	// 价格
	private List<RoomPrice> prices;
	// 总价
	private BigDecimal totalPrice;
	// PMS房型ID
	private String PmsRoomTypeId;

	public String getPmsRoomTypeId() {
		return PmsRoomTypeId;
	}

	public void setPmsRoomTypeId(String pmsRoomTypeId) {
		PmsRoomTypeId = pmsRoomTypeId;
	}


	public List<RoomPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<RoomPrice> prices) {
		this.prices = prices;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}


}