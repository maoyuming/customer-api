/**
 * 2016年3月28日下午8:11:56
 * zhaochuanbin
 */
package com.fangbaba.api.service;

import java.util.List;

import com.fangbaba.api.domain.open.hotel.Roomtype;

/**
 * @author zhaochuanbin
 *
 */
public interface PushRoomtypeService {
	public void pushRoomtype(String json);
	public List<Roomtype> queryRoomtypeByHotelId(Long hotelid);
}
