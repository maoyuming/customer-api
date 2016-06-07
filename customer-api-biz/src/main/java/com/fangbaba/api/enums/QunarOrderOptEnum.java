package com.fangbaba.api.enums;

import com.fangbaba.api.face.enums.OrderOptEnum;

public enum QunarOrderOptEnum {
	CONFIRM_ROOM_SUCCESS("CONFIRM_ROOM_SUCCESS","确认有房",OrderOptEnum.CONFIRM_ROOM_SUCCESS),
	CONFIRM_ROOM_FAILURE("CONFIRM_ROOM_FAILURE","确认无房",OrderOptEnum.CONFIRM_ROOM_FAILURE),
	ARRANGE_ROOM("ARRANGE_ROOM","安排房间",OrderOptEnum.ARRANGE_ROOM),
	ADD_REMARKS("ADD_REMARKS","添加备注",OrderOptEnum.ADD_REMARKS),
	APPLY_UNSUBSCRIBE("APPLY_UNSUBSCRIBE","申请退订",OrderOptEnum.APPLY_UNSUBSCRIBE),
	AGREE_UNSUBSCRBE("AGREE_UNSUBSCRBE","同意退订",OrderOptEnum.AGREE_UNSUBSCRBE),
	REFUSE_UNSUBSCRIBE("REFUSE_UNSUBSCRIBE","拒绝退订",OrderOptEnum.REFUSE_UNSUBSCRIBE),
	CONFIRM_SHOW("CONFIRM_SHOW","确认入住",OrderOptEnum.CONFIRM_SHOW),
	CONFIRM_NOSHOW("CONFIRM_NOSHOW","确认未入住",OrderOptEnum.CONFIRM_NOSHOW),
	AGREE_CASHBACK("AGREE_CASHBACK","确认离店可返现",OrderOptEnum.AGREE_CASHBACK),
	SEND_FAX("SEND_FAX","发送传真",OrderOptEnum.SEND_FAX),
	SEND_SMS("SEND_SMS","发送短信",OrderOptEnum.SEND_SMS),
	;
	
	private String code;
	private String name;
	private OrderOptEnum enum1;
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
	private QunarOrderOptEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	
	private QunarOrderOptEnum(String code, String name, OrderOptEnum enum1) {
		this.code = code;
		this.name = name;
		this.enum1 = enum1;
	}
	public OrderOptEnum getEnum1() {
		return enum1;
	}
	public void setEnum1(OrderOptEnum enum1) {
		this.enum1 = enum1;
	}
	public static QunarOrderOptEnum findByCode(String code){
		for (QunarOrderOptEnum value : QunarOrderOptEnum.values()) {
			if(value.getCode().equals(code)){
				return value;
			}
		}
		return null;
	}
	public static QunarOrderOptEnum findByOrderOptEnum(OrderOptEnum orderOptEnum){
		for (QunarOrderOptEnum value : QunarOrderOptEnum.values()) {
			if(value.getEnum1().equals(orderOptEnum)){
				return value;
			}
		}
		return null;
	}

	
}
