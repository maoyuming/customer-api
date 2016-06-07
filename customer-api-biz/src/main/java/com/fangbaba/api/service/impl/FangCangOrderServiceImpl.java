package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.fangcang.Response;
import com.fangbaba.api.domain.fangcang.order.CheckRoomAvailRequest;
import com.fangbaba.api.domain.fangcang.order.CheckRoomAvailResponse;
import com.fangbaba.api.domain.fangcang.order.CheckRoomAvailRootRequest;
import com.fangbaba.api.domain.fangcang.order.CheckRoomAvailRootResponse;
import com.fangbaba.api.domain.fangcang.order.GetOrderStatusResponse;
import com.fangbaba.api.domain.fangcang.order.OrderStatusQueryRequest;
import com.fangbaba.api.domain.fangcang.order.OrderStatusQueryResponse;
import com.fangbaba.api.domain.fangcang.order.OrderStatusRequest;
import com.fangbaba.api.domain.fangcang.order.SaleItem;
import com.fangbaba.api.domain.fangcang.order.SyncOrderStatusRequest;
import com.fangbaba.api.enums.FangCangCanBookEnum;
import com.fangbaba.api.enums.FangCangCanImmediateEnum;
import com.fangbaba.api.enums.FangCangDayCanBookEnum;
import com.fangbaba.api.enums.FangCangOrderStatusEnum;
import com.fangbaba.api.enums.FangCangOverDraftEnum;
import com.fangbaba.api.enums.FangCangPriceNeedCheckEnum;
import com.fangbaba.api.enums.FangCangRequestTypeEnum;
import com.fangbaba.api.enums.FangCangResultFlagEnum;
import com.fangbaba.api.enums.FangCangRoomStatusEnum;
import com.fangbaba.api.service.FangCangOrderService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.api.util.DateUtil;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.order.common.enums.CancelReasonEnum;
import com.fangbaba.order.common.enums.OrderStatusEnum;
import com.fangbaba.order.common.enums.PayTypeEnum;
import com.fangbaba.order.model.Base;
import com.fangbaba.order.model.CreateOrderDetail;
import com.fangbaba.order.model.CreateOrderRequest;
import com.fangbaba.order.model.Header;
import com.fangbaba.order.model.OtaCheckInUser;
import com.fangbaba.order.model.QueryOrderListResponse;
import com.fangbaba.order.model.Request;
import com.fangbaba.order.model.RoomTypeAvail;
import com.fangbaba.order.service.OrderService;
import com.google.gson.Gson;

@Service
public class FangCangOrderServiceImpl implements FangCangOrderService{

	private static Logger logger = LoggerFactory.getLogger(FangCangOrderServiceImpl.class);
	
	@Autowired
	private BusinessUtil<Response> businessUtil;
	
	@Autowired
	private BusinessUtil<OrderStatusQueryRequest> businessOrderStatusRequestUtil;
	
	@Autowired
	private BusinessUtil<OrderStatusQueryResponse> businessOrderStatusResponseUtil;
	
	@Autowired
	private BusinessUtil<CheckRoomAvailRootRequest> businessRoomAvailRequestUtil;
	
	@Autowired
	private BusinessUtil<CheckRoomAvailRootResponse> businessRoomAvailResponseUtil;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private IDistributorConfigService iDistributorConfigService;

	@Override
	public void syncOrderStatus(String spOrderId, int orderStatus) {
		try {
			logger.info("syncOrderStatus begin:{},{}",spOrderId,orderStatus);
			//封装xml信息
			OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
			SyncOrderStatusRequest syncOrderStatusRequest = new SyncOrderStatusRequest();
			syncOrderStatusRequest.setSpOrderId(spOrderId);
			syncOrderStatusRequest.setOrderStatus(orderStatus);
			
			orderStatusRequest.setSyncOrderStatusRequest(syncOrderStatusRequest);
			
			
			Map<String, String> param  = businessUtil.genFangCangRequest(orderStatusRequest, FangCangRequestTypeEnum.SyncOrderStatus);
			String responseXml = businessUtil.doPost(GdsChannelUrlEnum.SyncOrderStatus.getId(), param, ChannelEnum.fangcang.getId());
			Response response = businessUtil.decodeResponseXml(responseXml,Response.class);
			logger.info("syncOrderStatus result:{}",response.toString());
			if(response.isSuccess()){
				logger.info("syncOrderStatus success");
			}
		} catch (Exception e) {
			logger.error("syncOrderStatus error:",e);
		}
		
	}

