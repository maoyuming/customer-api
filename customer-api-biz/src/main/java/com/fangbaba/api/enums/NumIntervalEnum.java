package com.fangbaba.api.enums;

public enum NumIntervalEnum {


	d20("0,20","20间以下"),
	f20to30("20,30","20-30间"),
	u30("30,","30间以上"),
	;
	
	private final String price;
	private final String name;
	
	private NumIntervalEnum(String price,String name){
		this.price=price;
		this.name=name;
	}
	


	public String getPrice() {
		return price;
	}





	public String getName() {
		return name;
	}




	public static NumIntervalEnum findByCode(String code){
		for (NumIntervalEnum value : NumIntervalEnum.values()) {
			if(value.getPrice().equals(code)){
				return value;
			}
		}
		return null;
	}


}
