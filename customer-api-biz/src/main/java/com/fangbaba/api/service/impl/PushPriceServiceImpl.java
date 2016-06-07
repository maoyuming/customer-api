package com.fangbaba.api.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.open.hotel.RoomTypePricesData;
import com.fangbaba.api.service.ApiRoomtypePriceService;
import com.fangbaba.api.service.FangCangHotelPriceService;
import com.fangbaba.api.service.PushPriceService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.api.util.Config;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.bean.OtaHotel;
import com.fangbaba.gds.face.bean.Page;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IOtaHotelService;
import com.fangbaba.gds.face.service.IPriceService;
import com.fangbaba.gds.po.DistributorConfig;
import com.google.gson.Gson;

@Service
public class PushPriceServiceImpl implements PushPriceService {
	
	private static Logger logger = LoggerFactory.getLogger(PushPriceServiceImpl.class);
	
	@Autowired
    private IDistributorConfigService iDistributorConfigService;
    @Autowired
    private IPriceService priceService;
    @Autowired
    private ApiRoomtypePriceService apiRoomtypePriceService;
    @Autowired
    private IOtaHotelService otaHotelService;
    @Autowired
	private BusinessUtil businessUtil;
    @Autowired
    private FangCangHotelPriceService fangCangHotelPriceService;
    
    private Gson gson = new Gson();

    
    
    /**
     * 推送价格
     */
	@Override
	public void pushAllPriceInfos(List<OtaHotel> list,Integer channelId) {
		//调用推送接口，推送数据
		
		Calendar startc = Calendar.getInstance();
		Calendar endc = Calendar.getInstance();
		endc.add(Calendar.DAY_OF_MONTH, 31);
		for (OtaHotel otaHotel:list) {
			RoomTypePricesData data = apiRoomtypePriceService.queryPriceByRoomTypeid(otaHotel.getHotelid(), null, otaHotel.getOtatype(), startc.getTime(), endc.getTime());
			
			if(data!=null){
				data.setHotelid(otaHotel.getHotelid());
				String jsonstr = gson.toJson(data);
				businessUtil.push(GdsChannelUrlEnum.pushPrice.getId(), jsonstr,channelId,otaHotel.getHotelid().toString(),null);
				logger.info("推送价格 数据：{}",jsonstr);
			}else{
				logger.info("没有查询到酒店/房型信息");
			}
		}

	}



	@Override
	public void pushAllPriceInfos(Integer channelid) {
		logger.info("全量推送价格开始");

		ChannelEnum key = ChannelEnum.getById(channelid);
		switch (key) {
		case fangcang:
			try {
				fangCangHotelPriceService.syncRateInfo(null);
				fangCangHotelPriceService.syncRatePlan(null);
			} catch (Exception e) {
				logger.error("推送给房仓价格失败",e);
			}
			break;

		default:
			try {
				pushAllPriceInfosOtherChannel(channelid);
			} catch (Exception e) {
				logger.error("推送给渠道价格失败",e);
			}
			break;
		}
		
		
		
		logger.info("全量推送价格结束");
	
	}
	
	

	private void pushAllPriceInfosOtherChannel(Integer channelid) {
		//查询channelid查询分销商
				List<DistributorConfig> distributors = iDistributorConfigService.queryByChannelId(channelid);
				for (DistributorConfig distributorConfig:distributors) {
					/*if(otatype!=null && otatype!=distributorConfig.getOtatype()){
						continue;
					}*/
					//通过分销商otatype查询酒店 量太大dubbo报错 改成分页查询
					Page page = new Page();
					page.setPageNo(1);
					page.setPageSize(500);
					Map<String, Object> map = otaHotelService.queryByOtatype(distributorConfig.getOtatype(), page);
					List<OtaHotel> list = (List<OtaHotel>)map.get("data");
					this.pushAllPriceInfos(list,channelid);
					
					page.setTotalRecord((Integer) map.get("total"));
			        Integer totalpage = page.getTotalPage();
			        if(totalpage>1){
			        	for (int i = 2; i <= totalpage; i++) {
			        		Page page1 = new Page();
			        		page1.setPageNo(i);
			        		page1.setPageSize(500);
							Map<String, Object> otahotelmap = otaHotelService.queryByOtatype(distributorConfig.getOtatype(), page1);
							List<OtaHotel> list1 = (List<OtaHotel>)otahotelmap.get("data");
							this.pushAllPriceInfos(list1,channelid);
			        	}
			        }
				}
				/*String result = PostUtils.postToUrl(json, url,headerMap);*/
	}



	@Override
	public void pushAllPriceInfos() {
		String channelStr = Config.getValue("hotel_open_channel");
		
		if(StringUtils.isNotBlank(channelStr)){
			String[] array = channelStr.split("#");
			for (String string : array) {
				this.pushAllPriceInfos(Integer.valueOf(string));
			}
		}
		
		
	}
	

}
