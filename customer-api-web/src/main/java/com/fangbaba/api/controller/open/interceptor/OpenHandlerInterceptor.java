package com.fangbaba.api.controller.open.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fangbaba.api.controller.open.RoomTypeController;
import com.fangbaba.api.domain.open.OpenRequest;
import com.fangbaba.api.domain.open.hotel.RoomtypeRequest;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.util.Config;
import com.fangbaba.api.util.MD5Util;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class OpenHandlerInterceptor implements HandlerInterceptor{

	private static Logger logger = LoggerFactory.getLogger(HandlerInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String channelId = request.getHeader("channelid");
		String timeStamp = request.getHeader("timestamp");
		String token = request.getHeader("token");
		
		if(channelId==null){
			logger.info("入参渠道id为空");
			throw new OpenException(DistributionErrorEnum.channelidNulll.getName(),DistributionErrorEnum.channelidNulll.getId());
		}
		if(timeStamp==null){
			logger.info("入参时间戳为空");
			throw new OpenException(DistributionErrorEnum.timestampNulll.getName(),DistributionErrorEnum.timestampNulll.getId());
		}
		if(StringUtils.isBlank(token)){
			logger.info("入参token为空");
			throw new OpenException(DistributionErrorEnum.tokenError.getName(),DistributionErrorEnum.tokenError.getId());
		}
		
		
		String md = Config.getValue("ota_open_token_"+channelId);
		if(StringUtils.isBlank(md)){
			logger.info("token和渠道id不一致");
			throw new OpenException(DistributionErrorEnum.tokenError.getName(),DistributionErrorEnum.tokenError.getId());
		}
		if(!MD5Util.encryption(md).equals(token)){
			logger.info("token错误");
			throw new OpenException(DistributionErrorEnum.tokenError.getName(),DistributionErrorEnum.tokenError.getId());
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		System.out.println(MD5Util.encryption("imike"));
		
	}

}
