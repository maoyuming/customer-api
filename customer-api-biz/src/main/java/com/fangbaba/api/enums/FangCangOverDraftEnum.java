package com.fangbaba.api.enums;

/**
 * @author he
 * 是否可超：
1-可以；0-不可以
 */
public enum FangCangOverDraftEnum {

    can(1,"可以"),
    cannot(0,"不可以"),
	;
	
	private final Integer id;
	private final String name;
	
	private FangCangOverDraftEnum(Integer id,String name){
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
