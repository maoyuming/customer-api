package com.fangbaba.api.controller.open;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.domain.open.hotel.Roomtype;
import com.fangbaba.api.domain.open.hotel.RoomtypeRequest;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.service.PushRoomtypeService;
import com.fangbaba.basic.face.bean.RoomtypeModel;
import com.fangbaba.basic.face.service.RoomtypeService;
import com.google.gson.Gson;


@Controller
@RequestMapping(value = "/open/hotel/roomtype")
public class RoomTypeController {
	private static Logger logger = LoggerFactory.getLogger(RoomTypeController.class);
	@Autowired
	private RoomtypeService roomtypeService;
	@Autowired
	private PushRoomtypeService pushRoomtypeService;
	

    @Autowired
    private Mapper dozerMapper;
	/**
	 * 查询房型信息
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/query")
    public ResponseEntity<OpenResponse<List<Roomtype>>> query(@RequestBody  String body) {
		logger.info("查询房型，{}",body);
		
//		@SuppressWarnings("unchecked")
		RoomtypeRequest openRequest = new Gson().fromJson(body, RoomtypeRequest.class);
//		OpenRequest<RoomtypeRequest>  openRequest = new Gson().fromJson(body,new TypeToken<OpenRequest<RoomtypeRequest>>() {
//		}.getType());
		
		if (openRequest==null) {
			logger.info("酒店id为空");
			throw new OpenException(DistributionErrorEnum.hotelNull.getName(),DistributionErrorEnum.hotelNull.getId());
		}
//		if (openRequest.getData()==null) {
//			logger.info("酒店id为空");
//			throw new OpenException(DistributionErrorEnum.hotelNull.getName(),DistributionErrorEnum.hotelNull.getId());
//		}
		if (openRequest.getHotelid()==null) {
			logger.info("酒店id为空");
			throw new OpenException(DistributionErrorEnum.hotelNull.getName(),DistributionErrorEnum.hotelNull.getId());
		}
		
		Long hotelid = openRequest.getHotelid();
		
		List<Roomtype> roomtypes = pushRoomtypeService.queryRoomtypeByHotelId(hotelid);
		OpenResponse<List<Roomtype>> openResponse = new OpenResponse<List<Roomtype>>();

//		List<Roomtype> roomtypes = new ArrayList<Roomtype>();
//		if(CollectionUtils.isNotEmpty(list)){
//			for (RoomtypeModel roomtypeModel : list) {
//				Roomtype roomtype =  dozerMapper.map(roomtypeModel, Roomtype.class);
//				roomtypes.add(roomtype);
//			}
//		}
		
		openResponse.setResult(true+"");
		openResponse.setData(roomtypes);
		
		
		return new ResponseEntity<OpenResponse<List<Roomtype>>>(openResponse, HttpStatus.OK);
	}
}
