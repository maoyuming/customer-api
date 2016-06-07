package com.fangbaba.api.face.enums;

public enum OrderOptEnum {
	CONFIRM_ROOM_SUCCESS("CONFIRM_ROOM_SUCCESS","确认有房"),
	CONFIRM_ROOM_FAILURE("CONFIRM_ROOM_FAILURE","确认无房"),
	ARRANGE_ROOM("ARRANGE_ROOM","安排房间"),
	ADD_REMARKS("ADD_REMARKS","添加备注"),
	APPLY_UNSUBSCRIBE("APPLY_UNSUBSCRIBE","申请退订"),
	AGREE_UNSUBSCRBE("AGREE_UNSUBSCRBE","同意退订"),
	REFUSE_UNSUBSCRIBE("REFUSE_UNSUBSCRIBE","拒绝退订"),
	CONFIRM_SHOW("CONFIRM_SHOW","确认入住"),
	CONFIRM_NOSHOW("CONFIRM_NOSHOW","确认未入住"),
	AGREE_CASHBACK("AGREE_CASHBACK","确认离店可返现"),
	SEND_FAX("SEND_FAX","发送传真"),
	SEND_SMS("SEND_SMS","发送短信"),
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
	private OrderOptEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public static OrderOptEnum findByCode(String code){
		for (OrderOptEnum value : OrderOptEnum.values()) {
			if(value.getCode().equals(code)){
				return value;
			}
		}
		return null;
	}

	
}
