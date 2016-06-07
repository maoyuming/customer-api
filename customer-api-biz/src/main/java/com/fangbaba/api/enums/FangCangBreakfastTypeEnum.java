package com.fangbaba.api.enums;

/**
 * @author he
 * 早餐类型（可选）:
1-中;2-西;3-自;
 */
public enum FangCangBreakfastTypeEnum {

    zhong(1,"中"),
    xi(2,"西"),
    zi(3,"自"),
	;
	
	private final Integer id;
	private final String name;
	
	private FangCangBreakfastTypeEnum(Integer id,String name){
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
