package com.duantuke.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.duantuke.basic.face.base.RetInfo;
import com.duantuke.basic.face.service.CustomerService;
import com.duantuke.basic.po.Customer;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	

	@Autowired
	private CustomerService customerService;
	
    /**
     * 注册用户
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/register")
    public ResponseEntity<OpenResponse<Boolean>> register(HttpServletRequest request, HttpServletResponse response,Customer customer) {
		//校验参数
		checkParam(customer);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			RetInfo<Boolean> retInfo = customerService.register(customer);
			if(retInfo.isResult()){
				openResponse.setResult(Constants.SUCCESS);
			}else{
				openResponse.setResult(Constants.FAIL);
				openResponse.setErrorCode(retInfo.getCode());
				openResponse.setErrorMessage(retInfo.getMsg());
			}
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.checkFail.getId());
			openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
			logger.error("注册用户异常"+new Gson().toJson(customer),e);
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	/**
	 * 是否为注册用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkregister")
	public ResponseEntity<OpenResponse<Boolean>> checkregister(HttpServletRequest request, HttpServletResponse response,Customer customer) {
		//校验参数
		checkParam(customer);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			boolean flag = customerService.isExistCustomerByPhone(customer.getPhone());
			if(flag){
				openResponse.setResult(Constants.SUCCESS);
			}else{
				openResponse.setResult(Constants.FAIL);	
			}
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.checkFail.getId());
			openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
			logger.error("校验注册用户异常"+new Gson().toJson(customer),e);
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	/**
	 * 获取用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/query")
	public ResponseEntity<OpenResponse<Customer>> query(HttpServletRequest request, HttpServletResponse response,Customer customer) {
		//校验参数
		checkParam(customer);
		
		OpenResponse<Customer> openResponse = new OpenResponse<Customer>();
		try {
			Customer Customer2 = customerService.queryCustomerByPhone(customer.getPhone());
			openResponse.setData(Customer2);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.checkFail.getId());
			openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
			logger.error("获取c端用户异常"+new Gson().toJson(customer),e);
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<Customer>>(openResponse, HttpStatus.OK);
	}
	/**
	 * 修改用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/update")
	public ResponseEntity<OpenResponse<Boolean>> update(HttpServletRequest request, HttpServletResponse response,Customer customer) {
		//校验参数
		checkParam(customer);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			boolean flag = customerService.updateCustomer(customer);
			if(flag){
				openResponse.setData(flag);
				openResponse.setResult(Constants.SUCCESS);
			}else{
				openResponse.setResult(Constants.FAIL);
				openResponse.setErrorCode(ErrorEnum.updateUserFail.getId());
				openResponse.setErrorMessage(ErrorEnum.updateUserFail.getName());
			}
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("获取c端用户异常"+new Gson().toJson(customer),e);
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkParam(Customer customer){
		logger.info("c端注册信息",new Gson().toJson(customer));
		
		if(customer==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		if(StringUtils.isBlank(customer.getPhone())){
			throw new OpenException(ErrorEnum.phoneEmpty.getName(),ErrorEnum.phoneEmpty.getId());
		}
		
	}
	
}
