package com.duantuke.api.controller.customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.json.JSON;
import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.api.util.TokenUtil;
import com.duantuke.basic.face.bean.SkuRequest;
import com.duantuke.basic.face.bean.SkuResponse;
import com.duantuke.basic.face.service.SkuService;
import com.google.gson.Gson;

/**
 * sku查询服务
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/customer/sku")
public class SkuController {
	private static Logger logger = LoggerFactory.getLogger(SkuController.class);
	@Autowired
	private SkuService skuService;
	
	
	/**
     * 查询价格
     * @param request
     * @param response
     * @return
	 * @throws IOException 
     */
	@RequestMapping(value = "/query")
    public ResponseEntity<OpenResponse<SkuResponse>> query(HttpServletRequest request, HttpServletResponse response,
    		String json) throws IOException {
        SkuRequest skuRequest= new Gson().fromJson(json, SkuRequest.class);
		//校验参数
		checkParam(skuRequest,request);
		
		OpenResponse<SkuResponse> openResponse = new OpenResponse<SkuResponse>();
		try {
			SkuResponse skuResponse = skuService.querySku(skuRequest);
			if(skuResponse!=null){
				openResponse.setData(skuResponse);
				openResponse.setResult(Constants.SUCCESS);
			}else{
				openResponse.setResult(Constants.FAIL);
				openResponse.setErrorCode(ErrorEnum.saveFail.getId());
				openResponse.setErrorMessage(ErrorEnum.saveFail.getName());
			}
		} finally{
			logger.info("返回值openResponse：{}",JSON.json(openResponse));
		}
		return new ResponseEntity<OpenResponse<SkuResponse>>(openResponse, HttpStatus.OK);
	}
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkParam(SkuRequest skuRequest,HttpServletRequest request){
		
		logger.info("查询sku入参：{}",new Gson().toJson(skuRequest));
		
		if(skuRequest==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
//		if(skuRequest.getHotelId()==null){
//			throw new OpenException(ErrorEnum.hotelidNull.getName(),ErrorEnum.hotelidNull.getId());
//		}
		if(MapUtils.isEmpty(skuRequest.getSkuMap())){
			throw new OpenException(ErrorEnum.skuIdNull.getName(),ErrorEnum.skuIdNull.getId());
		}

		skuRequest.setCustomerId(TokenUtil.getUserIdByRequest(request));
		
		
	}
	
	
}
