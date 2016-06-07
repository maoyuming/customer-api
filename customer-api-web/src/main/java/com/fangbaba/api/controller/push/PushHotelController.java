package com.fangbaba.api.controller.push;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.face.service.IPushFullHotelService;
import com.fangbaba.api.service.PushFullHotelService;
import com.fangbaba.gds.face.service.IGdsChannelService;
import com.fangbaba.gds.po.GdsChannel;
import com.google.gson.Gson;


@Controller
@RequestMapping(value = "/push/hotel")
public class PushHotelController {
   private static Logger logger = LoggerFactory.getLogger(PushHotelController.class);
   @Autowired
   private IGdsChannelService gdsChannelService;
   @Autowired 
   private PushFullHotelService pushFullHotelService;
   private static Gson gson = new Gson();

	
	@RequestMapping(value = "/allpush")
    public ResponseEntity<Boolean> allpush(HttpServletRequest request,String channelid) {
        logger.info("全量推送酒店开始");
        List<GdsChannel> gdsChannels = new ArrayList<GdsChannel>();
        //如果channelid 为一个
        if (StringUtils.isNotEmpty(channelid)) {
            GdsChannel gdsChannel =  gdsChannelService.queryByChannelId(Integer.parseInt(channelid));
            gdsChannels.add(gdsChannel);
        }else{
            logger.warn("channelid参数为空");
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(), DistributionErrorEnum.channelidNotExists.getId());
        }
        logger.info("全量推送酒店的gdsChannels的长度:"+gdsChannels.size());
        
        for (GdsChannel gdsChannel : gdsChannels) {
        	pushFullHotelService.pushhotel(gdsChannel.getChannelid());
        }
        logger.info("全量推送酒店完毕");
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
	
}
