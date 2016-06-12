package com.duantuke.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.api.util.SpringContextUtil;
import com.duantuke.basic.face.service.UserTokenService;

/**
 * @author he 房仓header校验filter
 *
 */
public class OpenHeaderFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(OpenHeaderFilter.class);

	private  UserTokenService userTokenService= SpringContextUtil.getBean("userTokenService");

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,  
            FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
//		try {
//			String token = request.getHeader("token");
//			//checktoken
//			Long userId = userTokenService.queryUserByUserToken(token);
//			request.setAttribute("userId",userId);
//			
//			response.setHeader("userId",userId+"");
//			logger.info("校验用户token通过,{},{}",token,userId);
//		} catch (Exception e) {
//			logger.error("校验用户token错误",e);
//			throw new OpenException(ErrorEnum.tokenError);
//		}
		

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
