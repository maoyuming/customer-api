package com.duantuke.api.filter;

import java.io.IOException;
import java.util.Date;

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

import com.duantuke.api.util.IPUtil;
import com.duantuke.api.util.SpringContextUtil;
import com.duantuke.api.util.mongo.AccessLog;
import com.duantuke.api.util.mongo.AccessLogDelegate;
import com.duantuke.basic.face.SystemTypeEnum;


/**
 * @author he 房仓header校验filter
 *
 */
public class OpenHeaderFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(OpenHeaderFilter.class);

	private  AccessLogDelegate accessLogDelegate= SpringContextUtil.getBean("accessLogDelegate");
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,  
            FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		try {
			//保存访问日志
			//1、访问地址 2、访问用户，如果有的话，3、app版本 4、访问时间

			String token = request.getHeader("token");
			String appVersion = request.getHeader("appVersion");
			Date date = new Date();
			StringBuffer url = request.getRequestURL();

			String system = SystemTypeEnum.C.getId()+"";
			String ip = IPUtil.getRealIpAddr(request);
			
			AccessLog accessLog = new AccessLog();
			accessLog.setToken(token);
			accessLog.setUrl(url.toString());
			accessLog.setCreateTime(date);
			accessLog.setAppVersion(appVersion);
			accessLog.setSystem(system);
			accessLog.setIp(ip);
			
			accessLogDelegate.saveAccessLog(accessLog);
			//保存app版本
		} catch (Exception e) {
			logger.error("校验url访问出错错误",e);
		}
		

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
