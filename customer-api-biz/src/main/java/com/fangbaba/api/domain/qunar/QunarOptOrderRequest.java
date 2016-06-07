package com.fangbaba.api.domain.qunar;

import java.math.BigDecimal;


/**
 * 订单操作类
 * @author tankai
 *
 */
public class QunarOptOrderRequest {
	private String orderNum;//订单号			是			去哪儿订单号
	/**
	 * CONFIRM_ROOM_SUCCESS：确认有房
	CONFIRM_ROOM_FAILURE：确认无房
	ARRANGE_ROOM：安排房间
	ADD_REMARKS：添加备注
	APPLY_UNSUBSCRIBE：申请退订
	AGREE_UNSUBSCRBE：同意退订
	REFUSE_UNSUBSCRIBE：拒绝退订
	CONFIRM_SHOW：确认入住
	CONFIRM_NOSHOW：确认未入住
	AGREE_CASHBACK：确认离店可返现
	SEND_FAX：发送传真
	SEND_SMS：发送短信
	 */
	private String opt;//	操作类型 String	是	CONFIRM_ROOM_SUCCESS	CONFIRM_ROOM_SUCCESS	订单操作类型，总共12种订单操作，详细操作如下：
	/**
	 * hmac=md5(signkey+orderNum+opt+操作参数)
	a)	对参数利用一个signKey进行md5编码并转换为一个十六进制32位的字符串 ，参数加密顺序signKey,orderNum,opt,以及opt操作相关参数，opt操作相关参数有： （money,arrangeType，confirmationNumber，faxSendPartnerName、faxReceiveFaxNumber、faxSendType、prices、roomChargeSettlement、otherChargeSettlement）
	b)	如果某个参数为空，则不添加到加密字符串中即可。
	c)	必须严格按照以上顺序组装。
	d)	hmac小写，32位
	 */
	private  String hmac;//	认证签名	 String	是			订单操作的认证签名，
	/**
	 * NUMBER：按确认号入住
	NAME：按入住人姓名入住
	按确认号入住,需要输入参数confirmationNumber（确认号），按入住人姓名入住，不需要输入参数confirmationNumber（确认号）
	 */
	private  String arrangeType;//		 安排房间类型 否			opt=ARRANGE_ROOM(安排房间)时，arrangeType为必填
	private String	confirmationNumber;//	 安排房间的确认号	否			arrangeType=NUMBER时，才输入输入此参数，长度不能超过120，格式只能是数字、字母（大小写）以及逗号进行组合
	private BigDecimal	money;// 退款金额		否			opt=AGREE_UNSUBSCRBE(同意退订)或APPLY_UNSUBSCRIBE(申请退订)时，money为必填
	private	String remark;//	备注信息	否			opt=ADD_REMARKS(添加备注)时，remark为必填，长度不能超过100个字符。	opt=OTA_SEND_FAX(添加备注)时，remark为选填，长度不能超过500个字符
	private	String faxSendPartnerName;//	String	传真发送方名称 否			传真发送方名称，不填写，默认是与qunar签约主体，opt=SEND_FAX时，此参数必填
	private	String faxStamp;//	String	否		传真公章的文件名称	公章名称，上传公章时的名称，带文件后缀，opt=SEND_FAX时，此参数选填
	private	String faxReceiveName;//	String	否 接收方名称			不填写，默认取代理商酒店名称，opt=SEND_FAX时，此参数选填
	private	String faxReceiveFaxNumber;//	String	否	接收方传真号码		接收方传真号码，opt=SEND_FAX时，此参数必填
	private	String faxSender;//	String	否		传真发送者	传真发送者，opt=SEND_FAX时，此参数必填
	private	String faxSenderTelNumber;//	String	否			传真发送者电话，opt=SEND_FAX时，此参数选填
	private	String faxReceiver;//	String	否			传真接收者，opt=SEND_FAX时，此参数选填
	private	String faxReceiverTelNumber;//	String	否			传真接收者电话，opt=SEND_FAX时，此参数选填
	private	String faxSendType;//	String	否			传真类型，NEW_ORDER("新单"), AMENDING("变更"), CANCELLED("取消");opt=SEND_FAX时，此参数选填
	private	String prices;//	String	否			多日价以“|”隔开， 如入住日2015-01-14房费为：100，2015-01-15房费201，则用100|101表示，opt=SEND_FAX时，此参数必填
	private	String roomChargeSettlement;//	String	否			房费结算方式，ONLINE("我司预付"), DEBT("我司挂账"), CASH("前台现付");opt=SEND_FAX时，此参数选填
	private	String otherChargeSettlement;//	String	否			杂费/基金结算方式，ONLINE("我司预付"), DEBT("我司挂账"), CASH("前台现付");opt=SEND_FAX时，此参数选填
	private	String smsContent;//	String	否			发送短信时短信内容，opt=SEND_SMS时，此参数选填
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFaxSendPartnerName() {
		return faxSendPartnerName;
	}
	public void setFaxSendPartnerName(String faxSendPartnerName) {
		this.faxSendPartnerName = faxSendPartnerName;
	}
	public String getFaxStamp() {
		return faxStamp;
	}
	public void setFaxStamp(String faxStamp) {
		this.faxStamp = faxStamp;
	}
	public String getFaxReceiveName() {
		return faxReceiveName;
	}
	public void setFaxReceiveName(String faxReceiveName) {
		this.faxReceiveName = faxReceiveName;
	}
	public String getFaxReceiveFaxNumber() {
		return faxReceiveFaxNumber;
	}
	public void setFaxReceiveFaxNumber(String faxReceiveFaxNumber) {
		this.faxReceiveFaxNumber = faxReceiveFaxNumber;
	}
	public String getFaxSender() {
		return faxSender;
	}
	public void setFaxSender(String faxSender) {
		this.faxSender = faxSender;
	}
	public String getFaxSenderTelNumber() {
		return faxSenderTelNumber;
	}
	public void setFaxSenderTelNumber(String faxSenderTelNumber) {
		this.faxSenderTelNumber = faxSenderTelNumber;
	}
	public String getFaxReceiver() {
		return faxReceiver;
	}
	public void setFaxReceiver(String faxReceiver) {
		this.faxReceiver = faxReceiver;
	}
	public String getFaxReceiverTelNumber() {
		return faxReceiverTelNumber;
	}
	public void setFaxReceiverTelNumber(String faxReceiverTelNumber) {
		this.faxReceiverTelNumber = faxReceiverTelNumber;
	}
	public String getFaxSendType() {
		return faxSendType;
	}
	public void setFaxSendType(String faxSendType) {
		this.faxSendType = faxSendType;
	}
	public String getPrices() {
		return prices;
	}
	public void setPrices(String prices) {
		this.prices = prices;
	}
	public String getRoomChargeSettlement() {
		return roomChargeSettlement;
	}
	public void setRoomChargeSettlement(String roomChargeSettlement) {
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
