package com.fangbaba.api.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.common.Constants;
import com.fangbaba.api.domain.OrderExtend;
import com.fangbaba.api.domain.open.hotel.OrderRequest;
import com.fangbaba.api.domain.open.hotel.OrderRequest.NestGuestInfo;
import com.fangbaba.api.domain.open.hotel.OrderResponse;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.bean.OptOrder;
import com.fangbaba.api.face.bean.Order;
import com.fangbaba.api.service.OpenOrderService;
import com.fangbaba.api.util.DateUtil;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.order.common.enums.PayTypeEnum;
import com.fangbaba.order.exception.OrderException;
import com.fangbaba.order.model.Base;
import com.fangbaba.order.model.BatchQueryOrderDetailsRequest;
import com.fangbaba.order.model.CancelOrderRequest;
import com.fangbaba.order.model.CancelOrderResponse;
import com.fangbaba.order.model.CreateOrderDetail;
import com.fangbaba.order.model.CreateOrderRequest;
import com.fangbaba.order.model.CreateOrderResponse;
import com.fangbaba.order.model.DataOrderDetails;
import com.fangbaba.order.model.Header;
import com.fangbaba.order.model.OtaCheckInUser;
import com.fangbaba.order.model.OtaOrderDetail;
import com.fangbaba.order.model.QueryOrderListResponse;
import com.fangbaba.order.model.Request;
import com.fangbaba.order.model.Response;
import com.fangbaba.order.model.RoomPrice;
import com.fangbaba.order.model.UpdateOrderRequest;
import com.fangbaba.order.model.UpdateOrderResponse;
import com.fangbaba.order.service.OrderService;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
@Service
public class OpenOrderServiceImpl implements OpenOrderService{

	private static Logger logger = LoggerFactory.getLogger(OpenOrderServiceImpl.class);
	
	
	@Autowired
	private QunarOrderServiceImpl qunarOrderServiceImpl;
	@Autowired
	private IDistributorConfigService iDistributorConfigService;
    @Autowired
    private OrderService orderService;
	
	/**
	 * 查询订单
	 */
	public RetInfo<Order> queryOrder(Integer  channelId,Date beginTime,Date endTime){
		
		if(channelId.equals(ChannelEnum.QUNAR.getId())){
			//根据channelid查询otatype
			List<DistributorConfig> list = iDistributorConfigService.queryByChannelId(channelId);
			if(CollectionUtils.isNotEmpty(list)){
				return qunarOrderServiceImpl.queryQunarOrder(channelId,list.get(0).getOtatype(),beginTime, endTime);
			}
		}
		return null;
	}
	
	
	
	/**
	 * 操作订单
	 */
	public RetInfo<Order> optOrder(Integer  channelId,OptOrder optOrder){
		if(channelId.equals(ChannelEnum.QUNAR.getId())){
			//根据channelid查询otatype
			List<DistributorConfig> list = iDistributorConfigService.queryByChannelId(channelId);
			if(CollectionUtils.isNotEmpty(list)){
				return qunarOrderServiceImpl.optOrder(list.get(0).getOtatype(),optOrder);
			}
		}
		return null;
	
	}
	@Override
    public RetInfo<Map> cancelOrder(Long orderid, String cancelReason, String token, Integer channelid) {
		RetInfo<Map> retInfo = new RetInfo<Map>();
		retInfo.setResult(false);
		 try {
			 DistributorConfig config = queryDistributorConfigByChannelId(channelid);
			Map rtnMap = Maps.newHashMap();
			//1. 数据转换
			CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
			cancelOrderRequest.setOrderId(orderid);
			cancelOrderRequest.setCancelReason(cancelReason);
			cancelOrderRequest.setChannelId(config.getOtatype());
			Base base = new Base();
			base.setOrderId(orderid);

			//2. 业务调用
			Header header = new Header();
			header.setTimeStamp(new Date());

			if(StringUtils.isEmpty(token)){
				token = "api";
			}
			header.setToken(token);
			//2.1 取消订单
			Request<CancelOrderRequest> request = new Request<CancelOrderRequest>();
			request.setHeader(header);
			request.setData(cancelOrderRequest);
			Response<CancelOrderResponse> response = orderService.cancelOrder(request);
			
			if(response!=null&&response.isSuccess()){
				 //2.2 查询订单
			    Request<Base> baserequest = new Request<Base>();
			    baserequest.setHeader(header);
			    baserequest.setData(base);
			    Response<QueryOrderListResponse> queryOrderListResponseResponse = orderService.queryOrderById(baserequest);

			    //3. 组织返回数据
			    
			    QueryOrderListResponse queryOrderListResponse = queryOrderListResponseResponse.getData();
			    Integer status = null;
			    if (queryOrderListResponse.getOrderList() != null && queryOrderListResponse.getOrderList().size() > 0) {
			        com.fangbaba.order.model.Order order = queryOrderListResponse.getOrderList().get(0);
			        status = order.getStatus();
			    }
			    
			    rtnMap.put(Constants.ORDER_ID, orderid);
			    rtnMap.put(Constants.ORDER_STATUS, status);
			    
			    retInfo.setResult(response.isSuccess());
			    retInfo.setObj(rtnMap);
			}else if(response!=null){
				retInfo.setResult(response.isSuccess());
				retInfo.setMsg(response.getErrorMessage());
			}else{
				retInfo.setMsg("取消订单失败");
			}
		} catch (OrderException e) {
			retInfo.setMsg(e.getErrorKey());
		}
       
		logger.info("取消订单返回值：{}",new Gson().toJson(retInfo));
        return retInfo;
    }

