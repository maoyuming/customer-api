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
import org.springframework.web.bind.annotation.RequestMethod;

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.api.util.TokenUtil;
import com.duantuke.basic.face.service.CustomerLikeService;
import com.duantuke.basic.face.service.DuantukeLikeService;
import com.duantuke.basic.po.DuantukeLike;
import com.duantuke.basic.po.Hotel;
import com.duantuke.basic.po.Sight;
import com.google.gson.Gson;

/**
 * 点赞收藏服务
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/customer/like")
public class CustomerLikeController {
	private static Logger logger = LoggerFactory.getLogger(CustomerLikeController.class);
	@Autowired
	private DuantukeLikeService duantukeLikeService;
	
	@Autowired
	private CustomerLikeService customerLikeService;
	
    /**
     * 点赞收藏
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/like")
    public ResponseEntity<OpenResponse<Boolean>> like(HttpServletRequest request, HttpServletResponse response,DuantukeLike duantukeLike) {
		//校验参数
		checkParam(duantukeLike,request);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			//同一个不允许重复点赞和收藏
			int num = duantukeLikeService.countDuantukeLike(duantukeLike);
			if(num==0){
				int count = duantukeLikeService.insert(duantukeLike);
				if(count>0){
					openResponse.setResult(Constants.SUCCESS);
				}else{
					openResponse.setResult(Constants.FAIL);
					openResponse.setErrorCode(ErrorEnum.saveFail.getId());
					openResponse.setErrorMessage(ErrorEnum.saveFail.getName());
				}
			}else{
				throw new OpenException(ErrorEnum.duplicateSaveNull.getName(),ErrorEnum.duplicateSaveNull.getId());
			}
			
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
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
		checkParam(duantukeLike,request);
		
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
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
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
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<Integer>>(openResponse, HttpStatus.OK);
	}
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkParam(DuantukeLike duantukeLike,HttpServletRequest request){
		
		logger.info("点赞收藏入参：{}",new Gson().toJson(duantukeLike));
		
		if(duantukeLike==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		if(duantukeLike.getFid() == null){
			throw new OpenException(ErrorEnum.fidNull.getName(),ErrorEnum.fidNull.getId());
		}
//		if(duantukeLike.getCustomerId() == null){
//			throw new OpenException(ErrorEnum.customeridNull.getName(),ErrorEnum.customeridNull.getId());
//		}
		
		if(duantukeLike.getBusinessType() == null){
			throw new OpenException(ErrorEnum.businessTypeNull.getName(),ErrorEnum.businessTypeNull.getId());
		}
		
		duantukeLike.setCustomerId(TokenUtil.getUserIdByRequest(request));
		
		
	}
	
	
	/**
	 * 查询用户收藏的酒店信息
	 * @param 
	 */
	@RequestMapping(value = "/hotel", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<Hotel>>> hotel(HttpServletRequest request, HttpServletResponse response) {
		Long customerId = TokenUtil.getUserIdByRequest(request);
		logger.info("查询收藏酒店customerId：{}",customerId);
		OpenResponse<List<Hotel>> openResponse = new OpenResponse<List<Hotel>>();
		try {
			List<Hotel> list = customerLikeService.queryHotels(customerId);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerHotelController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<Hotel>>> (openResponse, HttpStatus.OK);
		
		
	}
	/**
	 * 查询用户收藏的景点信息
	 * @param 
	 */
	@RequestMapping(value = "/sight", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<Sight>>> sight(HttpServletRequest request, HttpServletResponse response) {
		Long customerId = TokenUtil.getUserIdByRequest(request);
		logger.info("查询收藏景点customerId：{}",customerId);
		OpenResponse<List<Sight>> openResponse = new OpenResponse<List<Sight>>();
		try {
			List<Sight> list = customerLikeService.querySights(customerId);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerHotelController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<Sight>>> (openResponse, HttpStatus.OK);
		
		
	}
	

}
