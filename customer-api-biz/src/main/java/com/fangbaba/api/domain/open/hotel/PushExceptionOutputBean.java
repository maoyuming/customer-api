package com.fangbaba.api.domain.open.hotel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PushExceptionOutputBean implements Serializable {
	private static final long serialVersionUID = 5669839114908444185L;
	private List<PushExceptionOutputInfoBean> hotels = new ArrayList<PushExceptionOutputInfoBean>();
	private List<PushExceptionOutputInfoBean> orders = new ArrayList<PushExceptionOutputInfoBean>();
	public List<PushExceptionOutputInfoBean> getHotels() {
		return hotels;
	}
	public void setHotels(List<PushExceptionOutputInfoBean> hotels) {
		this.hotels = hotels;
	}
	public List<PushExceptionOutputInfoBean> getOrders() {
		return orders;
	}
	public void setOrders(List<PushExceptionOutputInfoBean> orders) {
		this.orders = orders;
	}


}
