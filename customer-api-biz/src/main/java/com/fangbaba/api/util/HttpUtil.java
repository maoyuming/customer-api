package com.fangbaba.api.util;

import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http接口调用类
 * @author tankai
 *
 */
public class HttpUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	/**
	 * 调用http接口
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, Map<String, String> params, int timeout) {
		String back = "";
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);
			for (String key : params.keySet()) {
				method.setParameter(key, params.get(key));
			}
			
			int status = client.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				back = method.getResponseBodyAsString();
			} else {
				logger.error("异常：" + status);
				//throw new Exception("异常：" + status);
			}
		} catch (Exception e) {
			logger.error("doPost:异常" + e.getLocalizedMessage(),e);
			//throw e;
		} finally {
			method.releaseConnection();
		}
		return back;
	}
}
