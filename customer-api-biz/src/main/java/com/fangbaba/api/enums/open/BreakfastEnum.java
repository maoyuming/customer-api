package com.fangbaba.api.enums.open;

/**
 * @author he
 * 是否含早餐枚举
 */
public enum BreakfastEnum {

    no(0,"无早"),
    yes(1,"含早"),
	;
	
	private final Integer id;
	private final String name;
	
	private BreakfastEnum(Integer id,String name){
		this.id=id;
		this.name=name;
	}
	
	public Integer getId() {
		return id;
	}




	public String getName() {
		return name;
	}




	public static BreakfastEnum findByCode(String code){
		for (BreakfastEnum value : BreakfastEnum.values()) {
			if(value.getId().equals(code)){
				return value;
			}
		}
		return null;
	}

}
