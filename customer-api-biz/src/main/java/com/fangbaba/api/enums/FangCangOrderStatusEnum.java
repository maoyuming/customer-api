package com.fangbaba.api.enums;

/**
 * @author he
 * 订单状态:
	0-处理中;
	1-已确认;
	2-已拒绝;
	3-已取消;
 */
public enum FangCangOrderStatusEnum {

    operating(0,"处理中"),
    confirmed(1,"已确认"),
    refused(2,"已拒绝"),
    canceled(3,"已取消"),
    cancelfailure(4, "取消失败"),
	;
	
	private final Integer id;
	private final String name;
	
	private FangCangOrderStatusEnum(Integer id,String name){
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
