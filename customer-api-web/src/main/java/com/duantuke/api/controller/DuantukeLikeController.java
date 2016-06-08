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
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.basic.face.service.DuantukeLikeService;
import com.duantuke.basic.po.DuantukeLike;
import com.google.gson.Gson;

/**
 * 点赞收藏服务
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/like")
public class DuantukeLikeController {
	private static Logger logger = LoggerFactory.getLogger(DuantukeLikeController.class);
	@Autowired
	private DuantukeLikeService duantukeLikeService;
	
    /**
     * 点赞收藏
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/like")
    public ResponseEntity<OpenResponse<Boolean>> like(HttpServletRequest request, HttpServletResponse response,DuantukeLike duantukeLike) {
		//校验参数
		checkParam(duantukeLike);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			int count = duantukeLikeService.insert(duantukeLike);
			if(count>0){
				openResponse.setResult(Constants.SUCCESS);
			}else{
				openResponse.setResult(Constants.FAIL);
				openResponse.setErrorCode(ErrorEnum.saveFail.getId());
				openResponse.setErrorMessage(ErrorEnum.saveFail.getName());
			}
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.checkFail.getId());
			openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
			logger.error("发送消息异常",e);
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	/**
	 * 点赞收藏
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/canclelike")
	public ResponseEntity<OpenResponse<Boolean>> canclelike(HttpServletRequest request, HttpServletResponse response,DuantukeLike duantukeLike) {
		//校验参数
		checkParam(duantukeLike);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			int count = duantukeLikeService.deleteDuantukeLike(duantukeLike);
			if(count>0){
				openResponse.setResult(Constants.SUCCESS);
			}else{
				openResponse.setResult(Constants.FAIL);
				openResponse.setErrorCode(ErrorEnum.saveFail.getId());
				openResponse.setErrorMessage(ErrorEnum.saveFail.getName());
			}
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.checkFail.getId());
			openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
			logger.error("发送消息异常",e);
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	
	
	 /**
     * 点赞收藏数
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/count")
    public ResponseEntity<OpenResponse<Integer>> count(HttpServletRequest request, HttpServletResponse response,DuantukeLike duantukeLike) {
		
		OpenResponse<Integer> openResponse = new OpenResponse<Integer>();
		try {
			int count = duantukeLikeService.countDuantukeLike(duantukeLike);
			openResponse.setData(count);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.checkFail.getId());
			openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
			logger.error("查询点赞收藏数异常",e);
		}
		return new ResponseEntity<OpenResponse<Integer>>(openResponse, HttpStatus.OK);
	}
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkParam(DuantukeLike duantukeLike){
		
		logger.info("点赞收藏入参：{}",new Gson().toJson(duantukeLike));
		
		if(duantukeLike==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		if(duantukeLike.getFid() == null){
			throw new OpenException(ErrorEnum.fidNull.getName(),ErrorEnum.fidNull.getId());
		}
		if(duantukeLike.getCustomerId() == null){
			throw new OpenException(ErrorEnum.customeridNull.getName(),ErrorEnum.customeridNull.getId());
		}
		
	}
	

}
