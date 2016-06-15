package com.duantuke.api.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dtk.token.TokenUtils;
import com.dtk.token.TokenValidateUtils;
import com.duantuke.api.common.Constants;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.api.util.Config;

public class OpenHandlerInterceptor implements HandlerInterceptor{

	private static Logger logger = LoggerFactory.getLogger(HandlerInterceptor.class);
	

//	private  UserTokenService userTokenService= SpringContextUtil.getBean("userTokenService");
	/**
	 * token格式：用户id#用户类型#申请时间#过期时间
	 */
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
//			Long userId = userTokenService.queryUserByUserToken(token);
//			Long userId = 1L;
			String tokenHostUrl = Config.getValue("cas.server");
			boolean flag = TokenValidateUtils.validate(tokenHostUrl, token);
//			if(!flag){//TODO:暂时屏蔽掉验证方法
//				throw new OpenException(ErrorEnum.tokenError);
//			}
			
			String str = TokenUtils.decrypt(tokenHostUrl, token);
			if(StringUtils.isEmpty(str)){
				throw new OpenException(ErrorEnum.tokenError);
			}
			String array[] = str.split("#");
			
			Long userId = Long.valueOf(array[0]);
			request.setAttribute(Constants.USER_ID,userId);
//			response.setHeader("userId",userId+"");
			logger.info("校验用户token通过,{},{}",token,userId);
		
		} catch (Throwable e) {
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
