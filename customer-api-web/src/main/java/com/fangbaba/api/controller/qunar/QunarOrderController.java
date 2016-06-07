package com.fangbaba.api.controller.qunar;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.bean.OptOrder;
import com.fangbaba.api.face.bean.Order;
import com.fangbaba.api.face.enums.OrderOptEnum;
import com.fangbaba.api.face.enums.OrderSettlementEnum;
import com.fangbaba.api.service.OpenOrderService;
import com.fangbaba.api.service.impl.QunarOrderServiceImpl;
import com.fangbaba.api.util.DateUtil;



@Controller
@RequestMapping(value = "/qunar/order")
public class QunarOrderController {
	
	private static Logger logger = LoggerFactory.getLogger(QunarOrderController.class);
	
	@Autowired
	private OpenOrderService openOrderService;
	@Autowired
	private QunarOrderServiceImpl qunarOrderServiceImpl ;
	String pattern = "yyyyMMddHHmmss";
	
	@RequestMapping(value = "/queryOrder")
	public ResponseEntity<RetInfo<Order>> queryOrder(HttpServletRequest request, HttpServletResponse response,Integer channelId,String beginTime,String endTime) throws IOException {
		RetInfo<Order> retInfo = openOrderService.queryOrder(channelId, DateUtil.strToDate(beginTime, pattern), DateUtil.strToDate(endTime, pattern));
		return new ResponseEntity<RetInfo<Order>>(retInfo, HttpStatus.OK);
	}
	@RequestMapping(value = "/optOrder")
	public ResponseEntity<RetInfo<Order>> optOrder(HttpServletRequest request, HttpServletResponse response,Integer channelId,OptOrder optOrder) throws IOException {
		String opt= request.getParameter("opt");
		String roomChargeSettlement= request.getParameter("roomChargeSettlement");
		optOrder.setOpt(OrderOptEnum.findByCode(opt));
		optOrder.setRoomChargeSettlement(OrderSettlementEnum.findByCode(roomChargeSettlement));
		RetInfo<Order> retInfo = openOrderService.optOrder(channelId, optOrder);
		return new ResponseEntity<RetInfo<Order>>(retInfo, HttpStatus.OK);
	}
	@RequestMapping(value = "/q")
	public void queryQunarOrder(Long otatype,Date beginTime,Date endTime){ 
		qunarOrderServiceImpl.queryQunarOrder(2,11111L, new Date(), new Date());
		
	}
}
