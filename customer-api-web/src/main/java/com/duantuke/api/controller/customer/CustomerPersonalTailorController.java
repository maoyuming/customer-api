package com.duantuke.api.controller.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.util.TokenUtil;
import com.duantuke.basic.face.service.PersonalTailorService;
import com.duantuke.basic.po.PersonalTailor;
import com.duantuke.order.common.enums.OrderErrorEnum;


/**
 * 私人订制
 * @author yuming.mao
 *
 */
@Controller
@RequestMapping(value = "/customer/personal")
public class CustomerPersonalTailorController {
	private static Logger logger = LoggerFactory.getLogger(CustomerPersonalTailorController.class);
	@Autowired
	private PersonalTailorService personalTailorService;

	
    /**
     * 创建私人订制
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/create")
    public ResponseEntity<OpenResponse<PersonalTailor>> create(HttpServletRequest request, HttpServletResponse response) {
		OpenResponse<PersonalTailor> openResponse = new OpenResponse<PersonalTailor>();
		
		Long customerId = TokenUtil.getUserIdByRequest(request);

		String personalTailorJson = request.getParameter("data");
		logger.info("从request中获取的订制信息" + personalTailorJson);
		
		try {
            if (StringUtils.isBlank(personalTailorJson)) {
                logger.error("从request中获取的订制信息为空,无法创建");
                openResponse.setResult(Boolean.FALSE.toString());
                openResponse.setErrorCode(OrderErrorEnum.paramsError.getErrorCode());
                openResponse.setErrorMessage(OrderErrorEnum.paramsError.getErrorMsg());
            }else{
    			PersonalTailor personalTailor = JSON.parseObject(personalTailorJson, PersonalTailor.class);
    			personalTailor.setCustomerId(customerId);
    			personalTailorService.addPersonalTailor(personalTailor);
    			openResponse.setResult(Constants.SUCCESS);
    			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
            }
			
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("CustomerPersonalTailorController create error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<PersonalTailor>> (openResponse, HttpStatus.OK);
	}
	
	
	 /**
     * 查看订制详情
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<PersonalTailor>> detail(HttpServletRequest request, HttpServletResponse response,Long personalTailorId) {
		logger.info("根据personalTailorId查询订制详情，personalTailorId:{}",personalTailorId);
		OpenResponse<PersonalTailor> openResponse = new OpenResponse<PersonalTailor>();
		
		if(personalTailorId == null){
			openResponse.setErrorMessage("参数personalTailorId为空");
			openResponse.setResult(Constants.FAIL);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
			return new ResponseEntity<OpenResponse<PersonalTailor>> (openResponse, HttpStatus.OK);
		}
		
		try {
			PersonalTailor personalTailor = personalTailorService.queryPersonalTailorById(personalTailorId);
			openResponse.setData(personalTailor);
			openResponse.setResult(Constants.SUCCESS);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("CustomerPersonalTailorController detail error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<PersonalTailor>> (openResponse, HttpStatus.OK);
	}


	 /**
     * 根据用户ID查询订制列表
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/query")
    public ResponseEntity<OpenResponse<List<PersonalTailor>>> query(HttpServletRequest request, HttpServletResponse response,
    		 Long customerId,Integer pageNo,Integer pageSize) {
		if(customerId==null){
			customerId = TokenUtil.getUserIdByRequest(request);
		}
		logger.info("根据用户ID查询订制列表入参，customerId:{}",customerId);
		OpenResponse<List<PersonalTailor>> openResponse = new OpenResponse<List<PersonalTailor>>();
		
		if(customerId == null){
			openResponse.setErrorMessage("参数customerId为空");
			openResponse.setResult(Constants.FAIL);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
			return new ResponseEntity<OpenResponse<List<PersonalTailor>>> (openResponse, HttpStatus.OK);
		}
		
		if(pageNo==null || pageSize==null){
			openResponse.setErrorMessage("参数pageNo或者pageSize为空");
			openResponse.setResult(Constants.FAIL);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
			return new ResponseEntity<OpenResponse<List<PersonalTailor>>> (openResponse, HttpStatus.OK);
		}
		
		
		try {
			PersonalTailor personalTailor = new PersonalTailor();
			personalTailor.setCustomerId(customerId);			
			List<PersonalTailor> list = personalTailorService.queryPersonalTailors(personalTailor,pageNo,pageSize);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("CustomerPersonalTailorController query error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<List<PersonalTailor>>> (openResponse, HttpStatus.OK);
	}
}
