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
import com.duantuke.basic.face.service.JourneyService;
import com.duantuke.basic.po.Journey;


/**
 * 游记
 * @author yuming.mao
 *
 */
@Controller
@RequestMapping(value = "/journey")
public class JourneyController {
	private static Logger logger = LoggerFactory.getLogger(JourneyController.class);
	@Autowired
	private JourneyService journeyService;
	
	
    /**
     * 游记详情
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<Journey>> detail(HttpServletRequest request, HttpServletResponse response,Long journeyId) {
		logger.info("查看游记详情入参，journeyId:{}",journeyId);
		
		OpenResponse<Journey> openResponse = new OpenResponse<Journey>();
		if(journeyId == null){
			openResponse.setErrorMessage("参数journeyId为空");
			openResponse.setResult(Constants.FAIL);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
			return new ResponseEntity<OpenResponse<Journey>> (openResponse, HttpStatus.OK);
		}
		
		try {
			Journey journey = journeyService.queryJourneyById(journeyId);
			openResponse.setData(journey);
			openResponse.setResult(Constants.SUCCESS);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("JourneyController detail error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<Journey>> (openResponse, HttpStatus.OK);
	}
	
	
}
