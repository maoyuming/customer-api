package com.duantuke.api.controller.customer;

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

import com.alibaba.fastjson.JSON;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.util.TokenUtil;
import com.duantuke.order.model.CreateOrderRequest;
import com.duantuke.order.model.CreateOrderResponse;
import com.duantuke.order.model.Header;
import com.duantuke.order.model.Request;
import com.duantuke.order.model.Response;
import com.duantuke.order.service.OrderService;

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
	public ResponseEntity<OpenResponse<CreateOrderResponse>> list(HttpServletRequest request,
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
}
