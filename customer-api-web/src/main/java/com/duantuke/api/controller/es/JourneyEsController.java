package com.duantuke.api.controller.es;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gson.Gson;


/**
 * 游记
 * @author he
 *
 */
@Controller
@RequestMapping(value = "/es/journey")
public class JourneyEsController {
	private static Logger logger = LoggerFactory.getLogger(JourneyEsController.class);
	
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
	
}
