package com.fangbaba.api.domain.qunar;

import java.math.BigDecimal;
import java.util.List;

public class QunarOrderResponse{
//	"remark": [],
    private String promotionCode;//": "NONE",
    private String cityName;//": "北京",
    private Integer changeRule;//": 0,
    private String hotelName;//": "三亚宝宏龙都大酒店",
    private String roomName;//": "测试关房",
    private String request;//": "",
    private String serialVersionUID;//": 5368151272052259000,
    private String guestType;//": "内宾",
    /**
     * 订单创建时间，格式为YYYYMMDDHHMMSS，比如20120316111508
     */
    private String orderDate;//": "20150415122435",
    private String payTypeMsg;//": "预付",
    /**
     * 去哪儿订单号
     */
    private String orderNum;//": "100401749971",
    //"everyDayPrice": "[{\"date\":\"2015-04-15\",\"price\":\"90.00\",\"deposit\":null,\"roomStatus\":0,\"priceCut\":\"0\"}]",
    /**
     * 联系人姓名
     */
    private String contactName;//": "威锋网啊",
    private String breakfast;//": "无早",
    /**
     * 代理商房型id
     */
    private Long roomId;//": "2460847",
    /**
     * 代理商酒店id
     */
    private Long hotelId;//": "1509340",
    /**
     * 去哪儿网系统订单状态码
     */
    private Integer statusCode;//": 7,\
    /**
     * 预定房间数
     */
    private Integer roomNum;//": 2,
    private String  contactPhoneKey;//": "5057",
    private BigDecimal foreignCurrencyAmount;//": "180.00",
    private boolean needInvoice;//": false,
    /**
     * 入住人;多人以|分割。
     */
    private String customerName;//": "威锋网啊|威锋网啊啊",
    /**
     * 	联系人手机
     */
    private String contactPhone;//": "186****5057",
    /**
     * 用户入住日期，格式为YYYYMMDD，比如20120316
     */
    private String checkInDate;//": "20150415",
    /**
     * 用户离店日期，格式为YYYYMMDD，比如20120316
     */
    private String checkOutDate;//": "20150416",
    private String contactEmail;//": "chp***@163.com",
    
    private List<Logs> logs;//操作日志
    private String  currencyType;//": "CNY",
    private Integer isVouch;//": 2,
    private String  customerIp;//": "192.168.113.26",
    private String  statusMsg;//": "支付成功确认可住",
    private Integer payTypeCode;//": 0,
    private Integer invoiceFee;//": 0,
    /**
     * 用户支付总金额
     */
    private BigDecimal payMoney;//": 180,
    private String arriveTime;//": "18:00",
    private String bedType;//": "双床",
    private Integer maxOccupancy;//": 0,
    private String channel;//": "普通业务",
    private String  bedTypePreference;//": "",
    private boolean arriving;//": false,
//    "promotionList": [
//      {
//        "validType": 1,
//        "name": "免费送水果",
//        "description": "入住时，派发柚子"
//      },
//      {
//        "validType": 2,
//        "name": "100元-动物园门票",
//        "description": "动物园门票"
//      }
//    ]
    
    
    private String everyDayPrice;
    
    /**
     * 订单最后一次更新时间
     */
    private Long lastUpdateTime;
    /**
     * 订单的房费金额
     */
    private BigDecimal roomFee;
    
    private List<String> remark;//备注列表
	public String getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Integer getChangeRule() {
		return changeRule;
	}
	public void setChangeRule(Integer changeRule) {
		this.changeRule = changeRule;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getSerialVersionUID() {
		return serialVersionUID;
	}
	public void setSerialVersionUID(String serialVersionUID) {
		this.serialVersionUID = serialVersionUID;
	}
	public String getGuestType() {
		return guestType;
	}
	public void setGuestType(String guestType) {
		this.guestType = guestType;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getPayTypeMsg() {
		return payTypeMsg;
	}
	public void setPayTypeMsg(String payTypeMsg) {
		this.payTypeMsg = payTypeMsg;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getBreakfast() {
		return breakfast;
	}
	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public Integer getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(Integer roomNum) {
		this.roomNum = roomNum;
	}
	public String getContactPhoneKey() {
		return contactPhoneKey;
	}
	public void setContactPhoneKey(String contactPhoneKey) {
		this.contactPhoneKey = contactPhoneKey;
	}
	public BigDecimal getForeignCurrencyAmount() {
		return foreignCurrencyAmount;
	}
	public void setForeignCurrencyAmount(BigDecimal foreignCurrencyAmount) {
		this.foreignCurrencyAmount = foreignCurrencyAmount;
	}
	public boolean isNeedInvoice() {
		return needInvoice;
	}
	public void setNeedInvoice(boolean needInvoice) {
		this.needInvoice = needInvoice;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}
	public String getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public Integer getIsVouch() {
		return isVouch;
	}
	public void setIsVouch(Integer isVouch) {
		this.isVouch = isVouch;
	}
	public String getCustomerIp() {
		return customerIp;
	}
	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public Integer getPayTypeCode() {
		return payTypeCode;
	}
	public void setPayTypeCode(Integer payTypeCode) {
		this.payTypeCode = payTypeCode;
	}
	public Integer getInvoiceFee() {
		return invoiceFee;
	}
	public void setInvoiceFee(Integer invoiceFee) {
		this.invoiceFee = invoiceFee;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	public String getBedType() {
		return bedType;
	}
	public void setBedType(String bedType) {
		this.bedType = bedType;
	}
	public Integer getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(Integer maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getBedTypePreference() {
		return bedTypePreference;
	}
	public void setBedTypePreference(String bedTypePreference) {
		this.bedTypePreference = bedTypePreference;
	}
	public boolean isArriving() {
		return arriving;
	}
	public void setArriving(boolean arriving) {
		this.arriving = arriving;
	}
	public List<Logs> getLogs() {
		return logs;
	}
	public void setLogs(List<Logs> logs) {
		this.logs = logs;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public BigDecimal getRoomFee() {
		return roomFee;
	}
	public void setRoomFee(BigDecimal roomFee) {
		this.roomFee = roomFee;
	}
	public String getEveryDayPrice() {
		return everyDayPrice;
	}
	public void setEveryDayPrice(String everyDayPrice) {
		this.everyDayPrice = everyDayPrice;
	}
	public List<String> getRemark() {
		return remark;
	}
	public void setRemark(List<String> remark) {
		this.remark = remark;
	}
    
    
    
}
