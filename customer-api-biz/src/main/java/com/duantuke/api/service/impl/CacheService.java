package com.duantuke.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantuke.api.common.GlobalCache;
import com.duantuke.api.domain.area.AreaInfo;
import com.duantuke.api.domain.area.City;
import com.duantuke.api.domain.area.District;
import com.duantuke.api.domain.area.Province;
import com.duantuke.basic.face.service.CityService;
import com.duantuke.basic.face.service.DistrictService;
import com.duantuke.basic.face.service.ProvinceService;

@Service
public class CacheService {

	private static Logger logger = LoggerFactory.getLogger(CacheService.class);
	
	@Autowired
	private ProvinceService provinceService;
	@Autowired
	private CityService cityService;
	@Autowired
	private DistrictService districtService;


    @Autowired
    private Mapper dozerMapper;
	/**
	 * 初始化区域信息
	 */
    public void initArea(){
    	initArea2();
    }
    
    
    /**
     * 初始化区域信息
     */
	public void initArea2() {

		//查询区域start

		//查询城市
		List<com.duantuke.basic.po.Province> provinces = provinceService.queryAllProvinces();
		
		
		List<com.duantuke.basic.po.District> districts = districtService.queryAllDistricts();
		Map<Integer,List<District>> districtsMap =  new HashMap<Integer, List<District>>();
		Map<Integer,District> districtsCacheMap = GlobalCache.getInstance().getDistrictsMap();
		
		
		if(CollectionUtils.isNotEmpty(districts)){
			districtsCacheMap.clear();
			for (com.duantuke.basic.po.District districtModel : districts) {
				District district =  dozerMapper.map(districtModel, District.class);
				
				if(districtsMap.containsKey(district.getCityid())){
					districtsMap.get(district.getCityid()).add(district);
				}else{
					List<District> list =new ArrayList<District>();
					list.add(district);
					districtsMap.put(district.getCityid(), list);
				}
				
				districtsCacheMap.put(Integer.valueOf(district.getCode()), district);
				
			}
		}
		
		
		List<com.duantuke.basic.po.City> citys = cityService.queryAllCitys();
		Map<Integer,List<City>> citysMap = new HashMap<Integer, List<City>>();
		Map<Integer,City> citysCacheMap = GlobalCache.getInstance().getCitysMap();
		Map<Integer, City> districtsParentMap = GlobalCache.getInstance().getDistrictsParentMap();
		/**
		 * 省份子信息 key:省code，value:市code
		 */
		Map<Integer, List<District>> cityChildren = GlobalCache.getInstance().getCityChildren();



		
		if(CollectionUtils.isNotEmpty(citys)){
			citysCacheMap.clear();
			cityChildren.clear();
			districtsParentMap.clear();
			for (com.duantuke.basic.po.City cityModel : citys) {
				City city =  dozerMapper.map(cityModel, City.class);
				city.setDistrictList(districtsMap.get(Integer.valueOf(cityModel.getId()+"")));
				if(citysMap.containsKey(city.getProid())){
					citysMap.get(city.getProid()).add(city);
				}else{
					List<City> list =new ArrayList<City>();
					list.add(city);
					citysMap.put(city.getProid(), list);
				}
				
				citysCacheMap.put(Integer.valueOf(city.getCode()), city);
			
				
				if(CollectionUtils.isNotEmpty(city.getDistrictList())){
					cityChildren.put(Integer.valueOf(city.getCode()), city.getDistrictList());
					for (District district : city.getDistrictList()) {
						districtsParentMap.put(Integer.valueOf(district.getCode()), city);
					}
				}
			}
		}
		

		Map<Integer,List<Province>> provinceMap = new HashMap<Integer, List<Province>>();
		Map<Integer,Province> provinceCacheMap = GlobalCache.getInstance().getProvinceMap();
		/**
		 * 省份子信息 key:省code，value:市code
		 */
		Map<Integer, List<City>> provinceChildren = GlobalCache.getInstance().getProvinceChildren();
		Map<Integer, Province> cityParentMap = GlobalCache.getInstance().getCityParentMap();
		
		if(CollectionUtils.isNotEmpty(provinces)){
			provinceCacheMap.clear();
			provinceChildren.clear();
			cityParentMap.clear();
			logger.info("省份列表："+provinces.size());
			List<Province> provinceList = new ArrayList<Province>();
			for (com.duantuke.basic.po.Province provinceModel : provinces) {
				Province province =  dozerMapper.map(provinceModel, Province.class);
				province.setCityList(citysMap.get(Integer.valueOf(provinceModel.getId()+"")));
				provinceList.add(province);
				if(provinceMap.containsKey(province.getId())){
					provinceMap.get(province.getId()).add(province);
				}else{
					List<Province> list =new ArrayList<Province>();
					list.add(province);
					provinceMap.put(Integer.valueOf(province.getId()+""), list);
				}
				
				provinceCacheMap.put(Integer.valueOf(province.getCode()), province);
				
				
				if(CollectionUtils.isNotEmpty(province.getCityList())){
					provinceChildren.put(Integer.valueOf(province.getCode()), province.getCityList());
					for (City city : province.getCityList()) {
						cityParentMap.put(Integer.valueOf(city.getCode()), province);
					}
				}
				
				
			}
			AreaInfo areaInfo = GlobalCache.getInstance().getAreaInfo();
			areaInfo.setProvinceList(provinceList);
		}
	
		
	}

}
