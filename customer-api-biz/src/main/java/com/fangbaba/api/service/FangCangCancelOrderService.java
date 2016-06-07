package com.fangbaba.api.service;


public interface FangCangCancelOrderService {

	/**
	 * 取消订单
	 * @param SpOrderId
	 * @param CancelReason
	 */
	public String cancelOrder(String xml);
	/**
	 * 取消订单，TODO：test
	 * @param xml
	 * @param orderId
	 * @return
	 */
	public String cancelOrder(String xml,Long orderId);
}
