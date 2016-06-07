/**
 * 2016年3月28日下午8:13:31
 * zhaochuanbin
 */
package com.fangbaba.api.service.impl;

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

import com.alibaba.fastjson.JSONObject;
import com.fangbaba.api.domain.open.hotel.Roomtype;
import com.fangbaba.api.enums.BedTypeEnum;
import com.fangbaba.api.enums.MiKeStatusEnum;
import com.fangbaba.api.enums.open.BreakfastEnum;
import com.fangbaba.api.enums.open.PrepayEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.po.RoomtypeInfo;
import com.fangbaba.api.service.PushRoomtypeService;
import com.fangbaba.api.service.RoomtypeInfoService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.basic.face.bean.RoomtypeModel;
import com.fangbaba.basic.face.service.RoomtypeService;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.service.IHotelChannelSettingService;
import com.fangbaba.gds.po.HotelChannelSetting;
import com.google.gson.Gson;

/**
 * @author zhaochuanbin
 *
 */
@Service
public class PushRoomtypeServiceImpl implements PushRoomtypeService {

    private static Logger logger = LoggerFactory.getLogger(PushRoomtypeServiceImpl.class);
    @Autowired
    private RoomtypeService roomtypeService;
    @Autowired
    private Mapper dozerMapper;
    @Autowired
    private RoomtypeInfoService roomtypeinfoService;
    @Autowired
    private BusinessUtil businessUtil;

    @Autowired
    private IHotelChannelSettingService hotelChannelSettingService;

	//同步房型操作表示
	public static String ACTION_ADD = "add";
	public static String ACTION_MODIFY = "modify";
	public static String ACTION_DELETE = "delete";
	
	
	public void pushRoomtype(String json){
		logger.info("HotelEsConsumer consumeRoomtypeChangeMessage:" + json);
		JSONObject jsonObject = JSONObject.parseObject(json);
		if(jsonObject!=null){
			//解析json串
			Long hotelid = jsonObject.getLong("hotelid");
			Long roomtypeid = jsonObject.getLong("roomtypeid");
			String act = jsonObject.getString("act");
    		List<HotelChannelSetting> hotelChannelSettings = hotelChannelSettingService.queryHotelChannelSettingByHotelid(hotelid);
    		if(CollectionUtils.isNotEmpty(hotelChannelSettings)){
    			for (HotelChannelSetting hotelChannelSetting : hotelChannelSettings) {
    				this.pushRoomtype(hotelid, hotelChannelSetting.getChannelid(), roomtypeid, act);
				}
    		}
		}
	}
    

    
    /**
     * 推送房型信息
     */
    public void pushRoomtype(Long hotelId,Integer channelId,Long roomtypeid,String action) {
    	
    	if(action==null || ACTION_ADD.equals(action) || ACTION_MODIFY.equals(action)){
    		pushSaveOrUpdateRoomtype(hotelId, channelId,roomtypeid);
		}else if(ACTION_DELETE.equals(action)){
			//delete hotel
			pushDeleteRoomtype(hotelId, channelId,roomtypeid);
		}
        
    }
    
    /**
     * 推送新增和修改房型
     * @param hotelId
     * @param channelId
     * @param action
     */
    private void pushSaveOrUpdateRoomtype(Long hotelId,Integer channelId,Long roomtypeid){

		logger.info("房型id:{}",roomtypeid);
	    
	    List<Roomtype> roomtypes = this.queryRoomtypeByHotelId(hotelId);

	    List<Roomtype> roomtypelist = new ArrayList<Roomtype>();
	    
	    if(roomtypeid!=null){
	    	if(CollectionUtils.isNotEmpty(roomtypes)){
	    		for (Roomtype roomtype2 : roomtypes) {
	    			if(roomtype2.getId().intValue() == roomtypeid){
	    				roomtypelist.add(roomtype2);
	    			}
	    		}
	    	}
	    }else{
	    	roomtypelist = roomtypes;
	    }
		
		String json  = new Gson().toJson(roomtypelist);
		
		businessUtil.push(GdsChannelUrlEnum.pushRoomtype.getId(), json,channelId,hotelId.toString(),null);
	
    }
    
    /**
     * 根据房型id查询房型信息，包括房型基本信息床型、图片等
     * @param hotelid
     * @return
     */
    public List<Roomtype> queryRoomtypeByHotelId(Long hotelid){
    	List<RoomtypeModel> listRoom = roomtypeService.queryByHotelId(hotelid);
 		
    	//将所有房型存放在list中
 		List<Long> roomtypelist = new ArrayList<Long>();
 		for (RoomtypeModel rm : listRoom) {
 			roomtypelist.add(rm.getId());
 		}
         Map<Long,RoomtypeInfo> map = roomtypeinfoService.findRoomtypeinfoByRoomtypeIds(roomtypelist);
         
         List<Roomtype> roomtypes = new ArrayList<Roomtype>();
         if(CollectionUtils.isNotEmpty(listRoom)){
             for (RoomtypeModel roomtypeModel : listRoom) {
                 Roomtype roomtype =  dozerMapper.map(roomtypeModel, Roomtype.class);
                 if (map.get(roomtype.getId())!=null) {
                     RoomtypeInfo roomtypeInfo = map.get(roomtype.getId());
                     roomtype.setRoomtypeid(roomtype.getId());
                     roomtype.setHotelid(hotelid);
                     roomtype.setBedsize(roomtypeInfo.getBedsize());
                     roomtype.setRoomtypepics(roomtypeInfo.getPics());
                     roomtype.setArea(roomtypeInfo.getMaxarea()+"");
                     roomtype.setBedtype(roomtypeInfo.getBedtype()+"");
                     roomtype.setPrepay(PrepayEnum.yufu.getId().intValue()+"");
                     roomtype.setBreakfast(BreakfastEnum.no.getId()+"");
                     roomtype.setStatus(MiKeStatusEnum.NO.getId()+"");
                 }
                 roomtypes.add(roomtype);
             }
         }
         return roomtypes;
    }
    
    
    
    

    /**
     * 推送删除酒店
     * @param hotelId
     * @param channelId
     * @param action
     */
    public void pushDeleteRoomtype(Long hotelId,Integer channelId,Long roomtypeid){

		Map<String,Object> hotelMap = new HashMap<String,Object>();
		hotelMap.put("hotelid", hotelId);
		hotelMap.put("roomtypeid", roomtypeid);
		String json = new Gson().toJson(hotelMap);
		/*
		 * 格式
		{ 
			"hotelid":9999,
			"roomtypeid":"444,5555,333"
		}
		 */
    	businessUtil.push(GdsChannelUrlEnum.pushDeleteRoomtype.getId(), json,channelId,hotelId+"",null);
    }
    
    
}
