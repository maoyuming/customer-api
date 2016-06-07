package com.fangbaba.api.face.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fangbaba.api.face.enums.OrderOptEnum;
import com.fangbaba.api.face.enums.OrderSettlementEnum;


/**
 * 订单操作类
 * @author tankai
 *
 */
public class OptOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3702931608240249080L;
	private String orderNum;//订单号			是			去哪儿订单号
	private OrderOptEnum opt;//	操作类型 String	是	CONFIRM_ROOM_SUCCESS	CONFIRM_ROOM_SUCCESS	订单操作类型，总共12种订单操作，详细操作如下：
	private String arrangeType;//		 安排房间类型 否			opt=ARRANGE_ROOM(安排房间)时，arrangeType为必填
	private String	confirmationNumber;//	 安排房间的确认号	否			arrangeType=NUMBER时，才输入输入此参数，长度不能超过120，格式只能是数字、字母（大小写）以及逗号进行组合
	private BigDecimal	money;// 退款金额		否			opt=AGREE_UNSUBSCRBE(同意退订)或APPLY_UNSUBSCRIBE(申请退订)时，money为必填
	private	String prices;//	String	否			多日价以“|”隔开， 如入住日2015-01-14房费为：100，2015-01-15房费201，则用100|101表示，opt=SEND_FAX时，此参数必填
	private	OrderSettlementEnum roomChargeSettlement;//	String	否			房费结算方式，ONLINE("我司预付"), DEBT("我司挂账"), CASH("前台现付");opt=SEND_FAX时，此参数选填
	private	String otherChargeSettlement;//	String	否			杂费/基金结算方式，ONLINE("我司预付"), DEBT("我司挂账"), CASH("前台现付");opt=SEND_FAX时，此参数选填
	private	String smsContent;//	String	否			发送短信时短信内容，opt=SEND_SMS时，此参数选填
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public OrderOptEnum getOpt() {
		return opt;
	}
	public void setOpt(OrderOptEnum opt) {
		this.opt = opt;
	}
	public String getArrangeType() {
		return arrangeType;
	}
	public void setArrangeType(String arrangeType) {
		this.arrangeType = arrangeType;
	}
	public String getConfirmationNumber() {
		return confirmationNumber;
	}
	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getPrices() {
		return prices;
	}
	public void setPrices(String prices) {
		this.prices = prices;
	}
	public OrderSettlementEnum getRoomChargeSettlement() {
		return roomChargeSettlement;
	}
	public void setRoomChargeSettlement(OrderSettlementEnum roomChargeSettlement) {
		this.roomChargeSettlement = roomChargeSettlement;
	}
	public String getOtherChargeSettlement() {
		return otherChargeSettlement;
	}
	public void setOtherChargeSettlement(String otherChargeSettlement) {
		this.otherChargeSettlement = otherChargeSettlement;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	
	
	
	
}
