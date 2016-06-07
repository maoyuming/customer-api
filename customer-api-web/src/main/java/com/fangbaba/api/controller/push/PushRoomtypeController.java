package com.fangbaba.api.controller.push;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.service.PushRoomtypeService;
import com.fangbaba.gds.face.service.IHotelChannelSettingService;


/**
 * 只用于测试，不做正式使用
 * @author tankai
 *
 */
@Controller
@RequestMapping(value = "/push/roomtype")
public class PushRoomtypeController {
	private static Logger logger = LoggerFactory.getLogger(PushRoomtypeController.class);
	@Autowired
	private PushRoomtypeService pushRoomtypeService;
    @Autowired
    private IHotelChannelSettingService hotelChannelSettingService;
    
	/**
	 * 推送房型变化，测试使用
	 * act字段：传入add modify delete
	 * @return
	 */
	@RequestMapping(value = "/push")
    public ResponseEntity<Boolean> push(HttpServletRequest request,@RequestBody  String body) {
		logger.info("测试推送房型变化,json={}", body);
		pushRoomtypeService.pushRoomtype(body);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
}
