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
import com.duantuke.basic.face.service.DuantukeCommentService;
import com.duantuke.basic.po.DuantukeComment;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/comment")
public class DuantukeCommentController {
	private static Logger logger = LoggerFactory.getLogger(DuantukeCommentController.class);
	@Autowired
	private DuantukeCommentService duantukeCommentService;
	
    /**
     * 评价
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/save")
    public ResponseEntity<OpenResponse<Boolean>> save(HttpServletRequest request, HttpServletResponse response,DuantukeComment duantukeComment) {
		//校验参数
		checkParam(duantukeComment);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			int count = duantukeCommentService.insert(duantukeComment);
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
			logger.error("评价异常",e);
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkParam(DuantukeComment duantukeComment){
		
		logger.info("评价入参：{}",new Gson().toJson(duantukeComment));
		
		if(duantukeComment==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		if(duantukeComment.getFid() == null){
			throw new OpenException(ErrorEnum.fidNull.getName(),ErrorEnum.fidNull.getId());
		}
		if(duantukeComment.getCustomerId() == null){
			throw new OpenException(ErrorEnum.customeridNull.getName(),ErrorEnum.customeridNull.getId());
		}
		if(duantukeComment.getDuantukeComment() == null){
			throw new OpenException(ErrorEnum.commentNull.getName(),ErrorEnum.commentNull.getId());
		}
		
	}
	

}
