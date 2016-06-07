package com.fangbaba.api.face.service;

import java.util.List;

import com.fangbaba.api.face.bean.RoomTypeMappingBean;
import com.fangbaba.gds.po.RoomtypeMapping;

public interface IRoomtypeMappingService {
	
	/**
	 * 批量添加酒店
	 * @param json
	 * @return
	 */
	public boolean doAddRoomtypeMapping(List<RoomtypeMapping> roomtypeMappings);
	/**
	 * 批量删除酒店
	 * @param json
	 * @return
	 */
	public boolean doDeleteRoomtypeMapping(List<RoomtypeMapping> roomtypeMappings);
	/**
	 * 批量修改酒店
	 * @param json
	 * @return
	 */
	public boolean doUpdateRoomtypeMapping(List<RoomtypeMapping> roomtypeMappings);
	/**
	 * 批量添加酒店
	 * @param json
	 * @return
	 */
	public boolean doAddRoomtypeMapping(List<RoomtypeMapping> roomtypeMappings,Integer channelId);
	/**
	 * 批量删除酒店
	 * @param json
	 * @return
	 */
	public boolean doDeleteRoomtypeMapping(List<RoomtypeMapping> roomtypeMappings,Integer channelId);

	/**
	 * 删除房型映射接口
	 * @param hotelmapping
	 * @return
	 */
	public  boolean deleteRoomTypeMapping(RoomTypeMappingBean roomtypemapping);
	
	/**
	 * 批量删除房型映射接口
	 * @param roomTypeMappingBean
	 * @return
	 */
	public  boolean deleteRoomTypeMappingBatch(List<RoomTypeMappingBean> roomTypeMappingBean);
	
	
}
