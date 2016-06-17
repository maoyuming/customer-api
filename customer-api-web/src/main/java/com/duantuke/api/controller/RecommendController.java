package com.duantuke.api.controller;

import java.util.List;

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
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.basic.face.service.RecommendService;
import com.duantuke.basic.po.RecommendDetail;
import com.duantuke.basic.po.RecommendItem;
import com.google.gson.Gson;


/**
 * 推荐接口
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/recommend")
public class RecommendController {
	private static Logger logger = LoggerFactory.getLogger(RecommendController.class);
	@Autowired
	private RecommendService recommendService;
	
	
	 /**
     * 推荐列表
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/list")
    public ResponseEntity<OpenResponse<List<RecommendItem>>> list(HttpServletRequest request, HttpServletResponse response,
    		RecommendItem recommendItem) {
		checkParam(recommendItem);
		OpenResponse<List<RecommendItem>> openResponse = new OpenResponse<List<RecommendItem>>();
		try {
			List<RecommendItem> list = recommendService.queryRecommendItemList(recommendItem);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<RecommendItem>>>(openResponse, HttpStatus.OK);
	}
	/**
	 * 推荐详情
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public ResponseEntity<OpenResponse<RecommendDetail>> detail(HttpServletRequest request, HttpServletResponse response,
			RecommendDetail recommendDetail) {
		checkDetailParam(recommendDetail);
		OpenResponse<RecommendDetail> openResponse = new OpenResponse<RecommendDetail>();
		try {
			RecommendDetail recommendDetailResult = recommendService.selectByRecommendid(recommendDetail.getRecommendId());
			openResponse.setData(recommendDetailResult);
			openResponse.setResult(Constants.SUCCESS);
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<RecommendDetail>>(openResponse, HttpStatus.OK);
	}
	
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkParam(RecommendItem recommendItem){
		
		logger.info("推荐入参：{}",new Gson().toJson(recommendItem));
		
		if(recommendItem==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
//		if(duantukeComment.getFid() == null){
//			throw new OpenException(ErrorEnum.fidNull.getName(),ErrorEnum.fidNull.getId());
//		}
		if(recommendItem.getPositionId() == null){
			throw new OpenException(ErrorEnum.positionNull.getName(),ErrorEnum.positionNull.getId());
		}
		
		
	}
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkDetailParam(RecommendDetail recommendDetail){
		
		logger.info("推荐详情入参：{}",new Gson().toJson(recommendDetail));
		
		if(recommendDetail==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
//		if(duantukeComment.getFid() == null){
//			throw new OpenException(ErrorEnum.fidNull.getName(),ErrorEnum.fidNull.getId());
//		}
		if(recommendDetail.getRecommendId() == null){
			throw new OpenException(ErrorEnum.recommendIdNull.getName(),ErrorEnum.recommendIdNull.getId());
		}
		
		
	}
	

}
