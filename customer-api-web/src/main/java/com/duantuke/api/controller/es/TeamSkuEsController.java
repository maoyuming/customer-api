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
import com.duantuke.basic.face.esbean.output.TeamSkuOutputBean;
import com.duantuke.basic.face.esbean.query.TeamSkuQueryBean;
import com.duantuke.basic.face.service.TeamSkuSearchService;
import com.google.gson.Gson;


/**
 * 团体sku
 * @author he
 *
 */
@Controller
@RequestMapping(value = "/es/teamSku")
public class TeamSkuEsController {
	private static Logger logger = LoggerFactory.getLogger(TeamSkuEsController.class);
	@Autowired
	private TeamSkuSearchService teamSkuSearchService;
	
	
	/**
	 * @param teamSkuQueryBean
	 * 搜索团体skues
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<TeamSkuOutputBean>>> search(TeamSkuQueryBean teamSkuQueryBean) {
		logger.info("CustomerTeamSkuController search：{}",new Gson().toJson(teamSkuQueryBean));
		OpenResponse<List<TeamSkuOutputBean>> openResponse = new OpenResponse<List<TeamSkuOutputBean>>();
		try {
			List<TeamSkuOutputBean> list = teamSkuSearchService.searchTeamSkusFromEs(teamSkuQueryBean);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerTeamSkuController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<TeamSkuOutputBean>>> (openResponse, HttpStatus.OK);
	}
	
	
}
