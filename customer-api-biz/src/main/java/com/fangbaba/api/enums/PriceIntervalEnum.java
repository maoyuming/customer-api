package com.fangbaba.api.enums;

public enum PriceIntervalEnum {


	d50("0,50","50元以下"),
	f50to100("50,100","50-100元"),
	f100to150("100,150","100-150元"),
	f150to200("150,200","150-200元"),
	f200to250("200,250","200-500元"),
	u500("500,","500元以上"),
	;
	
	private final String price;
	private final String name;
	
	private PriceIntervalEnum(String price,String name){
		this.price=price;
		this.name=name;
	}
	


	public String getPrice() {
		return price;
	}





	public String getName() {
		return name;
	}




	public static PriceIntervalEnum findByCode(String code){
		for (PriceIntervalEnum value : PriceIntervalEnum.values()) {
			if(value.getPrice().equals(code)){
				return value;
			}
		}
		return null;
	}


}
