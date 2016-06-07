/**
 * 2016年3月25日下午2:13:21
 * zhaochuanbin
 */
package com.fangbaba.api.domain.open.hotel;

import java.util.List;

/**
 * @author zhaochuanbin
 *
 */
public class HotelDetail extends HotelListBean{
    
    
	private List<Roomtype> roomtypes;

	public List<Roomtype> getRoomtypes() {
		return roomtypes;
	}

	public void setRoomtypes(List<Roomtype> roomtypes) {
		this.roomtypes = roomtypes;
	}
	
	
}
