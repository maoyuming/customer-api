package com.fangbaba.api.service;

import java.util.List;

import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingRoomtypeRequest;
import com.fangbaba.api.face.service.IRoomtypeMappingService;

public interface RoomtypeMappingService extends IRoomtypeMappingService{


	/**
	 * 新增房型映射接口
	 * @param hotelmapping
	 * @return
	 */
	public  boolean addRoomTypeMapping(HotelMappingRoomtypeRequest RoomtypeRequest);
	
	

	/**
	 * 批量新增房型
	 * @param hotelMappingRoomtypeRequests
	 * @return
	 */
	public boolean addRoomTypeMappingBatch(List<HotelMappingRoomtypeRequest> hotelMappingRoomtypeRequests);
}
