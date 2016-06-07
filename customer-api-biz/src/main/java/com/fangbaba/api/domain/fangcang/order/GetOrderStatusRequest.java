package com.fangbaba.api.domain.fangcang.order;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author he
 * 订单状态查询xml bean
 */
@XmlRootElement
public class GetOrderStatusRequest implements Serializable{
	private static final long serialVersionUID = -4189890527246412977L;
	@XStreamAlias("SpOrderId")
    private String spOrderId;

	public String getSpOrderId() {
		return spOrderId;
	}

	public void setSpOrderId(String spOrderId) {
		this.spOrderId = spOrderId;
	}
	
	
	
}
