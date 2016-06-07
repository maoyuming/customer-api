package com.fangbaba.api.kafka.push;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fangbaba.api.service.PushPriceService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.gds.face.bean.OtaHotel;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IHotelChannelSettingService;
import com.fangbaba.gds.face.service.IOtaHotelService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.gds.po.HotelChannelSetting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicConsumer;


/**
 * @author he
 */
@MkMessageService
public class HotelPriceChangePushConsumer {

    private Logger logger = LoggerFactory.getLogger(HotelPriceChangePushConsumer.class);

    @Autowired
    private IOtaHotelService otaHotelService;
    @Autowired
    private PushPriceService pushPriceService;
    @Autowired
    private IHotelChannelSettingService hotelChannelSettingService;
    @Autowired
    private IDistributorConfigService iDistributorConfigService;
    
    private Gson gson = new Gson();
    /**
     * 酒店增量推送
     *
     * @param hotelTag
     */
    @MkTopicConsumer(topic = "hotel_price_change_push", group = "Api_Hotel_Price_Change_Push_hotelPriceChange", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void hotelPriceChangePush(String json) {
    	logger.info("hotel_price_change_push consumer json:{}",json);
        try {
        	Map<String, String> retMap = gson.fromJson(json,  
                    new TypeToken<Map<String, String>>() {  
                    }.getType());
        	String otatype = retMap.get("otatype");
        	String hotelid = retMap.get("hotelid");
        	List<OtaHotel> list = new ArrayList<OtaHotel>();
        	OtaHotel otaHotel = otaHotelService.queryByOtatypeAndHotelid(Long.parseLong(otatype), Long.parseLong(hotelid));
        	list.add(otaHotel);
        	
        	DistributorConfig config = iDistributorConfigService.queryByOtaType(Long.parseLong(otatype));
        	if(config==null){
        		logger.info("otatype，{}不存在",otatype);
        		return ;
        	}
	        pushPriceService.pushAllPriceInfos(list,config.getChannelid());
        	
		} catch (Exception e) {
			logger.error("hotel_price_change_push error:",e);
		}
       
    }
}
