package com.fangbaba.api.domain.qunar;

import java.math.BigDecimal;

public class EveryDayPrice {
	
	private String date;//	日期 Date	否	无	无	日期，格式为YYYY-MM-DD，比如：2012-07-12
	private Integer roomStatus;//房型状态	roomStatus	int	是	无	无	1为关房，0为开房
	private BigDecimal price ;//当日价格		BigDecimal	是	无	无	当日价格
	private BigDecimal deposit;//	BigDecimal	预付定金金额 否	无	无	预付定金金额
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getRoomStatus() {
		return roomStatus;
	}
	public void setRoomStatus(Integer roomStatus) {
		this.roomStatus = roomStatus;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}


	
	
}
