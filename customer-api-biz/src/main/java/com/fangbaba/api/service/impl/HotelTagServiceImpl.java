package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.open.hotel.HotelTag;
import com.fangbaba.api.service.IHotelTagService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.basic.face.bean.Tags;
import com.fangbaba.basic.face.service.HotelTagsService;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.service.IGdsChannelUrlService;
import com.fangbaba.gds.face.service.IHotelChannelSettingService;
import com.fangbaba.gds.po.HotelChannelSetting;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

/**
 * Created by nolan on 16/3/28.
 * description:
 */
@Service
public class HotelTagServiceImpl implements IHotelTagService {

    private Logger logger = LoggerFactory.getLogger(HotelTagServiceImpl.class);

    @Autowired
    private HotelTagsService hotelTagsService;

    @Autowired
    private IHotelChannelSettingService hotelChannelSettingService;

    @Autowired
    private IGdsChannelUrlService iGdsChannelUrlService;
    
    @Autowired
    private BusinessUtil businessUtil;

    @Override
    public int batchSyncHotelTags(Integer channelid) {
        logger.info("batchSyncHotelTags(Integer channelid), channelid:{}>>>>>>>>>>>>>Start", channelid);
        //1. 查询渠道对应酒店列表
        List<HotelChannelSetting> hotelChannelSettings = hotelChannelSettingService.queryHotelChannelSettingByChannelid(channelid);
        if (hotelChannelSettings == null || hotelChannelSettings.size() == 0) {
            logger.warn("该渠道下无酒店. channelid:{}", channelid);
            return 0;
        }


        int count = 0;
        for (HotelChannelSetting hotelChannelSetting : hotelChannelSettings) {
            Long hotelid = hotelChannelSetting.getHotelid();
            List<Tags> tagses = hotelTagsService.queryTagsByHotelid(hotelid);
            logger.info("酒店(hotelid:{})对应标签列表:{}", hotelid, tagses);

            if (tagses == null || tagses.size() == 0)
                continue;


			HotelTag hotelTagInfo = new HotelTag();
			hotelTagInfo.setHotelid(hotelid);
			hotelTagInfo.setTags(tagses);
            String json = new Gson().toJson(hotelTagInfo);
//            Map orgmap = ImmutableMap.of("hotelid", hotelid, "tags", tagses);
//            String json = new Gson().toJson(orgmap);
            
            logger.info("channelid:"+channelid+",pushjson:"+json);
            businessUtil.push(GdsChannelUrlEnum.pushTags.getId(), json,channelid,hotelid.toString(),null);

            count++;
        }

        logger.info("batchSyncHotelTags(Integer channelid), channelid:{}, result:{}>>>>>>>>>>>>>End", channelid, count);
        return count;
    }

    /**
     * @param hotelTag
     * @return
     */
    public int syncHotelTag(Map<String, List<Tags>> hotelTag,Integer channelId) {
        //1.基本参数校验
        if (hotelTag == null || hotelTag.size() == 0) {
            return 0;
        }
        
        int count=0;
        for (Entry<String, List<Tags>> entry : hotelTag.entrySet()) {
			
        	//check该酒店是否开通渠道分销
        	//1. 查询渠道对应酒店列表
        	List<HotelChannelSetting> hotelChannelSettings = 
        			hotelChannelSettingService.queryHotelChannelSettingByHotelid(Long.valueOf(entry.getKey()));
        	if (hotelChannelSettings == null || hotelChannelSettings.size() == 0) {
        		logger.warn("没有开通分销的酒店 ");
        		continue;
        	}else{
        		for (HotelChannelSetting hotelChannelSetting : hotelChannelSettings) {
        			  //3.1 构造对应json结构
        			HotelTag hotelTagInfo = new HotelTag();
        			hotelTagInfo.setHotelid(Long.valueOf(entry.getKey()));
        			hotelTagInfo.setTags(hotelTag.get(entry.getKey()));
//                    Map orgmap = ImmutableMap.of("hotelid", Long.valueOf(entry.getKey()), "tags", hotelTag.get(entry.getKey()));
                    String json = new Gson().toJson(hotelTagInfo);
                    logger.info("推送文本: {}", json);

                    businessUtil.push(GdsChannelUrlEnum.pushTags.getId(), json,hotelChannelSetting.getChannelid(),entry.getKey(),null);
				}
        	}
        	
        	
          
            count++;
        }
        return count;
    }
}
