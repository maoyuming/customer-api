package com.fangbaba.api.util.pushlog;

import java.util.Date;

import org.bson.Document;

import com.fangbaba.api.util.DateUtil;

public class PushLog {
	private Date createTime = new Date();
	private String channelid;//渠道id
	private String pushcontent;//推送内容
	private String result;//调用返回结果
	private String pushtype;//GdsChannelUrlEnum
	private String pushtypedesc;
	private boolean resultflag; 
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPushcontent() {
		return pushcontent;
	}
	public void setPushcontent(String pushcontent) {
		this.pushcontent = pushcontent;
	}
	public String getPushtype() {
		return pushtype;
	}
	public void setPushtype(String pushtype) {
		this.pushtype = pushtype;
	}
	public String getPushtypedesc() {
		return pushtypedesc;
	}
	public void setPushtypedesc(String pushtypedesc) {
		this.pushtypedesc = pushtypedesc;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public static PushLog toPushLog(Document doc) {
		PushLog pushLog = new PushLog();
		pushLog.setPushcontent(doc.getString("pushcontent"));
		pushLog.setPushtype(doc.getString("pushtype"));
		pushLog.setPushtypedesc(doc.getString("pushtypedesc"));
		pushLog.setCreateTime(DateUtil.strToDate(doc.getString("createtime"), "yyyy-MM-dd HH:mm:ss"));
		pushLog.setChannelid(doc.getString("channelid"));
		pushLog.setResult(doc.getString("result"));
		return pushLog;
	}
	
	public Document toDocument() {

		Document doc = new Document();
		this.put(doc, "pushcontent", this.getPushcontent());
		this.put(doc, "pushtype", this.getPushtype());
		this.put(doc, "pushtypedesc", this.getPushtypedesc());
		this.put(doc, "createtime", DateUtil.dateToStr(this.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		this.put(doc, "channelid", this.getChannelid());
		this.put(doc, "result", this.getResult());

		return doc;
	}
	private void put(Document doc, String key, Object value) {
		if (value == null) {
			return;
		}
		doc.put(key, value);
	}
	public boolean isResultflag() {
		return resultflag;
	}
	public void setResultflag(boolean resultflag) {
		this.resultflag = resultflag;
	}
	
}
