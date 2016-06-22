package com.duantuke.api.pay.common;
/** 
 * 接口返回的bean
 * @author  作者 yx 
 * @date 创建时间：2015年11月20日 下午12:22:47 
 * @version 1.0  
 */
public class RequestBean {
	private boolean success;
	private String msg;
	private Integer type;
	private Object data;
	private String _MSG_;
	
	public RequestBean() {
	}
	public RequestBean(boolean success) {
		this.success = success;
	}
	public RequestBean(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}
	public RequestBean(boolean success, String msg,Integer type) {
		this.success = success;
		this.msg = msg;
		this.type=type;
	}
//	public RequestBean(boolean success, Object data) {
//		this.success = success;
//		this.data=data;
//	}
	
	
	public RequestBean(boolean success, String msg, Integer type, Object data) {
		this.success = success;
		this.msg = msg;
		this.type = type;
		this.data = data;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String get_MSG_() {
		String msg="OK,";
		if(success){
			msg="OK,";
		}else{
			msg="ERROR,"+this.msg;
		}
		
		return msg;
	}
	public void set_MSG_(String _MSG_) {
		this._MSG_ = _MSG_;
	}
	
	

}
