package com.fangbaba.api.enums;

/**
 * @author he
 * 是否可预订：
	1-可预订；0-不可预订
 */
public enum FangCangCanBookEnum {

    can(1,"可预订"),
    cannot(0,"不可预订"),
	;
	
	private final Integer id;
	private final String name;
	
	private FangCangCanBookEnum(Integer id,String name){
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
