package com.fangbaba.api.service;

import java.util.List;
import java.util.Map;

import com.fangbaba.api.po.RoomtypeInfo;

public interface RoomtypeInfoService {
	public Map<Long, RoomtypeInfo> findRoomtypeinfoByRoomtypeIds(List<Long> roomtypelist);
	public Map<Long, RoomtypeInfo> queryByHotelid(Long hotelId) ;
}