	@Override
	public String getOrderStatus(String xml) {
		OrderStatusQueryResponse orderStatusResponse = new OrderStatusQueryResponse();
		try {
			logger.info("getOrderstatus begin:{}",xml);
			if(StringUtils.isBlank(xml)){
				orderStatusResponse.setResultMsg("入参为空");
				orderStatusResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
				String xmlResult =  businessOrderStatusResponseUtil.genFangCangResponse(orderStatusResponse,OrderStatusQueryResponse.class.getName());
				return  xmlResult;
			}
			
			//封装xml信息
			OrderStatusQueryRequest orderStatusQueryRequest = businessOrderStatusRequestUtil.decodeRequestXml(xml,OrderStatusQueryRequest.class);
			
			String spOrderId = orderStatusQueryRequest.getGetOrderStatusRequest().getSpOrderId();
			
			Base base = new Base();
			base.setOrderId(Long.parseLong(spOrderId));
			Header header = new Header();
			header.setTimeStamp(new Date());
			header.setToken("fangcang");
			Request<Base> baserequest = new Request<Base>();
		    baserequest.setHeader(header);
		    baserequest.setData(base);
		    com.fangbaba.order.model.Response<QueryOrderListResponse> queryOrderListResponseResponse = orderService.queryOrderById(baserequest);
		    logger.info("getOrderstatus order result:{}",new Gson().toJson(queryOrderListResponseResponse));
		    //3. 组织返回数据
		    
		    QueryOrderListResponse queryOrderListResponse = queryOrderListResponseResponse.getData();
		    GetOrderStatusResponse getOrderStatusResponse =new GetOrderStatusResponse();
		    
		    if (queryOrderListResponse.getOrderList() != null && queryOrderListResponse.getOrderList().size() > 0) {
		        com.fangbaba.order.model.Order order = queryOrderListResponse.getOrderList().get(0);
		        Integer status = order.getStatus();
		        Integer cancelreason = order.getCancelreason();
		        Long orderid = order.getId();
		        if(status.intValue()==OrderStatusEnum.toBeConfirmed.getId().intValue()){
		        	getOrderStatusResponse.setOrderStatus(FangCangOrderStatusEnum.operating.getId().toString());
		        	getOrderStatusResponse.setSpOrderId(orderid.toString());
		        	orderStatusResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
		        	orderStatusResponse.setGetOrderStatusResponse(getOrderStatusResponse);
		        }else if(status.intValue()==OrderStatusEnum.confirmed.getId().intValue() || status.intValue()==OrderStatusEnum.checkIn.getId().intValue() || status.intValue()==OrderStatusEnum.finished.getId().intValue() || status.intValue()==OrderStatusEnum.noshow.getId().intValue()){
		        	getOrderStatusResponse.setOrderStatus(FangCangOrderStatusEnum.confirmed.getId().toString());
		        	getOrderStatusResponse.setSpOrderId(orderid.toString());
		        	orderStatusResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
		        	orderStatusResponse.setGetOrderStatusResponse(getOrderStatusResponse);
		        }else if(status.intValue()==OrderStatusEnum.channelCanceled.getId().intValue()){
		        	//区分取消原因
		        	if(cancelreason!=null){
		        		if(cancelreason.intValue()==CancelReasonEnum.channelCanceled.getId().intValue()){
		        			getOrderStatusResponse.setOrderStatus(FangCangOrderStatusEnum.canceled.getId().toString());
		        			getOrderStatusResponse.setSpOrderId(orderid.toString());
		        			orderStatusResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
		        			orderStatusResponse.setGetOrderStatusResponse(getOrderStatusResponse);
		        		}else{
		        			getOrderStatusResponse.setOrderStatus(FangCangOrderStatusEnum.refused.getId().toString());
		        			getOrderStatusResponse.setSpOrderId(orderid.toString());
		        			orderStatusResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
		        			orderStatusResponse.setGetOrderStatusResponse(getOrderStatusResponse);
		        		}
		        	}
		        }
		    }else{
		    	orderStatusResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
		    	orderStatusResponse.setResultMsg("未查询到此订单信息");
		    }
		    logger.info("getOrderstatus result:{}",new Gson().toJson(orderStatusResponse));
		} catch (Exception e) {
			logger.error("getOrderstatus error:",e);
			orderStatusResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
			orderStatusResponse.setResultMsg("系统异常");
		}
		String xmlResult =  businessOrderStatusResponseUtil.genFangCangResponse(orderStatusResponse,OrderStatusQueryResponse.class.getName());
		return  xmlResult;
	}

