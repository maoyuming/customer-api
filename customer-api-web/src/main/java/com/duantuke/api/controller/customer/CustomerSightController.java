package com.duantuke.api.controller.customer;

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
import com.duantuke.basic.face.esbean.output.SightOutputBean;
import com.duantuke.basic.face.esbean.query.SightQueryBean;
import com.duantuke.basic.face.service.SightSearchService;
import com.google.gson.Gson;

/**
 * @author he
 * 景点
 */
@Controller
@RequestMapping(value = "/customer/sight")
public class CustomerSightController {
	
	private static Logger logger = LoggerFactory.getLogger(CustomerJournalController.class);
	
	@Autowired
	private SightSearchService sightSearchService;
	
	/**
	 * @param sightQueryBean
	 * 搜索景点es
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<SightOutputBean>>> search(SightQueryBean sightQueryBean) {
		logger.info("CustomerSightController search：{}",new Gson().toJson(sightQueryBean));
		OpenResponse<List<SightOutputBean>> openResponse = new OpenResponse<List<SightOutputBean>>();
		try {
			List<SightOutputBean> list = sightSearchService.searchSightsFromEs(sightQueryBean);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerSightController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<SightOutputBean>>> (openResponse, HttpStatus.OK);
	}
	
}
