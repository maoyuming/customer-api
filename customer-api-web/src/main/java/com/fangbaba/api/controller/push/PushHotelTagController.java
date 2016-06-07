package com.fangbaba.api.controller.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.domain.open.hotel.HotelTag;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.service.IHotelTagService;
import com.fangbaba.basic.face.bean.Tags;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.po.DistributorConfig;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 酒店标签对外接口
 * <p/>
 * Created by nolan on 16/3/28.
 * description:
 */
@Controller
@RequestMapping(value = "/push/hoteltag")
public class PushHotelTagController {
    private static Logger logger = LoggerFactory.getLogger(PushHotelTagController.class);

    @Autowired
    private IHotelTagService iHotelTagService;

    @Autowired
    private IDistributorConfigService iDistributorConfigService;

    
    
    /**
     * 同步全量标签信息
     * 入参：channelId 渠道
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/syncall")
    public ResponseEntity syncall(HttpServletRequest request) {
        logger.debug("syncall(HttpServletRequest request)>>>>>>>>>Begin");
        //1. 基础校验
        String channelId = request.getHeader("channelId");
        String token = request.getHeader("token");
        logger.debug("token: {}, channelId: {}", token, channelId);
        if (Strings.isNullOrEmpty(channelId)) {
            logger.warn("channelId参数为空");
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }
        List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(Integer.valueOf(channelId));
        if (!CollectionUtils.isNotEmpty(distributorConfigs)) {
            logger.warn("查询列表List<DistributorConfig>结果为空");
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }

        //2. 业务调用
        String channelname = distributorConfigs.get(0).getName();
        logger.debug("channelname: {}, channelId: {}", channelname, channelId);
        int count = iHotelTagService.batchSyncHotelTags(Integer.parseInt(channelId));

        //3. 构造返回数据
        Map<String, Object> rtnMap = Maps.newHashMap();
        rtnMap.put("result", count > 0);
        logger.debug("syncall(HttpServletRequest request), result:{}>>>>>>>>>End", count);
        return new ResponseEntity(rtnMap, HttpStatus.OK);
    }
    /**
     * 同步某个酒店标签信息
     * 入参：channelId 渠道
     * @param request
     * @return
     */
    @RequestMapping(value = "/synhoteltag")
    public ResponseEntity<Boolean> synhoteltag(HttpServletRequest request,@RequestBody  String body) {
    	HotelTag hotelTag = new Gson().fromJson(body, HotelTag.class);
    	Map<String, List<Tags>> hotelTagMap =  new HashMap<String, List<Tags>>();
    	hotelTagMap.put(hotelTag.getHotelid()+"", hotelTag.getTags());
    	iHotelTagService.syncHotelTag(hotelTagMap,null);
    	return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }


}
