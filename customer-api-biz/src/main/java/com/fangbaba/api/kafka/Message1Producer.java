//package com.fangbaba.api.kafka;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.mk.kafka.client.stereotype.MkMessageService;
//import com.mk.kafka.client.stereotype.MkTopicProducer;
//
//
///**
// * 发送消息
// * @author tankai
// *
// */
//@MkMessageService
//public class Message1Producer {
//
//	private static final Logger logger = LoggerFactory.getLogger(Message1Producer.class);
//
//	/**
//	 * 发送短信的topic
//	 * @param value
//	 */
//	@MkTopicProducer(topic = "Basic_hotel_sync_change", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 1)
//    public void commonSmsMsg(String message) {
//		logger.info("发送短信成功，"+message);
//    }
//	
//	/**
//	 * 发送短信的topic
//	 * @param value
//	 */
//	@MkTopicProducer(topic = "Basic_hotel_sync_change", serializerClass = "com.mk.kafka.client.serializer.StringEncoder",replicationFactor = 1)
//    public void commonSmsMsg(String message) {
//		logger.info("发送短信成功，"+message);
//    }
//	
//
//}
