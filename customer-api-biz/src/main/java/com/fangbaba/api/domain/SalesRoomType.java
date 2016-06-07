package com.fangbaba.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class SalesRoomType  implements Serializable{
	private String roomTypeName;//":"大床房",		//房型名称
	private BigDecimal salesPrice;//":"120"				//分销价格
	public String getRoomTypeName() {
		return roomTypeName;
	}
	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}
	public BigDecimal getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}
	
	
	
}
