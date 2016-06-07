package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.utils.Json;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.OrderExtend;
import com.fangbaba.api.domain.qunar.EveryDayPrice;
import com.fangbaba.api.domain.qunar.QunarOptOrderRequest;
import com.fangbaba.api.domain.qunar.QunarOptOrderResponse;
import com.fangbaba.api.domain.qunar.QunarOrderRequest;
import com.fangbaba.api.domain.qunar.QunarOrderResponse;
import com.fangbaba.api.domain.qunar.QunarResponse;
import com.fangbaba.api.enums.QunarOrderOptEnum;
import com.fangbaba.api.enums.QunarOrderSettlementEnum;
import com.fangbaba.api.enums.QunarOrderStatusMatchEnum;
import com.fangbaba.api.enums.QunarPayTypeEnum;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.bean.OptOrder;
import com.fangbaba.api.face.bean.Order;
import com.fangbaba.api.face.bean.OrderDetail;
import com.fangbaba.api.face.bean.RoomPrice;
import com.fangbaba.api.util.Config;
import com.fangbaba.api.util.DateUtil;
import com.fangbaba.api.util.HttpUtil;
import com.fangbaba.api.util.MD5Util;
import com.fangbaba.api.util.PostUtils;
import com.fangbaba.gds.enums.ChannelEnum;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


@Service
public class QunarOrderServiceImpl {
	private static Logger logger = LoggerFactory.getLogger(QunarOrderServiceImpl.class);
	
	
	private static final String PATTERN = "yyyyMMdd"; 
	private static final String PRICE_PATTERN = "yyyy-MM-dd"; 
	private static final String PATTERN_TIME = "yyyyMMddHHmmss"; 
	/**
	 * 查询去哪儿订单
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public RetInfo<Order> queryQunarOrder(Integer channelId ,Long otatype,Date beginTime,Date endTime){ 
		RetInfo<Order> retInfo = new RetInfo<Order>();

		try {
	//		QunarRequest<QunarOrderRequest> request = new QunarRequest<QunarOrderRequest>();
			
			
			QunarOrderRequest data = new QunarOrderRequest();
			data.setFromDate(DateUtil.dateToStr(beginTime, PATTERN_TIME));
			data.setToDate(DateUtil.dateToStr(endTime, PATTERN_TIME));
			
			
			data.setVersion(Config.getValue("qunar.version"));
			
			/**
			 * 对参数利用一个SignKey进行md5编码并转换为一个十六进制32位的字符串
				获取订单信息接口hmac=md5(Signkey+fromDate+toDate+version)
				a)	hmac生成说明：首先按顺序将Signkey和参数fromDate、toDate、version组装为一个字符串（如果某个参数为空，则组装后的字符串不包含该参数），然后使用md5算法对该字符串生成hmac。必须严格按照以上顺序组装
				b)	如果某个参数为空，则不添加到加密字符串中即可。
			 */
			String signKey = Config.getValue("qunar.signKey");
			String hmac = signKey+data.getFromDate()+data.getToDate()+data.getVersion();
			data.setHmac(MD5Util.encryption(hmac));
			
			logger.info("订单加密参数：{}",new Gson().toJson(data));
			
