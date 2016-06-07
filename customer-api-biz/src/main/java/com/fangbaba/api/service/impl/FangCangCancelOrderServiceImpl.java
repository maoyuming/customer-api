package com.fangbaba.api.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.common.Constants;
import com.fangbaba.api.domain.fangcang.order.CancelHotelOrderResponse;
import com.fangbaba.api.domain.fangcang.order.CancelOrderRequest;
import com.fangbaba.api.domain.fangcang.order.CancelOrderResponse;
import com.fangbaba.api.domain.fangcang.order.OrderStatusQueryResponse;
import com.fangbaba.api.enums.FangCangCancelOrderStatusEnum;
import com.fangbaba.api.enums.FangCangOrderStatusEnum;
import com.fangbaba.api.enums.FangCangResultEnum;
import com.fangbaba.api.enums.FangCangResultFlagEnum;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.service.IOrderService;
import com.fangbaba.api.service.FangCangCancelOrderService;
import com.fangbaba.api.service.FangCangOrderService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.order.common.enums.OrderStatusEnum;
import com.google.gson.Gson;

/**
 * 取消订单
 * @author tankai
 *
 */
@Service
public class FangCangCancelOrderServiceImpl implements FangCangCancelOrderService{

	private static Logger logger = LoggerFactory.getLogger(FangCangCancelOrderServiceImpl.class);
	
	@Autowired
	private BusinessUtil<CancelOrderRequest> businessUtil;
	@Autowired
	private BusinessUtil<CancelOrderResponse> businessUtilResponse;
	
