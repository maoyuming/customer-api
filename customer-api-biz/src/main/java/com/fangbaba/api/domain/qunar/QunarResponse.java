package com.fangbaba.api.domain.qunar;

public class QunarResponse<T> {
    private boolean ret;//": true, 
    private String errCode;// ": "SUCCESS", 
    private String errMsg;//": "SUCCESS", 
    private T data;//": [ ], 
    private Long totalSize;//": 0
	public boolean isRet() {
		return ret;
	}
	public void setRet(boolean ret) {
		this.ret = ret;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}
    
    
    
} 
