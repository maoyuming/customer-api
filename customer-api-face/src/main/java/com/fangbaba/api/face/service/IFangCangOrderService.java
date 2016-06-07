package com.fangbaba.api.face.service;

public interface IFangCangOrderService {
	/**
	 * @param spOrderId
	 * @param orderStatus
	 * 订单状态改变推送
	 */
	public void syncOrderStatus(String spOrderId,int orderStatus);
}
