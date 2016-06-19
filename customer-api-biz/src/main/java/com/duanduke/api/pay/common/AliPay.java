package com.duanduke.api.pay.common;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.DateUtil;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliPay {
	
	private static Logger log = LoggerFactory.getLogger(AliPay.class);
	
	public static JSONObject pay(String orderid,int fee, String body){
        JSONObject json=new JSONObject();
        json.put("partner", PayConfig.ALIPAY_PARTNER);
        json.put("seller_email", PayConfig.ALIPAY_MERCHANTEMAIL);
        json.put("private_key", PayConfig.ALIPAY_KEY);
        //回调URL
        json.put("notify_url", PayConfig.ALIPAY_NOTIFY_URL);
        //url..为空默认为m.alipay.com
        json.put("show_url", "m.alipay.com");
        //支付方式，为空默认为1，人民币
        json.put("payment_type", "1");
        //不传默认为mobile.securitypay.pay
        json.put("service", "mobile.securitypay.pay");
        //编码格式
        json.put("input_charset", "utf-8");
        //签名类型，为空默认为RSA
        json.put("sign_type", PayConfig.ALIPAY_SIGN_TYPE);
        //订单号
        json.put("tradeno", orderid);
        //订单金额
        float f=fee;
        json.put("orderprice", f/100);
        
        json.put("productname", body);
        json.put("productdescription", body);
        
        return json;
    }
	
	public static boolean refund(Long orderId,String tradeNo, BigDecimal price){
		
		boolean r = false;
		
    	Map<String, String> sPara =new HashMap<String, String>();
    	sPara.put("service", PayConfig.ALIPAY_NO_PWD_REFUND);
    	sPara.put("partner",PayConfig.ALIPAY_PARTNER);
    	sPara.put("_input_charset", PayConfig.ALIPAY_INPUT_CHARSET);
    	sPara.put("seller_email", PayConfig.ALIPAY_MERCHANTEMAIL);     
    	sPara.put("seller_user_id", PayConfig.ALIPAY_PARTNER);
    	sPara.put("refund_date", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
       	sPara.put("batch_no", DateUtil.formatDate(new Date(), "yyyyMMdd") + orderId);
       	sPara.put("batch_num", "1");
    	sPara.put("detail_data", tradeNo + "^" + price + "^退款");
//    	sPara.put("notify_url", "http://www.imike.com/pay.htm");
    	sPara.put("return_type", "xml");
    	try {
			r = invokeRefund(sPara);
		} catch (Exception e) {
			log.error("支付宝请求无密退款异常!", e);
		}
    	return r;
    }
	
	private static boolean invokeRefund(Map<String, String> sParaTemp) throws Exception{
		
		//除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        
        String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        mysign = MD5.sign(prestr, PayConfig.ALIPAY_DUANTUKE_KEY, PayConfig.ALIPAY_INPUT_CHARSET);
        
        sPara.put("sign", mysign);
        sPara.put("sign_type", PayConfig.ALIPAY_REFUND_SIGN_TYPE);
        
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(PayConfig.ALIPAY_INPUT_CHARSET);
        
        NameValuePair[] nvp= generatNameValuePair(sPara);
        
        log.info("支付宝退款参数:");
        for(NameValuePair nv : nvp) {
        	log.info(nv.toString());
        }
        
        request.setParameters(nvp);
        String url=PayConfig.ALIPAY_GATEWAY + "_input_charset=" + PayConfig.ALIPAY_INPUT_CHARSET;
        request.setUrl(url+"&");
        HttpResponse response = httpProtocolHandler.execute(request, "", "");
        if (response == null) {
            return false;
        }
        String strResult = response.getStringResult();
        
        log.info("支付宝退款返回:{}", strResult);
        
        return refundXml(strResult);
		
	}
	
	/**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        String s="";
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
            s=s+entry.getKey()+"="+entry.getValue()+"&";
        }
        return nameValuePair;
    }

    public static boolean refundXml(String xmlstring){
    	boolean b=false;
		try {
			SAXBuilder builder = new SAXBuilder(false);
			StringReader read = new StringReader(xmlstring);
			Document  doc = builder.build(read);
			Element elroot =doc.getRootElement();
			if(elroot.getChildText("is_success").equals("T")){
				b=true;
			}
		} catch (Exception e) {
			log.info("支付宝退款返回值解析异常!", e);
		}
    	return b;
    }
}
