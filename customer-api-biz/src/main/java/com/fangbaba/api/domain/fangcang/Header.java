package com.fangbaba.api.domain.fangcang;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 * 天下房仓的header
 * @author tankai
 *
 */
@XmlRootElement
public class Header implements Serializable {
	@XStreamAsAttribute
	@XStreamAlias("TimeStamp")
	private String timeStamp;//"2015-07-16 15:36:48"
	@XStreamAsAttribute
	@XStreamAlias("PartnerCode")
	private String partnerCode;//="test" 
	@XStreamAsAttribute
	@XStreamAlias("RequestType")
	private String requestType;//="getHotelInfo"
	@XStreamAsAttribute
	@XStreamAlias("Signature")
	private String signature;//="4024E88BF4A3B61C9F648999F5D5C80D"></Header>
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getPartnerCode() {
		return partnerCode;
	}
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	
	
	
}


