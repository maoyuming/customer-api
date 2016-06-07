package com.fangbaba.api.domain.open.hotel;

import java.math.BigDecimal;

public class Roomtype {
	private Long id;//	房型id	Long	Y	房爸爸房型唯一主键
	private Long hotelid;//	酒店id	Long	Y	房爸爸房型唯一主键
	private Long roomtypeid;//	房型id	Long	Y	房爸爸房型唯一主键
	private String name;//	房型名称	String	Y	
	private Integer roomnum;//	房间数	int	Y	总房间数，不是可用的
    private String bedtype;
    private String area;
    private String prepay; //支付方式
    private String breakfast;//早餐
    private String roomtypepics;//房型图片数组
    private String bedsize;//床尺寸
    
    private String status;//是否可定（关房）		0、可定；1、不可订
    
	public String getBedsize() {
		return bedsize;
	}
	public void setBedsize(String bedsize) {
		this.bedsize = bedsize;
	}
	public String getPrepay() {
		return prepay;
	}
	public void setPrepay(String prepay) {
		this.prepay = prepay;
	}
	public String getBreakfast() {
		return breakfast;
	}
	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}
	public String getRoomtypepics() {
		return roomtypepics;
	}
	public void setRoomtypepics(String roomtypepics) {
		this.roomtypepics = roomtypepics;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRoomnum() {
		return roomnum;
	}
	public void setRoomnum(Integer roomnum) {
		this.roomnum = roomnum;
	}
    public String getBedtype() {
        return bedtype;
    }
    public void setBedtype(String bedtype) {
        this.bedtype = bedtype;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
	public Long getHotelid() {
		return hotelid;
	}
	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}
	public Long getRoomtypeid() {
		return roomtypeid;
	}
	public void setRoomtypeid(Long roomtypeid) {
		this.roomtypeid = roomtypeid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