	@Autowired
	private IOrderService iOrderService;
	@Autowired
	private IDistributorConfigService iDistributorConfigService;
	@Autowired
	private FangCangOrderService fangCangOrderService;

	
	/**
	 * 取消订单
	 * @param SpOrderId
	 * @param CancelReason
	 */
	public String cancelOrder(String xml) {
		CancelOrderResponse cancelOrderResponse = new CancelOrderResponse();
		try {
			logger.info("cancelOrder begin:{}",xml);
			if(StringUtils.isBlank(xml)){
				cancelOrderResponse.setResultMsg("入参为空");
				cancelOrderResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
				String xmlResult =  businessUtilResponse.genFangCangResponse(cancelOrderResponse,CancelOrderResponse.class.getName());
				return  xmlResult;
			}
			
			//封装xml信息
			CancelOrderRequest cancelOrderRequest = businessUtil.decodeRequestXml(xml,CancelOrderRequest.class);
			
			DistributorConfig distributorConfig = null; 
			List<DistributorConfig> list = 	iDistributorConfigService.queryByChannelId(ChannelEnum.fangcang.getId());
			if(CollectionUtils.isNotEmpty(list)){
				distributorConfig = list.get(0);
			}
			if(distributorConfig!=null&&cancelOrderRequest.getCancelHotelOrderRequest()!=null){
				RetInfo<Map> retInfo = iOrderService.cancelOrder(Long.valueOf(cancelOrderRequest.getCancelHotelOrderRequest().getSpOrderId()), 
						cancelOrderRequest.getCancelHotelOrderRequest().getCancelReason(), "fangcang", distributorConfig.getChannelid());
				if(retInfo.isResult() && MapUtils.isNotEmpty(retInfo.getObj())){
					Long orderId = (Long)retInfo.getObj().get(Constants.ORDER_ID);
					CancelHotelOrderResponse cancelHotelOrderResponse = new CancelHotelOrderResponse();
					
					cancelHotelOrderResponse.setSpOrderId(String.valueOf(orderId));
					//订单状态转换 订单组反馈的结果是只有取消成功状态
					Integer orderstatus = (Integer)retInfo.getObj().get(Constants.ORDER_STATUS);
					if(orderstatus.intValue()==OrderStatusEnum.channelCanceled.getId().intValue()
							/*
							||orderstatus.intValue()==OrderStatusEnum.pmsCanceled.getId().intValue()
							||orderstatus.intValue()==OrderStatusEnum.serviceCanceled.getId().intValue()
							||orderstatus.intValue()==OrderStatusEnum.customerServiceCanceled.getId().intValue()*/
							){
						cancelHotelOrderResponse.setCancelStatus(FangCangCancelOrderStatusEnum.cancelsuccess.getId().toString());
					}else{
						cancelHotelOrderResponse.setCancelStatus(FangCangCancelOrderStatusEnum.cancelfailure.getId().toString());
					}
					cancelOrderResponse.setCancelHotelOrderResponse(cancelHotelOrderResponse);
					cancelOrderResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
					
					//推送到房仓订单变化 取消成功
			    	fangCangOrderService.syncOrderStatus(String.valueOf(orderId), FangCangOrderStatusEnum.canceled.getId());
				}else{
					cancelOrderResponse.setResultMsg(retInfo.getMsg());
					cancelOrderResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
					
					//推送到房仓订单变化 取消失败
					fangCangOrderService.syncOrderStatus(cancelOrderRequest.getCancelHotelOrderRequest().getSpOrderId(), FangCangOrderStatusEnum.cancelfailure.getId());
				}
			}
			logger.info("cancelOrder result:{}",new Gson().toJson(cancelOrderResponse));
		} catch (Exception e) {
			logger.error("cancelOrder error:",e);
			cancelOrderResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
			cancelOrderResponse.setResultMsg("系统异常");
			
		}
		String xmlResult =  businessUtilResponse.genFangCangResponse(cancelOrderResponse,CancelOrderResponse.class.getName());
		return  xmlResult;
		
	}
	/**
	 * 取消订单
	 * TODO:test
	 * @param SpOrderId
	 * @param CancelReason
	 */
	public String cancelOrder(String xml,Long orderId) {

		CancelOrderResponse cancelOrderResponse = new CancelOrderResponse();
		try {
			logger.info("cancelOrder begin:{}",xml);
			if(StringUtils.isBlank(xml)){
				cancelOrderResponse.setResultMsg("入参为空");
				cancelOrderResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
				String xmlResult =  businessUtilResponse.genFangCangResponse(cancelOrderResponse,CancelOrderResponse.class.getName());
				return  xmlResult;
			}
			
			//封装xml信息
			CancelOrderRequest cancelOrderRequest = businessUtil.decodeRequestXml(xml,CancelOrderRequest.class);
			
			DistributorConfig distributorConfig = null; 
			List<DistributorConfig> list = 	iDistributorConfigService.queryByChannelId(ChannelEnum.fangcang.getId());
			if(CollectionUtils.isNotEmpty(list)){
				distributorConfig = list.get(0);
			}
			if(distributorConfig!=null&&cancelOrderRequest.getCancelHotelOrderRequest()!=null){
				RetInfo<Map> retInfo = iOrderService.cancelOrder(orderId, 
						cancelOrderRequest.getCancelHotelOrderRequest().getCancelReason(), "fangcang", distributorConfig.getChannelid());
				if(retInfo.isResult() && MapUtils.isNotEmpty(retInfo.getObj())){
					CancelHotelOrderResponse cancelHotelOrderResponse = new CancelHotelOrderResponse();
					
					cancelHotelOrderResponse.setSpOrderId(String.valueOf(orderId));
					//订单状态转换 订单组反馈的结果是只有取消成功状态
					Integer orderstatus = (Integer)retInfo.getObj().get(Constants.ORDER_STATUS);
					if(orderstatus.intValue()==OrderStatusEnum.channelCanceled.getId().intValue()/*
							||orderstatus.intValue()==OrderStatusEnum.pmsCanceled.getId().intValue()
							||orderstatus.intValue()==OrderStatusEnum.serviceCanceled.getId().intValue()
							||orderstatus.intValue()==OrderStatusEnum.customerServiceCanceled.getId().intValue()*/){
						cancelHotelOrderResponse.setCancelStatus(FangCangCancelOrderStatusEnum.cancelsuccess.getId().toString());
					}else{
						cancelHotelOrderResponse.setCancelStatus(FangCangCancelOrderStatusEnum.cancelfailure.getId().toString());
					}
					cancelOrderResponse.setCancelHotelOrderResponse(cancelHotelOrderResponse);
					cancelOrderResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
					
					//推送到房仓订单变化 取消成功
			    	fangCangOrderService.syncOrderStatus(String.valueOf(orderId), FangCangOrderStatusEnum.canceled.getId());
				}else{
					cancelOrderResponse.setResultMsg(retInfo.getMsg());
					cancelOrderResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
					
					//推送到房仓订单变化 取消失败
					fangCangOrderService.syncOrderStatus(cancelOrderRequest.getCancelHotelOrderRequest().getSpOrderId(), FangCangOrderStatusEnum.cancelfailure.getId());
				}
			}
			logger.info("cancelOrder result:{}",new Gson().toJson(cancelOrderResponse));
		} catch (Exception e) {
			logger.error("cancelOrder error:",e);
			cancelOrderResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
			cancelOrderResponse.setResultMsg("系统异常");
			
		}
		String xmlResult =  businessUtilResponse.genFangCangResponse(cancelOrderResponse,CancelOrderResponse.class.getName());
		return  xmlResult;
		
	
	}
	
    
}