    @Override
    public Map createOrder(OrderRequest orderRequest, Integer channelId, String token) {
    	
    	DistributorConfig distributorConfig =  queryDistributorConfigByChannelId(channelId);
    	logger.info("渠道:{}",channelId);
    	logger.info("获取渠道的配置信息distributorConfig:{}",new Gson().toJson(distributorConfig));
        //1. 数据转换
        //1.1 订单信息
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setContactsPhone(orderRequest.getContactsphone());
        createOrderRequest.setBegintime(orderRequest.getBegintime());
        createOrderRequest.setEndtime(orderRequest.getEndtime());
        createOrderRequest.setContacts(orderRequest.getContact());
        createOrderRequest.setTeamName(distributorConfig.getName());
        createOrderRequest.setHotelId(orderRequest.getHotelId());
        createOrderRequest.setChannelId(distributorConfig.getOtatype());
        createOrderRequest.setChannelName(distributorConfig.getName());
        createOrderRequest.setOrdertype(com.fangbaba.order.common.enums.OrderTypeEnum.ota.getId());
        createOrderRequest.setPayTypeEnum(PayTypeEnum.getPayTypeEnum(Integer.parseInt(orderRequest.getPaytype())));

        

		OrderExtend extend = new OrderExtend();
		extend.setChannelId(channelId);
		extend.setScType(1);//TODO:暂时写死
		createOrderRequest.setBusinessInfo(new Gson().toJson(extend));
        
        //1.2 客单列表
        List<CreateOrderDetail> orderdeataillist = new ArrayList<CreateOrderDetail>();
        List<OtaCheckInUser> otaCheckInUsers = new ArrayList<OtaCheckInUser>();
        List<NestGuestInfo> guestInfo = orderRequest.getGuestinfo();
        for (NestGuestInfo guest : guestInfo) {
        	OtaCheckInUser otaCheckInUser = new OtaCheckInUser();
        	otaCheckInUser.setName(guest.getName());
        	otaCheckInUser.setPhone(guest.getPhone());
        	otaCheckInUsers.add(otaCheckInUser);
		}
        List<OrderRequest.NestOrderDetail> nestOrderDetails = orderRequest.getCreateorderdetails();
        for (int i = 0, n = nestOrderDetails.size(); i < n; i++) {
            OrderRequest.NestOrderDetail roomOrder = nestOrderDetails.get(i);

            //客单明细
            CreateOrderDetail createOrderDetail = new CreateOrderDetail();
            createOrderDetail.setOtaCheckInUsers(otaCheckInUsers);
            createOrderDetail.setNum(roomOrder.getNum());
            createOrderDetail.setRoomTypeId(roomOrder.getRoomtypeid());
            //客单价格
            List<RoomPrice> roomPrices = new ArrayList<RoomPrice>();
            for (OrderRequest.NestSaleRange roomTypePrice : roomOrder.getSalelist()) {
                RoomPrice roomPrice = new RoomPrice();
                roomPrice.setActiondate(DateUtil.strToDate(roomTypePrice.getActiondate(), "yyyy-MM-dd"));
                roomPrice.setPrice(roomTypePrice.getPrice());
                roomPrices.add(roomPrice);
            }
            createOrderDetail.setPrice(roomPrices);//每天的价格

            orderdeataillist.add(createOrderDetail);
        }
        createOrderRequest.setCreateOrderDetails(orderdeataillist);

        //2. 创建订单
        Request<CreateOrderRequest> request = new Request<CreateOrderRequest>();
        Header header = new Header();
        header.setTimeStamp(new Date());

		if(StringUtils.isEmpty(token)){
			token = "api";
		}
        header.setToken(token);
        request.setHeader(header);
        request.setData(createOrderRequest);
        Response<CreateOrderResponse> createOrderResponseResponse = null;
        Map rtnMap = Maps.newHashMap();
        try {
        	
        	createOrderResponseResponse = orderService.createOrder(request);
        	rtnMap.put("result", true);
		} catch (OrderException e) {
			logger.error("创建订单失败:"+e.getErrorEnum().getErrorCode()+""+e.getErrorKey());
			rtnMap.put(Constants.ERROR_CODE, e.getErrorEnum().getErrorCode());
			rtnMap.put(Constants.ERROR_MESSAGE,e.getErrorKey());
			throw new RuntimeException(rtnMap.get(Constants.ERROR_MESSAGE)+"");
			//return rtnMap;
		}
        catch (Exception e1) {
        	logger.error("创建订单失败:",e1);
        	rtnMap.put(Constants.ERROR_MESSAGE,"创建订单失败");
        	throw new RuntimeException(rtnMap.get(Constants.ERROR_MESSAGE)+"");
			//return rtnMap;
		}
        //3. 构造返回数据
        CreateOrderResponse createOrderResponse = createOrderResponseResponse.getData();
        rtnMap.put(Constants.ORDER_ID, createOrderResponse.getOrderid());
        rtnMap.put(Constants.ORDER_STATUS, createOrderResponse.getStauts());
        rtnMap.put("channelorderid", ""); //TODO 订单中不存在此信息
        return rtnMap;
    }

