package com.fangbaba.api.controller.fangcang.filter;

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

import com.fangbaba.api.domain.fangcang.Header;
import com.fangbaba.api.enums.FangCangResultFlagEnum;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.api.util.SpringContextUtil;

/**
 * @author he 房仓header校验filter
 *
 */
public class FangCangHeaderFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(FangCangHeaderFilter.class);
	
	private BusinessUtil<com.fangbaba.api.domain.fangcang.Request> businessRequestUtil = SpringContextUtil.getBean("businessUtil");

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,  
            FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		try {
			String xmlparam  =(String)request.getParameter("xml");
			Header header = businessRequestUtil.ParseHeaderByDom4j(xmlparam);
			com.fangbaba.api.domain.fangcang.Response xmlresponse = new com.fangbaba.api.domain.fangcang.Response();
			boolean isMatch  =  businessRequestUtil.isMatchFangCangSignature(header);
			if (!isMatch) {
				logger.info("校验签名失败:{}~param:{}",request.getRequestURI(),request.getParameter("xml"));
				xmlresponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
				xmlresponse.setResultMsg("校验签名错误");
				String retInfo = businessRequestUtil.genFangCangResponse(xmlresponse,com.fangbaba.api.domain.fangcang.Response.class.getName());
				response.getWriter().write(retInfo);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/xml;charset=UTF-8");
			} else {
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			logger.error("校验签名错误",e);
			e.printStackTrace();
			com.fangbaba.api.domain.fangcang.Response xmlresponse = new com.fangbaba.api.domain.fangcang.Response();
			xmlresponse.setResultFlag(FangCangResultFlagEnum.failure.getId());
			xmlresponse.setResultMsg("校验签名错误");
			String retInfo = businessRequestUtil.genFangCangResponse(xmlresponse,com.fangbaba.api.domain.fangcang.Response.class.getName());
			response.getWriter().write(retInfo);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/xml;charset=UTF-8");
		}
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
