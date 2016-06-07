package com.fangbaba.api.face.enums;

public enum OrderSettlementEnum {
	ONLINE("ONLINE","我司预付"), //去哪儿预付
	DEBT("DEBT","我司挂账"), 
	CASH("CASH","前台现付")
	;
	
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private OrderSettlementEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public static OrderSettlementEnum findByCode(String code){
		for (OrderSettlementEnum value : OrderSettlementEnum.values()) {
			if(value.getCode().equals(code)){
				return value;
			}
		}
		return null;
	}

	
}
