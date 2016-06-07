package com.fangbaba.api.domain.open.hotel;

import java.math.BigDecimal;

public class HotelRequest {
	private BigDecimal longitude;//	经度	N	
	private BigDecimal latitude;//	纬度	N	
	private Integer page;//	页码	Y	从1开始
	private Integer pagesize;//	每页条数	N	每页最多10条，默认每页10条
	private Integer citycode;// 城市编码 N
	
	
	public Integer getCitycode() {
		return citycode;
	}
	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	
	
	
}
