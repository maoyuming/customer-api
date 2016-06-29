package com.duantuke.api.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.basic.face.bean.HotelInfo;
import com.duantuke.basic.face.bean.MealInfo;
import com.duantuke.basic.face.bean.RoomTypeInfo;
import com.duantuke.basic.face.bean.TeamSkuInfo;
import com.duantuke.basic.face.service.HotelService;
import com.duantuke.basic.face.service.MealService;
import com.duantuke.basic.face.service.PriceService;
import com.duantuke.basic.face.service.RoomTypeService;
import com.duantuke.basic.face.service.TeamSkuService;
import com.duantuke.basic.po.Hotel;
import com.duantuke.basic.po.Meal;
import com.duantuke.basic.po.RoomType;
import com.duantuke.basic.po.TeamSku;


@Service
public class HotelOpenService {
	

	private static Logger logger = LoggerFactory.getLogger(HotelOpenService.class);

	@Autowired
	private HotelService hotelService;
	@Autowired
	private RoomTypeService roomTypeService;
	@Autowired
	private MealService mealService;
	@Autowired
	private TeamSkuService teamSkuService;
	@Autowired
	private PriceService priceService;
    @Autowired
    private Mapper dozerMapper;
    
    /**
     * 酒店详情
     * @param hotelId
     * @param begintime
     * @param endtime
     * @return
     */
	public HotelInfo detail(Long hotelId,String begintime,String endtime) {
		logger.info("查看农家院详情入参，hotelId:{}",hotelId+", begintime:"+begintime+", endtime:"+endtime);
		HotelInfo hotelInfo =  null;
		try {
			
			Hotel hotel = hotelService.queryHotelById(hotelId);
			if(hotel==null){
				throw new OpenException(ErrorEnum.hotelUnExists);
			}
			
			hotelInfo = dozerMapper.map(hotel, HotelInfo.class);
	        hotelInfo.setRoomTypes(queryRoomtype(hotelId, begintime, endtime));
			hotelInfo.setTeamSkus(queryTeamSku(hotelId, begintime, endtime));
			hotelInfo.setMeals(queryMeal(hotelId));
			logger.info("返回值hotelInfo：{}",JSON.toJSONString(hotelInfo));
		} catch (Exception e) {
			logger.error("query detail error",e);
			throw e;
		}
		
		return hotelInfo;
	}
	
	/**
	 * 查询房型信息
	 * @param hotelId
	 * @param begintime
	 * @param endtime
	 * @return
	 */
	public List<RoomTypeInfo> queryRoomtype(Long hotelId,String begintime,String endtime) {
		List<RoomType> roomTypes = roomTypeService.queryRoomtypeByHotleId(hotelId);
		
		List<RoomTypeInfo> roomTypeInfos = new ArrayList<RoomTypeInfo>();
		if(CollectionUtils.isNotEmpty(roomTypes)){
			List<Long> roomtypeIds = new ArrayList<Long>();
			for(RoomType roomType:roomTypes){
				roomtypeIds.add(roomType.getSkuId());
				roomTypeInfos.add(dozerMapper.map(roomType, RoomTypeInfo.class));
			}
			Map<Long, Map<String, BigDecimal>> prices = priceService.queryHotelPrices(hotelId, begintime, endtime, roomtypeIds);
			
			//遍历房型价格map赋值给与其房型ID匹配的roomTypeInfo
//	        Set<Entry<Long, Map<String, BigDecimal>>> sets = prices.entrySet();  
//	        for(Entry<Long, Map<String, BigDecimal>> entry : sets) {
	        	for(RoomTypeInfo roomTypeInfo:roomTypeInfos){
//	        		if(entry.getKey()==(roomTypeInfo.getSkuId())){
	        			roomTypeInfo.setPrices(prices.get(roomTypeInfo.getSkuId()));
//	        		}
	        	}
//	        }
//	        hotelInfo.setRoomTypes(roomTypeInfos);
		}
		return roomTypeInfos;
	}
	/**
	 * 查询房型信息
	 * @param hotelId
	 * @param begintime
	 * @param endtime
	 * @return
	 */
	public List<TeamSkuInfo> queryTeamSku(Long hotelId,String begintime,String endtime) {
		List<TeamSku> roomTypes = teamSkuService.queryTeamSkuByHotleId(hotelId);
		
		List<TeamSkuInfo> roomTypeInfos = new ArrayList<TeamSkuInfo>();
		if(CollectionUtils.isNotEmpty(roomTypes)){
			List<Long> roomtypeIds = new ArrayList<Long>();
			for(TeamSku roomType:roomTypes){
				roomtypeIds.add(roomType.getSkuId());
				roomTypeInfos.add(dozerMapper.map(roomType, TeamSkuInfo.class));
			}
			Map<Long, Map<String, BigDecimal>> prices = priceService.queryHotelPrices(hotelId, begintime, endtime, roomtypeIds);
			
			//遍历房型价格map赋值给与其房型ID匹配的roomTypeInfo
//			Set<Entry<Long, Map<String, BigDecimal>>> sets = prices.entrySet();  
//			for(Entry<Long, Map<String, BigDecimal>> entry : sets) {
				for(TeamSkuInfo roomTypeInfo:roomTypeInfos){
//					if(entry.getKey()==(roomTypeInfo.getSkuId())){
						roomTypeInfo.setPrices(prices.get(roomTypeInfo.getSkuId()));
//					}
				}
//			}
//	        hotelInfo.setRoomTypes(roomTypeInfos);
		}
		return roomTypeInfos;
	}
	/**
	 * 查询餐饮信息
	 * @param hotelId
	 * @return
	 */
	public List<MealInfo> queryMeal(Long hotelId) {
		List<Meal> roomTypes = mealService.queryMealByHotleId(hotelId);
		
		List<MealInfo> roomTypeInfos = new ArrayList<MealInfo>();
		if(CollectionUtils.isNotEmpty(roomTypes)){
			for(Meal roomType:roomTypes){
				roomTypeInfos.add(dozerMapper.map(roomType, MealInfo.class));
			}
		}
		return roomTypeInfos;
	}
}
