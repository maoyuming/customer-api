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
public class HotelMappingSyncProducer {

	private static final Logger logger = LoggerFactory.getLogger(HotelMappingSyncProducer.class);

	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "Add_Hotel_Mapping", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 12)
	public void addHotelMappingBatch(String json) {
		logger.info("发送消息成功，"+json.toString());
    }
	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "Delete_Hotel_Mapping", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 12)
	public void deleteHotelMappingBatch(String json) {
		logger.info("发送消息成功，"+json.toString());
	}
	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "Update_Hotel_Mapping", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 12)
	public void updateHotelMappingBatch(String json) {
		logger.info("发送消息成功，"+json.toString());
	}
	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "Sync_Hotel_Mapping", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 12)
	public void syncHotelMappingBatch(String json) {
		logger.info("发送消息成功，"+json.toString());
	}
	
	

}
