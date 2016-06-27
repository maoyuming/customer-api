package com.duantuke.api.controller.customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.duantuke.api.common.Constants;
import com.duantuke.api.core.RedisCacheManager;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.pay.common.AliPay;
import com.duantuke.api.pay.common.AlipayNotify;
import com.duantuke.api.pay.common.PayConfig;
import com.duantuke.api.pay.common.Signature;
import com.duantuke.api.pay.common.WXPay;
import com.duantuke.api.pay.common.XMLParser;
import com.duantuke.api.util.DateUtil;
import com.duantuke.api.util.IPUtil;
import com.duantuke.sc.face.model.ScPay;
import com.duantuke.sc.face.service.ICustomerService;

@Controller
@RequestMapping(value = "/customer/sc")
public class SettlementCenterController {
	
	private static Logger log = LoggerFactory.getLogger(SettlementCenterController.class);

	@Resource
    private RedisCacheManager redisCacheManager;
	
	@Resource
	private ICustomerService settlementService;
    
    private BigDecimal dividend = new BigDecimal(100);
    
    @RequestMapping(value="/pay")
    @ResponseBody
    public ResponseEntity<OpenResponse<Object>> pay(HttpServletRequest request, HttpServletResponse response, Long orderId, 
    		Integer type, Integer payChannel, Integer feeType, Long customerId, Integer sum) {
    	
    	String ip = IPUtil.getIpAddr(request);
    	
    	log.info("支付调用, orderId:{}, type:{}, payChannel:{}, feeType:{}, customerId:{}, sum:{}, ip:{}", 
    			orderId, type, payChannel, feeType, customerId, sum, ip);
    	
    	boolean mock = true;
    	
    	if(mock) {
    		Long payId = null;
    		BigDecimal bMoney = new BigDecimal(sum).divide(dividend).setScale(4, BigDecimal.ROUND_HALF_UP);
    		// 支付宝
            if (payChannel == 1) {
                
            	payId = settlementService.insertPayRecord(orderId, 1, bMoney, 1);
            	
                
            } else if (payChannel == 2) {
                
            	payId = settlementService.insertPayRecord(orderId, 1, bMoney, 2);
                
            }
            
            log.info("订单:{}支付流水号:{}", orderId, payId);
            
            OpenResponse<Object> openResponse = new OpenResponse<Object>();
            
            openResponse.setResult(Constants.SUCCESS);
            return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
            
    	}
    	
    	String redisKey = null;
        String redisValue = null;
        
        OpenResponse<Object> openResponse = new OpenResponse<Object>();
        
        try {

			redisKey = genPrefix4Order("H", orderId.toString());
            
            String lockValue = redisCacheManager.tryLock(redisKey, 10);;
            if (StringUtils.isNotEmpty(lockValue)) {
                log.info("订单:" + redisKey + "获取分布锁成功,继续执行支付流程.");
                
                redisValue = lockValue;
            } else {
                log.info("订单:" + redisKey + "获取分布锁失败,返回.");
                
                openResponse.setResult(Constants.FAIL);
                openResponse.setErrorCode(ErrorEnum.accntPaying.getId());
                openResponse.setErrorMessage(ErrorEnum.accntPaying.getName());
                return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
            }

            if (settlementService.isPayed(orderId)) {

                log.info("订单:{}已经支付过.", orderId);

                openResponse.setResult(Constants.FAIL);
                openResponse.setErrorCode(ErrorEnum.accntPayed.getId());
                openResponse.setErrorMessage(ErrorEnum.accntPayed.getName());
                return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
            }   
            
            BigDecimal bMoney = new BigDecimal(sum).divide(dividend).setScale(4, BigDecimal.ROUND_HALF_UP);
            BigDecimal balance = settlementService.getBanlance(customerId).setScale(4, BigDecimal.ROUND_HALF_UP);
            
            if(type == 2) {//全部使用余额支付, 不需要调用第三方支付, 需要判断余额是否够
                
                log.info("订单:{}使用余额支付全部金额, 客户当前账户余额:{}, 订单金额:{}.", orderId, balance, bMoney);
                
                if(balance.compareTo(bMoney) > -1 ) {
                    
                    log.info("余额足, 直接paybill支付.");
                    
                    openResponse.setResult(Constants.SUCCESS);
                    return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
                } else {
                    
                    log.info("余额不足, 返回.");
                    
                    openResponse.setResult(Constants.FAIL);
                    openResponse.setErrorCode(ErrorEnum.accntbalanceno.getId());
                    openResponse.setErrorMessage(ErrorEnum.accntbalanceno.getName());
                    return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
                }
            }

            String body = "储值账户支付";
            
            JSONObject json = null;
            
            Long payId = null;
            String thirdOrderId = null;
            // 支付宝
            if (payChannel == 1) {
                
            	payId = settlementService.insertPayRecord(orderId, 1, bMoney, 1);
                
            	thirdOrderId = genPrefix4Order("HOrder", String.valueOf(payId));
                
                json = alipay(thirdOrderId, sum, body);

            } else if (payChannel == 2) {
                
            	payId = settlementService.insertPayRecord(orderId, 1, bMoney, 2);
                
            	thirdOrderId = genPrefix4Order("HOrder", String.valueOf(payId));
                
                json = weixinpay(thirdOrderId, sum, body, ip);

                
            }
            log.info("调用第三方支付完毕.json:{}", json.toString());
            
            openResponse.setResult(Constants.SUCCESS);
            openResponse.setData(json);    
                
                
        } catch (Exception e) {
            log.error("支付错误", e);
            
            openResponse.setResult(Constants.FAIL);
            openResponse.setErrorCode(ErrorEnum.accntPayError.getId());
            openResponse.setErrorMessage(ErrorEnum.accntPayError.getName());
            
        } finally {
            
            redisCacheManager.releaseLock(redisKey, redisValue);
        }
    	
    	return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
    }
    
