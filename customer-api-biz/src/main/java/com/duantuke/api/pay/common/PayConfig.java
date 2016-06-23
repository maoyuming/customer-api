package com.duantuke.api.pay.common;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class PayConfig {
    
    private static Logger log = LoggerFactory.getLogger(PayConfig.class);
    
    private static final String PAY_CONFIG = "pay_config.properties";
    
    public static String ENVIRONMENT = "";
    
    /********** 微信支付  **********/
    public final static String WECHAT_KEY = "lTzGxXMJjYLhRtiWvaQpVqbsLvMIscJm";

    //微信分配的公众号ID（开通公众号之后可以获取到）
    public final static String WECHAT_APPID = "wxbda1a0b1738a6656";
    
    //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
    public final static String WECHAT_MCHID = "1315533801";
    
    public static String WECHAT_NOTIFY_URL = "";
    public final static String WECHAT_PACKAGEVALUE = "Sign=WXPay";
    
//    private static String packagevalue="";
//
//    //受理模式下给子商户分配的子商户号
//    private static String subMchID = "";
//
//    //HTTPS证书的本地路径
//    private static String certLocalPath = "";
//
//    //HTTPS证书密码，默认密码等于商户号MCHID
//    private static String certPassword = "";
//
//    //是否使用异步线程的方式来上报API测速，默认为异步模式
//    private static boolean useThreadToDoReport = true;

    //机器IP
    public final static String LOCALIP = IPUtil.getLocalIPV4();

    //以下是几个API的路径：
    //1）支付API
    public final static String WECHAT_PAY_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //2）被扫支付查询API
    public final static String WECHAT_PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

    //3）退款API
    public final static String WECHAT_REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    //4）退款查询API
    public final static String WECHAT_REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

    //5）撤销API
    public final static String WECHAT_REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";

    //6）下载对账单API
    public final static String WECHAT_DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";

    //7) 统计上报API
    public final static String WECHAT_REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";
    
    public final static String WECHAT_HTTPSREQUEST_CLASSNAME = "com.duanduke.api.pay.common.HttpsRequest";
    
    public final static String WECHAT_APP_CERTLOCALPATH = "wx_app/apiclient_cert.p12";
    
    /********** 支付宝  **********/
    
    /** 支付宝 商家Email */
    public static final String ALIPAY_MERCHANTEMAIL = "imikeapp@163.com";
    
    /** 支付宝 用户的授权 */
    public static final String ALIPAY_AUTHTOKEN = "用户的授权，需要用户授权的接口不能为空";
    
    /** 支付宝 AOP分配给应用的唯一标识 */
    public static final String ALIPAY_APPID = "imikeapp@163.com";
    
    /** 支付宝 终端类型：web：PC方式;wap：手机WAP; mobile：手机客户端应用方式 */
    public static final String ALIPAY_TERMINALTYPE = "mobile";
    
    /** 支付宝 私钥 */
    public static final String ALIPAY_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ3L+4XyRa8Ox4+lNeKaPE2NIGKiWXrhrGuO/qoAD7CqfTV"
            + "qszHqMHSLPPibCfj/6hNnI+ojB0p/+Khzrj/IP+rTYo1uzyl/dNYI1ubIOFJBwUbCXZuhbpn16iHUiAIjEtmDF4h97ol+OUM+DMwXTUAF4QqJpjloe8zKszd5HDH"
            + "hAgMBAAECgYApAGjRzDsJGrkF/TK8N8F1kTcaCf9gK6WGqThI6g0ppZM1Pw1i6ew/TDLCkl6MActL0WAGpaVuWUQ+WRis4BajQF+arZlIJynlrq/Za6+qzKD0nFm"
            + "oTXMzPMEYtPrzcHqb4mujXH5uKeMxiSxxZdz7XMc1Ze/PiJS2HLlKqiSoAQJBAM5TBjtudlYnNRh35ht8RHEBKTXdIkgk4oMNeLvuqQRu0MLycxgLkKtzcDVej0t"
            + "C2CwVWdKlatlG/ItxFefuk0ECQQDDyex55iNY5cRJx8wcVu76WddTAs1HA9yzmtadiuUaKP9Hew/GCr1oTWrRgljYJtR771ip0N/AYEtFqAkC6hahAkAvmdgGPiX"
            + "vGnyVeJQOI01nBO2ND8eo/VmFAsaoFRfJfgeL8oO7OG/YHnn01iwZIQLeqM5gWumb11TMlvqG1/QBAkAn1zcftw0KUfcXbGjEe4w1tYJzieZrxQVsJt31QQZNxHn"
            + "t+cTD7/uK9fX+nB+oHH6wmYvZQn/FjdLDYeYziZ8BAkAfgqjKf2C1yMJ3c9HOBxDBNx0r3U26DiVo5bqRBG+243qA5M3/6qh2oCm2P7njkE3CvmCTag3Qqz0m2LJUsuLA";
    
    /** 支付宝 公钥 */
    public static final String ALIPAY_PUBLIKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKB"
            + "oLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    
    /** 支付宝 退款 方法 */                          
	public static final String ALIPAY_PWD_REFUND = "refund_fastpay_by_platform_pwd";
	
	/** 支付宝 无密退款 方法 */                          
	public static final String ALIPAY_NO_PWD_REFUND = "refund_fastpay_by_platform_nopwd";
	
    public static final String ALIPAY_PARTNER = "2088712153071777";
    public static String ALIPAY_NOTIFY_URL = "";
    
    public static final String ALIPAY_DUANTUKE_KEY = "";
    
    public static final String ALIPAY_SIGN_TYPE = "RSA";
    public static final String ALIPAY_INPUT_CHARSET = "utf-8";
    
    public static final String ALIPAY_REFUND_SIGN_TYPE = "MD5";
    
    public static final String ALIPAY_GATEWAY = "https://mapi.alipay.com/gateway.do?";
    
    static {

        log.info("==============初始化支付回调URL==============");

        try {
            Properties pro = PropertiesLoaderUtils.loadAllProperties(PAY_CONFIG);

            WECHAT_NOTIFY_URL = pro.getProperty("wechat_notify_url");
            ALIPAY_NOTIFY_URL = pro.getProperty("alipay_notify_url");
            ENVIRONMENT = pro.getProperty("environment");

            log.info("当前环境:{}, 支付宝回调地址:{}, 微信支付回调地址:{}", ENVIRONMENT, ALIPAY_NOTIFY_URL, WECHAT_NOTIFY_URL);

        } catch (Exception e) {

            log.error("获取支付回调URL配置异常!", e);
        }

        log.info("=============初始化支付回调URL完毕===========");
    }

}