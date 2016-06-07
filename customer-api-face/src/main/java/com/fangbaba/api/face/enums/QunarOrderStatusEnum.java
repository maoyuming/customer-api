package com.fangbaba.api.face.enums;




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
public enum QunarOrderStatusEnum {
	orderSuccessWaitPay(1,"预订成功等待支付"),
	waitConfirmed(2,"等待房间确认"),//待确认
	fail(3,"预订失败"),
	waitArrangeRoom(5,"支付成功待安排房间"),
	channelCanceled(6,"订单取消"),
	paySuccessCanCheckin(7,"支付成功确认可住"),
	alreadyCheckinThaw(12,"已入住解冻成功"),
	refunding(13,"退款申请中"),
	refundFinish(15,"全额退款退款完成"),
	refuseRefundThaw(20,"拒绝退订解冻成功"),
	arrangeWaitRoom(22,"确认成功待安排房间"),
	arrangeRoom(23,"确认可住"),
	alreadyCheckin(24,"已入住"),
	noShow(25,"未到店入住"),
	submitSuccessWaitAssure(28,"提交成功等待担保"),
	toBeConfirmed(29,"担保成功，待房间确认"),
	assureSuccessArrangeRoom(30,"担保成功确认可住"),
	alreadyCheckinAssureRevoke(32,"已入住，担保撤消成功"),
	noShowDebit(37,"未到店入住，扣款成功"),
	cancelOrderAssureRevoke(40,"订单取消担保撤消成功"),
	failAssureRevoke(42,"预订失败担保撤消成功"),
	confirmRommDebit(50,"确认有房，扣款处理中"),
	confirmRommDebitFail(51,"确认有房扣款失败"),
	checkOut(66,"已离店"),
	
	
	;
	
	private Integer code;
	private String name;
	
	
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
	
	

	private QunarOrderStatusEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static QunarOrderStatusEnum findByCode(Integer code){
		for (QunarOrderStatusEnum value : QunarOrderStatusEnum.values()) {
			if(value.getCode().equals(code)){
				return value;
			}
		}
		return null;
	}


	
	
	
}
