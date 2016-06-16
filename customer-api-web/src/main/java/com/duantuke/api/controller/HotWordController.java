package com.duantuke.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.HotWordOutBean;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.face.service.HotWordService;
import com.duantuke.basic.po.HotWord;
import com.google.gson.Gson;

/**
 * @author he
 * 热词
 */
@Controller
@RequestMapping(value = "/hotword")
public class HotWordController {
	
	private static Logger logger = LoggerFactory.getLogger(HotWordController.class);
	
	@Autowired
	private HotWordService hotWordService;
	
	@Autowired
    private Mapper dozerMapper;
	
	
	/**
	 * 热词查询
	 */
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<HotWordOutBean>>> query(HotWord hotWord) {
		logger.info("CustomerHotWordController query：{}",new Gson().toJson(hotWord));
		OpenResponse<List<HotWordOutBean>> openResponse = new OpenResponse<List<HotWordOutBean>>();
		try {
			List<HotWord> list = hotWordService.queryHotWords(hotWord);
			List<HotWordOutBean> list1 = new ArrayList<HotWordOutBean>();
			if(CollectionUtils.isNotEmpty(list)){
				for (HotWord hotWordBean:list) {
					HotWordOutBean hotWordOutBean = dozerMapper.map(hotWordBean, HotWordOutBean.class);
					list1.add(hotWordOutBean);
				}
			}
			openResponse.setData(list1);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerHotWordController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<HotWordOutBean>>> (openResponse, HttpStatus.OK);
		
		
	}
	
}
