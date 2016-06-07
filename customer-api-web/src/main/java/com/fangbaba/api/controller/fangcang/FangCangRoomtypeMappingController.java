package com.fangbaba.api.controller.fangcang;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingRoomtypeRequest;
import com.fangbaba.api.face.bean.RoomTypeMappingBean;
import com.fangbaba.api.face.bean.SpRoomtype;
import com.fangbaba.api.kafka.fangcang.RoomtypeMappingSyncProducer;
import com.fangbaba.api.service.RoomtypeMappingService;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.po.RoomtypeMapping;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


/**
 * 房仓房型映射测试类，用于测试和维护使用
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/fangcang/roomtypemapping")
public class FangCangRoomtypeMappingController {

	private static Logger logger = LoggerFactory.getLogger(FangCangRoomtypeMappingController.class);

    @Autowired 
    private RoomtypeMappingService iRoomtypeMappingService;
    
    @Autowired
    private RoomtypeMappingSyncProducer roomtypeMappingSyncProducer;

	/**
	 * 新增房型映射-推送给房仓
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addRoomTypeMappingfc")
    public ResponseEntity<String> addRoomTypeMapping(HttpServletRequest request,HotelMappingRoomtypeRequest RoomtypeRequest) {
		boolean flag = iRoomtypeMappingService.addRoomTypeMapping(RoomtypeRequest);
		
		return new ResponseEntity<String>(flag+"", HttpStatus.OK);
	}
	
	/**
	 * 删除房型映射-推送给房仓
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteRoomTypeMappingfc")
    public ResponseEntity<String> deleteRoomTypeMapping(HttpServletRequest request,RoomTypeMappingBean roomtypemapping){
//    		List<String> rooms) {
		
//		List<SpRoomtype> roomTypeList = new ArrayList<SpRoomtype>();
//		for (String string : rooms) {
//			SpRoomtype roomtype = new SpRoomtype();
//			roomtype.setSpRoomTypeId(string);
//			roomTypeList.add(roomtype);
//		}
//		roomtypemapping.setRoomTypeList(roomTypeList);
		
		boolean flag = iRoomtypeMappingService.deleteRoomTypeMapping(roomtypemapping);
		
		return new ResponseEntity<String>(flag +"", HttpStatus.OK);
	}
	/**
	 * 新增房型映射-本地保存，推送给房仓
	 * 不发送消息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addRoomTypeMappingBatch")
	public ResponseEntity<String> addRoomTypeMappingBatch(HttpServletRequest request,String json) {

    	logger.info("批量add房型映射：{}",json);
    	List<RoomtypeMapping> roomtypeMappings = new Gson().fromJson(json,new TypeToken<List<RoomtypeMapping>>() {}.getType());
    	boolean flag = iRoomtypeMappingService.doAddRoomtypeMapping(roomtypeMappings);
    
		return new ResponseEntity<String>(flag+"", HttpStatus.OK);
	}
	
	/**
	 * 删除房型映射-本地保存，推送给房仓
	 * [{"hotelid":111111,"otaHotelid":"123213213","roomtypeid":1111,"roomtypename":"2000","otaRoomtypeid":"123213213","otaRoomtypename":"otatatat","channelid":2}]
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteRoomTypeMappingBatch")
	public ResponseEntity<String> deleteRoomTypeMappingBatch(HttpServletRequest request,String json) {

    	logger.info("批量delete房型映射：{}",json);
    	List<RoomtypeMapping> roomtypeMappings = new Gson().fromJson(json,new TypeToken<List<RoomtypeMapping>>() {}.getType());
    	boolean flag = iRoomtypeMappingService.doDeleteRoomtypeMapping(roomtypeMappings);
    
		return new ResponseEntity<String>(flag+"", HttpStatus.OK);
	}
	
	
	/**
	 * 发送新增房型映射mq消息
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendAddRoomtypeMapping")
	public ResponseEntity<String> sendAddRoomtypeMapping(HttpServletRequest request,String json) {
		roomtypeMappingSyncProducer.sendAddRoomtypeMapping(json);
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	
	/**
	 * 发送删除房型映射mq消息
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendDeleteRoomtypeMapping")
	public ResponseEntity<String> sendDeleteRoomtypeMapping(HttpServletRequest request,String json) {
		roomtypeMappingSyncProducer.sendDeleteRoomtypeMapping(json);
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	/**
	 * 发送更新房型映射mq消息
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendUpdateRoomtypeMapping")
	public ResponseEntity<String> sendUpdateRoomtypeMapping(HttpServletRequest request,String json) {
		roomtypeMappingSyncProducer.sendAddRoomtypeMapping(json);
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	
	/**
	 * 发送同步房型映射mq消息-系统主要使用这个topick
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendSyncRoomtypeMapping")
	public ResponseEntity<String> sendSyncRoomtypeMapping(HttpServletRequest request,String json) {
		roomtypeMappingSyncProducer.sendSyncRoomtypeMapping(json);
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	
	public static void main(String[] args) {

		List<RoomtypeMapping> hotelmapping = new ArrayList<RoomtypeMapping>();
		RoomtypeMapping hotelMappingInfo = new RoomtypeMapping();
		hotelMappingInfo.setHotelid(111111L);
		hotelMappingInfo.setRoomtypeid(1111L);
		hotelMappingInfo.setRoomtypename("2000");
		hotelMappingInfo.setOtaHotelid("123213213");
		hotelMappingInfo.setOtaRoomtypeid("123213213");
		hotelMappingInfo.setOtaRoomtypename("otatatat");
		hotelMappingInfo.setChannelid(ChannelEnum.QUNAR.getId());
		
		hotelmapping.add(hotelMappingInfo);
		
		System.out.println(new Gson().toJson(hotelmapping));
	}
}
