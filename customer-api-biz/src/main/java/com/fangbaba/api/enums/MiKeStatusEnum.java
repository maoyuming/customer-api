package com.fangbaba.api.enums;

public enum MiKeStatusEnum {
	//是否可订（关房）	是	0、可订；1、不可订
    YES(0,"可订"),
    NO(1,"不可订"),
	;
	
	private final Integer id;
	private final String name;
	
	private MiKeStatusEnum(Integer id,String name){
		this.id=id;
		this.name=name;
	}
	
	public Integer getId() {
		return id;
	}




	public String getName() {
		return name;
	}




	public static MiKeStatusEnum findByCode(String code){
		for (MiKeStatusEnum value : MiKeStatusEnum.values()) {
			if(value.getId().equals(code)){
				return value;
			}
		}
		return null;
	}

}
