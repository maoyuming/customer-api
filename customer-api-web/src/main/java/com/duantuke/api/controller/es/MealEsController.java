package com.duantuke.api.controller.es;

import java.util.List;

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
import com.duantuke.basic.face.esbean.output.MealOutputBean;
import com.duantuke.basic.face.esbean.query.MealQueryBean;
import com.duantuke.basic.face.service.MealSearchService;
import com.google.gson.Gson;


/**
 * 餐饮
 * @author he
 *
 */
@Controller
@RequestMapping(value = "/es/meal")
public class MealEsController {
	private static Logger logger = LoggerFactory.getLogger(MealEsController.class);
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
	
	
}
