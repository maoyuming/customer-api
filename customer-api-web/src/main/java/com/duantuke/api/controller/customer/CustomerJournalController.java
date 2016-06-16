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
import com.duantuke.basic.face.service.JourneyService;
import com.duantuke.basic.po.Journey;


/**
 * 游记详情
 * @author yuming.mao
 *
 */
@Controller
@RequestMapping(value = "/customer/journal")
public class CustomerJournalController {
	private static Logger logger = LoggerFactory.getLogger(CustomerJournalController.class);
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
		logger.info("游记详情，journeyId：{}",journeyId);
		OpenResponse<Journey> openResponse = new OpenResponse<Journey>();
		try {
			Journey journey = journeyService.queryJourneyById(journeyId);
			openResponse.setData(journey);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("游记详情异常",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<Journey>> (openResponse, HttpStatus.OK);
	}
	
	
}