	@Override
	public String checkRoomAvail(String xml) {
		CheckRoomAvailRootResponse checkRoomAvailRootResponse = new CheckRoomAvailRootResponse();
		try {
			logger.info("checkRoomAvail begin:{}",xml);
			if(StringUtils.isBlank(xml)){
				checkRoomAvailRootResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
				checkRoomAvailRootResponse.setResultMsg("入参为空");
				String xmlResult =  businessRoomAvailResponseUtil.genFangCangResponse(checkRoomAvailRootResponse,CheckRoomAvailRootResponse.class.getName());
				return  xmlResult;
			}
			
			//封装xml信息
			CheckRoomAvailRootRequest checkRoomAvailRootRequest = businessRoomAvailRequestUtil.decodeRequestXml(xml,CheckRoomAvailRootRequest.class);
			DistributorConfig distributorConfig = null; 
			List<DistributorConfig> list = 	iDistributorConfigService.queryByChannelId(ChannelEnum.fangcang.getId());
			if(CollectionUtils.isNotEmpty(list)){
				distributorConfig = list.get(0);
			}
			CheckRoomAvailRequest checkRoomAvailRequest = checkRoomAvailRootRequest.getCheckRoomAvailRequest();
			//封装试预订参数开始
			String checkInDateQuery = checkRoomAvailRequest.getCheckInDate();
			if(checkInDateQuery!=null){
				if(!DateUtil.isLteToday(checkInDateQuery)){
					checkRoomAvailRootResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
					checkRoomAvailRootResponse.setResultMsg("入住时间须大于或者等于今天");
					String xmlResult =  businessRoomAvailResponseUtil.genFangCangResponse(checkRoomAvailRootResponse,CheckRoomAvailRootResponse.class.getName());
					return  xmlResult;
				}
			}
			String checkOutDateQuery = checkRoomAvailRequest.getCheckOutDate();
			if(checkOutDateQuery!=null){
				if(!DateUtil.isLteToday(checkOutDateQuery)){
					checkRoomAvailRootResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
					checkRoomAvailRootResponse.setResultMsg("离店时间须大于或者等于今天");
					String xmlResult =  businessRoomAvailResponseUtil.genFangCangResponse(checkRoomAvailRootResponse,CheckRoomAvailRootResponse.class.getName());
					return  xmlResult;
				}
			}
			String roomNumQuery = checkRoomAvailRequest.getRoomNum();
			String spHotelIdQuery = checkRoomAvailRequest.getSpHotelId();
			String spRatePlanIdQuery = checkRoomAvailRequest.getSpRatePlanId();
			String spRoomTypeIdQuery = checkRoomAvailRequest.getSpRoomTypeId();
			
			com.fangbaba.order.model.Request<CreateOrderRequest> orderCreateRequest = new com.fangbaba.order.model.Request<CreateOrderRequest>();
			Header header = new Header();
			header.setTimeStamp(new Date());
			header.setToken("fangcang");
			orderCreateRequest.setHeader(header);
			CreateOrderRequest querydata = new CreateOrderRequest();
			querydata.setBegintime(checkInDateQuery);
			querydata.setEndtime(checkOutDateQuery);
			querydata.setChannelId(distributorConfig.getOtatype());
			querydata.setHotelId(Long.parseLong(spHotelIdQuery));
			querydata.setChannelName(distributorConfig.getName());
			querydata.setOrdertype(com.fangbaba.order.common.enums.OrderTypeEnum.ota.getId());
			querydata.setContacts("fangcangcheckavail");
			querydata.setTeamName("fangcang");
			querydata.setPayTypeEnum(PayTypeEnum.PT);
			List<CreateOrderDetail> createOrderDetaillist= new ArrayList<CreateOrderDetail>();
			CreateOrderDetail createOrderDetail = new CreateOrderDetail();
			createOrderDetail.setNum(Integer.parseInt(roomNumQuery));
			createOrderDetail.setRoomTypeId(Long.parseLong(spRoomTypeIdQuery));
			List<OtaCheckInUser> checkInUsers = new ArrayList<OtaCheckInUser>();
			OtaCheckInUser otaCheckInUser = new OtaCheckInUser();
			otaCheckInUser.setName("fangcangcheckavail");
			checkInUsers.add(otaCheckInUser);
			createOrderDetail.setOtaCheckInUsers(checkInUsers);
			createOrderDetaillist.add(createOrderDetail);
			querydata.setCreateOrderDetails(createOrderDetaillist);
			
			orderCreateRequest.setData(querydata);
			//封装试预订参数结束
			
			logger.info("checkRoomAvail order param:{}",new Gson().toJson(orderCreateRequest));
			
			com.fangbaba.order.model.Response<Map<Long, List<RoomTypeAvail>>> orderresponse =orderService.validateOrder(orderCreateRequest);
			
		    logger.error("checkRoomAvail order result:{}",new Gson().toJson(orderresponse));
		    //组织返回数据
		    CheckRoomAvailResponse checkRoomAvailResponse = new CheckRoomAvailResponse();
		    checkRoomAvailResponse.setSpRatePlanId(spRatePlanIdQuery);
		    Map<Long, List<RoomTypeAvail>> datamap = orderresponse.getData();
		    List<SaleItem> saleItemList = new ArrayList<SaleItem>();
		    if(datamap!=null && !datamap.isEmpty() && datamap.containsKey(Long.parseLong(spRoomTypeIdQuery))){
		    	List<RoomTypeAvail> roomTypeAvailList = datamap.get(Long.parseLong(spRoomTypeIdQuery));
		    	for (RoomTypeAvail roomTypeAvail:roomTypeAvailList) {
		    		SaleItem saleItem = new SaleItem();
		    		saleItem.setSaleDate(DateUtil.dateToStr(roomTypeAvail.getDay(), "yyyy-MM-dd"));
		    		saleItem.setDayCanBook(roomTypeAvail.isDayCanBook()==true?FangCangDayCanBookEnum.can.getId().toString():FangCangDayCanBookEnum.cannot.getId().toString());
		    		saleItem.setPriceNeedCheck(FangCangPriceNeedCheckEnum.no.getId().toString());
		    		saleItem.setSalePrice(roomTypeAvail.getRoomprice().toString());
		    		saleItem.setCurrency("CNY");
		    		saleItem.setBreakfastType("");
		    		saleItem.setBreakfastNum("0");
		    		saleItem.setAvailableQuotaNum(roomTypeAvail.getStockNum().toString());
		    		if(roomTypeAvail.getStockNum()>0){
		    			saleItem.setRoomStatus(FangCangRoomStatusEnum.available.getId().toString());
		    		}else{
		    			saleItem.setRoomStatus(FangCangRoomStatusEnum.full.getId().toString());
		    		}
		    		saleItem.setOverDraft(FangCangOverDraftEnum.cannot.getId().toString());
		    		saleItemList.add(saleItem);
		    	}
		    	
		    }
		    if(CollectionUtils.isNotEmpty(saleItemList))
		    	checkRoomAvailResponse.setSaleItemList(saleItemList);
		    if(orderresponse.isSuccess()){
		    	//可预订
		    	checkRoomAvailResponse.setCanBook(FangCangCanBookEnum.can.getId().toString());
		    }else{
		    	//不可预订
		    	checkRoomAvailResponse.setCanBook(FangCangCanBookEnum.cannot.getId().toString());
		    }
		    checkRoomAvailResponse.setCanImmediate(FangCangCanImmediateEnum.cannot.getId().toString());
		    checkRoomAvailRootResponse.setCheckRoomAvailResponse(checkRoomAvailResponse);
		    checkRoomAvailRootResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
		    logger.info("checkRoomAvail result:{}",new Gson().toJson(checkRoomAvailRootResponse));
		}catch(Exception e){
			logger.error("checkRoomAvail error:",e);
			checkRoomAvailRootResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
			checkRoomAvailRootResponse.setResultMsg("系统异常");
		}
		String xmlResult =  businessRoomAvailResponseUtil.genFangCangResponse(checkRoomAvailRootResponse,CheckRoomAvailRootResponse.class.getName());
		return  xmlResult;
	}
}
