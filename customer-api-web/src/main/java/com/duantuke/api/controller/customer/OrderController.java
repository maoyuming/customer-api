package com.duantuke.api.controller.customer;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.duantuke.order.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.util.DateUtil;
import com.duantuke.api.util.TokenUtil;
import com.duantuke.order.common.enums.OrderErrorEnum;
import com.duantuke.order.service.OrderService;

@Controller
@RequestMapping(value = "/customer/order")
public class OrderController {

	private static Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrderService orderService;

	/**
	 * 创建订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public ResponseEntity<OpenResponse<CreateOrderResponse>> create(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("接收到创建订单请求,request = {}", JSON.toJSONString(request.getParameterMap()));
		OpenResponse<CreateOrderResponse> openResponse = new OpenResponse<CreateOrderResponse>();
		try {
			Request<CreateOrderRequest> req = new Request<CreateOrderRequest>();
			Header header = new Header();
			header.setTimeStamp(new Date());
			req.setHeader(header);

			Long userId = TokenUtil.getUserIdByRequest(request);

            // 从request中获取订单信息json
            String orderJson = request.getParameter("data");
            logger.info("从request中获取的订单信息为" + orderJson);

            if (StringUtils.isBlank(orderJson)) {
                logger.error("从request中获取的订单信息为空,无法创建订单");
                openResponse.setResult(Boolean.FALSE.toString());
                openResponse.setErrorCode(OrderErrorEnum.paramsError.getErrorCode());
                openResponse.setErrorMessage(OrderErrorEnum.paramsError.getErrorMsg());
            } else {

                // 把订单数据封装到对象中
                CreateOrderRequest createOrderRequest = new CreateOrderRequest();
                Order order = JSON.parseObject(orderJson, Order.class);
                createOrderRequest.setOrder(order);
                req.setData(createOrderRequest);

                Response<CreateOrderResponse> res = orderService.create(req);
                openResponse.setResult(Boolean.toString(res.isSuccess()));
                openResponse.setData(res.getData());
                openResponse.setErrorCode(res.getErrorCode());
                openResponse.setErrorMessage(res.getErrorMessage());
            }
		} catch (Exception e) {
            logger.error("创建订单出现异常,request= {}", JSON.toJSONString(request.getParameterMap()), e);
            openResponse.setResult(Boolean.FALSE.toString());
            openResponse.setErrorCode(OrderErrorEnum.customError.getErrorCode());
            openResponse.setErrorMessage(OrderErrorEnum.customError.getErrorMsg());
		}
		return new ResponseEntity<OpenResponse<CreateOrderResponse>>(openResponse, HttpStatus.OK);
	}

    /**
     * 取消订单
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/cancel")
    public ResponseEntity<OpenResponse<CancelOrderResponse>> cancel(HttpServletRequest request,
                                                                    HttpServletResponse response) {
        logger.info("接收到取消订单请求,request = {}", JSON.toJSONString(request.getParameterMap()));
        OpenResponse<CancelOrderResponse> openResponse = new OpenResponse<CancelOrderResponse>();
        try {
            Request<CancelOrderRequest> req = new Request<CancelOrderRequest>();
            Header header = new Header();
            header.setTimeStamp(new Date());
            req.setHeader(header);

            // 从request中获取取消订单信息json
            String cancelJson = request.getParameter("data");
            logger.info("从request中获取的取消订单信息为" + cancelJson);

            if (StringUtils.isBlank(cancelJson)) {
                logger.error("从request中获取的取消订单信息为空,无法取消订单");
                openResponse.setResult(Boolean.FALSE.toString());
                openResponse.setErrorCode(OrderErrorEnum.paramsError.getErrorCode());
                openResponse.setErrorMessage(OrderErrorEnum.paramsError.getErrorMsg());
            } else {

                // 把取消订单数据封装到对象中
                CancelOrderRequest cancelOrderRequest = JSON.parseObject(cancelJson, CancelOrderRequest.class);
                req.setData(cancelOrderRequest);

                Response<CancelOrderResponse> res = orderService.cancel(req);
                openResponse.setResult(Boolean.toString(res.isSuccess()));
                openResponse.setData(res.getData());
                openResponse.setErrorCode(res.getErrorCode());
                openResponse.setErrorMessage(res.getErrorMessage());
            }
        } catch (Exception e) {
            logger.error("取消订单出现异常,request= {}", JSON.toJSONString(request.getParameterMap()), e);
            openResponse.setResult(Boolean.FALSE.toString());
            openResponse.setErrorCode(OrderErrorEnum.customError.getErrorCode());
            openResponse.setErrorMessage(OrderErrorEnum.customError.getErrorMsg());
        }
        return new ResponseEntity<OpenResponse<CancelOrderResponse>>(openResponse, HttpStatus.OK);
    }

	/**
	 * 查询订单列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/list")
	public ResponseEntity<OpenResponse<List<Order>>> queryOrders(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("接收到查询订单列表请求,入参:{}", JSON.toJSONString(request.getParameterMap()));
		OpenResponse<List<Order>> openResponse = new OpenResponse<List<Order>>();
		try {
			String pageNo = request.getParameter("pageNo");
			String pageSize = request.getParameter("pageSize");
			String beginTime = request.getParameter("beginTime");
			String endTime = request.getParameter("endTime");

			Request<QueryOrderRequest> req = new Request<QueryOrderRequest>();
			Header header = new Header();
			header.setTimeStamp(new Date());
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
			logger.info("查询订单列表成功,返回结果:{}", JSON.toJSONString(openResponse));
		} catch (Exception e) {
			logger.error("查询订单列表异常", e);
			openResponse.setResult(Boolean.toString(false));
			openResponse.setErrorCode(OrderErrorEnum.customError.getErrorCode());
			openResponse.setErrorMessage(OrderErrorEnum.customError.getErrorMsg());
		}

		return new ResponseEntity<OpenResponse<List<Order>>>(openResponse, HttpStatus.OK);
	}

	/**
	 * 根据订单号查询订单详情
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{id}")
	public ResponseEntity<OpenResponse<Order>> queryOrder(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Long id) {
		logger.info("接收到查询订单请求,入参:{}", JSON.toJSONString(request.getParameterMap()));
		OpenResponse<Order> openResponse = new OpenResponse<Order>();
		try {

			Request<Base> req = new Request<Base>();
			Header header = new Header();
			header.setTimeStamp(new Date());
			req.setHeader(header);

			Base base = new Base();
			base.setOrderId(id);
			req.setData(base);

			Response<Order> res = orderService.queryOrderByOrderId(req);
			openResponse.setResult(Boolean.toString(res.isSuccess()));
			openResponse.setData(res.getData());
			openResponse.setErrorCode(res.getErrorCode());
			openResponse.setErrorMessage(res.getErrorMessage());
			logger.info("查询订单详情成功,返回结果:{}", JSON.toJSONString(openResponse));
		} catch (Exception e) {
			logger.error("查询订单详情异常", e);
			openResponse.setResult(Boolean.toString(false));
			openResponse.setErrorCode(OrderErrorEnum.customError.getErrorCode());
			openResponse.setErrorMessage(OrderErrorEnum.customError.getErrorMsg());
		}

		return new ResponseEntity<OpenResponse<Order>>(openResponse, HttpStatus.OK);
	}

}
