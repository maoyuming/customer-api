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

import com.alibaba.fastjson.JSONObject;
import com.fangbaba.api.kafka.push.HotelConsumer;
import com.google.common.base.Strings;


@Controller
@RequestMapping(value = "/push/hotel/")
public class PushIncrementHotelController {
	private static Logger logger = LoggerFactory.getLogger(PushIncrementHotelController.class);

   @Autowired
   private HotelConsumer hotelConsumer;
	
	@RequestMapping(value = "/increment")
    public ResponseEntity<String> allpush(HttpServletRequest request,@RequestBody  String body) {
        logger.info("增量推送酒店开始:{}",body);
        Integer channelid = Integer.valueOf(request.getHeader(com.fangbaba.api.common.Constants.CHANNEL_ID));
        logger.info("渠道id:{}",channelid);
        JSONObject jsonObject = JSONObject.parseObject(body);
        String flag =jsonObject.getString("flag");
        String hotelid =jsonObject.getString("hotelid");
        String message = "";
        logger.info("酒店推送flag:{}",flag);
        if (Strings.isNullOrEmpty(flag)) {
        	message = "酒店推送标示不能为空";
		}
        if (Strings.isNullOrEmpty(hotelid)) {
        	message = "酒店id不能为空";
		}
        if ("basic_synchotelmodel".equals(flag)) {
			 hotelConsumer.hotelPush(body);
		}else if ("hotel_distribution_add".equals(flag)) {
			 jsonObject.put("channelid", channelid);
			 hotelConsumer.hotelDistributionPush(jsonObject.toJSONString());
		}else if ("hotel_distribution_delete".equals(flag)) {
			 jsonObject.put("channelid", channelid);
			 hotelConsumer.hotelDistributionDeletePush(jsonObject.toJSONString());
		}else {
			message = "酒店推送标示错误";
		}
			
        message="酒店推送完毕";
        logger.info("增量推送酒店完毕");
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
	
}
