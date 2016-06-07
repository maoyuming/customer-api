package com.fangbaba.api.service;

import com.fangbaba.basic.face.bean.Tags;

import java.util.List;
import java.util.Map;

/**
 * 酒店标签同步服务类
 * <p/>
 * Created by nolan on 16/3/28.
 * description:
 */
public interface IHotelTagService {
    /**
     * 单渠道商全量同步
     *
     * @param channelid 渠道id
     * @return
     */
    int batchSyncHotelTags(Integer channelid);

    /**
     * 同步酒店标签至多渠道商
     *
     * @param hotelTag
     * @return
     */
    int syncHotelTag(Map<String, List<Tags>> hotelTag,Integer channelid);
}
