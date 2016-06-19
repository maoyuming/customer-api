package com.duanduke.api.pay.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author shellingford
 * @version 创建时间：2012-8-1 上午9:47:34
 * 
 */
public class HttpUtils {
    
    private Logger log = LoggerFactory.getLogger(HttpUtils.class);
	private String charset="UTF-8";
	private int timeout=20000;
	private CookieStore cookieStore;
	
	public HttpUtils(){}
	public HttpUtils(CookieStore cookieStore){
		this.cookieStore=cookieStore;
	}
	

	public  String dojson(String url,List<Header> headers,String json) throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpclient=null;
		try {
			HttpPost post=new HttpPost(url);
			if(headers!=null && headers.size()>0){
				for (Header header : headers) {
					post.addHeader(header);
				}
			}
			StringEntity entity=new StringEntity(json,charset);
			post.setEntity(entity);
			if(url.startsWith("https")){
				httpclient=createSSLClientDefault(cookieStore);
			}else{
				if(cookieStore!=null){
					httpclient=HttpClients.custom().setDefaultCookieStore(cookieStore).build();
				}else{
					httpclient=HttpClients.custom().build();;
				}
			}
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
			post.setConfig(requestConfig);
			CloseableHttpResponse response=httpclient.execute(post);
			int code=response.getStatusLine().getStatusCode();
			if(code==200){
				return EntityUtils.toString(response.getEntity(),charset);
			}else{
				log.error("code:"+code);
				throw new RuntimeException("code:"+code);
			}
		} finally{
			if(httpclient!=null){
				httpclient.close();
			}
		}
	}
	
	
	public String doget(String url,List<Header> headers,Map<String,String> map) throws ClientProtocolException, IOException, URISyntaxException{
		CloseableHttpClient httpclient=null;
		try {
			HttpGet get=new HttpGet();
			if(headers!=null && headers.size()>0){
				for (Header header : headers) {
					get.addHeader(header);
				}
			}
			StringBuilder sb=new StringBuilder();
			sb.append(url);
			if(map!=null){
				Iterator<Map.Entry<String,String>> iter=map.entrySet().iterator();
				boolean first=true;
				while(iter.hasNext()){
					if(first){
						first=false;
						sb.append("?");
					}else{
						sb.append("&");
					}
					Map.Entry<String, String> entry=iter.next();
					sb.append(entry.getKey());
					sb.append("=");
					sb.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
				}
			}
			get.setURI(new URI(sb.toString()));
			if(url.startsWith("https")){
				httpclient=createSSLClientDefault(cookieStore);
			}else{
				if(cookieStore!=null){
					httpclient=HttpClients.custom().setDefaultCookieStore(cookieStore).build();
				}else{
					httpclient=HttpClients.custom().build();;
				}
			}
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
			get.setConfig(requestConfig);
			CloseableHttpResponse response=httpclient.execute(get);
			int code=response.getStatusLine().getStatusCode();
			if(code==200){
				return EntityUtils.toString(response.getEntity(),charset);
			}else{
				log.error("code:"+code);
				throw new HttpException(code);
			}
		} finally{
			if(httpclient!=null){
				httpclient.close();
			}
		}
	}
	
	public String dopost(String url,List<Header> headers,Map<String,String> map) throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient=null;
		try {
			HttpPost post=new HttpPost(url);
			if(headers!=null && headers.size()>0){
				for (Header header : headers) {
					post.addHeader(header);
				}
			}
			List<BasicNameValuePair> values=new ArrayList<BasicNameValuePair>();
			if(map!=null && map.size()>0){
				for (Entry<String, String> entry : map.entrySet()) {
					BasicNameValuePair se=new BasicNameValuePair(entry.getKey(), entry.getValue());
					values.add(se);
				}
			}
			UrlEncodedFormEntity entity=new UrlEncodedFormEntity(values,charset);  
			post.setEntity(entity);
			if(url.startsWith("https")){
				httpclient=createSSLClientDefault(cookieStore);
			}else{
				if(cookieStore!=null){
					httpclient=HttpClients.custom().setDefaultCookieStore(cookieStore).build();
				}else{
					httpclient=HttpClients.custom().build();;
				}
			}
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
			post.setConfig(requestConfig);
			CloseableHttpResponse response=httpclient.execute(post);
			int code=response.getStatusLine().getStatusCode();
			if(code==200){
				return EntityUtils.toString(response.getEntity(),charset);
			}else{
				log.error("code:"+code);
				throw new HttpException(code);
			}
		} finally{
			if(httpclient!=null){
				httpclient.close();
			}
		}
		
	}
	
	public static JSONObject doPostRequest(String url, String params) throws IOException {
		HttpClientBuilder hcb = HttpClientBuilder.create();
		CloseableHttpClient client = hcb.build();
		HttpPost httpPost = new HttpPost(url);
		JSONObject object = null;
		try {
			httpPost.setEntity(new StringEntity(params, "UTF-8"));
			HttpResponse resp = client.execute(httpPost);
			HttpEntity entity = resp.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity, "UTF-8");
				object = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
			}
		}
		return object;
	}
	
	
	public CloseableHttpClient createSSLClientDefault(CookieStore cookieStore){
		try {
			
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				//信任所有
				public boolean isTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			if(cookieStore!=null){
				return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore).build();
			}else{
				return HttpClients.custom().setSSLSocketFactory(sslsf).build();
			}
		} catch (Exception e) {
			log.error("code:"+e);
			throw new RuntimeException();
		}
	}
	
	public static String urlEncodeUTF8(Map<?,?> map) {
	    StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                urlEncodeUTF8(entry.getKey().toString()),
                urlEncodeUTF8(entry.getValue().toString())
            ));
        }
	        return sb.toString();       
	}
	
	public static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
	}
	
	public String dopostMain(String url,List<Header> headers,String content) throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient=null;
		try {
			HttpPost post=new HttpPost(url);
			if(headers!=null && headers.size()>0){
				for (Header header : headers) {
					post.addHeader(header);
				}
			}
			StringEntity entity=new StringEntity(content,charset);  
			post.setEntity(entity);
			if(url.startsWith("https")){
				httpclient=createSSLClientDefault(cookieStore);
			}else{
				if(cookieStore!=null){
					httpclient=HttpClients.custom().setDefaultCookieStore(cookieStore).build();
				}else{
					httpclient=HttpClients.custom().build();;
				}
			}
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
			post.setConfig(requestConfig);
			CloseableHttpResponse response=httpclient.execute(post);
			int code=response.getStatusLine().getStatusCode();
			if(code==200){
				return EntityUtils.toString(response.getEntity(),charset);
			}else{
				log.error("code:"+code);
				throw new HttpException(code);
			}
		} finally{
			if(httpclient!=null){
				httpclient.close();
			}
		}
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}

