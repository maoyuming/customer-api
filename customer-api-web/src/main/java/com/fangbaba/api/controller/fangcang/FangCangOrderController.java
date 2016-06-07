package com.fangbaba.api.controller.fangcang;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.domain.fangcang.order.CreateOrderResponse;
import com.fangbaba.api.service.FangCangCancelOrderService;
import com.fangbaba.api.service.FangCangCreateOrderService;
import com.fangbaba.api.service.FangCangOrderService;
import com.thoughtworks.xstream.XStream;

/**
 * 订单类
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/fangcang/order")
public class FangCangOrderController {
	private static Logger logger = LoggerFactory.getLogger(FangCangHotelController.class);
	
	@Autowired
	private FangCangCancelOrderService cancelOrderService;
	@Autowired
	private FangCangOrderService orderService;
	@Autowired
	private FangCangCreateOrderService fangCangCreateOrderService;
	
	
	/**
	 * 取消订单
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/cancelHotelOrder")
    public void cancelHotelOrder(HttpServletRequest request,HttpServletResponse response,String xml) throws IOException {
		logger.info("取消订单");
		String retInfo = cancelOrderService.cancelOrder(xml);
		logger.info("取消订单result:{}",retInfo);
		response.getWriter().write(retInfo);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/xml;charset=UTF-8");
	}
	/**
	 * 取消订单
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/cancelHotelOrderTest")
	public void cancelHotelOrderTest(HttpServletRequest request,HttpServletResponse response,String xml,Long orderId) throws IOException {
		logger.info("取消订单");
		String retInfo = cancelOrderService.cancelOrder(xml,orderId);
		logger.info("取消订单result:{}",retInfo);
		response.getWriter().write(retInfo);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/xml;charset=UTF-8");
	}
	/**
	 * 获取订单状态
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getOrderStatus")
	public void getOrderStatus(HttpServletRequest request,HttpServletResponse response,String xml) throws IOException {
		logger.info("获取订单状态 xml:{}",xml);
		String retInfo = orderService.getOrderStatus(xml);
		logger.info("获取订单状态result:{}",retInfo);
		response.getWriter().write(retInfo);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/xml;charset=UTF-8");
	}
	
	/** 创建订单
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/createOrder")
    public  void createOrder(HttpServletRequest request,HttpServletResponse response ,String xml) throws IOException{
		logger.info("创建订单传来的xml:{}",xml);
		 String retInfo = fangCangCreateOrderService.createOrder(xml);
		 logger.info("创建订单结束:{}",retInfo);
		 response.getWriter().write(retInfo);
		 response.setCharacterEncoding("UTF-8");
		 response.setContentType("application/xml;charset=UTF-8");
	}
	/** 试预订接口
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/checkRoomAvail")
	public  void checkRoomAvail(HttpServletRequest request,HttpServletResponse response ,String xml) throws IOException {
		logger.info("试预订接口传来的xml:{}",xml);
		String retInfo = orderService.checkRoomAvail(xml);
		logger.info("获预订接口result:{}",retInfo);
		response.getWriter().write(retInfo);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/xml;charset=UTF-8");
	}
}
