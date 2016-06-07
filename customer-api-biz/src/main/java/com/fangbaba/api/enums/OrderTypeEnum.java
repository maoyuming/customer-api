package com.fangbaba.api.enums;

public enum OrderTypeEnum {
	toBeConfirmed(0, "待确认"), 
	confirmed(1, "已确认"),
	finish(2, "完成"), 
	cancel(3, "取消"),
	;
	
	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	private OrderTypeEnum(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	public static OrderTypeEnum findByCode(Integer code){
		for (OrderTypeEnum value : OrderTypeEnum.values()) {
			if(value.id.equals(code)){
				return value;
			}
		}
		return null;
	}
	public static String findNameByCode(Integer code){
		for (OrderTypeEnum value : OrderTypeEnum.values()) {
			if(value.id.equals(code)){
				return value.getName();
			}
		}
		return null;
	}
	
}
