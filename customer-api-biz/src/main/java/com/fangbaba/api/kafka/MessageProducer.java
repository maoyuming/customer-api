package com.fangbaba.api.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicProducer;
import com.mk.mms.kafka.model.Message;
import com.mk.mms.kafka.model.SmsMessage;


/**
 * 发送消息
 * @author tankai
 *
 */
@MkMessageService
public class MessageProducer {

	private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "commonSmsMsg", serializerClass = "com.mk.kafka.client.serializer.SerializerEncoder",replicationFactor = 1)
    public void commonSmsMsg(SmsMessage message) {
		logger.info("发送短信成功，"+message.toString());
    }
	
	/**
	 * 发送短信的topic
	 * @param value
	 */
	@MkTopicProducer(topic = "templateSmsMsg", serializerClass = "com.mk.kafka.client.serializer.SerializerEncoder",replicationFactor = 1)
	public void sendSmsMsg(Message message) {
		logger.info("发送短信成功，"+message.toString());
	}
	

}
