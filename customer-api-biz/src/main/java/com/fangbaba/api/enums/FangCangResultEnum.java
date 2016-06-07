package com.fangbaba.api.enums;

public enum FangCangResultEnum {


	Success("Success","成功"),
	Failure("Failure","失败"),
	
	;
	
	private final String result;
	private final String name;



	public String getResult() {
		return result;
	}



	public String getName() {
		return name;
	}



	private FangCangResultEnum(String result, String name) {
		this.result = result;
		this.name = name;
	}



	public static FangCangResultEnum findByCode(String code){
		for (FangCangResultEnum value : FangCangResultEnum.values()) {
			if(value.getResult().equals(code)){
				return value;
			}
		}
		return null;
	}


}
