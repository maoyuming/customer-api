package com.fangbaba.api.enums;

/**
 * @author he
 * 价格是否待查：
1-是；0-否
 */
public enum FangCangPriceNeedCheckEnum {

    yes(1,"是"),
    no(0,"否"),
	;
	
	private final Integer id;
	private final String name;
	
	private FangCangPriceNeedCheckEnum(Integer id,String name){
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
