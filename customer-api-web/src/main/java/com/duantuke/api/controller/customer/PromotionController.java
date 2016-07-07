package com.duantuke.api.controller.customer;

import java.util.Arrays;
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
import com.duantuke.promotion.domain.PromotionQueryIn;
import com.duantuke.promotion.face.service.PromotionService;
import com.duantuke.promotion.po.Promotion;
import com.google.gson.Gson;


@Controller
@RequestMapping(value = "/customer/promotion")
public class PromotionController {
	private static Logger logger = LoggerFactory.getLogger(PromotionController.class);
	@Autowired
	private PromotionService promotionService;
	
	
	/**
     * 查询我的优惠券列表
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/queryPromotionList")
    public ResponseEntity<OpenResponse<List<Promotion>>> queryPromotionList(HttpServletRequest request, HttpServletResponse response,
    		PromotionQueryIn promotionQueryIn) {

		//校验参数
		checkPromotionListParam(promotionQueryIn,request);
		
		OpenResponse<List<Promotion>> openResponse = new OpenResponse<List<Promotion>>();
		try {
			List<Promotion>  list = promotionService.queryPromotionList(promotionQueryIn);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<List<Promotion>>>(openResponse, HttpStatus.OK);
	
	}
	
	private void checkPromotionListParam(PromotionQueryIn promotionQueryIn,HttpServletRequest request){
		logger.info("c端用户促销信息",new Gson().toJson(promotionQueryIn));
		
		promotionQueryIn.setCustomerId(TokenUtil.getUserIdByRequest(request));
		
	}
	/**
	 * 领取优惠券
	 * @param request
	 * @param response
	 * @param promotionDefinitionId
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/get")
	public ResponseEntity<Boolean> get(HttpServletRequest request, HttpServletResponse response,
			Long promotionDefinitionId,Long customerId ) {
		if(promotionDefinitionId==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		promotionService.createCustomerPromotion(promotionDefinitionId, Arrays.asList(TokenUtil.getUserIdByRequest(request)));
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
}
