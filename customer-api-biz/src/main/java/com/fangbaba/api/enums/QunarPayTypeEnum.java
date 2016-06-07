package com.fangbaba.api.enums;

import com.fangbaba.order.common.enums.PayTypeEnum;

public enum QunarPayTypeEnum {

	YF(0,PayTypeEnum.YF),//待确认
	PT(1,PayTypeEnum.PT),
	;
	
	private Integer code;
	private PayTypeEnum payTypeEnum;
	
	
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	

	private QunarPayTypeEnum(Integer code, PayTypeEnum payTypeEnum) {
		this.code = code;
		this.payTypeEnum = payTypeEnum;
	}
	public static QunarPayTypeEnum findByCode(Integer code){
		for (QunarPayTypeEnum value : QunarPayTypeEnum.values()) {
			if(value.getCode().equals(code)){
				return value;
			}
		}
		return null;
	}


	
	
	

}
