package com.fangbaba.api.kafka.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fangbaba.api.service.PushRoomtypeService;
import com.fangbaba.gds.face.service.IHotelChannelSettingService;
import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicConsumer;

/**
 * 推送变化的酒店
 * zhangyajun
 * description:
 */
@MkMessageService
public class RoomtypeConsumer {

    private Logger logger = LoggerFactory.getLogger(RoomtypeConsumer.class);
    @Autowired
    private PushRoomtypeService pushRoomtypeService;
    @Autowired
    private IHotelChannelSettingService hotelChannelSettingService;
    /**
     * 
     * 房型同步
     * 包括增删改
	 * @param json
	 */
	@MkTopicConsumer(topic = "Basic_RoomtypeChange", group = "Api_Basic_RoomtypeChange", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
	public void roomtypePush(String json){
		pushRoomtypeService.pushRoomtype(json);
	}
    
}