    @Override
    public List<OrderResponse> queryOrderByIds(List<Long> ids, String token, Integer channelid) {
        //1. 数据转换
        BatchQueryOrderDetailsRequest batchQueryOrderDetailsRequest = new BatchQueryOrderDetailsRequest();
        batchQueryOrderDetailsRequest.setOrderids(ids);

        //2. 查询订单列表
        Request<BatchQueryOrderDetailsRequest> request = new Request<BatchQueryOrderDetailsRequest>();
        Header header = new Header();
        header.setTimeStamp(new Date());

		if(StringUtils.isEmpty(token)){
			token = "api";
		}
        header.setToken(token);
        request.setHeader(header);
        request.setData(batchQueryOrderDetailsRequest);
        Response<Map<com.fangbaba.order.model.Order, List<DataOrderDetails>>> mapResponse = orderService.batchQueryOrderDetails(request);

        //3. 组织返回数据
        List<OrderResponse> rtnList = Lists.newArrayList();
        Map<com.fangbaba.order.model.Order, List<DataOrderDetails>> maplist = mapResponse.getData();
        if (maplist != null && maplist.size() > 0) {
            OrderResponse orderResponse;
            for (Map.Entry<com.fangbaba.order.model.Order, List<DataOrderDetails>> entry : maplist.entrySet()) {
                com.fangbaba.order.model.Order tmpOrder = entry.getKey();
                List<DataOrderDetails> tmpdetailList = entry.getValue();

                //3.1 订单信息
                orderResponse = new OrderResponse();
                orderResponse.setOrderid(tmpOrder.getId());
                orderResponse.setOrderstatus(tmpOrder.getStatus());
                orderResponse.setHotelid(tmpOrder.getHotelid());
                orderResponse.setPaytype(tmpOrder.getPaytype());
                orderResponse.setArrivetime(DateUtil.dateToStr(tmpOrder.getBegintime(), "yyyyMMdd"));
                orderResponse.setLeavetime(DateUtil.dateToStr(tmpOrder.getEndtime(), "yyyyMMdd"));
                orderResponse.setContact(tmpOrder.getContacts());
                orderResponse.setPhone(tmpOrder.getContactsphone());
                orderResponse.setMemo(tmpOrder.getNote());

                if (tmpdetailList != null && tmpdetailList.size() > 0) {
                    List<Map<String, String>> userlist = Lists.newArrayList();
                    List<OrderResponse.NestOrderDetailResponse> orderdetaillist = Lists.newArrayList();

                    //3.2 数据整理
                    for (DataOrderDetails tmpdataOrderDetails : tmpdetailList) {
                        //3.2.1 整理入住人信息, 单客单用户列表拼接
                    	List<OtaCheckInUser> otaCheckInUsers = tmpdataOrderDetails.getOtacheckinusers();
                        if (!CollectionUtils.isEmpty(otaCheckInUsers)) {
                           for (OtaCheckInUser otaCheckInUser : otaCheckInUsers) {
                        	   Map<String, String> usermap = new HashMap<String,String>();
                        	   if (!Strings.isNullOrEmpty(otaCheckInUser.getName())) {
                        		   usermap.put("name", otaCheckInUser.getName());
                        		   userlist.add(usermap);
						        }
                        	  
						  }
                        	/*String userstr = Joiner.on(";").join(Iterables.transform(tmpdataOrderDetails.getOtacheckinusers(),
                                    new Function<OtaCheckInUser, Object>() {
                                        @Override
                                        public Object apply(OtaCheckInUser input) {
                                            return input.getName();
                                        }
                                    }));*/
                           // Map<String, String> usermap = ImmutableMap.of("name", userstr);
                           
                        }
                        //2.2.2 整理预订单明细
                        OrderResponse.NestOrderDetailResponse nestOrderDetailResponse = new OrderResponse().new NestOrderDetailResponse();
                        OtaOrderDetail otaorderdetail = tmpdataOrderDetails.getOtaorderdetail();
                        if (otaorderdetail != null) {
                            nestOrderDetailResponse.setBooknum(otaorderdetail.getBooknum());
                            nestOrderDetailResponse.setRoomtypeid(otaorderdetail.getRoomtypeid());
                        }
                        if (tmpdataOrderDetails.getRoomprices() != null && tmpdataOrderDetails.getRoomprices().size() > 0) {
                            List<OrderResponse.NestRoomTypePriceResponse> priceList = Lists.newArrayList();
                            for (RoomPrice tmproomPrice : tmpdataOrderDetails.getRoomprices()) {
                            	//类型(1,ota下单价格，2，酒店结算价格）
                            	if (tmproomPrice.getType()==null||tmproomPrice.getType().intValue()!=1) {
									continue;
								}
                                OrderResponse.NestRoomTypePriceResponse nestRoomTypePriceResponse = new OrderResponse().new NestRoomTypePriceResponse();
                                nestRoomTypePriceResponse.setCost(tmproomPrice.getPrice());
                                nestRoomTypePriceResponse.setTime(DateUtil.dateToStr(tmproomPrice.getActiondate(), "yyyyMMdd"));
                                priceList.add(nestRoomTypePriceResponse);
                            }
                            nestOrderDetailResponse.setCost(priceList);
                        }
                        orderdetaillist.add(nestOrderDetailResponse);
                    }
                    //3.3 预订单明细
                    orderResponse.setOrderdetails(orderdetaillist);
                    //3.4 入住人信息
                    orderResponse.setUser(userlist);
                }
                rtnList.add(orderResponse);
            }
        }
        return rtnList;
    }
    
    
    /**
     * 修改订单
     * @param tripOrder
     * @return
     */
    public  RetInfo<Boolean> updateOrder(OrderRequest order, Integer channelId, String token){
    	RetInfo<Boolean> retInfo = new RetInfo<Boolean>();
    	retInfo.setResult(false);
    	Request<UpdateOrderRequest> request = new Request<UpdateOrderRequest>();
    	DistributorConfig config = this.queryDistributorConfigByChannelId(channelId);
    	
    	Header header = new Header();
 		header.setTimeStamp(new Date());
 		header.setToken(token);
 		if(order.getOrderid()==null){
 			retInfo.setMsg("订单号不能为空");
 			return retInfo;
 		}
    	if(StringUtils.isBlank(order.getContact())||StringUtils.isBlank(order.getContactsphone())){
        	retInfo.setMsg("联系人和联系电话不能都为空");
        	return retInfo;
    	}
    	
    	UpdateOrderRequest orderRequest = new UpdateOrderRequest();
    	orderRequest.setContacts(order.getContact());
    	orderRequest.setContactsPhone(order.getContactsphone());

    	orderRequest.setChannelId(config.getOtatype());
    	orderRequest.setOrderId(order.getOrderid());
    	request.setHeader(header);
    	request.setData(orderRequest);
    	Response<UpdateOrderResponse> response = null;
		try {
			response = orderService.updateOrder(request);
		} catch (OrderException o) {
			logger.info("修改订单异常:{}",o.getErrorEnum().getErrorMsg());
			retInfo.setMsg(o.getErrorEnum().getErrorMsg());
			return retInfo;
		}catch (Exception e) {
			logger.info("修改订单异常",e);
			retInfo.setMsg("系统错误");
    		return retInfo;
		}
    	
    	if(response==null){
    		retInfo.setMsg("系统错误");
    		return retInfo;
    	}
    	if(response!=null&&response.isSuccess()){
    		retInfo.setResult(true);
    		return retInfo;
    	}
    	retInfo.setMsg(response.getErrorMessage());
    	return retInfo;
    	
    }
    
    
    public DistributorConfig queryDistributorConfigByChannelId(Integer channelId){
    	List<DistributorConfig> list = iDistributorConfigService.queryByChannelId(channelId);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			throw new OpenException("渠道信息不存在");
		}
    }

}
