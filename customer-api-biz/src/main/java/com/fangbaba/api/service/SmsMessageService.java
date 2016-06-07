package com.fangbaba.api.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.core.RedisCacheManager;
import com.fangbaba.api.util.Config;
import com.fangbaba.basic.face.enums.MyErrorEnum;
import com.google.common.base.Strings;


@Service
public class SmsMessageService {

	private static final Logger logger = LoggerFactory.getLogger(SmsMessageService.class);
	
	private static final String CACHE_NAME="openapi";

	@Autowired
	private RedisCacheManager cacheManager;
	
	public String generateVerifyCode(String phone){
		logger.info("开始生成验证码");
		String codeNumString="";
		String charSequence="";
		//缓存过期时间，默认值为60秒
		Integer expiresTimeInteger=60;
		
		codeNumString=Config.getValue("message.code_num");
		charSequence=Config.getValue("message.char_sequence");
		//如果读取内容为空，则设置默认值
		codeNumString=Strings.isNullOrEmpty(codeNumString)?"4":codeNumString;
		charSequence=Strings.isNullOrEmpty(charSequence)?"0123456789":charSequence;
		expiresTimeInteger=Integer.parseInt(Config.getValue("message.expires_time"));
		if (Strings.isNullOrEmpty(codeNumString)) {
			throw MyErrorEnum.customError
					.getMyException("资源文件属性message.code_num值失败");
		}
		if (Strings.isNullOrEmpty(charSequence)) {
			throw MyErrorEnum.customError
					.getMyException("资源文件属性message.char_sequence值失败");
		}
		logger.info("生成验证码长度为{}，所有验证码字符集为：{}，过期时间：{}",codeNumString,charSequence,expiresTimeInteger);
		String message=genCode(Long.parseLong(codeNumString), charSequence);
		
		 //将验证码存入缓存
		 
		 cacheManager.setExpires(CACHE_NAME, phone, message, expiresTimeInteger);
		return message;
	}
	
	
	public boolean checkVerifyCode(String phone, String code) {
		 //2.验证验证码是否有效
        String cacheName=CACHE_NAME;
        String key=phone;
        String cacheCodeString="";
        try {
        	cacheCodeString=(String)cacheManager.getExpires(cacheName, key);
        	logger.info("缓存中验证码为：{}",cacheCodeString);
		} catch (Exception e) {
			logger.info("获取验证码失败，该key对应的验证码不存在或已过期");
		}
        boolean checkResult=false;
        if (Strings.isNullOrEmpty(cacheCodeString)) {
			logger.info("获取redis中验证码为空，key为{}",key);
		}else {
			if (cacheCodeString.equals(code)) {
	        	checkResult=true;
	        	//验证完成后删除缓存
	        	cacheManager.remove(cacheName, key);
	        	logger.info("验证码验证成功");
			}else {
				logger.info("缓存中验证码与传递过来的验证码不匹配，redis code：{}，phone code：{}",cacheCodeString,code);
			}
		}
        return checkResult;
	}
	
	
	/**
	 * 随机生成验证码
	 * @param codeNum
	 * @param charSequence
	 * @return
	 */
	private String genCode(Long codeNum,String charSequence){
		StringBuffer verifyCode=new StringBuffer();
		char[] chars=charSequence.toCharArray();
		if (chars!=null && chars.length>0&&codeNum>0) {
			Random random = new Random();
			for (int i = 0; i < codeNum; i++) {
				String randomCharString=String.valueOf(chars[random.nextInt(chars.length)]);
				verifyCode.append(randomCharString);
			}
		}
		return verifyCode.toString();
	}
}
