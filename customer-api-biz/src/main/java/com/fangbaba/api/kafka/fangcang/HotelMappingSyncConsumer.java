package com.fangbaba.api.kafka.fangcang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fangbaba.api.core.RedisCacheManager;
import com.fangbaba.api.enums.MappingOptEnum;
import com.fangbaba.gds.enums.HotelMappingStateEnum;
import com.fangbaba.gds.po.HotelMapping;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicConsumer;

/**
 * Created by nolan on 16/2/17.
 * description:
 */
@MkMessageService
public class HotelMappingSyncConsumer {
    private Logger logger = LoggerFactory.getLogger(HotelMappingSyncConsumer.class);
    @Autowired 
    private com.fangbaba.api.face.service.IHotelMappingService iHotelMappingServiceApi;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @MkTopicConsumer(topic = "Add_Hotel_Mapping", group = "Api_HotelMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void addHotelMappingBatch(String json) {
    	logger.info("批量add酒店映射：{}",json);
    	List<HotelMapping> hotelMappings = new Gson().fromJson(json,new TypeToken<List<HotelMapping>>() {}.getType());
    	
    	
    	List<HotelMapping> hotelMappingAdd = new ArrayList<HotelMapping>();
    	for (HotelMapping hotelMapping : hotelMappings) {
			if(StringUtils.isBlank(hotelMapping.getOpt())){
				logger.info("没有opt参数：{}",new Gson().toJson(hotelMapping));
				continue;
			}
			
			MappingOptEnum enum1 = MappingOptEnum.findByOpt(hotelMapping.getOpt());
			if(enum1==null){
				logger.info("opt参数错误：{}",new Gson().toJson(hotelMapping));
				continue;
			}
			switch (enum1) {
			case insert:
				hotelMappingAdd.add(hotelMapping);
				break;
			case update:
				logger.info("更新操作不执行其他的json：{}",new Gson().toJson(hotelMapping));
				break;
			case delete:
				logger.info("更新操作不执行其他的json：{}",new Gson().toJson(hotelMapping));
				break;

			default:
				break;
			}
		}
    	
    	
    	iHotelMappingServiceApi.doAddHotelMapping(hotelMappingAdd);
    	
    	
    }
    @MkTopicConsumer(topic = "Update_Hotel_Mapping", group = "Api_HotelMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void updateHotelMappingBatch(String json) {
    	logger.info("批量update酒店映射：{}",json);
    	List<HotelMapping> hotelMappings = new Gson().fromJson(json,new TypeToken<List<HotelMapping>>() {}.getType());
    	
    	
    	List<HotelMapping> hotelMappingUpdate = new ArrayList<HotelMapping>();
    	for (HotelMapping hotelMapping : hotelMappings) {
    		if(StringUtils.isBlank(hotelMapping.getOpt())){
    			logger.info("没有opt参数：{}",new Gson().toJson(hotelMapping));
    			continue;
    		}
    		
    		MappingOptEnum enum1 = MappingOptEnum.findByOpt(hotelMapping.getOpt());
    		if(enum1==null){
    			logger.info("opt参数错误：{}",new Gson().toJson(hotelMapping));
    			continue;
    		}
    		switch (enum1) {
    		case insert:
    			logger.info("新增操作不执行其他的json：{}",new Gson().toJson(hotelMapping));
    			break;
    		case update:
    			hotelMappingUpdate.add(hotelMapping);
    			break;
    		case delete:
    			logger.info("新增操作不执行其他的json：{}",new Gson().toJson(hotelMapping));
    			break;
    			
    		default:
    			break;
    		}
    	}
    	
    	
    	iHotelMappingServiceApi.doUpdateHotelMapping(hotelMappingUpdate);
    	
    	
    }
    @MkTopicConsumer(topic = "Delete_Hotel_Mapping", group = "Api_HotelMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void deleteHotelMappingBatch(String json) {
    	logger.info("批量delete酒店映射：{}",json);
    	List<HotelMapping> hotelMappings = new Gson().fromJson(json,new TypeToken<List<HotelMapping>>() {}.getType());
    	List<HotelMapping> hotelMappingDelete = new ArrayList<HotelMapping>();
    	for (HotelMapping hotelMapping : hotelMappings) {
			if(StringUtils.isBlank(hotelMapping.getOpt())){
				logger.info("没有opt参数：{}",new Gson().toJson(hotelMapping));
				continue;
			}
			
			MappingOptEnum enum1 = MappingOptEnum.findByOpt(hotelMapping.getOpt());
			if(enum1==null){
				logger.info("opt参数错误：{}",new Gson().toJson(hotelMapping));
				continue;
			}
			switch (enum1) {
			case insert:
				logger.info("删除操作不执行其他的json：{}",new Gson().toJson(hotelMapping));
				break;
			case update:
				logger.info("删除操作不执行其他的json：{}",new Gson().toJson(hotelMapping));
				break;
			case delete:
				hotelMappingDelete.add(hotelMapping);
				break;

			default:
				break;
			}
		}
    	
    	
    	iHotelMappingServiceApi.doDeleteHotelMapping(hotelMappingDelete);
    }
    
    
    /**
     * 同步消息
     * @param json
     */
    @MkTopicConsumer(topic = "Sync_Hotel_Mapping", group = "Api_HotelMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void syncHotelMappingBatch(String json) {
    	logger.info("批量Sync酒店映射：{}",json);
    	try {
			List<HotelMapping> hotelMappings = new Gson().fromJson(json,new TypeToken<List<HotelMapping>>() {}.getType());
			
			List<HotelMapping> hotelMappingAdd = new ArrayList<HotelMapping>();
			List<HotelMapping> hotelMappingUpdate = new ArrayList<HotelMapping>();
			List<HotelMapping> hotelMappingDelete = new ArrayList<HotelMapping>();
			
			if(CollectionUtils.isNotEmpty(hotelMappings)){
				for (HotelMapping hotelMapping : hotelMappings) {
			
					if(StringUtils.isBlank(hotelMapping.getOpt())){
						logger.info("没有opt参数：{}",new Gson().toJson(hotelMapping));
						continue;
					}
					MappingOptEnum enum1 = MappingOptEnum.findByOpt(hotelMapping.getOpt());
					if(enum1==null){
						logger.info("opt参数错误：{}",new Gson().toJson(hotelMapping));
						continue;
					}
					switch (enum1) {
					case insert:
						hotelMappingAdd.add(hotelMapping);
						break;
					case update:
						hotelMappingUpdate.add(hotelMapping);
						break;
					case delete:
						hotelMappingDelete.add(hotelMapping);
						break;

					default:
						break;
					}
				}
				iHotelMappingServiceApi.doAddHotelMapping(hotelMappingAdd);
				iHotelMappingServiceApi.doUpdateHotelMapping(hotelMappingUpdate);
				iHotelMappingServiceApi.doDeleteHotelMapping(hotelMappingDelete);
			}else{
				logger.info("消息体为空");
			}
			
		} catch (Exception e) {
			logger.error("接收同步酒店映射异常",e);
		}
    }
    
    
    
