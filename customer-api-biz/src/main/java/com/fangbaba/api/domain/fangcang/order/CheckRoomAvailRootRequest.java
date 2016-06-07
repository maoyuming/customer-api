package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("Request")
public class CheckRoomAvailRootRequest extends Request {
	private static final long serialVersionUID = 4684411078587842205L;
	
	@XStreamAlias("CheckRoomAvailRequest")
	private CheckRoomAvailRequest checkRoomAvailRequest;

	public CheckRoomAvailRequest getCheckRoomAvailRequest() {
		return checkRoomAvailRequest;
	}

	public void setCheckRoomAvailRequest(CheckRoomAvailRequest checkRoomAvailRequest) {
		this.checkRoomAvailRequest = checkRoomAvailRequest;
	}



}