	//		request.setData(data);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("signKey", signKey);
			params.put("hmac", data.getHmac());
			params.put("version", data.getVersion());
			params.put("fromDate", data.getFromDate());
			params.put("toDate", data.getToDate());
			logger.info("查询订单入参："+new Gson().toJson(params));
			String result = null;
			try {
				result = HttpUtil.doPost(Config.getValue("qunar.queryOrder.url"), params, 10000);
			} catch (Exception e) {
				logger.error("调用去哪儿失败",e);
			}
	//		String result = PostUtils.postToUrl(new Gson().toJson(data), Config.getValue("qunar.queryOrder.url"));
	//		String result = "{\"ret\":true,\"errCode\":\"SUCCESS\",\"errMsg\":\"SUCCESS\",\"data\":[{\"remark\":[],\"promotionCode\":\"NONE\",\"cityName\":\"北京\",\"changeRule\":0,\"hotelName\":\"三亚宝宏龙都大酒店\",\"roomName\":\"测试关房\",\"request\":\"\",\"serialVersionUID\":5368151272052259000,\"guestType\":\"内宾\",\"orderDate\":\"20150415122435\",\"payTypeMsg\":\"预付\",\"orderNum\":\"100401749971\",\"everyDayPrice\":[{\"date\":\"2015-04-15\",\"price\":\"90.00\",\"deposit\":0,\"roomStatus\":0,\"priceCut\":\"0\"}],\"contactName\":\"威锋网啊\",\"breakfast\":\"无早\",\"roomId\":\"29995\",\"hotelId\":\"2813\",\"statusCode\":7,\"roomNum\":2,\"contactPhoneKey\":\"5057\",\"foreignCurrencyAmount\":\"180.00\",\"needInvoice\":false,\"customerName\":\"威锋网啊|威锋网啊啊\",\"contactPhone\":\"186****5057\",\"checkInDate\":\"20150415\",\"checkOutDate\":\"20150416\",\"contactEmail\":\"chp***@163.com\",\"logs\":[{\"operator\":\"QAtest:005\",\"opTime\":\"20150415122802\",\"content\":\"QAtest:005申请订单100401749971退款,退款金额:180元,退款原因:满房\"},{\"operator\":\"系统\",\"opTime\":\"20150415122642\",\"content\":\"订单状态流转为:已确认\"},{\"operator\":\"QAtest:005\",\"opTime\":\"20150415122641\",\"content\":\"QAtest:005安排订单100401749971房间\"},{\"operator\":\"系统\",\"opTime\":\"20150415122627\",\"content\":\"支付单状态流转为:支付成功\"},{\"operator\":\"系统\",\"opTime\":\"20150415122438\",\"content\":\"订单状态流转为:待确认\"},{\"operator\":\"confirm-system\",\"opTime\":\"20150415122437\",\"content\":\"确认单状态流转为:待确认\"},{\"operator\":\"FLOW_ENGINE\",\"opTime\":\"20150415122436\",\"content\":\"订单状态流转为:初始确认\"},{\"operator\":\"jtbj9205\",\"opTime\":\"20150415122435\",\"content\":\"创建订单\"}],\"currencyType\":\"CNY\",\"isVouch\":2,\"customerIp\":\"192.168.113.26\",\"statusMsg\":\"支付成功确认可住\",\"payTypeCode\":0,\"invoiceFee\":0,\"payMoney\":180,\"arriveTime\":\"18:00\",\"bedType\":\"双床\",\"maxOccupancy\":0,\"channel\":\"普通业务\",\"bedTypePreference\":\"\",\"arriving\":false,\"promotionList\":[{\"validType\":1,\"name\":\"免费送水果\",\"description\":\"入住时，派发柚子\"},{\"validType\":2,\"name\":\"100元-动物园门票\",\"description\":\"动物园门票\"}]},{\"remark\":[],\"promotionCode\":\"NONE\",\"cityName\":\"北京\",\"changeRule\":0,\"hotelName\":\"三亚宝宏龙都大酒店\",\"roomName\":\"测试关房\",\"request\":\"\",\"serialVersionUID\":5368151272052259000,\"guestType\":\"内宾\",\"orderDate\":\"20150415122500\",\"payTypeMsg\":\"预付\",\"orderNum\":\"100401887581\",\"everyDayPrice\":[{\"date\":\"2015-04-15\",\"price\":\"90.00\",\"deposit\":null,\"roomStatus\":0,\"priceCut\":\"0\"}],\"contactName\":\"威锋网啊\",\"breakfast\":\"无早\",\"roomId\":\"29995\",\"hotelId\":\"2813\",\"statusCode\":2,\"roomNum\":1,\"contactPhoneKey\":\"3212\",\"foreignCurrencyAmount\":\"90.00\",\"needInvoice\":false,\"customerName\":\"威锋网啊\",\"contactPhone\":\"134****3212\",\"checkInDate\":\"20150415\",\"checkOutDate\":\"20150416\",\"logs\":[{\"operator\":\"FLOW_ENGINE\",\"opTime\":\"20150415122501\",\"content\":\"订单状态流转为:初始确认\"},{\"operator\":\"confirm-system\",\"opTime\":\"20150415122500\",\"content\":\"确认单状态流转为:待确认\"},{\"operator\":\"jtbj9205\",\"opTime\":\"20150415122500\",\"content\":\"创建订单\"}],\"currencyType\":\"CNY\",\"isVouch\":2,\"customerIp\":\"192.168.113.26\",\"statusMsg\":\"等待房间确认\",\"payTypeCode\":0,\"invoiceFee\":0,\"payMoney\":90,\"arriveTime\":\"18:00\",\"bedType\":\"双床\",\"maxOccupancy\":0,\"channel\":\"普通业务\",\"bedTypePreference\":\"\",\"arriving\":false}],\"totalSize\":2}";
			logger.info("查询订单回参："+result);
			if(StringUtils.isNotBlank(result)){
				QunarResponse<List<QunarOrderResponse>>  qunarResponse = new Gson().fromJson(result,new TypeToken<QunarResponse<List<QunarOrderResponse>>>() {
				}.getType());
				logger.info("成功");
				if(qunarResponse.isRet()){
					retInfo.setResult(qunarResponse.isRet());
					List<Order> list = new ArrayList<Order>();
					
					
					for (QunarOrderResponse qunarOrderResponse : qunarResponse.getData()) {
						logger.info("订单号：{},状态{}，时间：{}",qunarOrderResponse.getOrderNum(),qunarOrderResponse.getStatusCode(),qunarOrderResponse.getOrderDate());
						Order order = new Order();
						order.setHotelid(qunarOrderResponse.getHotelId());
						order.setHotelname(qunarOrderResponse.getHotelName());
						order.setBegintime(DateUtil.strToDate(qunarOrderResponse.getCheckInDate(),PATTERN));
						order.setEndtime(DateUtil.strToDate(qunarOrderResponse.getCheckOutDate(),PATTERN));
						order.setCreatetime(DateUtil.strToDate(qunarOrderResponse.getOrderDate(),PATTERN_TIME));
						order.setTotalprice(qunarOrderResponse.getRoomFee());
						order.setChannelid(otatype);
						order.setContacts(qunarOrderResponse.getCustomerName());
						order.setContactsphone(qunarOrderResponse.getContactPhone());
						order.setCustomerName(qunarOrderResponse.getCustomerName());
						order.setChannelname(ChannelEnum.getById(channelId).getName());
						order.setOrderno(qunarOrderResponse.getOrderNum());
						
						
						OrderExtend extend = new OrderExtend();
						extend.setChannelId(channelId);
						extend.setScType(1);//TODO:暂时写死
						order.setExtend(new Gson().toJson(extend));
						
//						order.setRequest(qunarOrderResponse.getRequest());
						//TODO:写死
						order.setRequest("测试备注");
						order.setRemark(qunarOrderResponse.getRemark());
						
						//状态转码
						QunarOrderStatusMatchEnum enum1 = QunarOrderStatusMatchEnum.findByCode(qunarOrderResponse.getStatusCode());
						if(enum1!=null){
							order.setStatus(enum1.getOrderStatusEnum().getId());
						}
						order.setQunarStatus(qunarOrderResponse.getStatusCode());
						
						//TODO:订单类型转码
						order.setOrdertype(null);
						//支付类型
						QunarPayTypeEnum enum2 = QunarPayTypeEnum.findByCode(qunarOrderResponse.getPayTypeCode());
						if(enum2!=null){
							order.setPaytype(enum2.getCode());
						}
						order.setLastUpdateTime(qunarOrderResponse.getLastUpdateTime());
						
						List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
						//封装房型信息
						OrderDetail detail = new OrderDetail();
						detail.setRoomTypeId(qunarOrderResponse.getRoomId());
						detail.setNum(qunarOrderResponse.getRoomNum());
						detail.setTotalPrice(qunarOrderResponse.getPayMoney());
						
						
						List<RoomPrice> prices = new ArrayList<RoomPrice>();
						
//						String everyDayPriceStr = qunarOrderResponse.getEveryDayPrice();
						//TODO:测试
						String everyDayPriceStr = "[{\"date\":\""+DateUtil.dateToStr(new Date(), "yyyy-MM-dd")+"\",\"price\":\"545\",\"deposit\":null,\"roomStatus\":0,\"priceCut\":\"0\"}]";
						
						List<EveryDayPrice> everyDayPrices  =new Gson().fromJson(everyDayPriceStr, new TypeToken<List<EveryDayPrice>>() {
						}.getType());
						
						for (EveryDayPrice everyDayPrice : everyDayPrices) {
							RoomPrice price = new RoomPrice();
							price.setPrice(everyDayPrice.getPrice());
							price.setActiondate(DateUtil.strToDate(everyDayPrice.getDate(), PRICE_PATTERN));
							prices.add(price);
						}
						detail.setPrices(prices);
						orderDetails.add(detail);
						order.setDetails(orderDetails);
						list.add(order);
						
					}
					retInfo.setList(list);
					
				}else{
					retInfo.setResult(false);
					retInfo.setMsg(qunarResponse.getErrMsg());
				}
				
			}else{
				logger.info("查询去哪儿订单列表为空");
				retInfo.setResult(false);
				retInfo.setMsg("查询去哪儿订单列表为空");
			}
		} catch (Exception e) {
			logger.error("query qunar order fail",e);

			retInfo.setResult(false);
			retInfo.setMsg("查询去哪儿订单失败");
		
		}
		
		return retInfo;
		
	
	}
	
	
	
	/**
	 * 操作订单
	 * 订单操作的认证签名，
hmac=md5(signkey+orderNum+opt+操作参数)
a)	对参数利用一个signKey进行md5编码并转换为一个十六进制32位的字符串 ，参数加密顺序signKey,orderNum,opt,以及opt操作相关参数，opt操作相关参数有： （money,arrangeType，confirmationNumber，faxSendPartnerName、faxReceiveFaxNumber、faxSendType、prices、roomChargeSettlement、otherChargeSettlement）
	 */
	public RetInfo<Order> optOrder(Long otaType,OptOrder optOrder){
		RetInfo<Order> retInfo = new RetInfo<Order>();
//		QunarRequest<QunarOptOrderRequest> request = new QunarRequest<QunarOptOrderRequest>();
		
		
		try {
			QunarOptOrderRequest data = new QunarOptOrderRequest();
//		data.setHmac(Config.getValue("qunar.token"));
			
			QunarOrderOptEnum enum1 = QunarOrderOptEnum.findByOrderOptEnum(optOrder.getOpt());
			if(enum1!=null){
				data.setOpt(enum1.getCode());
			}
			
			//此处需要进行状态转换 转换结算状态
			QunarOrderSettlementEnum enum2 = QunarOrderSettlementEnum.findByOrderSettlementEnum(optOrder.getRoomChargeSettlement());
			if(enum2!=null){
				data.setRoomChargeSettlement(enum2.getCode());
			}
			
			//加密hmac
			String signKey = Config.getValue("qunar.signKey");
			String hmac = signKey+optOrder.getOrderNum()+data.getOpt();
			if(StringUtils.isNotBlank(data.getRoomChargeSettlement())){
				hmac+=data.getRoomChargeSettlement();
			}
			logger.info("加密前,{}",hmac);
			data.setHmac(MD5Util.encryption(hmac));
			logger.info("加密后,{}",data.getHmac());
			
//		request.setVersion(Config.getValue("qunar.version"));
			data.setOrderNum(optOrder.getOrderNum());
			//TODO:此处需要进行状态转换
			data.setArrangeType(optOrder.getArrangeType());
			//TODO:此处需要进行状态转换
			data.setConfirmationNumber(optOrder.getConfirmationNumber());
			//此处需要进行状态转换  转换订单状态
			
			//TODO:此处需要进行状态转换
			data.setOtherChargeSettlement(optOrder.getOtherChargeSettlement());
			
			data.setSmsContent(optOrder.getSmsContent());
			
//		request.setData(data);
			logger.info("操作订单入参：{}",new Gson().toJson(data));
			
			String strdata = "{  \"orderNum\": \"24810314434753952\",  \"opt\": \"CONFIRM_ROOM_SUCCESS\","
					+ "  \"hmac\": \"baedceb0821c9fd063bdc72d8feb4e70\","
					+ "  \"arrangeType\": \"\","
					+ "  \"confirmationNumber\": \"\","
					+ "  \"money\": \"\","
					+ "  \"remark\": \"\","
					+ "  \"faxSendPartnerName\": \"\","
					+ "  \"faxStamp\": \"\","
					+ "  \"faxReceiveName\": \"\","
					+ "  \"faxReceiveFaxNumber\": \"\","
					+ "  \"faxSender\": \"\","
					+ "  \"faxSenderTelNumber\": \"\","
					+ "  \"faxReceiver\": \"\","
					+ "  \"faxReceiverTelNumber\": \"\","
					+ "  \"faxSendType\": \"\","
					+ "  \"prices\": \"\","
					+ "  \"roomChargeSettlement\": \"\","
					+ "  \"otherChargeSettlement\": \"\","
					+ "  \"smsContent\": \"\"}";
			
			
			String result = null;
			try {
				result = HttpUtil.doPost(Config.getValue("qunar.optOrder.url"), new Gson().fromJson(new Gson().toJson(data),Map.class), 100000);
			} catch (Exception e) {
				logger.error("操作订单异常",e);
				
			}
			
//		String result = PostUtils.postToUrl(new Gson().toJson(data), Config.getValue("qunar.optOrder.url"));
//		String result ="{\"ret\":false,\"statusCode\":0,\"errorMsg\":[\"订单状态不满足当前操作\"],\"errCode\":\"ERR0014\"}";
//		String result = PostUtils.postToUrl(strdata, Config.getValue("qunar.optOrder.url"));
			logger.info("操作订单返回："+result);
			if(StringUtils.isNotBlank(result)){
				QunarOptOrderResponse  qunarResponse = new Gson().fromJson(result, QunarOptOrderResponse.class);
				if(qunarResponse.isRet()){
					retInfo.setResult(qunarResponse.isRet());
				}else{
					retInfo.setResult(false);
					StringBuffer mess = new StringBuffer("");
					if (qunarResponse.getErrorMsg()!=null) {
						for (String str : qunarResponse.getErrorMsg()) {
							mess.append(","+str);
						}
					}
					logger.info("操作订单返回错误code:{},错误信息:{}",qunarResponse.getErrCode(),mess.toString());
					retInfo.setCode(qunarResponse.getErrCode());
					if (!Strings.isNullOrEmpty(mess.toString())) {
						retInfo.setMsg(mess.toString().substring(1));
					}
					
				}
				
			}else{
				logger.info("查询去哪儿订单列表为空");
				retInfo.setResult(false);
				retInfo.setMsg("查询去哪儿订单列表为空");
			}
		} catch (Exception e) {
			logger.error("操作订单失败",e);
			retInfo.setResult(false);
			retInfo.setMsg("操作订单失败");
		}
		
		return retInfo;
	}

}
