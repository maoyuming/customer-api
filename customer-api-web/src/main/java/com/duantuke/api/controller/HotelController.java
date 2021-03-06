package com.duantuke.api.controller;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.service.impl.HotelOpenService;
import com.duantuke.api.util.DateUtil;
import com.duantuke.basic.face.bean.HotelInfo;


/**
 * 农家院
 * @author yuming.mao
 *
 */
@Controller
@RequestMapping(value = "/hotel")
public class HotelController {
	private static Logger logger = LoggerFactory.getLogger(HotelController.class);
	@Autowired
	private HotelOpenService hotelOpenService;
	
    /**
     * 农家院详情
     * @param hotelId
     * @param begintime
     * @param endtime
     * @return
     * @throws Exception 
     */
	@RequestMapping(value = "/detail")
    public ResponseEntity<OpenResponse<HotelInfo>> detail(Long hotelId,String begintime,String endtime) throws Exception {
		logger.info("查看农家院详情入参，hotelId:{}",hotelId+", begintime:"+begintime+", endtime:"+endtime);
		OpenResponse<HotelInfo> openResponse = new OpenResponse<HotelInfo>();
		
		try {
			if(hotelId == null){
				openResponse.setErrorMessage("参数hotelId为空");
				openResponse.setResult(Constants.FAIL);
				logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
				return new ResponseEntity<OpenResponse<HotelInfo>> (openResponse, HttpStatus.OK);
			}
			if(StringUtils.isBlank(begintime)){
				begintime = DateUtil.dateToStr(new Date(), DateUtil.DateFormat);
			}
			if(StringUtils.isBlank(endtime)){
				endtime = DateUtil.dateToStr(DateUtil.getDay(-1), DateUtil.DateFormat);
			}
			HotelInfo hotelInfo =  hotelOpenService.detail(hotelId, begintime, endtime);
			openResponse.setData(hotelInfo);;
			openResponse.setResult(Constants.SUCCESS);
			logger.info("返回值openResponse：{}",JSON.toJSONString(openResponse));
		} catch (Exception e) {
			openResponse.setResult(Constants.FAIL);
			logger.error("HotelController detail error",e);
			throw e;
		}
		
		return new ResponseEntity<OpenResponse<HotelInfo>> (openResponse, HttpStatus.OK);
	}
	
}
