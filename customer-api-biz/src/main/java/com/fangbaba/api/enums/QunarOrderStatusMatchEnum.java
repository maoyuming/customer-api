package com.fangbaba.api.enums;

import com.fangbaba.order.common.enums.OrderStatusEnum;


/**
 * 订单状态描述（订单状态code）展示如下： 预订成功等待支付(1)
等待房间确认(2)
预订失败(3)
支付成功待安排房间(5)
订单取消(6)
支付成功确认可住(7)
已入住解冻成功(12)
退款申请中(13)
全额退款退款完成(15)
拒绝退订解冻成功(20)
确认成功待安排房间(22)
确认可住(23)
已入住(24)
未到店入住(25)
提交成功等待担保(28)
担保成功，待房间确认(29)
担保成功确认可住(30)
已入住，担保撤消成功(32)
未到店入住，扣款成功(37)
订单取消担保撤消成功(40)
预订失败担保撤消成功(42)
确认有房，扣款处理中(50)
确认有房扣款失败(51)
已离店(66)
 * @author tankai
 *
 */
public enum QunarOrderStatusMatchEnum {
	waitConfirmed(2,OrderStatusEnum.toBeConfirmed),//待确认
	waitArrangeRoom(5,OrderStatusEnum.toBeConfirmed),//待确认
	toBeConfirmed(29,OrderStatusEnum.toBeConfirmed),//待确认
	confirmed(23,OrderStatusEnum.confirmed),
	finished(66,OrderStatusEnum.finished),
	channelCanceled(6,OrderStatusEnum.channelCanceled),
	;
	
	private Integer code;
	private OrderStatusEnum orderStatusEnum;
	
	
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public OrderStatusEnum getOrderStatusEnum() {
		return orderStatusEnum;
	}
	public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
		this.orderStatusEnum = orderStatusEnum;
	}
	
	
	

	private QunarOrderStatusMatchEnum(Integer code, OrderStatusEnum orderStatusEnum) {
		this.code = code;
		this.orderStatusEnum = orderStatusEnum;
	}
	public static QunarOrderStatusMatchEnum findByCode(Integer code){
		for (QunarOrderStatusMatchEnum value : QunarOrderStatusMatchEnum.values()) {
			if(value.getCode().equals(code)){
				return value;
			}
		}
		return null;
	}


	
	
	
}
