package com.fangbaba.api.domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("SalePriceItem")
public class SalePriceItem {

	@XStreamAlias("SaleDate")
	private String SaleDate;
	@XStreamAlias("SalePrice")
	private BigDecimal SalePrice;
	@XStreamAlias("BreakfastType")
	private Integer BreakfastType;
	@XStreamAlias("BreakfastNum")
	private Integer BreakfastNum;
	public String getSaleDate() {
		return SaleDate;
	}
	public void setSaleDate(String saleDate) {
		SaleDate = saleDate;
	}
	public BigDecimal getSalePrice() {
		return SalePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		SalePrice = salePrice;
	}
	public Integer getBreakfastType() {
		return BreakfastType;
	}
	public void setBreakfastType(Integer breakfastType) {
		BreakfastType = breakfastType;
	}
	public Integer getBreakfastNum() {
		return BreakfastNum;
	}
	public void setBreakfastNum(Integer breakfastNum) {
		BreakfastNum = breakfastNum;
	}
	
	
}
