package com.fangbaba.api.enums;

/**
 * @author he
 * 房态展示：
1-有房;2-待查;3-满房
 */
public enum FangCangRoomStatusEnum {

    available(1,"有房"),
    tobeconfirm(2,"待查"),
    full(3,"满房"),
	;
	
	private final Integer id;
	private final String name;
	
	private FangCangRoomStatusEnum(Integer id,String name){
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
