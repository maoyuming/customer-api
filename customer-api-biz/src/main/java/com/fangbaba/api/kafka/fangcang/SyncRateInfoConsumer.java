package com.fangbaba.api.kafka.fangcang;

import com.fangbaba.api.service.FangCangHotelPriceService;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.face.base.RetInfo;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by nolan on 16/4/15.
 * description:
 */
@MkMessageService
public class SyncRateInfoConsumer {
    private Logger logger = LoggerFactory.getLogger(SyncRateInfoConsumer.class);

    @Autowired
    private FangCangHotelPriceService fangCangHotelPriceService;
    @Autowired
    private IHotelSearchService iHotelSearchService;

    @MkTopicConsumer(topic = "hotel_price_change_push", group = "Api_Hotel_Price_Change_Push_SyncRateInfo", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void consumeSyncRateInfo(String json) throws Exception {
        logger.info("syncRateInfo(String json:{})>>>>>START", json);
        Gson gson = new Gson();
        Map<String, String> retMap = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
        Long otatype = Long.parseLong(retMap.get("otatype"));
        Long hotelid = Long.parseLong(retMap.get("hotelid"));
        logger.info("map中获取参数otatype:{},hotelid:{}",otatype,hotelid);
        Long roomtypeid = null;
        if (!Strings.isNullOrEmpty(retMap.get("roomtypeid"))) {
        	roomtypeid = Long.parseLong(retMap.get("roomtypeid"));
        }

        HotelModelEsBean hotelModelEsBean = new HotelModelEsBean();
        hotelModelEsBean.setOtatype(otatype);
        hotelModelEsBean.setId(hotelid);
        logger.info("es酒店对象参数:{}",new Gson().toJson(hotelModelEsBean));
        RetInfo<HotelModelEsBean> retInfo = iHotelSearchService.queryHotelByHotelId(hotelModelEsBean);
        if (retInfo != null
                && retInfo.isResult()
                && retInfo.getObj() != null
                && retInfo.getObj().getChannelId() != null
                && ChannelEnum.fangcang.getId() == retInfo.getObj().getChannelId().intValue()) {
            //roomtypeid为null,则为全量房型更新
            fangCangHotelPriceService.syncRateInfo(otatype, hotelid, roomtypeid);
        }
        logger.info("syncRateInfo(String json:{})>>>>>END", json);
    }

    @MkTopicConsumer(topic = "Basic_RoomtypeChange", group = "Api_Basic_RoomtypeChange", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
    public void consumeSyncRatePlan(String json) {
        logger.info("void consumeSyncRatePlan(String json:{})>>>>>>>Start", json);
        Gson gson = new Gson();
        Map<String, String> retMap = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
        Long roomtypeid = Long.parseLong(retMap.get("roomtypeid"));
        Long hotelid = Long.parseLong(retMap.get("hotelid"));
        String act = String.valueOf(retMap.get("act"));

        if ("add".equals(act) || "modify".equals(act)) {
            try {
                fangCangHotelPriceService.syncRatePlan(hotelid, roomtypeid);
            } catch (Exception e) {
                logger.error("同步价格计划出错", e);
            }
        } else if ("delete".equals(act)) {
            try {
                fangCangHotelPriceService.deleteRatePlan(hotelid, roomtypeid);
            } catch (Exception e) {
                logger.error("删除价格计划出错", e);
            }
        } else {
            logger.warn("act数据有误");
        }

        logger.info("void consumeSyncRatePlan(String json:{})>>>>>>>End", json);
    }
}
