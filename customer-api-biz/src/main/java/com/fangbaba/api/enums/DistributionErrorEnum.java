package com.fangbaba.api.enums;

public enum DistributionErrorEnum {


	tokenError("100001","token错误"),
    hotelNull("100002","酒店id为空"),
    argsNull("100003","参数为空"),
    channelidNulll("100004","渠道为空"),
    timestampNulll("100005","时间戳为空"),
    begintimeNulll("100006","开始时间为空"),
    endtimeNulll("100007","结束时间为空"),
    roomTypeNull("100008","酒店房型为空"),
    bodyNull("100009","接收body参数错误"),
    otatypeNull("100010","otatype为空"),
    dateFormatNull("100011","时间格式应为yyyy-MM-dd"),
    longitudeNulll("100012","经度为空"),
    latitudeNulll("100013","纬度为空"),
    channelidNotExists("100014","渠道不存在"),
    timeParseNulll("100015","时间格式不正确"),
    findroomtypestockError("100016","调用stock查询接口错误"),
    roomtypesidTypeError("100017","房型id类型错误"),
    hotelidNotExists("100018","酒店信息不存在"),
    hotelidAndRoomtypeNull("100019","酒店id,房型id不同时为空"),
    nofindRoomtype("100020","根据酒店id并未查询到房型"),
    nofindRoomtypePrice("100021","该时间段内的酒店并未查询到房型的价格"),
    noStocktype("100022","库存类型应为true或false"),
	;
	
	private final String id;
	private final String name;
	
	private DistributionErrorEnum(String id,String name){
		this.id=id;
		this.name=name;
	}
	
	public String getId() {
		return id;
	}




	public String getName() {
		return name;
	}




	public static DistributionErrorEnum findByCode(String code){
		for (DistributionErrorEnum value : DistributionErrorEnum.values()) {
			if(value.getId().equals(code)){
				return value;
			}
		}
		return null;
	}


}
