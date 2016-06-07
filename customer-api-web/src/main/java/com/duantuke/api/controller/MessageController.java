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
import com.duantuke.api.util.TokenUtil;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.mk.mms.face.service.ISmsMessageService;
import com.mk.mms.kafka.common.SmsMessageTypeEnum;
import com.mk.mms.kafka.model.SmsMessage;

@Controller
@RequestMapping(value = "/message")
public class MessageController {
	private static Logger logger = LoggerFactory.getLogger(MessageController.class);
	

	@Autowired
	private ISmsMessageService smsMessageService;
	@Autowired
	private com.duantuke.api.kafka.MessageProducer messageProducer;
	
    /**
     * 发送短信
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/send")
    public ResponseEntity<OpenResponse<Boolean>> send(HttpServletRequest request, HttpServletResponse response,SmsMessage message) {
		String token = request.getHeader("TOKEN");
		//校验参数
		checkParam(token, message);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			messageProducer.commonSmsMsg(message);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.checkFail.getId());
			openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
			logger.error("发送消息异常",e);
		}
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	
	
	/**
	 * @param phone   手机号码,必填
	 * @param message 发送内容,非必填
	 * @param type    非必填，默认为普通短信. 1——普通短信 , 2——语音消息
	 * @return
	 */
	@RequestMapping("/verifycode/send")
	public ResponseEntity<OpenResponse<Boolean>> send(HttpServletRequest request,  SmsMessage smsMessage,String type) throws Exception {
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			logger.info("发送验证码,{},类型：{}",new Gson().toJson(smsMessage),type);
			String token = request.getHeader("TOKEN");
			//校验参数
			//check token
			checkParamSendVerify(token, smsMessage);
			
			SmsMessageTypeEnum smsMessageTypeEnum = SmsMessageTypeEnum.normal;
			if (StringUtils.isNotBlank(type)) {
				smsMessageTypeEnum = SmsMessageTypeEnum.getEnumById(type);
			}
			if (smsMessageTypeEnum == null) {
				throw new OpenException(ErrorEnum.notMessageType);
			}
			smsMessage.setSmsMessageTypeEnum(smsMessageTypeEnum);

			smsMessageService.sendVerifycode(smsMessage);

			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("发送短信异常",e);
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.checkFail.getId());
			openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
		}
		logger.info("发送验证码结果,{}",new Gson().toJson(openResponse));
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	
	
	@Deprecated
	@RequestMapping("/verifycode/verify")
	public ResponseEntity<OpenResponse<Boolean>> verifyCode(HttpServletRequest request, String phone, String verifycode) throws Exception {
		String token = request.getHeader("TOKEN");
		//校验参数
		checkParamCheckVerify(token, verifycode, phone);
       
        logger.info("验证码校验开始: code:{},phone:{}",verifycode, phone);
		boolean checkResult=smsMessageService.checkVerifyCode(phone, verifycode);
		
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			if(checkResult){
				openResponse.setResult(Constants.SUCCESS);
			}else{
				openResponse.setResult(Constants.FAIL);
				openResponse.setErrorCode(ErrorEnum.checkFail.getId());
				openResponse.setErrorMessage(ErrorEnum.checkFail.getName());
			}
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			openResponse.setErrorCode(ErrorEnum.systemError.getId());
			openResponse.setErrorMessage(ErrorEnum.systemError.getName());
		}
       
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	
	
	
	/**
	 * 校验发送验证码参数
	 * @param token
	 */
	private void checkParamSendVerify(String token , SmsMessage message){
		//check token
		if(!TokenUtil.checkOmsToken(token)){
			logger.info("token错误,"+token);
			throw new OpenException(ErrorEnum.tokenError.getName(),ErrorEnum.tokenError.getId());
		}
		
		
		if(message==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		
		if(StringUtils.isBlank(message.getPhone())){
			throw new OpenException(ErrorEnum.phoneEmpty.getName(),ErrorEnum.phoneEmpty.getId());
		}
		logger.info("token={},入参：{}",token,new Gson().toJson(message));
	}
	/**
	 * 校验验证验证码参数
	 * @param token
	 */
	private void checkParamCheckVerify(String token ,String verifycode, String phone){
		//check token
		if(!TokenUtil.checkOmsToken(token)){
			logger.info("token错误,"+token);
			throw new OpenException(ErrorEnum.tokenError.getName(),ErrorEnum.tokenError.getId());
		}
		
		if(StringUtils.isBlank(phone)){
			throw new OpenException(ErrorEnum.phoneEmpty.getName(),ErrorEnum.phoneEmpty.getId());
		}
		
		
        if(Strings.isNullOrEmpty(verifycode)){
        	throw new OpenException(ErrorEnum.codeEmpty.getName(),ErrorEnum.codeEmpty.getId());
        }
		
		
		
		logger.info("token={},入参：{},{}",token,phone,verifycode);
	}
	
	/**
	 * 校验参数
	 * @param token
	 */
	private void checkParam(String token , SmsMessage message){
		//check token
		if(!TokenUtil.checkOmsToken(token)){
			logger.info("token错误,"+token);
			throw new OpenException(ErrorEnum.tokenError.getName(),ErrorEnum.tokenError.getId());
		}
		
		
		if(message==null){
			throw new OpenException(ErrorEnum.argsNull.getName(),ErrorEnum.argsNull.getId());
		}
		if(StringUtils.isBlank(message.getMessage())){
			throw new OpenException(ErrorEnum.messageEmpty.getName(),ErrorEnum.messageEmpty.getId());
		}
		
		if(StringUtils.isBlank(message.getPhone())){
			throw new OpenException(ErrorEnum.phoneEmpty.getName(),ErrorEnum.phoneEmpty.getId());
		}
		logger.info("token={},入参：{}",token,new Gson().toJson(message));
	}
	
	
}
