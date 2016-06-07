package com.fangbaba.api.service.impl;

import com.fangbaba.api.common.Constants;
import com.fangbaba.api.domain.Contacter;
import com.fangbaba.api.domain.GuestInfo;
import com.fangbaba.api.domain.SalePriceItem;
import com.fangbaba.api.domain.fangcang.order.CreateHotelOrderRequest;
import com.fangbaba.api.domain.fangcang.order.CreateHotelOrderResponse;
import com.fangbaba.api.domain.fangcang.order.CreateOrderRequest;
import com.fangbaba.api.domain.fangcang.order.CreateOrderResponse;
import com.fangbaba.api.domain.open.hotel.OrderRequest;
import com.fangbaba.api.domain.open.hotel.OrderRequest.NestGuestInfo;
import com.fangbaba.api.domain.open.hotel.OrderRequest.NestOrderDetail;
import com.fangbaba.api.domain.open.hotel.OrderRequest.NestSaleRange;
import com.fangbaba.api.enums.FangCangOrderStatusEnum;
import com.fangbaba.api.enums.FangCangResultEnum;
import com.fangbaba.api.enums.FangCangResultFlagEnum;
import com.fangbaba.api.face.service.IOrderService;
import com.fangbaba.api.service.FangCangCreateOrderService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.gds.enums.ChannelEnum;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class FangCangCreateOrderServiceImpl implements FangCangCreateOrderService{

	private static Logger logger = LoggerFactory.getLogger(FangCangCancelOrderServiceImpl.class);
	@Autowired
	private BusinessUtil<CreateOrderRequest> businessUtil;
	@Autowired
	private IOrderService iOrderService;
	@Autowired
	private BusinessUtil<CreateOrderResponse> businessOrderStatusResponseUtil;
	@Override
	public String createOrder(String xml) {
			CreateOrderResponse createOrderResponse = new CreateOrderResponse();
			createOrderResponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
		try {
			logger.info("创建订单传来的xml:{}",xml);
			if(StringUtils.isBlank(xml)){
				createOrderResponse.setResultMsg("入参为空");
				return  businessOrderStatusResponseUtil.genFangCangResponse(createOrderResponse,CreateOrderResponse.class.getName());
			}
			//封装xml信息
			CreateOrderRequest createOrderRequest = businessUtil.decodeRequestXml(xml,CreateOrderRequest.class);
			OrderRequest orderRequest = new OrderRequest();
			CreateHotelOrderRequest createHotelOrderRequest = createOrderRequest.getCreateHotelOrderRequest();
			Contacter contact = createHotelOrderRequest.getContacter();
		    List<GuestInfo> fcGuestInfos = createHotelOrderRequest.getGuestInfoList();
			//入住人
			//List<OccupancyInfo> occupancyInfos = createHotelOrderRequest.getOccupancyInfoList();
			//订单天的销售价
		    List<SalePriceItem> salePriceItem = createHotelOrderRequest.getSalePriceList();
		    
		    //封装订单参数
			orderRequest.setBegintime(createHotelOrderRequest.getCheckInDate());
			orderRequest.setEndtime(createHotelOrderRequest.getCheckOutDate());
			orderRequest.setChannelorderid(createHotelOrderRequest.getFcOrderId());
			orderRequest.setContact(contact.getLinkMan());
			orderRequest.setContactsphone(contact.getLinkManPhone());
			orderRequest.setHotelId(Long.parseLong(createHotelOrderRequest.getSpHotelId()));
			orderRequest.setPaytype(createHotelOrderRequest.getConfirmType() + "");
			
			//入住人设置
		    List<NestGuestInfo> guestInfos = new ArrayList<NestGuestInfo>();
		    for (GuestInfo guestInfo : fcGuestInfos) {
		    NestGuestInfo nestGuestInfo = new OrderRequest().new NestGuestInfo();
		    	nestGuestInfo.setName(guestInfo.getGuestName());
		        //nestGuestInfo.setPhone(""); //无手机号
		    	guestInfos.add(nestGuestInfo);
			}
			orderRequest.setGuestinfo(guestInfos);
			
			//订单详情
			List<NestOrderDetail> createOrderDetails = new ArrayList<NestOrderDetail>();
			List<NestSaleRange> nestSaleRanges = new ArrayList<NestSaleRange>();
			
			for (SalePriceItem salePrce : salePriceItem) {
				NestSaleRange nestSaleRange = new OrderRequest().new NestSaleRange();
				nestSaleRange.setActiondate(salePrce.getSaleDate());
				nestSaleRange.setPrice(salePrce.getSalePrice());
				nestSaleRanges.add(nestSaleRange);
			}
			
			NestOrderDetail nestOrderDetail = new OrderRequest().new NestOrderDetail();
			nestOrderDetail.setNum(createHotelOrderRequest.getRoomNum());
			nestOrderDetail.setRoomtypeid(Long.parseLong(createHotelOrderRequest.getSpRoomTypeId()));
			nestOrderDetail.setSalelist(nestSaleRanges);
			createOrderDetails.add(nestOrderDetail);
			orderRequest.setCreateorderdetails(createOrderDetails);
			

			Map resutlmap = iOrderService.createOrder(orderRequest, ChannelEnum.fangcang.getId(), null);
			
			CreateHotelOrderResponse createHotelOrderResponse = new CreateHotelOrderResponse();
			if (resutlmap==null) {
				logger.info("创建订单失败!");
				createHotelOrderResponse.setResultCode(FangCangResultEnum.Failure.getResult());
				createOrderResponse.setResultMsg("内部创建订单失败");
			}else {
				logger.info("订单状态:{}",resutlmap.get(Constants.ORDER_ID));
				if ("true".equals(resutlmap.get(Constants.RESULT)+"")) {
					createHotelOrderResponse.setSpOrderId(String.valueOf(resutlmap.get(Constants.ORDER_ID)));
					createHotelOrderResponse.setFcOrderId(createHotelOrderRequest.getFcOrderId());
					createHotelOrderResponse.setOrderStatus(FangCangOrderStatusEnum.operating.getId());
					createOrderResponse.setCreateHotelOrderResponse(createHotelOrderResponse); 
					createOrderResponse.setResultFlag(FangCangResultFlagEnum.success.getId());
				}else{
					createHotelOrderResponse.setResultMsg(String.valueOf(resutlmap.get(Constants.ERROR_MESSAGE)));
				}
				
			}
			
			}catch(Exception e){
				//e.printStackTrace();
				logger.error("创订单失败",e);
				createOrderResponse.setResultMsg("内部创建订单失败");
				
			}
        return businessOrderStatusResponseUtil.genFangCangResponse(createOrderResponse,CreateOrderResponse.class.getName());
}
}