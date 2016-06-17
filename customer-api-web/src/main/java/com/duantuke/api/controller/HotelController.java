package com.duantuke.api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.face.bean.HotelInfo;
import com.duantuke.basic.face.bean.RoomTypeInfo;
import com.duantuke.basic.face.service.HotelService;
import com.duantuke.basic.face.service.PriceService;
import com.duantuke.basic.face.service.RoomTypeService;
import com.duantuke.basic.po.Hotel;
import com.duantuke.basic.po.RoomType;
import com.duantuke.basic.po.Sight;


/**
 * 农家院
 * @author yuming.mao
 *
 */
@Controller
@RequestMapping(value = "/hotel")
public class HotelController {
	private static Logger logger = LoggerFactory.getLogger(HotelController.class);
	@Autowired
	private HotelService hotelService;
	@Autowired
	private RoomTypeService roomTypeService;
	@Autowired
	private PriceService priceService;
    @Autowired
    private Mapper dozerMapper;
	
    /**
     * 农家院详情
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<HotelInfo>> detail(Long hotelId,String begintime,String endtime) {
		logger.info("农家院详情，hotelId：{}",hotelId);
		
		Hotel hotel = hotelService.queryHotelById(hotelId);
		HotelInfo hotelInfo = dozerMapper.map(hotel, HotelInfo.class);
		
		List<RoomType> roomTypes = roomTypeService.queryRoomtypeByHotleId(hotelId);
		System.out.println(roomTypes.get(0).getSkuId());
		System.out.println(roomTypes.get(0).getName());


		List<RoomTypeInfo> roomTypeInfos = new ArrayList<RoomTypeInfo>();
		
		List<Long> roomtypeIds = new ArrayList<Long>();
		for(RoomType roomType:roomTypes){
			roomtypeIds.add(1L);
			roomTypeInfos.add(dozerMapper.map(roomType, RoomTypeInfo.class));
		}
		
		Map<Long, Map<String, BigDecimal>> prices = priceService.queryHotelPrices(hotelId, begintime, endtime, roomtypeIds);
		
		
		
		
        Set<Entry<Long, Map<String, BigDecimal>>> sets = prices.entrySet();  
        for(Entry<Long, Map<String, BigDecimal>> entry : sets) {
        	for(RoomTypeInfo roomTypeInfo:roomTypeInfos){
        		System.out.println(roomTypeInfo.getSkuId());
        		if(entry.getKey()==(roomTypeInfo.getSkuId())){
        			roomTypeInfo.setPrices(entry.getValue());
        		}
        	}
        }  
		
		
				

		

		hotelInfo.setRoomTypes(roomTypeInfos);
		
		OpenResponse<HotelInfo> openResponse = new OpenResponse<HotelInfo>();

		
		try {
			openResponse.setData(hotelInfo);;
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("SightController detail error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<HotelInfo>> (openResponse, HttpStatus.OK);
	}
	
}
