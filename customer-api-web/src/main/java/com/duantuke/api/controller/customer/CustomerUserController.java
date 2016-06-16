package com.duantuke.api.controller.customer;

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
import com.duantuke.api.util.TokenUtil;
import com.duantuke.basic.face.service.CustomerService;
import com.duantuke.basic.po.Customer;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/customer/user")
public class CustomerUserController {
	private static Logger logger = LoggerFactory.getLogger(CustomerUserController.class);
	

	@Autowired
	private CustomerService customerService;
	
	/**
	 * 获取用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/query")
	public ResponseEntity<OpenResponse<Customer>> query(HttpServletRequest request,
			HttpServletResponse response,Customer customer) {
		//校验参数
		checkParam(customer);
		
		OpenResponse<Customer> openResponse = new OpenResponse<Customer>();
		try {
			Customer Customer2 = customerService.queryCustomerById(TokenUtil.getUserIdByRequest(request));
			openResponse.setData(Customer2);
			openResponse.setResult(Constants.SUCCESS);
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
	public ResponseEntity<OpenResponse<Boolean>> update(HttpServletRequest request, HttpServletResponse response,
			Customer customer) {
		//校验参数
		checkParam(customer);
		customer.setCustomerId(TokenUtil.getUserIdByRequest(request));
		
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
			throw e;
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
		logger.info("c端用户信息",new Gson().toJson(customer));
		
//		if(customer==null){
//			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
//		}
		
//		if(StringUtils.isBlank(customer.getPhone())){
//			throw new OpenException(ErrorEnum.phoneEmpty.getName(),ErrorEnum.phoneEmpty.getId());
//		}
//		if(userId==null){
//			throw new OpenException(ErrorEnum.userIdNull.getName(),ErrorEnum.userIdNull.getId());
//		}
		
		
		
	}
	
}
