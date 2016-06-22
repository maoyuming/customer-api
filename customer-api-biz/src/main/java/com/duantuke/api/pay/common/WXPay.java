package com.duantuke.api.pay.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class WXPay {
    
    private static Logger log = LoggerFactory.getLogger(WXPay.class);

    public  static  SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
    
    public static Map<String, Object> requestScanPayService(String body, String orderid, int fee, String ip) throws Exception {
        Date date = new Date();
        ScanPayReqData scanPayReqData = new ScanPayReqData();
        // 随机字符串
        scanPayReqData.setNonce_str(format.format(new Date()));
        // 商品描述
        scanPayReqData.setBody(body);
        // 商品详情
        scanPayReqData.setDetail(body);
        // 商户订单号
        scanPayReqData.setOut_trade_no(orderid);
        // 总金额
        scanPayReqData.setTotal_fee(fee);
        // 终端IP
        scanPayReqData.setSpbill_create_ip(ip);
        // 交易起始时间 yyyyMMddHHmmss
        // 交易结束时间
        scanPayReqData.setTime_start(format.format(date));
        scanPayReqData.setAppid(PayConfig.WECHAT_APPID);
        scanPayReqData.setMch_id(PayConfig.WECHAT_MCHID);
        // 交易结束时间 最短失效时间间隔必须大于5分钟
        scanPayReqData.setTime_expire(format.format(new Date(date.getTime() + 10 * 60 * 1000)));
        scanPayReqData.setNotify_url(PayConfig.WECHAT_NOTIFY_URL);
        scanPayReqData.setSign(Signature.getSign(scanPayReqData.toMap()));
        
        String prepay_id = topost(scanPayReqData);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appid", PayConfig.WECHAT_APPID);
        map.put("partnerid", PayConfig.WECHAT_MCHID);
        map.put("prepayid", prepay_id);
        map.put("package", PayConfig.WECHAT_PACKAGEVALUE);
        map.put("noncestr", format.format(new Date()));
        map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map.put("sign", Signature.getSign(map));
        map.put("packagevalue", PayConfig.WECHAT_PACKAGEVALUE);
        map.put("tradeno", orderid);
        map.remove("package");

        return map;
    } 
    
    
    public static String topost(ScanPayReqData scanPayReqData) throws Exception{
        String prepay_id="";
        String url = PayConfig.WECHAT_PAY_API;
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(scanPayReqData);
        log.info("xml:"+postDataXML);
        HttpUtils http=new HttpUtils();
        List<Header> list=new ArrayList<>();
        Header h1=new BasicHeader("Content-Type", "application/xml");
        list.add(h1);
        String rejson=http.dojson(url, list, postDataXML);
        Map<String, Object> map=XMLParser.getMapFromXML(rejson);
        log.info("微信支付返回 xml:"+map);
        if("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code")))
        {
            prepay_id=map.get("prepay_id").toString();
        }else{
            log.error("微信支付 返回："+rejson);
            throw new Exception("调用微信支付出错！");
        }
        return prepay_id;
    }
    
    public static boolean refund(Long orderId, String transactionId, int price) throws Exception {
    	
    	boolean result = false;
    	
    	RefundReqData rfrd = new RefundReqData(transactionId , orderId.toString(), price);
    	
    	String r = requestRefundService(rfrd);
    	
    	log.info("微信退款订单:" + orderId + "从微信得到的返回值:" + r);
	    //这个跟上面的方法一样，都可以解析xml 到对象
	    RefundResData rf=XMLParser.getRefundResData(r);
	    if(rf.getReturn_code().equals("SUCCESS")){
	    	log.info("微信退款订单:" + orderId + "Return_code返回 SUCCESS");
	    	if(rf.getResult_code().equals("SUCCESS")){
	    		log.info("微信退款订单:" + orderId + "Result_code返回 SUCCESS");
	    		//退款成功，返回退款单号
	    		String refundid = rf.getRefund_id();
	    		String outRefundNo = rf.getOut_refund_no();
	    		
	    		log.info("微信退款订单:" + orderId + ", 微信退款单号:" + refundid + ", 商家退款单号:" + outRefundNo);
	    		
	    		result = true;
	    	}else{ //抛出错误信息
	    		log.error("微信退款订单:" + orderId + "微信退款错误代码:" + rf.getErr_code() + " ,错误信息：" + rf.getErr_code_des());
	    	}
	    }
    	
    	return result;
    }
    
    /**
     * 请求退款服务
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public static String requestRefundService(RefundReqData refundReqData) throws Exception{
        return new RefundService().request(refundReqData);
    }

//    /**
//     * 请求支付服务
//     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @return API返回的数据
//     * @throws Exception
//     */
//    public static String requestScanPayService(ScanPayReqData scanPayReqData) throws Exception{
//        return new ScanPayService().request(scanPayReqData);
//    }
//
//    /**
//     * 请求支付查询服务
//     * @param scanPayQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @return API返回的XML数据
//     * @throws Exception
//     */
//    public static String requestScanPayQueryService(ScanPayQueryReqData scanPayQueryReqData) throws Exception{
//        return new ScanPayQueryService().request(scanPayQueryReqData);
//    }
//
//
//    /**
//     * 请求退款查询服务
//     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @return API返回的XML数据
//     * @throws Exception
//     */
//    public static String requestRefundQueryService(RefundQueryReqData refundQueryReqData) throws Exception{
//        return new RefundQueryService().request(refundQueryReqData);
//    }
//
//    /**
//     * 请求撤销服务
//     * @param reverseReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @return API返回的XML数据
//     * @throws Exception
//     */
//    public static String requestReverseService(ReverseReqData reverseReqData) throws Exception{
//        return new ReverseService().request(reverseReqData);
//    }
//
//    /**
//     * 请求对账单下载服务
//     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @return API返回的XML数据
//     * @throws Exception
//     */
//    public static String requestDownloadBillService(DownloadBillReqData downloadBillReqData) throws Exception{
//        return new DownloadBillService().request(downloadBillReqData);
//    }

//    /**
//     * 直接执行被扫支付业务逻辑（包含最佳实践流程）
//     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
//     * @throws Exception
//     */
//    public static void doScanPayBusiness(ScanPayReqData scanPayReqData, ScanPayBusiness.ResultListener resultListener) throws Exception {
//        new ScanPayBusiness().run(scanPayReqData, resultListener);
//    }
//
//    /**
//     * 调用退款业务逻辑
//     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @param resultListener 业务逻辑可能走到的结果分支，需要商户处理
//     * @throws Exception
//     */
//    public static void doRefundBusiness(RefundReqData refundReqData, RefundBusiness.ResultListener resultListener) throws Exception {
//        new RefundBusiness().run(refundReqData,resultListener);
//    }
//
//    /**
//     * 运行退款查询的业务逻辑
//     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
//     * @throws Exception
//     */
//    public static void doRefundQueryBusiness(RefundQueryReqData refundQueryReqData,RefundQueryBusiness.ResultListener resultListener) throws Exception {
//        new RefundQueryBusiness().run(refundQueryReqData,resultListener);
//    }
//
//    /**
//     * 请求对账单下载服务
//     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
//     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
//     * @return API返回的XML数据
//     * @throws Exception
//     */
//    public static void doDownloadBillBusiness(DownloadBillReqData downloadBillReqData,DownloadBillBusiness.ResultListener resultListener) throws Exception {
//        new DownloadBillBusiness().run(downloadBillReqData,resultListener);
//    }
}
