package com.fangbaba.api.controller.open;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.common.Constants;
import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.domain.open.hotel.OrderRequest;
import com.fangbaba.api.domain.open.hotel.OrderResponse;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.service.IOrderService;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.po.DistributorConfig;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

/**
 * 房爸爸对外订单统一接口
 * <p/>
 * Created by nolan on 16/3/25.
 * description:
 */
@Controller
@RequestMapping("/open/order")
public class OrderController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IDistributorConfigService iDistributorConfigService;

    /**
     * 创建订单
     *
     * @param request
     * @param body
     * @return
     */
    @RequestMapping(value = "/create")
    public ResponseEntity<OpenResponse<Map>> createOrder(HttpServletRequest request, @RequestBody String body) {
        logger.info("createOrder(HttpServletRequest request, @RequestBody String body), body:{} >>>>>>>>>Begin", body);
        //1. 基础校验
        String channelid = request.getHeader("channelid");
        String token = request.getHeader("token");
        logger.info("token: {}, channelId: {}", token, channelid);
        if (Strings.isNullOrEmpty(channelid)) {
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }
        List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(Integer.valueOf(channelid));
        if (!CollectionUtils.isNotEmpty(distributorConfigs)) {
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }

        //2. 业务调用
        String channelname = distributorConfigs.get(0).getName();
        logger.info("channelname: {}, channelId: {}", channelname, channelid);
        OrderRequest orderRequest = new Gson().fromJson(body, OrderRequest.class);
        Map result = orderService.createOrder(orderRequest, Integer.valueOf(channelid), token);


        //3. 组织返回数据
        OpenResponse<Map> openResponse = new OpenResponse<Map>();
        openResponse.setResult(Boolean.TRUE.toString());
        openResponse.setData(result);
        logger.info("createOrder(HttpServletRequest request, @RequestBody String body), result:{} >>>>>>>>>End", openResponse);
        return new ResponseEntity<OpenResponse<Map>>(openResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/cancel")
    public ResponseEntity cancelorder(HttpServletRequest request, @RequestBody String body) {
        logger.debug("cancelorder(HttpServletRequest request, @RequestBody String body), body:{} >>>>>>>>>Begin", body);
        //1. 基础校验
        String channelid = request.getHeader("channelid");
        String token = request.getHeader("token");
        logger.info("token: {}, channelid: {}", token, channelid);
        if (Strings.isNullOrEmpty(channelid)) {
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }
        List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(Integer.valueOf(channelid));
        if (!CollectionUtils.isNotEmpty(distributorConfigs)) {
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }
        Map<String, Object> bodyMap = new Gson().fromJson(body, Map.class);
        if (Strings.isNullOrEmpty(String.valueOf(bodyMap.get(Constants.ORDER_ID)))) {
            throw new OpenException("orderid不允许为空");
        }

        //2. 业务调用
        String channelname = distributorConfigs.get(0).getName();
        logger.info("channelname: {}, channelId: {}", channelname, channelid);
        Long orderId = Long.parseLong(String.valueOf(bodyMap.get(Constants.ORDER_ID)));
        String cancelreason = String.valueOf(bodyMap.get("cancelreason"));
        logger.info("orderId: {}, cancelreason: {}", orderId, cancelreason);
        RetInfo<Map> result = orderService.cancelOrder(orderId, cancelreason, token, Integer.valueOf(channelid));

        OpenResponse openResponse = new OpenResponse();
        //3. 组织返回数据
        if(result.isResult()){
        	openResponse.setResult(Boolean.TRUE.toString());
        	openResponse.setData(result);
        }else{
        	openResponse.setResult(Boolean.FALSE.toString());
        	openResponse.setErrormessage(result.getMsg());
        	openResponse.setErrorcode(result.getCode());
        }
        logger.info("cancelorder(HttpServletRequest request, @RequestBody String body), result:{} >>>>>>>>>End", openResponse);
        return new ResponseEntity(openResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/queryorder")
    public ResponseEntity queryorder(HttpServletRequest request, @RequestBody String body) {
        logger.debug("queryorder(HttpServletRequest request, @RequestBody String body), body:{} >>>>>>>>>Begin", body);
        //1. 基础校验
        String channelid = request.getHeader("channelid");
        String token = request.getHeader("token");
        logger.info("token: {}, channelid: {}", token, channelid);
        if (Strings.isNullOrEmpty(channelid)) {
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }
        List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(Integer.valueOf(channelid));
        if (!CollectionUtils.isNotEmpty(distributorConfigs)) {
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }
        Map<String, Object> bodyMap = new Gson().fromJson(body, Map.class);
        if (!bodyMap.containsKey(Constants.ORDER_ID) || Strings.isNullOrEmpty(String.valueOf(bodyMap.get(Constants.ORDER_ID)))) {
            throw new OpenException("orderid不允许为空");
        }

        //2. 业务调用
        List<OrderResponse> result = null;
        String channelname = distributorConfigs.get(0).getName();
        logger.info("channelname: {}, channelid: {}", channelname, channelid);
        logger.info("orderid: {}", bodyMap.get(Constants.ORDER_ID));

        Map<String, String> requestMap = new Gson().fromJson(body, Map.class);
        String orderstr = requestMap.get(Constants.ORDER_ID);
        if (!Strings.isNullOrEmpty(orderstr)) {
            List<String> idsstr = Splitter.on(",").trimResults().splitToList(orderstr);
            List<Long> idslist = Lists.transform(idsstr, new Function<String, Long>() {
                @Override
                public Long apply(String input) {
                    return Long.parseLong(input);
                }
            });
            result = orderService.queryOrderByIds(idslist, token, Integer.valueOf(channelid));
        }

        //3. 组织返回数据
        OpenResponse openResponse = new OpenResponse();
        openResponse.setResult(Boolean.TRUE.toString());
        openResponse.setData(result);
        logger.info("cancelorder(HttpServletRequest request, @RequestBody String body), result:{} >>>>>>>>>End", openResponse);
        return new ResponseEntity(openResponse, HttpStatus.OK);
    }
    
    /**
     * 修改订单
     * @param request
     * @param body
     * @return
     */
    @RequestMapping(value = "/update")
    public ResponseEntity updateorder(HttpServletRequest request, @RequestBody String body) {
    	logger.info("updateorder(HttpServletRequest request, @RequestBody String body), body:{} >>>>>>>>>Begin", body);
    	//1. 基础校验
    	String channelid = request.getHeader("channelid");
    	String token = request.getHeader("token");
    	logger.info("token: {}, channelid: {}", token, channelid);
    	if (Strings.isNullOrEmpty(channelid)) {
    		throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
    	}
    	
    	OrderRequest order = new Gson().fromJson(body, OrderRequest.class);
    	RetInfo<Boolean> retInfo = orderService.updateOrder(order, Integer.valueOf(channelid), token);
    	
    	
    	//3. 组织返回数据
    	OpenResponse openResponse = new OpenResponse();
    	openResponse.setResult(retInfo.isResult()+"");
    	openResponse.setErrormessage(retInfo.getMsg());
    	openResponse.setErrorcode(retInfo.getCode());
    	
    	logger.info("updateorder(HttpServletRequest request, @RequestBody String body), result:{} >>>>>>>>>End", openResponse);
    	return new ResponseEntity(openResponse, HttpStatus.OK);
    }
}