    public static void main(String[] args) {
    	
    	List<Integer> hotelMappings =  new ArrayList<Integer>();
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
    	hotelMappings.add(1);
		List<Integer> hotelMappingList =  new ArrayList<Integer>();
		//10个一组，调用房仓的同步接口
		if(hotelMappings.size()<=10){
			hotelMappingList = hotelMappings;
		}else{
			int interval = 10;
			int begin=0;
			
			while(true){

				int end = begin+interval;
				if(end>hotelMappings.size()){
					end = hotelMappings.size();
				}
				hotelMappingList =  new ArrayList<Integer>();
				hotelMappingList = hotelMappings.subList(begin, end);
				System.out.println(hotelMappingList.size());
				System.out.println(new Gson().toJson(hotelMappingList));
				if(CollectionUtils.isEmpty(hotelMappingList)){
					break;
				}
				if(end<=hotelMappings.size()){
					begin = end;
				}
				
			
			}
//			for (int i = 0; i < hotelMappings.size()/interval+1; i+10) {
//				int end = begin+interval;
//				if(end>hotelMappings.size()){
//					end = hotelMappings.size();
//				}
//				hotelMappingList =  new ArrayList<Integer>();
//				hotelMappingList = hotelMappings.subList(begin, begin+interval);
//				System.out.println(hotelMappingList.size());
//				System.out.println(new Gson().toJson(hotelMappingList));
//				
//				if(end<=hotelMappings.size()){
//					begin = end+1;
//				}
//			}
		}
		
	}
//    @MkTopicConsumer(topic = "Roomtype_Mapping_json", group = "Gds_HotelMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
//    public void saveHotelMapping(String json) {
//    	logger.info("酒店映射：{}",json);
//    	iHotelMappingService.saveHotelMapping(json);
//    }
//    @MkTopicConsumer(topic = "Roomtype_Mapping", group = "Gds_HotelMapping", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
//    public void saveHotelMapping(HotelMapping hotelMapping) {
//    	logger.info("酒店映射：{}",new Gson().toJson(hotelMapping));
//    	iHotelMappingService.saveHotelMapping(hotelMapping);
//    }
//    @MkTopicConsumer(topic = "Roomtype_Mapping_Batch", group = "Gds_HotelMapping", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
//    public void saveHotelMappingBatch(List<HotelMapping> hotelMappings) {
//    	logger.info("批量酒店映射：{}",new Gson().toJson(hotelMappings));
//    	iHotelMappingService.saveHotelMappingBatch(hotelMappings);
//    }
}
