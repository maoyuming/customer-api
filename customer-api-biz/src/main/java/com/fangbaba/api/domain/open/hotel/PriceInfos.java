/**
 * 2016年3月22日下午4:48:34
 * zhaochuanbin
 */
package com.fangbaba.api.domain.open.hotel;

import java.math.BigDecimal;

/**
 * @author zhaochuanbin
 *
 */
public class PriceInfos {
    
    private String date;

    private BigDecimal cost;
    
    private BigDecimal price;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

    
}