    @RequestMapping(value="/recharge")
    @ResponseBody
    public ResponseEntity<OpenResponse<Object>> recharge(HttpServletRequest request, HttpServletResponse response, 
    		Integer payChannel, Long customerId, Integer sum) {
    	
    	String ip = IPUtil.getIpAddr(request);
    	
    	log.info("充值调用, payChannel:{}, feeType:{}, customerId:{}, sum:{}, ip:{}", payChannel, customerId, sum, ip);
    	
    	String redisKey = null;
        String redisValue = null;
        
        OpenResponse<Object> openResponse = new OpenResponse<Object>();
        
        try {

        	redisKey = genPrefix4Order("R", customerId + DateUtil.dateToStr(new Date(), "yyyyMMdd"));
            
            String lockValue = redisCacheManager.tryLock(redisKey, 10);;
            if (StringUtils.isNotEmpty(lockValue)) {
                log.info("订单:" + redisKey + "获取分布锁成功,继续执行充值流程.");
                
                redisValue = lockValue;
            } else {
                log.info("订单:" + redisKey + "获取分布锁失败,返回.");
                
                openResponse.setResult(Constants.FAIL);
                openResponse.setErrorCode(ErrorEnum.accntPaying.getId());
                openResponse.setErrorMessage(ErrorEnum.accntPaying.getName());
                return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
            }

            String body = "储值账户充值";
            
            JSONObject json = null;
            
            Long payId = null;
            String thirdOrderId = null;
            
            Long rechargeOrderId = System.currentTimeMillis();
            BigDecimal bMoney = new BigDecimal(sum).divide(dividend).setScale(4, BigDecimal.ROUND_HALF_UP);
            // 支付宝
            if (payChannel == 1) {
                
            	payId = settlementService.insertPayRecord(rechargeOrderId, 2, bMoney, 1);
                
            	thirdOrderId = genPrefix4Order("HOrder", String.valueOf(payId));
                
                json = alipay(thirdOrderId, sum, body);

            } else if (payChannel == 2) {
                
            	payId = settlementService.insertPayRecord(rechargeOrderId, 2, bMoney, 2);
                
            	thirdOrderId = genPrefix4Order("HOrder", String.valueOf(payId));
                
                json = weixinpay(thirdOrderId, sum, body, ip);

                
            }
            log.info("调用第三方支付完毕.json:{}", json.toString());
            
            openResponse.setResult(Constants.SUCCESS);
            openResponse.setData(json);    
                
                
        } catch (Exception e) {
            log.error("充值错误", e);
            
            openResponse.setResult(Constants.FAIL);
            openResponse.setErrorCode(ErrorEnum.accntRechargeError.getId());
            openResponse.setErrorMessage(ErrorEnum.accntRechargeError.getName());
            
        } finally {
            
            redisCacheManager.releaseLock(redisKey, redisValue);
        }
    	
    	return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
    }
    
