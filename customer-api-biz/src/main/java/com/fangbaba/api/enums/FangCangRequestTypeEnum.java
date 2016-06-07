package com.fangbaba.api.enums;


/**
 * 房仓请求类型枚举
 * @author tankai
 *
 */
public enum FangCangRequestTypeEnum {
	GetHotelInfo("getHotelInfo","酒店基本信息查询接口"),
	AddHotelMapping("addHotelMapping","新增酒店映射接口"),
	AddRoomTypeMapping("addRoomTypeMapping","新增房型映射接口"),
	DeleteHotelMapping("deleteHotelMapping","删除酒店映射接口"),
	DeleteRoomTypeMapping("deleteRoomTypeMapping","删除房型映射接口"),
	SyncRatePlan("syncRatePlan","同步价格计划接口"),
	DeleteRatePlan("deleteRatePlan","删除价格计划接口"),
	SyncRateInfo("syncRateInfo","同步价格信息接口"),
	SyncOrderStatus("syncOrderStatus","订单状态推送接口"),
	CheckRoomAvail("checkRoomAvail","试预订(可订检查)接口"),//房爸爸提供
	CreateHotelOrder("createHotelOrder","创建订单接口"),//房爸爸提供
	CancelHotelOrder("cancelHotelOrder","取消订单接口"),//房爸爸提供
	GetOrderStatus("getOrderstatus","订单状态查询接口"),//房爸爸提供
	
	;
	private String requestType;
	private String name;
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private FangCangRequestTypeEnum(String requestType, String name) {
		this.requestType = requestType;
		this.name = name;
	}
	
	
}
