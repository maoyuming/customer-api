package com.fangbaba.api.controller.push;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.service.PushPriceService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.gds.face.bean.OtaHotel;
import com.fangbaba.gds.face.bean.Page;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IGdsChannelService;
import com.fangbaba.gds.face.service.IOtaHotelService;
import com.fangbaba.gds.po.DistributorConfig;

@Controller
@RequestMapping(value = "/push/price")
public class PushPriceController {
	private static Logger logger = LoggerFactory.getLogger(PushPriceController.class);
	
	@Autowired
	private IGdsChannelService gdsChannelService;
	@Autowired
	private BusinessUtil businessUtil;
	
	
    @Autowired
    private IDistributorConfigService distributorConfigService;
    @Autowired
    private IOtaHotelService otaHotelService;
    @Autowired
    private PushPriceService pushPriceService;
    
	
	/**
	 * 按渠道推送，全量推送价格信息  推送30天价格
	 * @param request
	 * @return
	 * {
		    "hotelid": "2013",
		    "hotelname": "测试酒店",
		    "roomtypeid": "11122",
		    "roomtypename" : "大床房",
		    "priceinfo": [
		        {
		            "day": "20160328",
		            "price": "218"
		        },
		        {
		            "day": "20160329",
		            "price": "300"
		        }
		    ]
		}
     * @throws Exception 
	 */
	@RequestMapping(value = "/pushAllPriceInfos")
    public ResponseEntity<Boolean> pushAllPriceInfos(HttpServletRequest request) {
		logger.info("全量推送价格开始");
		Integer channelid = Integer.valueOf(request.getHeader(com.fangbaba.api.common.Constants.CHANNEL_ID));
		
		pushPriceService.pushAllPriceInfos(channelid);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	
}
