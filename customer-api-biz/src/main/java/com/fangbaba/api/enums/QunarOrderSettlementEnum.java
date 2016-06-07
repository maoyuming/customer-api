package com.fangbaba.api.enums;

import com.fangbaba.api.face.enums.OrderSettlementEnum;

public enum QunarOrderSettlementEnum {
	ONLINE("ONLINE","我司预付",OrderSettlementEnum.ONLINE), //去哪儿预付
	DEBT("DEBT","我司挂账",OrderSettlementEnum.DEBT), 
	CASH("CASH","前台现付",OrderSettlementEnum.CASH)
	;
	
	private String code;
	private String name;
	private OrderSettlementEnum orderSettlementEnum;
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
	private QunarOrderSettlementEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public static QunarOrderSettlementEnum findByCode(String code){
		for (QunarOrderSettlementEnum value : QunarOrderSettlementEnum.values()) {
			if(value.getCode().equals(code)){
				return value;
			}
		}
		return null;
	}
	public static QunarOrderSettlementEnum findByOrderSettlementEnum(OrderSettlementEnum orderSettlementEnum){
		for (QunarOrderSettlementEnum value : QunarOrderSettlementEnum.values()) {
			if(value.getOrderSettlementEnum().equals(orderSettlementEnum)){
				return value;
			}
		}
		return null;
	}
	public OrderSettlementEnum getOrderSettlementEnum() {
		return orderSettlementEnum;
	}
	public void setOrderSettlementEnum(OrderSettlementEnum orderSettlementEnum) {
		this.orderSettlementEnum = orderSettlementEnum;
	}
	private QunarOrderSettlementEnum(String code, String name,
			OrderSettlementEnum orderSettlementEnum) {
		this.code = code;
		this.name = name;
		this.orderSettlementEnum = orderSettlementEnum;
	}

	
	
	
}
