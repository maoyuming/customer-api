package com.fangbaba.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fangbaba.api.domain.area.AreaInfo;
import com.fangbaba.api.domain.area.City;
import com.fangbaba.api.domain.area.District;
import com.fangbaba.api.domain.area.Province;
import com.fangbaba.gds.po.GdsChannelUrl;



public class GlobalCache {
    
	private static GlobalCache instance = new GlobalCache();

	
	public Map<String, String> oatCityMap = new ConcurrentHashMap<String, String>();
	
	/**
	 * 省份
	 */
	private Map<Integer,Province> provinceMap = new ConcurrentHashMap<Integer, Province>();

	/**
	 * 城市
	 */
	private Map<Integer,City> citysMap = new ConcurrentHashMap<Integer, City>();

	/**
	 * 区县
	 */
	private Map<Integer,District> districtsMap = new ConcurrentHashMap<Integer, District>();
	
	
	/**
	 * 省份子信息 key:省code，value:市code
	 */
	private Map<Integer, List<City>> provinceChildren = new ConcurrentHashMap<Integer, List<City>>();
	/**
	 * 城市子信息 key：市code，value：区县code
	 */
	private Map<Integer, List<District>> cityChildren = new ConcurrentHashMap<Integer, List<District>>();
	
	
	/**
	 * 省市父信息 key：城市code value：省份信息
	 */
	private Map<Integer, Province> cityParentMap = new ConcurrentHashMap<Integer, Province>(); 
	/**
	 * 区县父信息 key:区县code value：城市
	 */
	private Map<Integer, City> districtsParentMap = new ConcurrentHashMap<Integer, City>(); 
	
	/**
	 * 区域信息
	 */
	private AreaInfo areaInfo = new AreaInfo();
	
	/**
	 * 渠道url map
	 */
	public Map<Integer, List<GdsChannelUrl>> channelUrlMap = new ConcurrentHashMap<Integer, List<GdsChannelUrl>>();
	/**
	 * 渠道url map key:channelid_urlid
	 */ 
	public Map<String, GdsChannelUrl> gdsChannelUrlMap = new ConcurrentHashMap<String, GdsChannelUrl>();
	
	public Map<String, String> getOatCityMap() {
		return oatCityMap;
	}



	public void setOatCityMap(Map<String, String> oatCityMap) {
		this.oatCityMap = oatCityMap;
	}



	public static GlobalCache getInstance(){
		return instance;
	}



	public Map<Integer, Province> getProvinceMap() {
		return provinceMap;
	}



	public void setProvinceMap(Map<Integer, Province> provinceMap) {
		this.provinceMap = provinceMap;
	}



	public Map<Integer, City> getCitysMap() {
		return citysMap;
	}



	public void setCitysMap(Map<Integer, City> citysMap) {
		this.citysMap = citysMap;
	}



	public Map<Integer, District> getDistrictsMap() {
		return districtsMap;
	}



	public void setDistrictsMap(Map<Integer, District> districtsMap) {
		this.districtsMap = districtsMap;
	}



	public Map<Integer, List<City>> getProvinceChildren() {
		return provinceChildren;
	}



	public void setProvinceChildren(Map<Integer, List<City>> provinceChildren) {
		this.provinceChildren = provinceChildren;
	}



	public Map<Integer, List<District>> getCityChildren() {
		return cityChildren;
	}



	public void setCityChildren(Map<Integer, List<District>> cityChildren) {
		this.cityChildren = cityChildren;
	}



	public Map<Integer, Province> getCityParentMap() {
		return cityParentMap;
	}



	public void setCityParentMap(Map<Integer, Province> cityParentMap) {
		this.cityParentMap = cityParentMap;
	}



	public Map<Integer, City> getDistrictsParentMap() {
		return districtsParentMap;
	}



	public void setDistrictsParentMap(Map<Integer, City> districtsParentMap) {
		this.districtsParentMap = districtsParentMap;
	}



	public AreaInfo getAreaInfo() {
		return areaInfo;
	}



	public void setAreaInfo(AreaInfo areaInfo) {
		this.areaInfo = areaInfo;
	}



	public Map<Integer, List<GdsChannelUrl>> getChannelUrlMap() {
		return channelUrlMap;
	}



	public void setChannelUrlMap(Map<Integer, List<GdsChannelUrl>> channelUrlMap) {
		this.channelUrlMap = channelUrlMap;
	}



	public Map<String, GdsChannelUrl> getGdsChannelUrlMap() {
		return gdsChannelUrlMap;
	}



	public void setGdsChannelUrlMap(Map<String, GdsChannelUrl> gdsChannelUrlMap) {
		this.gdsChannelUrlMap = gdsChannelUrlMap;
	}






	
}
