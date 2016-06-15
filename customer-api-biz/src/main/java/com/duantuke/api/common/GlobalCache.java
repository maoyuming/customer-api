package com.duantuke.api.common;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.duantuke.api.domain.area.AreaInfo;
import com.duantuke.api.domain.area.City;
import com.duantuke.api.domain.area.District;
import com.duantuke.api.domain.area.Province;



public class GlobalCache {
    
	private static GlobalCache instance = new GlobalCache();

	
	public Map<String, String> oatCityMap = new ConcurrentHashMap<String, String>();
	
	/**
	 * 省份
	 */
	private Map<Long,Province> provinceMap = new ConcurrentHashMap<Long, Province>();

	/**
	 * 城市
	 */
	private Map<Long,City> citysMap = new ConcurrentHashMap<Long, City>();

	/**
	 * 区县
	 */
	private Map<Long,District> districtsMap = new ConcurrentHashMap<Long, District>();
	
	
	/**
	 * 省份子信息 key:省code，value:市code
	 */
	private Map<Long, List<City>> provinceChildren = new ConcurrentHashMap<Long, List<City>>();
	/**
	 * 城市子信息 key：市code，value：区县code
	 */
	private Map<Long, List<District>> cityChildren = new ConcurrentHashMap<Long, List<District>>();
	
	
	/**
	 * 省市父信息 key：城市code value：省份信息
	 */
	private Map<Long, Province> cityParentMap = new ConcurrentHashMap<Long, Province>(); 
	/**
	 * 区县父信息 key:区县code value：城市
	 */
	private Map<Long, City> districtsParentMap = new ConcurrentHashMap<Long, City>(); 
	
	/**
	 * 区域信息
	 */
	private AreaInfo areaInfo = new AreaInfo();
	
	public Map<String, String> getOatCityMap() {
		return oatCityMap;
	}



	public void setOatCityMap(Map<String, String> oatCityMap) {
		this.oatCityMap = oatCityMap;
	}



	public static GlobalCache getInstance(){
		return instance;
	}



	public Map<Long, Province> getProvinceMap() {
		return provinceMap;
	}



	public void setProvinceMap(Map<Long, Province> provinceMap) {
		this.provinceMap = provinceMap;
	}



	public Map<Long, City> getCitysMap() {
		return citysMap;
	}



	public void setCitysMap(Map<Long, City> citysMap) {
		this.citysMap = citysMap;
	}



	public Map<Long, District> getDistrictsMap() {
		return districtsMap;
	}



	public void setDistrictsMap(Map<Long, District> districtsMap) {
		this.districtsMap = districtsMap;
	}



	public Map<Long, List<City>> getProvinceChildren() {
		return provinceChildren;
	}



	public void setProvinceChildren(Map<Long, List<City>> provinceChildren) {
		this.provinceChildren = provinceChildren;
	}



	public Map<Long, List<District>> getCityChildren() {
		return cityChildren;
	}



	public void setCityChildren(Map<Long, List<District>> cityChildren) {
		this.cityChildren = cityChildren;
	}



	public Map<Long, Province> getCityParentMap() {
		return cityParentMap;
	}



	public void setCityParentMap(Map<Long, Province> cityParentMap) {
		this.cityParentMap = cityParentMap;
	}



	public Map<Long, City> getDistrictsParentMap() {
		return districtsParentMap;
	}



	public void setDistrictsParentMap(Map<Long, City> districtsParentMap) {
		this.districtsParentMap = districtsParentMap;
	}



	public AreaInfo getAreaInfo() {
		return areaInfo;
	}



	public void setAreaInfo(AreaInfo areaInfo) {
		this.areaInfo = areaInfo;
	}

	
}
