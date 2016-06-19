package com.duanduke.api.pay.common;

/**
 * User: rizenguo
 * Date: 2014/10/22
 * Time: 21:29
 */

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求被扫支付API需要提交的数据
 */
public class ScanPayReqData {

    //每个字段具体的意思请查看API文档
    
    //公众账号ID 	
    private String appid ;	
    //商户号 	
    private String mch_id; 	
   //随机字符串 	
   private String nonce_str ;	
   // 签名 
    private String sign ;
   // 商品描述 	
    private String body ;	
    //商品详情 	
    private String detail ;	
   // 商户订单号 	
    private String out_trade_no ;	
    //货币类型 	
    private String fee_type="CNY" ;
    //总金额  
    private int	total_fee; 	
   // 终端IP 	
    private String  spbill_create_ip ; 
   // 交易起始时间 	yyyyMMddHHmmss
    //交易结束时间 	
    private String  time_start;
    //交易结束时间 	 最短失效时间间隔必须大于5分钟
    private String  time_expire;
  
    //通知地址 	
    private String  notify_url;	
    //交易类型 	
    private String  trade_type="APP"; 
   

    public String getAppid() {
		return appid=PayConfig.WECHAT_APPID;
	}






	public void setAppid(String appid) {
		this.appid = appid;
	}






	public String getMch_id() {
		return PayConfig.WECHAT_MCHID;
	}






	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}






	public String getNonce_str() {
		return nonce_str;
	}






	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}






	public String getSign() {
		String sign = Signature.getSign(toMap());
	  return sign;
	}






	public void setSign(String sign) {
		this.sign = sign;
	}






	public String getBody() {
		return body;
	}






	public void setBody(String body) {
		this.body = body;
	}






	public String getDetail() {
		return detail;
	}






	public void setDetail(String detail) {
		this.detail = detail;
	}






	public String getOut_trade_no() {
		return out_trade_no;
	}






	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}






	public String getFee_type() {
		return fee_type;
	}






	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}






	public int getTotal_fee() {
		return total_fee;
	}






	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}






	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}






	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}






	public String getTime_start() {
		return time_start;
	}






	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}






	public String getTime_expire() {
		return time_expire;
	}






	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}






	public String getNotify_url() {
		return notify_url;
	}






	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}






	public String getTrade_type() {
		return trade_type;
	}






	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}






	public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