    @RequestMapping(value="/refund")
    @ResponseBody
    public ResponseEntity<OpenResponse<Object>> refund(HttpServletRequest request, HttpServletResponse response, Long orderId) {
    	
    	boolean r = false;
    	log.info("订单退款, orderId:{}", orderId);
    	
    	String redisKey = null;
        String redisValue = null;
        
        OpenResponse<Object> openResponse = new OpenResponse<Object>();
        
        try {

        	redisKey = genPrefix4Order("RH", orderId.toString());
            
            String lockValue = redisCacheManager.tryLock(redisKey, 10);;
            if (StringUtils.isNotEmpty(lockValue)) {
                log.info("订单:" + redisKey + "获取分布锁成功,继续执行退款流程.");
                
                redisValue = lockValue;
            } else {
                log.info("订单:" + redisKey + "获取分布锁失败,返回.");
                
                openResponse.setResult(Constants.FAIL);
                openResponse.setErrorCode(ErrorEnum.accntRefunding.getId());
                openResponse.setErrorMessage(ErrorEnum.accntRefunding.getName());
                return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
            }

            ScPay pay = settlementService.getPay(orderId);
            
            String thirdNo = pay.getThirdNo();
            BigDecimal sum = pay.getPrice();
            if(pay.getPayChannel() == 1) {
            	
            	log.info("订单:{}进行支付宝退款.", orderId);
            	r = AliPay.refund(orderId, thirdNo, sum);
            }
            
            if (pay.getPayChannel() == 2) {
            	
            	log.info("订单:{}进行微信退款.", orderId);
				r = WXPay.refund(orderId, thirdNo, sum.divide(sum).intValue());
			}
			log.info("订单:{}退款完毕, 结果:{}", orderId, r);
            
            if (r) {
				openResponse.setResult(Constants.SUCCESS);
			} else {
				openResponse.setResult(Constants.FAIL);
			}
			openResponse.setData(r);    
                
                
        } catch (Exception e) {
            log.error("退款错误", e);
            
            openResponse.setResult(Constants.FAIL);
            openResponse.setErrorCode(ErrorEnum.accntRefundFail.getId());
            openResponse.setErrorMessage(ErrorEnum.accntRefundFail.getName());
            
        } finally {
            
            redisCacheManager.releaseLock(redisKey, redisValue);
        }
    	
    	return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
    }
    
    @RequestMapping(value="/balance")
    @ResponseBody
    public ResponseEntity<OpenResponse<Object>> balance(HttpServletRequest request, HttpServletResponse response, Long customerId) {
    	
    	log.info("查询用户账户余额, customerId:{}", customerId);
    	
        OpenResponse<Object> openResponse = new OpenResponse<Object>();
        
        try {

        	BigDecimal balance = settlementService.getBanlance(customerId);
            
            openResponse.setResult(Constants.SUCCESS);
            openResponse.setData(balance);    
                
                
        } catch (Exception e) {
            log.error("充值错误", e);
            
            openResponse.setResult(Constants.FAIL);
            openResponse.setErrorCode(ErrorEnum.accntselectbug.getId());
            openResponse.setErrorMessage(ErrorEnum.accntselectbug.getName());
            
        }
    	
    	return new ResponseEntity<OpenResponse<Object>>(openResponse, HttpStatus.OK);
    }
    
    @RequestMapping(value="/pay/aliPayCallback")
    @SuppressWarnings("rawtypes")
    public void aliPayCallback(HttpServletRequest request, HttpServletResponse response) {
        
        log.info("支付宝回调...");
        String redisKey = null;
        String redisValue = null;
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                // valueStr = new String(valueStr.getBytes("ISO-8859-1"),
                // "gbk");
                params.put(name, valueStr);
            }

            Map<String, String> map = new HashMap<String, String>();

            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            
            String lockValue = redisCacheManager.tryLock(out_trade_no, 20);;
            if (StringUtils.isNotEmpty(lockValue)) {
                log.info("订单:" + out_trade_no + "获取分布锁成功,继续执行支付宝回调流程.");
                redisKey = out_trade_no;
                redisValue = lockValue;
            } else {
                log.info("订单:" + out_trade_no + "获取分布锁失败,中断支付宝回调流程,返回.");
                
                return;
            }
            
