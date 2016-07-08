package com.duantuke.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.promotion.face.service.ActivityService;
import com.duantuke.promotion.face.service.PromotionService;
import com.duantuke.promotion.po.Activity;
import com.duantuke.promotion.po.Promotion;
import com.google.gson.Gson;


@Controller
@RequestMapping(value = "/activity")
public class ActivityController {
	private static Logger logger = LoggerFactory.getLogger(ActivityController.class);
	@Autowired
	private PromotionService promotionService;
	@Autowired
	private ActivityService activityService;
	
	
	/**
     * 查询活动列表
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/list")
    public ResponseEntity<OpenResponse<List<Activity>>> list(HttpServletRequest request, HttpServletResponse response) {

		OpenResponse<List<Activity>> openResponse = new OpenResponse<List<Activity>>();
		try {
			List<Activity>  list = activityService.queryAllVisiableActivity();
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<Activity>>>(openResponse, HttpStatus.OK);
	
	}
	/**
	 * 查询活动下优惠券列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryPromotionList")
	public ResponseEntity<OpenResponse<List<Promotion>>> queryPromotionList(HttpServletRequest request, HttpServletResponse response,
			Long activityId) {
		if(activityId==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		
		OpenResponse<List<Promotion>> openResponse = new OpenResponse<List<Promotion>>();
		try {
			List<Promotion>  list = promotionService.queryPromotionListByActivityId(activityId);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		}finally{
			logger.info("返回值openResponse：{}",com.alibaba.fastjson.JSONObject.toJSON(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<Promotion>>>(openResponse, HttpStatus.OK);
		
	}
	
	
}
