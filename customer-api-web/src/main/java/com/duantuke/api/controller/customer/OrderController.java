package com.duantuke.api.controller.customer;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.util.DateUtil;
import com.duantuke.api.util.TokenUtil;
import com.duantuke.order.common.enums.OrderErrorEnum;
import com.duantuke.order.model.CreateOrderRequest;
import com.duantuke.order.model.CreateOrderResponse;
import com.duantuke.order.model.Header;
import com.duantuke.order.model.Order;
import com.duantuke.order.model.QueryOrderRequest;
import com.duantuke.order.model.Request;
import com.duantuke.order.model.Response;
import com.duantuke.order.service.OrderService;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/customer/order")
public class OrderController {

	private static Logger logger = LoggerFactory.getLogger(SkuController.class);
	@Autowired
	private OrderService orderService;

	/**
	 * 创建订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/create")
	public ResponseEntity<OpenResponse<CreateOrderResponse>> create(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("接收到创建订单请求,request = {}", JSON.toJSONString(request));
		OpenResponse<CreateOrderResponse> openResponse = new OpenResponse<CreateOrderResponse>();
		try {
			Request<CreateOrderRequest> req = new Request<CreateOrderRequest>();
			Header header = new Header();
			header.setTimeStamp(new Date());
			header.setToken("token");
			req.setHeader(header);

			Long userId = TokenUtil.getUserIdByRequest(request);

			Response<CreateOrderResponse> res = orderService.create(req);
			openResponse.setResult(Boolean.toString(res.isSuccess()));
			openResponse.setData(res.getData());
			openResponse.setErrorCode(res.getErrorCode());
			openResponse.setErrorMessage(res.getErrorMessage());
		} catch (Exception e) {

		}
		return new ResponseEntity<OpenResponse<CreateOrderResponse>>(openResponse, HttpStatus.OK);
	}

	/**
	 * 查询订单列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryOrders")
	public ResponseEntity<OpenResponse<List<Order>>> queryOrders(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("接收到查询订单请求,入参:{}",JSON.toJSONString(request.getParameterMap()));
		OpenResponse<List<Order>> openResponse = new OpenResponse<List<Order>>();
		try {
			String pageNo = request.getParameter("pageNo");
			String pageSize = request.getParameter("pageSize");
			String beginTime = request.getParameter("beginTime");
			String endTime = request.getParameter("endTime");
			
			Request<QueryOrderRequest> req = new Request<QueryOrderRequest>();
			Header header = new Header();
			header.setTimeStamp(new Date());
			header.setToken("token");
			req.setHeader(header);

			// 获取用户信息
			Long customerId = TokenUtil.getUserIdByRequest(request);
			logger.info("用户信息获取完成,customerId = {}", customerId);

			QueryOrderRequest queryOrderRequest = new QueryOrderRequest();
			queryOrderRequest.setCustomerId(customerId);
			queryOrderRequest.setPageNo(Integer.parseInt(pageNo));
			queryOrderRequest.setPageSize(Integer.parseInt(pageSize));
			queryOrderRequest.setStartDate(DateUtil.strToDate(beginTime, "yyyy-MM-dd"));
			queryOrderRequest.setEndDate(DateUtil.strToDate(endTime, "yyyy-MM-dd"));
			req.setData(queryOrderRequest);

			Response<List<Order>> res = orderService.queryOrders(req);
			openResponse.setResult(Boolean.toString(res.isSuccess()));
			openResponse.setData(res.getData());
			openResponse.setErrorCode(res.getErrorCode());
			openResponse.setErrorMessage(res.getErrorMessage());
			logger.info("查询订单成功,返回结果:{}",JSON.toJSONString(openResponse));
		} catch (Exception e) {
			logger.error("查询订单异常", e);
			openResponse.setResult(Boolean.toString(false));
			openResponse.setErrorCode(OrderErrorEnum.customError.getErrorCode());
			openResponse.setErrorMessage(OrderErrorEnum.customError.getErrorMsg());
		}
		
		return new ResponseEntity<OpenResponse<List<Order>>>(openResponse, HttpStatus.OK);
	}
}
