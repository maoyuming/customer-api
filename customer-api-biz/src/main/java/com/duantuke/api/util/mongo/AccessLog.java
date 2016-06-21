package com.duantuke.api.util.mongo;

import java.util.Date;

import org.bson.Document;

/**
 * 业务日志实体.
 * <p>
 * 可以采用链式赋值.
 *
 * @author zhaoshb
 * @since 0.0.1
 */
public class AccessLog  {

	/**
	 *
	 */
	private static final long serialVersionUID = -6016004536838792301L;

	public static final String TOKEN = "token";

	public static final String URL = "url";

	public static final String APP_VERSION = "appVersion";


	public static final String CN_CREATETIME = "createTime";
	public static final String SYSTEM = "system";

	private String token = null;

	private String url = null;

	private String appVersion = null;
	private String system = null;


	private Date createTime = new Date();

	
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public Document toDocument() {
		this.check();

		Document doc = new Document();
		this.put(doc, AccessLog.TOKEN, this.getToken());
		this.put(doc, AccessLog.URL, this.getUrl());
		this.put(doc, AccessLog.APP_VERSION, this.getAppVersion());
		this.put(doc, AccessLog.CN_CREATETIME, this.getCreateTime());
		this.put(doc, AccessLog.SYSTEM, this.getSystem());

		return doc;
	}

	public static AccessLog toAccessLog(Document doc) {
		AccessLog bisLog = new AccessLog();
		bisLog.setToken(doc.getString(AccessLog.TOKEN));
		bisLog.setUrl(doc.getString(AccessLog.URL));
		bisLog.setAppVersion(doc.getString(AccessLog.APP_VERSION));
		bisLog.setCreateTime(doc.getDate(AccessLog.CN_CREATETIME));
		bisLog.setSystem(doc.getString(AccessLog.SYSTEM));

		return bisLog;
	}

	private void check() {
	}

	private void put(Document doc, String key, Object value) {
		if (value == null) {
			return;
		}
		doc.put(key, value);
	}

}
