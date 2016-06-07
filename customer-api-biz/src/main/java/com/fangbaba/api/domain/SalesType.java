package com.fangbaba.api.domain;

import java.io.Serializable;
import java.util.List;

public class SalesType implements Serializable{
	private String salesTypeId;//	分销种类ID
	private String salesDiscount;//分销折扣
	private Integer discountType;//分销折扣类型
	
	private String salesTypeName;//":"5间夜批发价",  //分销种类名称
	private List<SalesRoomType> salesRoomType;
	
	public String getSalesTypeId() {
		return salesTypeId;
	}
	public void setSalesTypeId(String salesTypeId) {
		this.salesTypeId = salesTypeId;
	}
	public String getSalesDiscount() {
		return salesDiscount;
	}
	public void setSalesDiscount(String salesDiscount) {
		this.salesDiscount = salesDiscount;
	}
	public String getSalesTypeName() {
		return salesTypeName;
	}
	public void setSalesTypeName(String salesTypeName) {
		this.salesTypeName = salesTypeName;
	}
	public List<SalesRoomType> getSalesRoomType() {
		return salesRoomType;
	}
	public void setSalesRoomType(List<SalesRoomType> salesRoomType) {
		this.salesRoomType = salesRoomType;
	}
	public Integer getDiscountType() {
		return discountType;
	}
	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}
	
	
}
