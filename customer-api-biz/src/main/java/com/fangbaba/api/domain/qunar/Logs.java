package com.fangbaba.api.domain.qunar;

public class Logs {
	private String operator;//": "QAtest:005",
	private String opTime;//": "20150415122802",
    private String content;//": "QAtest:005申请订单100401749971退款,退款金额:180元,退款原因:满房"
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
    
    
}