            map.put("out_trade_no", out_trade_no);
            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            map.put("trade_no", trade_no);
            // 买家支付宝账号
            String buyer_email = new String(request.getParameter("buyer_email").getBytes("ISO-8859-1"), "UTF-8");
            map.put("buyer_email", buyer_email);
            // 买家支付宝id
            String buyer_id = new String(request.getParameter("buyer_id").getBytes("ISO-8859-1"), "UTF-8");
            map.put("buyer_id", buyer_id);
            // 交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            map.put("trade_status", trade_status);
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            log.info("支付宝返回：" + params);
            log.info("返回的参数：" + map.toString());
            if (AlipayNotify.verify(params)) {// 验证成功

                if (trade_status.equals("TRADE_SUCCESS")) {
                    
                    ScPay pay = settlementService.getPay(out_trade_no);
                    
                    if(pay == null) {
                        log.error("支付宝支付回调, 业务单号:{}没有查询到支付记录.", out_trade_no);
                    } else {
                        pay.setBuyerId(buyer_id);
                        pay.setBuyerMail(buyer_email);
                        pay.setThirdNo(trade_no);
                        pay.setCallbackTime(new Date());
                        pay.setPayNo(out_trade_no);
                        
                        settlementService.updatePayRecord(pay);
                        
                    }

                } else {
                    log.info("支付宝支付失败, TRADE_SUCCESS:{}", trade_status);
                }
                out.println("success"); // 请不要修改或删除
            } else {// 验证失败
                log.info("支付宝回调签名验证失败！");
                out.println("fail");

            }
        } catch (Exception e) {
            log.error("支付宝回调异常!", e);
            try {
                out.println("fail");
            } catch (IOException e1) {
                
                log.error("返回失败信息异常!", e);
            }
        } finally {
            
            redisCacheManager.releaseLock(redisKey, redisValue);
            
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("关闭输出流失败!", e);
                }
            }
        }
    }
    
    /**
     * 微信支付回调
     * @param request
     * @param response
     */
    @RequestMapping(value="/pay/weChatCallback")
    public void weChatCallback(HttpServletRequest request, HttpServletResponse response) {
        
        log.info("微信回调...");
        String SUCCESSXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        String errorXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[f]]></return_msg></xml>";
        ServletInputStream in = null;
        ServletOutputStream out = null;
        
        String redisKey = null;
        String redisValue = null;
        String xml = errorXml;
        try {
            in = request.getInputStream();
            out = response.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            log.info("微信回调：" + sb.toString());
            Map<String, Object> map = XMLParser.getMapFromXML(sb.toString());
            
            // 订单号
            String out_trade_no = map.get("out_trade_no").toString();
            
            String lockValue = redisCacheManager.tryLock(out_trade_no, 20);;
            if (StringUtils.isNotEmpty(lockValue)) {
                log.info("订单:" + out_trade_no + "获取分布锁成功,继续执行微信回调流程.");
                redisKey = out_trade_no;
                redisValue = lockValue;
            } else {
                log.info("订单:" + out_trade_no + "获取分布锁失败,微信回调流程中断,返回.");
                
                return;
            }
            
            if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
                String sign = map.get("sign").toString();
                map.put("sign", "");
                // 验证签名
                if (sign.equals(Signature.getSign(map))) {
                    
                    // 微信订单号
                    String transaction_id = map.get("transaction_id").toString();

                    String openid = map.get("openid").toString();
                     
                    ScPay pay = settlementService.getPay(out_trade_no);
                    
                    if(pay == null) {
                        log.error("微信支付回调, 业务单号:{}没有查询到支付记录.", out_trade_no);
                    } else {
                    	
                        pay.setBuyerId(openid);
                        pay.setThirdNo(transaction_id);
                        pay.setBankType(map.get("bank_type").toString());
                        pay.setCallbackTime(new Date());
                        pay.setPayNo(out_trade_no);
                        
                        settlementService.updatePayRecord(pay);
                        
                    }
                    
                    xml = SUCCESSXml;
                } else {
                    log.error("微信回调签名验证失败!");
                }
            } else {
                log.info("微信支付失败, return_code:{}, result_code:{}, err_code:{}, err_code_des:{}", map.get("return_code"), map.get("result_code"), 
                        map.get("err_code"), map.get("err_code_des"));
            }

            out.println(xml);

        } catch (Exception e) {
            log.error("微信回调处理异常!", e);
            try {
                out.println(xml);
            } catch (IOException e1) {
                log.error("返回失败信息异常!", e);
            }
        } finally {
            
            redisCacheManager.releaseLock(redisKey, redisValue);
            
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("关闭输出流失败!", e);
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("关闭输入流失败!", e);
                }
            }
        }
    }
    
    private String genPrefix4Order(String prifix, String orderId) {
        
        if(PayConfig.ENVIRONMENT.endsWith("PRO")) {
            
            return prifix + "_" + orderId;
           
       }
        
        return prifix + "T_" + orderId;
       
   }
    
    /**
     * 支付宝支付
     * @param orderid 订单号
     * @param fee 金额 分
     * @param body 描述
     * @return
     */
    private JSONObject alipay(String orderid,int fee, String body){
        
    	return AliPay.pay(orderid, fee, body);
    }
    
    /**
     *  微信支付
     * @param orderid 订单号
     * @param fee 金额
     * @param body 描述
     * @param ip ip
     * @return
     * @throws Exception
     */
    private JSONObject weixinpay(String orderid,int fee, String body,String ip) throws Exception {
        
        JSONObject json=new JSONObject();
        Map<String, Object> map= WXPay.requestScanPayService(body, orderid, fee, ip);
        json=JSONObject.fromObject(map);
        
        return json;
    }
    
}
