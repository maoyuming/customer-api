package com.fangbaba.api.kafka.push;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.fangbaba.api.service.PushHotelService;
import com.fangbaba.gds.face.service.IHotelChannelSettingService;
import com.fangbaba.gds.po.HotelChannelSetting;
import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicConsumer;

/**
 * 推送变化的酒店
 * zhangyajun
 * description:
 */
@MkMessageService
public class HotelConsumer {

    private Logger logger = LoggerFactory.getLogger(HotelConsumer.class);
    @Autowired
    private PushHotelService pushHotelService;
    @Autowired
    private IHotelChannelSettingService hotelChannelSettingService;
    /**
     * 同步酒店信息
     * 从pms过来的酒店信息同步
     * @param json
     */
    @MkTopicConsumer(topic = "Basic_SyncHotelModel", group = "apipush_all_hotel_sync", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void hotelPush(String json) {
    	logger.info("Basic_SyncHotelModel传来的参数:{}",json);
    	JSONObject jsonObject = JSONObject.parseObject(json);
    	//解析json串
    	Long hotelid = jsonObject.getLong("id");
//		String act = jsonObject.getString("act");
    	logger.info("HotelConsumer Basic_SyncHotelModel,start:" + hotelid);
    	try{
    		List<HotelChannelSetting> hotelChannelSettings = hotelChannelSettingService.queryHotelChannelSettingByHotelid(hotelid);
    		if(CollectionUtils.isNotEmpty(hotelChannelSettings)){
    			for (HotelChannelSetting hotelChannelSetting : hotelChannelSettings) {
    				pushHotelService.pushHotel(hotelid,hotelChannelSetting.getChannelid());
				}
    		}
    	}catch(Exception e){
    	    logger.error("HotelConsumer Basic_SyncHotelModel:hotelid"+hotelid,e);
    	}
        logger.info("HotelConsumer Basic_SyncHotelModel,end:" + hotelid);
    }
    /**
     * 同步酒店信息
     * 分销酒店同步
     * @param json
     */
    @MkTopicConsumer(topic = "hotel_distribution_add", group = "apipush_all_hotel_sync", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void hotelDistributionPush(String json) {
    	logger.info("hotel_distribution_add传来的参数:{}",json);
    	JSONObject jsonObject = JSONObject.parseObject(json);
    	//解析json串
    	Long hotelid = jsonObject.getLong("hotelid");
		Integer channelid = jsonObject.getInteger("channelid");
    	logger.info("HotelConsumer hotel_distribution_add,start:" + hotelid);
    	try{
			pushHotelService.pushHotel(hotelid,channelid);
    	}catch(Exception e){
    		logger.error("HotelConsumer hotel_distribution_add:hotelid"+hotelid,e);
    	}
    	logger.info("HotelConsumer hotel_distribution_add,end:" + hotelid);
    }
    /**
     * 同步酒店信息
     * 删除分销酒店
     * @param json
     */
    @MkTopicConsumer(topic = "hotel_distribution_delete", group = "apipush_all_hotel_sync", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void hotelDistributionDeletePush(String json) {
    	logger.info("hotel_distribution_delete传来的参数:{}",json);
    	JSONObject jsonObject = JSONObject.parseObject(json);
    	//解析json串
    	Long hotelid = jsonObject.getLong("hotelid");
    	Integer channelid = jsonObject.getInteger("channelid");
    	logger.info("HotelConsumer hotel_distribution_delete,start:" + hotelid);
    	try{
    		pushHotelService.pushDeleteHotel(hotelid,channelid);
    	}catch(Exception e){
    		logger.error("HotelConsumer hotel_distribution_delete:hotelid"+hotelid,e);
    	}
    	logger.info("HotelConsumer hotel_distribution_delete,end:" + hotelid);
    }
    
    
}
