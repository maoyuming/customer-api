package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.service.PushFullHotelService;
import com.fangbaba.api.service.PushHotelService;
import com.fangbaba.api.util.Config;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IGdsChannelService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.gds.po.GdsChannel;
import com.google.gson.Gson;
@Service
public class PushFullHotelServiceImpl implements PushFullHotelService{
	private static Logger logger = LoggerFactory.getLogger(PushFullHotelServiceImpl.class);
	@Autowired
    private IDistributorConfigService iDistributorConfigService;
    @Autowired
    private IGdsChannelService gdsChannelService;
    @Autowired
    private PushHotelService pushHotelDetailService;
	@Override
	public void pushFullHotel() {
		  logger.info("全量推送酒店开始...");
		  List<GdsChannel> gdsChannels = new ArrayList<GdsChannel>();
		  String channelStr = Config.getValue("hotel_fullpush_channel");
		  logger.info("获取全量推送酒店渠道id:{}",channelStr);
		  if(StringUtils.isNotBlank(channelStr)){
			  String[] array = channelStr.split("#");
				for (String s : array) {
				 GdsChannel gdsChannel =  gdsChannelService.queryByChannelId(Integer.parseInt(s));
				 gdsChannels.add(gdsChannel);
				}
			}
	     
	      for (GdsChannel gc : gdsChannels) {
	    	  pushhotel(gc.getChannelid());
	      }
	     logger.info("全量推送酒店完毕...");

		
	}
	@Override
	public void pushhotel(Integer channelid) {
		// TODO Auto-generated method stub
		 List<DistributorConfig> distributors = iDistributorConfigService.queryByChannelId(channelid);
         for (DistributorConfig distributorConfig : distributors) {
             logger.info("全量推送酒店的distributorConfig:"+new Gson().toJson(distributorConfig));
             pushHotelDetailService.otatypeRes(distributorConfig.getOtatype(),channelid);
         }
     }


}
