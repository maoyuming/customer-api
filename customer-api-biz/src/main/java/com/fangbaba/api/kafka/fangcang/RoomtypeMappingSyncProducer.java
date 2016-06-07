package com.fangbaba.api.kafka.fangcang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicProducer;


/**
 * 发送消息
 * @author tankai
 *
 */
@MkMessageService
public class RoomtypeMappingSyncProducer {

	private static final Logger logger = LoggerFactory.getLogger(RoomtypeMappingSyncProducer.class);

	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "Add_Roomtype_Mapping", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 12)
	public void sendAddRoomtypeMapping(String json) {
		logger.info("发送消息成功，"+json.toString());
    }
	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "Delete_Roomtype_Mapping", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 12)
	public void sendDeleteRoomtypeMapping(String json) {
		logger.info("发送消息成功，"+json.toString());
	}
	
	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "Update_Roomtype_Mapping", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 12)
	public void sendUpdateRoomtypeMapping(String json) {
		logger.info("发送消息成功，"+json.toString());
	}
	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "Sync_Roomtype_Mapping", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 12)
	public void sendSyncRoomtypeMapping(String json) {
		logger.info("发送消息成功，"+json.toString());
	}
	
	

}
