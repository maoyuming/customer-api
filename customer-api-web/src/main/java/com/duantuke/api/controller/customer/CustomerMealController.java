package com.duantuke.api.controller.customer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.face.esbean.output.JourneyOutputBean;
import com.duantuke.basic.face.esbean.output.MealOutputBean;
import com.duantuke.basic.face.esbean.query.MealQueryBean;
import com.duantuke.basic.face.service.MealSearchService;
import com.duantuke.basic.face.service.MealService;
import com.duantuke.basic.po.Meal;
import com.google.gson.Gson;


/**
 * 吃详情
 * @author yuming.mao
 *
 */
@Controller
@RequestMapping(value = "/customer/meal")
public class CustomerMealController {
	private static Logger logger = LoggerFactory.getLogger(CustomerMealController.class);
	@Autowired
	private MealService mealService;
	@Autowired
	private MealSearchService mealSearchService;
	
	
	/**
	 * @param mealQueryBean
	 * 搜索餐饮es
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<MealOutputBean>>> search(MealQueryBean mealQueryBean) {
		logger.info("CustomerMealController search：{}",new Gson().toJson(mealQueryBean));
		OpenResponse<List<MealOutputBean>> openResponse = new OpenResponse<List<MealOutputBean>>();
		try {
			List<MealOutputBean> list = mealSearchService.searchMealsFromEs(mealQueryBean);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerMealController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<MealOutputBean>>> (openResponse, HttpStatus.OK);
	}
	
	/**
     * 饮食详情
     * @param request
     * @param response
     * @return
    */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<Meal>> detail(Long skuId) {
		logger.info("饮食详情，skuid：{}",skuId);
		OpenResponse<Meal> openResponse = new OpenResponse<Meal>();

		if(skuId == null){
			openResponse.setErrorMessage("参数skuId为空");
			openResponse.setResult(Constants.FAIL);
			return new ResponseEntity<OpenResponse<Meal>> (openResponse, HttpStatus.OK);
		}
		
		try {			
			Meal meal = mealService.queryMealById(skuId);
			openResponse.setData(meal);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("CustomerMealController search error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<Meal>> (openResponse, HttpStatus.OK);
	}
	
	
}
