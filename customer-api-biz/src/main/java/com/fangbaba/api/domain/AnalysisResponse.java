package com.fangbaba.api.domain;

import java.math.BigDecimal;

public class AnalysisResponse {

	private Integer top;//":"500",// 本区入住率排名
	private BigDecimal roomPrice;//":"999"//本区平均房价
	public Integer getTop() {
		return top;
	}
	public void setTop(Integer top) {
		this.top = top;
	}
	public BigDecimal getRoomPrice() {
		return roomPrice;
	}
	public void setRoomPrice(BigDecimal roomPrice) {
		this.roomPrice = roomPrice;
	}
	
	
}
