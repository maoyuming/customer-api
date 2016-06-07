package com.fangbaba.api.domain.fangcang.order;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author he
 * 同步订单状态xml bean
 */
@XmlRootElement
public class SyncOrderStatusRequest implements Serializable{
	private static final long serialVersionUID = -1419283162668655237L;
	@XStreamAlias("SpOrderId")
    private String spOrderId;
	@XStreamAlias("OrderStatus")
	private int orderStatus;
	public String getSpOrderId() {
		return spOrderId;
	}
	public void setSpOrderId(String spOrderId) {
		this.spOrderId = spOrderId;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
