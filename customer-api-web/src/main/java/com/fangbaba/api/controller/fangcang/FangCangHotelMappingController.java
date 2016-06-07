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

import com.fangbaba.api.face.service.IHotelMappingService;
import com.fangbaba.api.kafka.fangcang.HotelMappingSyncProducer;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.po.HotelMapping;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


/**
 * 房仓酒店映射测试类，用于测试和维护使用
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/fangcang/hotelmapping")
public class FangCangHotelMappingController {

	private static Logger logger = LoggerFactory.getLogger(FangCangHotelMappingController.class);
    @Autowired
    private IHotelMappingService iHotelMappingService;
    @Autowired
    private HotelMappingSyncProducer hotelMappingSyncProducer;

    
    /**
	 * 新增酒店映射-直接推送给房仓
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addHotelMappingfc")
    public ResponseEntity<String> addHotelMapping(HttpServletRequest request,HotelMapping hotelMappingInfo) {
		List<HotelMapping> hotelmapping = new ArrayList<HotelMapping>();
		hotelmapping.add(hotelMappingInfo);
		boolean flag = iHotelMappingService.addHotelMapping(hotelmapping);
		
		return new ResponseEntity<String>(flag+"", HttpStatus.OK);
	}
	/**
	 * 新增酒店映射-保存本地数据库，并且推送给房仓
	 * 不发送mq消息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addHotelMappingBatch")
	public ResponseEntity<String> addHotelMappingBatch(HttpServletRequest request,String json,Integer channelId) {

    	logger.info("批量add酒店映射：{}",json);
    	List<HotelMapping> hotelMappings = new Gson().fromJson(json,new TypeToken<List<HotelMapping>>() {}.getType());
    	boolean flag = iHotelMappingService.doAddHotelMapping(hotelMappings,channelId);
    
		return new ResponseEntity<String>(flag+"", HttpStatus.OK);
	}
	/**
	 * 删除酒店映射-保存本地数据库，并且推送给房仓
	 * 不发送mq消息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteHotelMappingBatch")
	public ResponseEntity<String> deleteHotelMappingBatch(HttpServletRequest request,String json,Integer channelId) {
		
		logger.info("批量add酒店映射：{}",json);
		List<HotelMapping> hotelMappings = new Gson().fromJson(json,new TypeToken<List<HotelMapping>>() {}.getType());
		boolean flag = iHotelMappingService.doDeleteHotelMapping(hotelMappings,channelId);
		
		return new ResponseEntity<String>(flag+"", HttpStatus.OK);
	}
	/**
	 * 新增酒店映射-直接推送给房仓
	 * json=[{"hotelid":211113211,"otaHotelid":211113211,"hotelname":"酒店名称","hoteladdr":"2000","channelid":2,"hotelphone":"123213213"}]
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteHotelMappingfc")
    public ResponseEntity<String> deleteHotelMapping(HttpServletRequest request,HotelMapping hotelMapping) {
		List<HotelMapping> hotelmapping = new ArrayList<>();
		hotelmapping.add(hotelMapping);
		boolean flag = iHotelMappingService.deleteHotelMapping(hotelmapping);
		
		return new ResponseEntity<String>(flag+"", HttpStatus.OK);
	}
	
	/**
	 *  发送新增酒店映射mq
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendAddHotelMapping")
	public ResponseEntity<String> sendAddHotelMapping(HttpServletRequest request,String json) {
		hotelMappingSyncProducer.addHotelMappingBatch(json);
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	
	/**
	 * 发送删除酒店映射mq
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendDeleteHotelMapping")
	public ResponseEntity<String> sendDeleteHotelMapping(HttpServletRequest request,String json) {
		hotelMappingSyncProducer.deleteHotelMappingBatch(json);
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	
	/**
	 * 发送更新酒店映射mq
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendUpdateHotelMapping")
	public ResponseEntity<String> sendUpdateHotelMapping(HttpServletRequest request,String json) {
		hotelMappingSyncProducer.updateHotelMappingBatch(json);
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	
	/**
	 * 同步酒店mapping信息
	 * 发送mq信息
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/sendSyncHotelMapping")
	public ResponseEntity<String> sendSyncHotelMapping(HttpServletRequest request,String json) {
		hotelMappingSyncProducer.syncHotelMappingBatch(json);
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	
	
	
	
	
	public static void main(String[] args) {

		List<HotelMapping> hotelmapping = new ArrayList<HotelMapping>();
		HotelMapping hotelMappingInfo = new HotelMapping();
		hotelMappingInfo.setId(111111L);
		hotelMappingInfo.setHotelname("酒店名称");
		hotelMappingInfo.setHoteladdr("2000");
		hotelMappingInfo.setHotelphone("123213213");
		hotelMappingInfo.setChannelid(ChannelEnum.QUNAR.getId());
		
		hotelmapping.add(hotelMappingInfo);
		
		System.out.println(new Gson().toJson(hotelmapping));
	}
}
