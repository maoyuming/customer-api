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
import com.duantuke.basic.face.service.HotWordService;
import com.duantuke.basic.po.HotWord;
import com.google.gson.Gson;

/**
 * @author he
 * 热词
 */
@Controller
@RequestMapping(value = "/customer/hotword")
public class CustomerHotWordController {
	
	private static Logger logger = LoggerFactory.getLogger(CustomerJournalController.class);
	
	@Autowired
	private HotWordService hotWordService;
	
	
	/**
	 * 热词查询
	 */
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<HotWord>>> query(HotWord hotWord) {
		logger.info("CustomerHotWordController query：{}",new Gson().toJson(hotWord));
		OpenResponse<List<HotWord>> openResponse = new OpenResponse<List<HotWord>>();
		try {
			List<HotWord> list = hotWordService.queryHotWords(hotWord);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerHotWordController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<HotWord>>> (openResponse, HttpStatus.OK);
		
		
	}
	
}
