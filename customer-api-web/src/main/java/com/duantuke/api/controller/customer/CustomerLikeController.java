package com.duantuke.api.controller.customer;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.api.util.StringUtils;
import com.duantuke.api.util.TokenUtil;
import com.duantuke.basic.face.bean.PageItem;
import com.duantuke.basic.face.esbean.output.HotelOutputBean;
import com.duantuke.basic.face.esbean.query.HotelQueryBean;
import com.duantuke.basic.face.service.CustomerLikeService;
import com.duantuke.basic.face.service.DuantukeLikeService;
import com.duantuke.basic.face.service.HotelSearchService;
import com.duantuke.basic.po.DuantukeLike;
import com.duantuke.basic.po.Hotel;
import com.duantuke.basic.po.Journey;
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
	@Autowired
	private HotelSearchService hotelSearchService;
	
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
	public ResponseEntity<OpenResponse<List<HotelOutputBean>>> hotel(HttpServletRequest request, HttpServletResponse response,
			PageItem pageItem) {
		Long customerId = TokenUtil.getUserIdByRequest(request);
		logger.info("查询收藏酒店customerId：{},分页：{}",customerId,JSONObject.toJSON(pageItem));
		OpenResponse<List<HotelOutputBean>> openResponse = new OpenResponse<List<HotelOutputBean>>();
		try {
			List<Hotel> list = customerLikeService.queryHotels(customerId,pageItem);
			logger.info("查询收藏酒店hotel数据库结果：{}",JSONObject.toJSON(list));
			//根据id反查es
			
			if(CollectionUtils.isNotEmpty(list)){
				List<Long> hotelList = new ArrayList<Long>();
				for (Hotel hotel : list) {
					hotelList.add(hotel.getHotelId());
				}
				String queryhotelids = StringUtils.listToString(hotelList, ',');
				HotelQueryBean hotelQueryBean = new HotelQueryBean();
				hotelQueryBean.setQueryhotelids(queryhotelids);
				logger.info("查询收藏es入参：{}",queryhotelids);
				List<HotelOutputBean> hotelOutputBeanList = hotelSearchService.searchHotelsFromEs(hotelQueryBean,null,null);
				logger.info("查询收藏es结果：{}",JSONObject.toJSON(hotelOutputBeanList));
				openResponse.setData(hotelOutputBeanList);
			}
			
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerHotelController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<HotelOutputBean>>> (openResponse, HttpStatus.OK);
		
		
	}
	/**
	 * 查询用户收藏的景点信息
	 * @param 
	 */
	@RequestMapping(value = "/sight", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<Sight>>> sight(HttpServletRequest request, HttpServletResponse response,
			PageItem pageItem) {
		Long customerId = TokenUtil.getUserIdByRequest(request);
		logger.info("查询收藏景点customerId：{},分页:{}",customerId,JSONObject.toJSON(pageItem));
		OpenResponse<List<Sight>> openResponse = new OpenResponse<List<Sight>>();
		try {
			List<Sight> list = customerLikeService.querySights(customerId,pageItem);
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
	/**
	 * 查询用户收藏的景点信息
	 * @param 
	 */
	@RequestMapping(value = "/journey", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<Journey>>> journey(HttpServletRequest request, HttpServletResponse response,
			PageItem pageItem) {
		Long customerId = TokenUtil.getUserIdByRequest(request);
		logger.info("查询收藏游记customerId：{},分页:{}",customerId,JSONObject.toJSON(pageItem));
		OpenResponse<List<Journey>> openResponse = new OpenResponse<List<Journey>>();
		try {
			List<Journey> list = customerLikeService.queryJourneys(customerId,pageItem);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerHotelController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<Journey>>> (openResponse, HttpStatus.OK);
		
		
	}
	

}
