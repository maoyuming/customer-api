package com.fangbaba.api.kafka.fangcang;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fangbaba.api.enums.MappingOptEnum;
import com.fangbaba.gds.face.service.IRoomtypeMappingService;
import com.fangbaba.gds.po.HotelMapping;
import com.fangbaba.gds.po.RoomtypeMapping;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicConsumer;

/**
 * Created by nolan on 16/2/17.
 * description:
 */
@MkMessageService
public class RoomtypeMappingSyncConsumer {
    private Logger logger = LoggerFactory.getLogger(RoomtypeMappingSyncConsumer.class);
    
//    @Autowired 
//    private IRoomtypeMappingService iRoomtypeMappingService;
    
    
    @Autowired 
    private com.fangbaba.api.face.service.IRoomtypeMappingService iRoomtypeMappingService;

    @MkTopicConsumer(topic = "Add_Roomtype_Mapping", group = "Api_RoomtypeMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void addRoomtypeMappingBatch(String json) {
    	logger.info("批量add房型映射：{}",json);
    	List<RoomtypeMapping> roomtypeMappings = new Gson().fromJson(json,new TypeToken<List<RoomtypeMapping>>() {}.getType());
    	List<RoomtypeMapping> roomtypeMappingAdd = new ArrayList<RoomtypeMapping>();
    	for (RoomtypeMapping roomtypeMapping : roomtypeMappings) {
			if(StringUtils.isBlank(roomtypeMapping.getOpt())){
				logger.info("没有opt参数：{}",new Gson().toJson(roomtypeMapping));
				continue;
			}
			
			MappingOptEnum enum1 = MappingOptEnum.findByOpt(roomtypeMapping.getOpt());
			if(enum1==null){
				logger.info("opt参数错误：{}",new Gson().toJson(roomtypeMapping));
				continue;
			}
			switch (enum1) {
			case insert:
				roomtypeMappingAdd.add(roomtypeMapping);
				break;
			case update:
				logger.info("新增操作不执行其他的json：{}",new Gson().toJson(roomtypeMapping));
				break;
			case delete:
				logger.info("新增操作不执行其他的json：{}",new Gson().toJson(roomtypeMapping));
				break;

			default:
				break;
			}
		}
    	
    	
    	iRoomtypeMappingService.doAddRoomtypeMapping(roomtypeMappingAdd);
    }
    @MkTopicConsumer(topic = "Delete_Roomtype_Mapping", group = "Api_RoomtypeMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void deleteRoomtypeMappingBatch(String json) {
    	logger.info("批量delete房型映射：{}",json);
    	List<RoomtypeMapping> roomtypeMappings = new Gson().fromJson(json,new TypeToken<List<RoomtypeMapping>>() {}.getType());
    	List<RoomtypeMapping> roomtypeMappingDelete = new ArrayList<RoomtypeMapping>();
    	for (RoomtypeMapping roomtypeMapping : roomtypeMappings) {
			if(StringUtils.isBlank(roomtypeMapping.getOpt())){
				logger.info("没有opt参数：{}",new Gson().toJson(roomtypeMapping));
				continue;
			}
			
			MappingOptEnum enum1 = MappingOptEnum.findByOpt(roomtypeMapping.getOpt());
			if(enum1==null){
				logger.info("opt参数错误：{}",new Gson().toJson(roomtypeMapping));
				continue;
			}
			switch (enum1) {
			case insert:
				logger.info("删除操作不执行其他的json：{}",new Gson().toJson(roomtypeMapping));
				break;
			case update:
				logger.info("删除操作不执行其他的json：{}",new Gson().toJson(roomtypeMapping));
				break;
			case delete:
				roomtypeMappingDelete.add(roomtypeMapping);
				break;

			default:
				break;
			}
		}
    	iRoomtypeMappingService.doDeleteRoomtypeMapping(roomtypeMappingDelete);
    }
    @MkTopicConsumer(topic = "Update_Roomtype_Mapping", group = "Api_RoomtypeMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void updateRoomtypeMappingBatch(String json) {
    	logger.info("批量update房型映射：{}",json);
    	List<RoomtypeMapping> roomtypeMappings = new Gson().fromJson(json,new TypeToken<List<RoomtypeMapping>>() {}.getType());
    	List<RoomtypeMapping> roomtypeMappingUpdate = new ArrayList<RoomtypeMapping>();
    	for (RoomtypeMapping roomtypeMapping : roomtypeMappings) {
    		if(StringUtils.isBlank(roomtypeMapping.getOpt())){
    			logger.info("没有opt参数：{}",new Gson().toJson(roomtypeMapping));
    			continue;
    		}
    		
    		MappingOptEnum enum1 = MappingOptEnum.findByOpt(roomtypeMapping.getOpt());
    		if(enum1==null){
    			logger.info("opt参数错误：{}",new Gson().toJson(roomtypeMapping));
    			continue;
    		}
    		switch (enum1) {
    		case insert:
    			logger.info("修改操作不执行其他的json：{}",new Gson().toJson(roomtypeMapping));
    			break;
    		case update:
    			roomtypeMappingUpdate.add(roomtypeMapping);
    			break;
    		case delete:
    			logger.info("修改操作不执行其他的json：{}",new Gson().toJson(roomtypeMapping));
    			break;
    			
    		default:
    			break;
    		}
    	}
    	iRoomtypeMappingService.doUpdateRoomtypeMapping(roomtypeMappingUpdate);
    }
    @MkTopicConsumer(topic = "Sync_Roomtype_Mapping", group = "Api_RoomtypeMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void syncRoomtypeMappingBatch(String json) {
    	logger.info("批量sync房型映射：{}",json);
    	List<RoomtypeMapping> roomtypeMappings = new Gson().fromJson(json,new TypeToken<List<RoomtypeMapping>>() {}.getType());
    	
    	
    	List<RoomtypeMapping> roomtypeMappingAdd = new ArrayList<RoomtypeMapping>();
    	List<RoomtypeMapping> roomtypeMappingUpdate = new ArrayList<RoomtypeMapping>();
    	List<RoomtypeMapping> roomtypeMappingDelete = new ArrayList<RoomtypeMapping>();
    	
    	if(CollectionUtils.isNotEmpty(roomtypeMappings)){
    		for (RoomtypeMapping roomtypeMapping : roomtypeMappings) {
    			
    			
    			if(StringUtils.isBlank(roomtypeMapping.getOpt())){
        			logger.info("没有opt参数：{}",new Gson().toJson(roomtypeMapping));
        			continue;
        		}
        		
        		MappingOptEnum enum1 = MappingOptEnum.findByOpt(roomtypeMapping.getOpt());
        		if(enum1==null){
        			logger.info("opt参数错误：{}",new Gson().toJson(roomtypeMapping));
        			continue;
        		}
        		switch (enum1) {
        		case insert:
        			roomtypeMappingAdd.add(roomtypeMapping);
        			break;
        		case update:
        			roomtypeMappingUpdate.add(roomtypeMapping);
        			break;
        		case delete:
        			roomtypeMappingDelete.add(roomtypeMapping);
        			break;
        			
        		default:
        			break;
        		}
    			
			}
    	}
    	
    	iRoomtypeMappingService.doAddRoomtypeMapping(roomtypeMappingAdd);
    	iRoomtypeMappingService.doUpdateRoomtypeMapping(roomtypeMappingUpdate);
    	iRoomtypeMappingService.doDeleteRoomtypeMapping(roomtypeMappingDelete);
    }

//    @MkTopicConsumer(topic = "Roomtype_Mapping_Batch_json", group = "Gds_RoomtypeMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
//    public void saveRoomtypeMappingBatch(String json) {
//    	logger.info("批量房型映射：{}",json);
//    	iRoomtypeMappingService.saveRoomtypeMappingBatch(json);
//    }
//    @MkTopicConsumer(topic = "Roomtype_Mapping_json", group = "Gds_RoomtypeMapping", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
//    public void saveRoomtypeMapping(String json) {
//    	logger.info("房型映射：{}",json);
//    	iRoomtypeMappingService.saveRoomtypeMapping(json);
//    }
//    @MkTopicConsumer(topic = "Roomtype_Mapping", group = "Gds_RoomtypeMapping", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
//    public void saveRoomtypeMapping(RoomtypeMapping roomtypeMapping) {
//    	logger.info("房型映射：{}",new Gson().toJson(roomtypeMapping));
//    	iRoomtypeMappingService.saveRoomtypeMapping(roomtypeMapping);
//    }
//    @MkTopicConsumer(topic = "Roomtype_Mapping_Batch", group = "Gds_RoomtypeMapping", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
//    public void saveRoomtypeMappingBatch(List<RoomtypeMapping> roomtypeMappings) {
//    	logger.info("批量房型映射：{}",new Gson().toJson(roomtypeMappings));
//    	iRoomtypeMappingService.saveRoomtypeMappingBatch(roomtypeMappings);
//    }
}
