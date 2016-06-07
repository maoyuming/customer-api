package com.fangbaba.api.domain.qunar;

import scala.Int;

public class QunarOptOrderResponse {
/**
 * "ret": false, 
    "statusCode": 0, 
    "errorMsg": [
        "订单状态不满足当前操作"
    ], 
    "errCode": "ERR0014"
 */
	//操作结果是否成功
	private boolean ret;
	//去哪儿订单状态码
	private Integer statusCode;
	//ret=false时，返回的具体错误信息
	private String[] errorMsg;
	//去哪儿订单状态码
	private String  errCode;
	//去哪儿订单状态描述
	private String  statusDesc;
	public boolean isRet() {
		return ret;
	}
	public void setRet(boolean ret) {
		this.ret = ret;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String[] getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String[] errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
}
