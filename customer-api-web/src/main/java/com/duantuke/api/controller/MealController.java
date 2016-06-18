package com.duantuke.api.controller;

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
import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.face.service.MealService;
import com.duantuke.basic.po.Meal;


/**
 * 饮食
 * @author yuming.mao
 *
 */
@Controller
@RequestMapping(value = "/meal")
public class MealController {
	private static Logger logger = LoggerFactory.getLogger(MealController.class);
	@Autowired
	private MealService mealService;
	
	/**
     * 饮食详情
     * @param request
     * @param response
     * @return
    */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<Meal>> detail(Long skuId) {
		logger.info("查看饮食详情入参，skuId:{}",skuId);
		OpenResponse<Meal> openResponse = new OpenResponse<Meal>();

		if(skuId == null){
			openResponse.setErrorMessage("参数skuId为空");
			openResponse.setResult(Constants.FAIL);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
			return new ResponseEntity<OpenResponse<Meal>> (openResponse, HttpStatus.OK);
		}
		
		try {			
			Meal meal = mealService.queryMealById(skuId);
			openResponse.setData(meal);
			openResponse.setResult(Constants.SUCCESS);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("MealController search error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<Meal>> (openResponse, HttpStatus.OK);
	}
	
	
}
