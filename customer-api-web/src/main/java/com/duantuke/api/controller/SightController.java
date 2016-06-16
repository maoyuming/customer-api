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

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.face.service.SightService;
import com.duantuke.basic.po.Journey;
import com.duantuke.basic.po.Sight;


/**
 * 景点
 * @author yuming.mao
 *
 */
@Controller
@RequestMapping(value = "/sight")
public class SightController {
	private static Logger logger = LoggerFactory.getLogger(SightController.class);
	@Autowired
	private SightService sightService;
	
	
    /**
     * 景点详情
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<Sight>> detail(HttpServletRequest request, HttpServletResponse response,Long sightId) {
		logger.info("景点详情，sightId：{}",sightId);
		
		OpenResponse<Sight> openResponse = new OpenResponse<Sight>();
		if(sightId == null){
			openResponse.setErrorMessage("参数sightId为空");
			openResponse.setResult(Constants.FAIL);
			return new ResponseEntity<OpenResponse<Sight>> (openResponse, HttpStatus.OK);
		}
		
		try {
			Sight sight = sightService.querySightById(sightId);
			openResponse.setData(sight);;
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("SightController detail error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<Sight>> (openResponse, HttpStatus.OK);
	}
	
	
}
