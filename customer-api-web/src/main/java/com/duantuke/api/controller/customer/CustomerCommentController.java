package com.duantuke.api.controller.customer;

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
import com.duantuke.api.util.TokenUtil;
import com.duantuke.basic.face.service.DuantukeCommentService;
import com.duantuke.basic.po.DuantukeComment;
import com.google.gson.Gson;


/**
 * 评价服务
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/customer/comment")
public class CustomerCommentController {
	private static Logger logger = LoggerFactory.getLogger(CustomerCommentController.class);
	@Autowired
	private DuantukeCommentService duantukeCommentService;
	
    /**
     * 评价
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/comment")
    public ResponseEntity<OpenResponse<Boolean>> comment(HttpServletRequest request, HttpServletResponse response,DuantukeComment duantukeComment) {
		//校验参数
		checkSubmitParam(duantukeComment,request);
		
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
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	
	
	
	 /**
     * 评价数
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/count")
    public ResponseEntity<OpenResponse<Integer>> count(HttpServletRequest request, HttpServletResponse response,DuantukeComment duantukeComment) {

		checkParam(duantukeComment,request);
		OpenResponse<Integer> openResponse = new OpenResponse<Integer>();
		try {
			int count = duantukeCommentService.countDuantukeComment(duantukeComment);
			openResponse.setData(count);
			openResponse.setResult(Constants.SUCCESS);
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<Integer>>(openResponse, HttpStatus.OK);
	}
	/**
	 * 评价列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ResponseEntity<OpenResponse<List<DuantukeComment>>> list(HttpServletRequest request, HttpServletResponse response,DuantukeComment duantukeComment) {

		checkParam(duantukeComment,request);
		OpenResponse<List<DuantukeComment>> openResponse = new OpenResponse<List<DuantukeComment>>();
		try {
			
			List<DuantukeComment> list = duantukeCommentService.selectByDuantukeComment(duantukeComment);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		}  finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<DuantukeComment>>>(openResponse, HttpStatus.OK);
	}
	
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkParam(DuantukeComment duantukeComment,HttpServletRequest request){
		
		logger.info("评价入参：{}",new Gson().toJson(duantukeComment));
		
		if(duantukeComment==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
//		if(duantukeComment.getFid() == null){
//			throw new OpenException(ErrorEnum.fidNull.getName(),ErrorEnum.fidNull.getId());
//		}
//		if(duantukeComment.getCustomerId() == null){
//			throw new OpenException(ErrorEnum.customeridNull.getName(),ErrorEnum.customeridNull.getId());
//		}
//		if(duantukeComment.getDuantukeComment() == null){
//			throw new OpenException(ErrorEnum.commentNull.getName(),ErrorEnum.commentNull.getId());
//		}
		
		if(duantukeComment.getBusinessType() == null){
			throw new OpenException(ErrorEnum.businessTypeNull.getName(),ErrorEnum.businessTypeNull.getId());
		}
		duantukeComment.setCustomerId(TokenUtil.getUserIdByRequest(request));
		
		
	}
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkSubmitParam(DuantukeComment duantukeComment,HttpServletRequest request){
		
		logger.info("评价入参：{}",new Gson().toJson(duantukeComment));
		
		if(duantukeComment==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		if(duantukeComment.getFid() == null){
			throw new OpenException(ErrorEnum.fidNull.getName(),ErrorEnum.fidNull.getId());
		}
//		if(duantukeComment.getCustomerId() == null){
//			throw new OpenException(ErrorEnum.customeridNull.getName(),ErrorEnum.customeridNull.getId());
//		}
		if(duantukeComment.getDuantukeComment() == null){
			throw new OpenException(ErrorEnum.commentNull.getName(),ErrorEnum.commentNull.getId());
		}
		if(duantukeComment.getBusinessType() == null){
			throw new OpenException(ErrorEnum.businessTypeNull.getName(),ErrorEnum.businessTypeNull.getId());
		}
		
		duantukeComment.setCustomerId(TokenUtil.getUserIdByRequest(request));
		
		
	}
	

}
