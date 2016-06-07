package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.po.RoomtypeInfo;
import com.fangbaba.api.service.RoomtypeInfoService;
import com.fangbaba.basic.face.bean.RoomtypeModel;
import com.fangbaba.basic.face.bean.RoomtypeinfoModel;
import com.fangbaba.basic.face.service.RoomtypeService;
import com.fangbaba.basic.face.service.RoomtypeinfoService;

@Service
public class RoomtypeInfoServiceImpl implements RoomtypeInfoService{
	
	
	@Autowired
	private RoomtypeService roomtypeService;

    @Autowired
    private RoomtypeinfoService roomtypeinfoService;
	

    @Autowired
    private Mapper dozerMapper;
    @Override
    public Map<Long, RoomtypeInfo> findRoomtypeinfoByRoomtypeIds(List<Long> roomtypelist) {

        Map<Long, RoomtypeInfo> map = new HashMap<Long, RoomtypeInfo>();
        
        List<RoomtypeinfoModel> infos =  roomtypeinfoService.findRoomtypeinfoByRoomtypeIds(roomtypelist);
        if(CollectionUtils.isNotEmpty(infos)){
            for(RoomtypeinfoModel roomtypeInfo : infos){
            	RoomtypeInfo info = dozerMapper.map(roomtypeInfo, RoomtypeInfo.class);
                map.put(roomtypeInfo.getRoomtypeid(), info);
            }
        }
        
        return map;
    }
    @Override
    public Map<Long, RoomtypeInfo> queryByHotelid(Long hotelId) {
    	
    	 List<RoomtypeModel> listRoom = roomtypeService.queryByHotelId(hotelId);
 		
    	//将所有房型存放在list中
 		List<Long> roomtypelist = new ArrayList<Long>();
 		for (RoomtypeModel rm : listRoom) {
 			roomtypelist.add(rm.getId());
 		}
    	
    	return this.findRoomtypeinfoByRoomtypeIds(roomtypelist);
    }
}
