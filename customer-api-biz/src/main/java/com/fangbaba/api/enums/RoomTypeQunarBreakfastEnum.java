package com.fangbaba.api.enums;

public enum RoomTypeQunarBreakfastEnum {

    no("0","无早餐"),
    ;
	
	
	private final String id;
	private final String name;
	
	private RoomTypeQunarBreakfastEnum(String id,String name){
		this.id=id;
		this.name=name;
	}
	
	public String getId() {
		return id;
	}




	public String getName() {
		return name;
	}




	public static RoomTypeQunarBreakfastEnum findByCode(String code){
		for (RoomTypeQunarBreakfastEnum value : RoomTypeQunarBreakfastEnum.values()) {
			if(value.getId().equals(code)){
				return value;
			}
		}
		return null;
	}

}
