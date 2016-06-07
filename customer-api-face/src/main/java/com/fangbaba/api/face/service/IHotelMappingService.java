package com.fangbaba.api.face.service;

import java.util.List;

import com.fangbaba.gds.po.HotelMapping;

public interface IHotelMappingService {
	
	/**
	 * 批量添加酒店
	 * @param json
	 * @return
	 */
	public boolean doAddHotelMapping(List<HotelMapping> hotelMappings,Integer channelId);
	/**
	 * 批量删除酒店
	 * @param json
	 * @return
	 */
	public boolean doDeleteHotelMapping(List<HotelMapping> hotelMappings,Integer channelId);
	
	

	
	/**
	 * 批量添加酒店
	 * @param json
	 * @return
	 */
	public boolean doAddHotelMapping(List<HotelMapping> hotelMappings);
	/**
	 * 批量删除酒店
	 * @param json
	 * @return
	 */
	public boolean doDeleteHotelMapping(List<HotelMapping> hotelMappings);
	/**
	 * 批量更新酒店
	 * @param json
	 * @return
	 */
	public boolean doUpdateHotelMapping(List<HotelMapping> hotelMappings);


	/**
	 * 添加酒店映射
	 * @param hotelmapping
	 * @return
	 */
	public  boolean addHotelMapping(List<HotelMapping> hotelmapping);
	/**
	 * 删除酒店映射
	 * @param hotelmapping
	 * @return
	 */
	public  boolean deleteHotelMapping(List<HotelMapping> hotelmapping);
	
}
