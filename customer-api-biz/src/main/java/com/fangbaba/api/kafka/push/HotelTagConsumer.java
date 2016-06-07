package com.fangbaba.api.kafka.push;

import com.fangbaba.api.service.IHotelTagService;
import com.fangbaba.basic.face.bean.Tags;
import com.mk.kafka.client.stereotype.MkMessageService;
import com.mk.kafka.client.stereotype.MkTopicConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by nolan on 16/3/28.
 * description:
 */
@MkMessageService
public class HotelTagConsumer {

    private Logger logger = LoggerFactory.getLogger(HotelTagConsumer.class);

    @Autowired
    private IHotelTagService iHotelTagService;

    /**
     * 增量同步酒店标签
     *
     * @param hotelTag
     */
    @MkTopicConsumer(topic = "Basic_SyncHoteltags", group = "Gds_Hotel", serializerClass = "com.mk.kafka.client.serializer.SerializerDecoder")
    public void consume(Map<String, List<Tags>> hotelTag) {
        logger.debug("增量同步第三方酒店标签, hoteltag:{}. >>>>>>>>>>>>>>>>//Start", hotelTag);
        iHotelTagService.syncHotelTag(hotelTag,null);
        logger.debug("增量同步第三方酒店标签, hoteltag:{}. >>>>>>>>>>>>>>>>//End", hotelTag);
    }
}
