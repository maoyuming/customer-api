package com.duantuke.api.enums;

public enum ErrorEnum {


	tokenError("100001","token错误"),
    pmsNull("100002","酒店id为空"),
    argsNull("100003","参数为空"),
    salesTypeNull("100004","分销类型为空"),
    salesDiscountNull("100005","分销折扣为空"),
    systemError("100006","系统错误"),
    dataEmpty("100007","分销数据为空"),
    phoneEmpty("100008","手机号码为空"),
    messageEmpty("100009","内容为空"),
    notMessageType("100010","无此消息类型"),
    checkFail("100011","校验失败"),
    codeEmpty("100012","验证码为空"),
    passwordEmpty("100013","密码为空"),
    usernameEmpty("100014","用户名为空"),
    updatePasswdFail("100015","修改密码失败"),
    findHotelRankFail("100016","查询酒店排名失败"),
    findAverageRoomPriceFail("100017","查询平均房价失败"),
    
    emptyChannelId("100018","渠道id为空"),
    emptyOperator("100019","操作人为空"),
    emptySwitch("100020","开通关闭状态为空"),
    distributioncodeNull("100021","分销种类为空"),
    distributionswitchNull("100022","开关类型为空"),
    distributionswitchParmError("100023","开关类型错误"),
    
    
    accntisnull("200024","提现申请账户ID为空或账户角色为空"),
    accntsumisnull("200025","提现申请提现金额不能为空"),
    accnthmsnotfind("200026","HMS系统未查到该酒店账户"),
    accntsettenotfind("200027","结算中心未查到该酒店账户"),
    accntbalanceno("200028","账户余额不足"),
    accntselectbug("200029","查询账户异常!"),
    
    
    
    updateUserFail("300001","更新用户信息失败"),
	;
	
	private final String id;
	private final String name;
	
	private ErrorEnum(String id,String name){
		this.id=id;
		this.name=name;
	}
	
	public String getId() {
		return id;
	}




	public String getName() {
		return name;
	}




	public static ErrorEnum findByCode(String code){
		for (ErrorEnum value : ErrorEnum.values()) {
			if(value.getId().equals(code)){
				return value;
			}
		}
		return null;
	}


}
