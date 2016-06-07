package com.fangbaba.api.service;

import com.fangbaba.api.face.service.IFangCangOrderService;

public interface FangCangOrderService extends IFangCangOrderService{
	
	/**
	 * @param xml
	 * 获取订单状态
	 */
	public String getOrderStatus(String xml);
	/**
	 * @param xml
	 * 试预订
	 */
	public String checkRoomAvail(String xml);

}
