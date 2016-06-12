package com.duantuke.api.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.api.util.SpringContextUtil;
import com.duantuke.basic.face.service.UserTokenService;

public class OpenHandlerInterceptor implements HandlerInterceptor{

	private static Logger logger = LoggerFactory.getLogger(HandlerInterceptor.class);
	

	private  UserTokenService userTokenService= SpringContextUtil.getBean("userTokenService");
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String token = request.getHeader("token");
		
		if(StringUtils.isBlank(token)){
			logger.info("入参token为空");
			throw new OpenException(ErrorEnum.tokenError.getName(),ErrorEnum.tokenError.getId());
		}
		try{
				
	
			//checktoken
			Long userId = userTokenService.queryUserByUserToken(token);
			request.setAttribute("userId",userId);
			
			response.setHeader("userId",userId+"");
			logger.info("校验用户token通过,{},{}",token,userId);
		
		} catch (Exception e) {
			logger.error("校验用户token错误",e);
			throw new OpenException(ErrorEnum.tokenError);
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
	

}
