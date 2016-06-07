package com.fangbaba.api.domain.fangcang.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Request;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class OrderStatusRequest extends Request {
	private static final long serialVersionUID = 4684411078587842205L;
	
	@XStreamAlias("SyncOrderStatusRequest")
	private SyncOrderStatusRequest syncOrderStatusRequest;

	public SyncOrderStatusRequest getSyncOrderStatusRequest() {
		return syncOrderStatusRequest;
	}

	public void setSyncOrderStatusRequest(SyncOrderStatusRequest syncOrderStatusRequest) {
		this.syncOrderStatusRequest = syncOrderStatusRequest;
	}

}
