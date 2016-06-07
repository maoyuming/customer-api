package com.fangbaba.api.enums;

/**
 * @author he
 */
public enum FangCangCancelOrderStatusEnum {

    cancelsuccess(1,"取消成功"),
    cancelfailure(2,"取消失败"),
	;
	
	private final Integer id;
	private final String name;
	
	private FangCangCancelOrderStatusEnum(Integer id,String name){
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
