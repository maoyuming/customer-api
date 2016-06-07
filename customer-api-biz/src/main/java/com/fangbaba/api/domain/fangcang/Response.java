package com.fangbaba.api.domain.fangcang;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.enums.FangCangResultEnum;
import com.thoughtworks.xstream.annotations.XStreamAlias;
@XmlRootElement
@XStreamAlias("Response")
public class Response implements Serializable{
	@XStreamAlias("ResultCode")
	private String resultCode;//Succes
	@XStreamAlias("ResultNo")
	private String resultNo;//>000</ResultNo>
	@XStreamAlias("ResultMsg")
	private String resultMsg;//>success</ResultMsg>
	
	@XStreamAlias("ResultFlag")
	private Integer resultFlag;//1-成功，0-失败
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultNo() {
		return resultNo;
	}
	public void setResultNo(String resultNo) {
		this.resultNo = resultNo;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	public Integer getResultFlag() {
		return resultFlag;
	}
	public void setResultFlag(Integer resultFlag) {
		this.resultFlag = resultFlag;
	}
	public boolean isSuccess(){
		return FangCangResultEnum.Success.getResult().equals(this.resultCode);
			
	}
	@Override
	public String toString() {
		return "Response [resultCode=" + resultCode + ", resultNo=" + resultNo + ", resultMsg=" + resultMsg + "]";
	}
	
}
