package com.fangbaba.api.kafka.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.fangbaba.api.domain.fangcang.Response;
import com.fangbaba.api.enums.FangCangOrderStatusEnum;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.service.IFangCangOrderService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.order.model.MultipleOrder;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicConsumer;

/**
 * zhangyajun
 * description:
 */
@MkMessageService
public class OrderStatusPushConsumer {

    private Logger logger = LoggerFactory.getLogger(OrderStatusPushConsumer.class);

    @Autowired
	private BusinessUtil businessUtil;
    @Autowired
    private IFangCangOrderService fangCangOrderService;
    @Autowired
	private IDistributorConfigService iDistributorConfigService;
    private Gson gson = new Gson();
    /**
     * 创建订单的订单状态推送
     *
     * @param multipleOrder
     */
    @MkTopicConsumer(topic = "createOrderOk", group = "api_createOrderOk", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
    public void createOrderOk(MultipleOrder multipleOrder) {
    	logger.info("创建订单的订单状态推送传来的参数:"+gson.toJson(multipleOrder));
    	RetInfo<Integer> retInfo = checkParam(multipleOrder);
    	if (!retInfo.isResult()) {
			return ;
		}
		int channelId = retInfo.getObj();
	    if (ChannelEnum.imike.getId()==channelId) {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("orderid", multipleOrder.getOrder().getId());
	    	jsonObject.put("orderstatus", multipleOrder.getOrder().getStatus());
    		orderStatusPush(jsonObject.toString(),channelId);
		}
	    if (ChannelEnum.getById(channelId)!=null){
	    logger.info("[{}]创建订单的订单状态推送结束...",ChannelEnum.getById(channelId).getName());
	    }
    }
   
    /**
     * 确认订单
     * @param multipleOrder
     */
    @MkTopicConsumer(topic = "order_confirmedOrder", group = "api_order_confirmedOrder", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
    public void confirmedOrder(MultipleOrder multipleOrder) {
    	logger.info("确认订单的订单状态推送传来的参数:"+gson.toJson(multipleOrder));
    	RetInfo<Integer> retInfo = checkParam(multipleOrder);
    	if (!retInfo.isResult()) {
			return ;
		}
		int channelId = retInfo.getObj();
		if (ChannelEnum.fangcang.getId()==channelId) {
    		//推送到房仓订单变化
    		fangCangOrderService.syncOrderStatus(multipleOrder.getOrder().getId().toString(), FangCangOrderStatusEnum.confirmed.getId());
		}else if(ChannelEnum.imike.getId()==channelId) {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("orderid", multipleOrder.getOrder().getId());
	    	jsonObject.put("orderstatus", multipleOrder.getOrder().getStatus());
    		orderStatusPush(jsonObject.toString(),channelId);
		}
		if (ChannelEnum.getById(channelId)!=null) {
			 logger.info("[{}]确认订单的订单状态推送结束...",ChannelEnum.getById(channelId).getName());
		}
		
	    }
   
    /**
     * 取消订单
     * @param multipleOrder
     */
    @MkTopicConsumer(topic = "cancelOrderDetailOK", group = "api_cancelOrderDetailOK", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
    public void cancelOrderDetailOK(MultipleOrder multipleOrder) {
    	logger.info("取消订单的订单状态推送传来的参数:"+gson.toJson(multipleOrder));
    	
    	RetInfo<Integer> retInfo = checkParam(multipleOrder);
    	if (!retInfo.isResult()) {
			return ;
		}
		int channelId = retInfo.getObj();
	    if (ChannelEnum.fangcang.getId()==channelId) {
    		//推送到房仓订单变化
    		fangCangOrderService.syncOrderStatus(multipleOrder.getOrder().getId().toString(), FangCangOrderStatusEnum.refused.getId());
		}else if(ChannelEnum.imike.getId()==channelId) {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("orderid", multipleOrder.getOrder().getId());
	    	jsonObject.put("orderstatus", multipleOrder.getOrder().getStatus());
    		orderStatusPush(jsonObject.toString(),channelId);
		}
		if (ChannelEnum.getById(channelId)!=null) {
			 logger.info("[{}]确认订单的订单状态推送结束...",ChannelEnum.getById(channelId).getName());
		}
	    
    }
   
   
   /**
    * 订完成的订单状态推送
    *
    * @param order
    */
  @MkTopicConsumer(topic = "Order_Status_Finished_MESS", group = "api_Order_Status_Finished_MESS", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
   public void OrderStatusFinishedMess(String order) {
	 logger.info("完成的订单状态推送传来的参数:"+gson.toJson(order));
	 JSONObject jsonObject = JSONObject.parseObject(order);
	 Integer channelId = jsonObject.getInteger("channelid");
	 if (channelId==null) {
		return ;
	 }
	 Long orderid =jsonObject.getLong("id");
	 Integer orderstatus =jsonObject.getInteger("status");
	 if (orderid==null) {
		 return ;
	 }
	 if (orderstatus==null) {
		 return ;
	 }
     if(ChannelEnum.imike.getId()==channelId) {
	     jsonObject.put("orderid", orderid);
	     jsonObject.put("orderstatus",orderstatus);
  		 orderStatusPush(jsonObject.toString(),channelId);
		}
		if (ChannelEnum.getById(channelId)!=null) {
			 logger.info("[{}]确认订单的订单状态推送结束...",ChannelEnum.getById(channelId).getName());
		}
   }
  
 /**
  * 订单入住
  * @param multipleOrder
  */
  @MkTopicConsumer(topic = "order_statusCheckIn", group = "api_order_statusCheckIn", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
  public void order_statusCheckIn(MultipleOrder multipleOrder) {
	  logger.info("订单入住状态推送传来的参数:"+gson.toJson(multipleOrder));
  	RetInfo<Integer> retInfo = checkParam(multipleOrder);
  	if (!retInfo.isResult()) {
			return ;
		}
		int channelId = retInfo.getObj();
	    if (ChannelEnum.imike.getId()==channelId) {
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("orderid", multipleOrder.getOrder().getId());
	    	jsonObject.put("orderstatus", multipleOrder.getOrder().getStatus());
  		orderStatusPush(jsonObject.toString(),channelId);
		}
	    if (ChannelEnum.getById(channelId)!=null){
	    logger.info("[{}]订单入住状态推送结束...",ChannelEnum.getById(channelId).getName());
	    }
	
  }
  
	  private RetInfo<Integer> checkParam(MultipleOrder multipleOrder){
		  RetInfo<Integer> retInfo = new RetInfo<Integer>();
		  if (multipleOrder==null) {
					logger.info("订单状态推送传来的参数为空");
					retInfo.setResult(false);
			 }
		  if (multipleOrder.getOrder()==null) {
					logger.info("订单状态推送传来的order对象为空");
					retInfo.setResult(false);
			}
		  if (multipleOrder.getOrder().getChannelid()==null) {
		  	logger.info("订单状态推送传来的otatype为空");
		  	retInfo.setResult(false);
			}
		  if (multipleOrder.getOrder().getId()==null) {
		  	logger.info("订单状态推送传来的渠订单id为空");
		  	retInfo.setResult(false);
			}
		  DistributorConfig distributorConfig=iDistributorConfigService.queryByOtaType(multipleOrder.getOrder().getChannelid());
			if (distributorConfig==null||distributorConfig.getChannelid()==null) {
				logger.info("根据order中channelid:{}获取该ota的渠道id为空",multipleOrder.getOrder().getChannelid());
				retInfo.setResult(false);
			}
			logger.info("获取channelid:{}",distributorConfig.getChannelid());
			retInfo.setResult(true);
			retInfo.setObj(distributorConfig.getChannelid());
			return retInfo;
	  }
	  private void orderStatusPush(String json,Integer channelId){
			businessUtil.push(GdsChannelUrlEnum.pushOrderStatus.getId(), json,channelId,null,null);
	  }
  
  
}
