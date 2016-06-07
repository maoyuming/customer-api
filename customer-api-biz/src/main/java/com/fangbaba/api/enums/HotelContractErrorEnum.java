package com.fangbaba.api.enums;

public enum HotelContractErrorEnum {

    tokenError("200001","token错误"),
    ContentNullError("200002","合同内容为空"),
    AuthorNullError("200003","合同签约人为空"),
    createTimeNullError("200004","签约时间为空"),
    hotelidNullError("200005","签约酒店为空"),
    typeNullError("200006","签约酒店类型为空"),
    ;
	
	
	private final String id;
	private final String name;
	
	private HotelContractErrorEnum(String id,String name){
		this.id=id;
		this.name=name;
	}
	
	public String getId() {
		return id;
	}




	public String getName() {
		return name;
	}




	public static HotelContractErrorEnum findByCode(String code){
		for (HotelContractErrorEnum value : HotelContractErrorEnum.values()) {
			if(value.getId().equals(code)){
				return value;
			}
		}
		return null;
	}

}
