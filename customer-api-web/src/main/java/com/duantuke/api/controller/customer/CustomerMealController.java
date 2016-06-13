package com.duantuke.api.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.face.service.MealService;
import com.duantuke.basic.po.Journey;
import com.duantuke.basic.po.Meal;


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
	
    /**
     * 吃详情
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<Meal>> detail(HttpServletRequest request, HttpServletResponse response,Long mealId) {
		logger.info("吃详情，mealId：{}",mealId);
		OpenResponse<Meal> openResponse = new OpenResponse<Meal>();
		try {
			Meal meal = mealService.queryMealById(mealId);
			openResponse.setData(meal);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("吃详情异常",e);
		}
		
		return new ResponseEntity<OpenResponse<Meal>> (openResponse, HttpStatus.OK);
	}
	
	
}
