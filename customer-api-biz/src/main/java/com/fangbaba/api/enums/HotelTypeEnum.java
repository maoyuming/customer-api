package com.fangbaba.api.enums;

public enum HotelTypeEnum {

    HMSHOTEL(1,"旅馆"),
    THEMEDHOTEL(2,"主题酒店"),
    PLAZAHOTEL(3,"精品酒店"),
    APARTMENTHOTEL(4,"公寓"),
    HOSTELS(5,"招待所"),
    INNER(6,"客栈");
	
	;
	
	private final Integer id;
	private final String name;
	
	private HotelTypeEnum(Integer id,String name){
		this.id=id;
		this.name=name;
	}
	
	public Integer getId() {
		return id;
	}




	public String getName() {
		return name;
	}




	public static HotelTypeEnum findByCode(String code){
		for (HotelTypeEnum value : HotelTypeEnum.values()) {
			if(value.getId().equals(code)){
				return value;
			}
		}
		return null;
	}

}
