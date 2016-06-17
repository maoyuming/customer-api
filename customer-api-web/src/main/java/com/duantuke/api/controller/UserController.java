package com.duantuke.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dtk.token.TokenHttpUtils;
import com.dtk.token.TokenUtils;
import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.api.util.Config;
import com.duantuke.api.util.DateUtil;
import com.duantuke.basic.face.UserTokenTypeEnum;
import com.duantuke.basic.face.base.RetInfo;
import com.duantuke.basic.face.service.CustomerService;
import com.duantuke.basic.po.Customer;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.mk.mms.face.service.ISmsMessageService;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ISmsMessageService smsMessageService;
//	@Autowired
//	private UserTokenService userTokenService;
	
    /**
     * 注册用户
     * @param request
     * @param response
     * @return
     * @throws Exception 
     * @throws NumberFormatException 
     */
	@RequestMapping(value = "/register")
    public ResponseEntity<OpenResponse<String>> register(HttpServletRequest request, HttpServletResponse response,
    		Customer customer, String verifycode) throws NumberFormatException, Exception {
		//校验参数
		checkParam(customer);
		

		//校验参数
		checkParamCheckVerify(verifycode, customer.getPhone());
		
		OpenResponse<String> openResponse = new OpenResponse<String>();
		try {
			
			logger.info("验证码校验开始: code:{},phone:{}",verifycode, customer.getPhone());
			boolean checkResult = smsMessageService.checkVerifyCode(customer.getPhone(), verifycode);
			if(checkResult){
				logger.info("验证校验通过，准备开始注册账号");
				RetInfo<Boolean> retInfo = customerService.register(customer);
				if(retInfo.isResult()){
					//生成token

					String	token = TokenHttpUtils.createToken(Config.getValue("cas.server"), customer.getCustomerId()+"",
								UserTokenTypeEnum.C.getId()+"", Long.valueOf(Config.getValue("token.expiredTime")));
					openResponse.setData(token);
					openResponse.setResult(Constants.SUCCESS);
				}else{
					openResponse.setResult(Constants.FAIL);
					openResponse.setErrorCode(retInfo.getCode());
					openResponse.setErrorMessage(retInfo.getMsg());
				}
			}else{
				logger.info("验证校验失败");
				openResponse.setResult(Constants.FAIL);
				openResponse.setErrorCode(ErrorEnum.verifyCodeFail.getId());
				openResponse.setErrorMessage(ErrorEnum.verifyCodeFail.getName());
			}
			
		}finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<String>>(openResponse, HttpStatus.OK);
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
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	/**
	 * 生成用户token
	 * TODO:这个接口是否开放出去？？？
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value = "/gentoken")
	public ResponseEntity<OpenResponse<String>> gentoken(HttpServletRequest request, HttpServletResponse response,Customer customer) throws NumberFormatException, Exception {
		//校验参数
		checkParam(customer);
		
		OpenResponse<String> openResponse = new OpenResponse<String>();
		try {
			
			//根据手机号码查询userid
			Customer customer2 = customerService.queryCustomerByPhone(customer.getPhone());
			if(customer2==null){
				throw new OpenException(ErrorEnum.customeridNull);
			}
			String token = TokenHttpUtils.getToken(Config.getValue("cas.server"), customer2.getCustomerId()+"");
			if(StringUtils.isEmpty(token)){
				token = TokenHttpUtils.createToken(Config.getValue("cas.server"), customer2.getCustomerId()+"",
						UserTokenTypeEnum.C.getId()+"", Long.valueOf(Config.getValue("token.expiredTime")));
			}
			
			//userTokenService.genUserToken(UserTokenTypeEnum.C,customer.getPhone());
			if(StringUtils.isNotBlank(token)){
				openResponse.setData(token);
				openResponse.setResult(Constants.SUCCESS);
			}else{
				openResponse.setResult(Constants.FAIL);	
			}
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		return new ResponseEntity<OpenResponse<String>>(openResponse, HttpStatus.OK);
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
	
	/**
	 * 校验验证验证码参数
	 * @param token
	 */
	private void checkParamCheckVerify(String verifycode, String phone){
		
		if(StringUtils.isBlank(phone)){
			throw new OpenException(ErrorEnum.phoneEmpty.getName(),ErrorEnum.phoneEmpty.getId());
		}
		
		
        if(Strings.isNullOrEmpty(verifycode)){
        	throw new OpenException(ErrorEnum.codeEmpty.getName(),ErrorEnum.codeEmpty.getId());
        }
		
		
		
		logger.info("入参：{},{}",phone,verifycode);
	}
	
}
