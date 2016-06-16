package com.duantuke.api.controller.customer;

import java.util.ArrayList;
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
import com.duantuke.basic.face.esbean.query.JourneyQueryBean;
import com.duantuke.basic.face.service.JourneySearchService;
import com.duantuke.basic.face.service.JourneyService;
import com.duantuke.basic.po.Journey;
import com.google.gson.Gson;


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
	
	@Autowired
	private JourneySearchService journeySearchService;
	
	/**
	 * @param journeyQueryBean
	 * 搜索游记es
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<JourneyOutputBean>>> search(JourneyQueryBean journeyQueryBean,String hotelIds,String sightIds) {
		logger.info("CustomerJourneyController search：{}",new Gson().toJson(journeyQueryBean));
		OpenResponse<List<JourneyOutputBean>> openResponse = new OpenResponse<List<JourneyOutputBean>>();
		try {
			Map<String,List<String>> hotelidmap = new HashMap<String,List<String>>();
			if(StringUtils.isNotEmpty(hotelIds)){
				List<String> hotelIdList = Arrays.asList(hotelIds.split(","));
				hotelidmap.put("hotelIds", hotelIdList);
			}
			Map<String,List<String>> sightidmap = new HashMap<String,List<String>>();
			if(StringUtils.isNotEmpty(sightIds)){
				List<String> sightIdList = Arrays.asList(sightIds.split(","));
				hotelidmap.put("sightIds", sightIdList);
			}
			List<JourneyOutputBean> list = journeySearchService.searchJourneysFromEs(journeyQueryBean,hotelidmap,sightidmap);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerJourneyController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<JourneyOutputBean>>> (openResponse, HttpStatus.OK);
	}
	
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
