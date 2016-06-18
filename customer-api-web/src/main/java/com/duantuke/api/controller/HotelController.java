package com.duantuke.api.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.face.bean.HotelInfo;
import com.duantuke.basic.face.bean.RoomTypeInfo;
import com.duantuke.basic.face.service.HotelService;
import com.duantuke.basic.face.service.PriceService;
import com.duantuke.basic.face.service.RoomTypeService;
import com.duantuke.basic.po.Hotel;
import com.duantuke.basic.po.RoomType;
import com.google.gson.Gson;


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
     * @param hotelId
     * @param begintime
     * @param endtime
     * @return
     */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<HotelInfo>> detail(Long hotelId,String begintime,String endtime) {
		logger.info("查看农家院详情入参，hotelId:{}",hotelId+", begintime:"+begintime+", endtime:"+endtime);
		OpenResponse<HotelInfo> openResponse = new OpenResponse<HotelInfo>();
		
		try {
			if(hotelId == null){
				openResponse.setErrorMessage("参数hotelId为空");
				openResponse.setResult(Constants.FAIL);
				logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
				return new ResponseEntity<OpenResponse<HotelInfo>> (openResponse, HttpStatus.OK);
			}
			
			Hotel hotel = hotelService.queryHotelById(hotelId);
			if(hotel==null){
				openResponse.setErrorMessage("没有hotelId等于"+hotelId+"的酒店");
				openResponse.setResult(Constants.FAIL);
				logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
				return new ResponseEntity<OpenResponse<HotelInfo>> (openResponse, HttpStatus.OK);
			}
			
			HotelInfo hotelInfo = dozerMapper.map(hotel, HotelInfo.class);
			List<RoomType> roomTypes = roomTypeService.queryRoomtypeByHotleId(hotelId);
			
			if(CollectionUtils.isNotEmpty(roomTypes)){
				List<RoomTypeInfo> roomTypeInfos = new ArrayList<RoomTypeInfo>();
				List<Long> roomtypeIds = new ArrayList<Long>();
				for(RoomType roomType:roomTypes){
					roomtypeIds.add(roomType.getSkuId());
					roomTypeInfos.add(dozerMapper.map(roomType, RoomTypeInfo.class));
				}
				Map<Long, Map<String, BigDecimal>> prices = priceService.queryHotelPrices(hotelId, begintime, endtime, roomtypeIds);
				
				//遍历房型价格map赋值给与其房型ID匹配的roomTypeInfo
		        Set<Entry<Long, Map<String, BigDecimal>>> sets = prices.entrySet();  
		        for(Entry<Long, Map<String, BigDecimal>> entry : sets) {
		        	for(RoomTypeInfo roomTypeInfo:roomTypeInfos){
		        		if(entry.getKey()==(roomTypeInfo.getSkuId())){
		        			roomTypeInfo.setPrices(entry.getValue());
		        		}
		        	}
		        }
		        hotelInfo.setRoomTypes(roomTypeInfos);
			}
			
			openResponse.setData(hotelInfo);;
			openResponse.setResult(Constants.SUCCESS);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("HotelController detail error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<HotelInfo>> (openResponse, HttpStatus.OK);
	}
	
}
