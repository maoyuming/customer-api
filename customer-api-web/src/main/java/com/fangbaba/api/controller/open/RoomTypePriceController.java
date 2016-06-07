/**
 * 2016年3月21日下午6:53:35
 * zhaochuanbin
 */
package com.fangbaba.api.controller.open;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.domain.open.hotel.ApiRoomTypePriceRequest;
import com.fangbaba.api.domain.open.hotel.RoomTypePricesData;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.service.ApiRoomtypePriceService;
import com.fangbaba.api.util.PostUtils;
import com.fangbaba.gds.face.bean.OtaHotel;
import com.fangbaba.gds.face.bean.Page;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IGdsChannelService;
import com.fangbaba.gds.face.service.IOtaHotelService;
import com.fangbaba.gds.face.service.IPriceService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.gds.po.GdsChannel;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * @author zhaochuanbin
 *
 */
@Controller
@RequestMapping(value = "/open/hotel")
public class RoomTypePriceController {
    
    private static Logger logger = LoggerFactory.getLogger(RoomTypePriceController.class);
    
    @Autowired
    private ApiRoomtypePriceService apiRoomtypePriceService;
    @Autowired
    private IDistributorConfigService iDistributorConfigService;
    
    private static Gson gson = new Gson();
    
    @RequestMapping(value = "/queryprice")
    public ResponseEntity<OpenResponse<RoomTypePricesData>> queryprice(HttpServletRequest request,@RequestBody  String body) {
        logger.info("查询房型，{}",gson.toJson(body));
        
        String channelId = request.getHeader("channelid");
        OpenResponse<RoomTypePricesData> openResponse = new OpenResponse<RoomTypePricesData>();
        
        ApiRoomTypePriceRequest  roomTypePriceRequest = gson.fromJson(body, ApiRoomTypePriceRequest.class);
        logger.info("roomTypePriceRequest"+gson.toJson(roomTypePriceRequest));
        
        if (roomTypePriceRequest==null) {
            logger.warn("接收body参数错误");
            throw new OpenException(DistributionErrorEnum.bodyNull.getName(),DistributionErrorEnum.bodyNull.getId());
        }
        if(roomTypePriceRequest.getHotelid()==null){
            logger.warn("酒店为空");
            throw new OpenException(DistributionErrorEnum.hotelNull.getName(),DistributionErrorEnum.hotelNull.getId());
        }
        if(roomTypePriceRequest.getBegintime()==null){
            logger.warn("开始时间为空");
            throw new OpenException(DistributionErrorEnum.begintimeNulll.getName(),DistributionErrorEnum.begintimeNulll.getId());
        }
        if(roomTypePriceRequest.getEndtime()==null){
            logger.warn("结束时间为空");
            throw new OpenException(DistributionErrorEnum.endtimeNulll.getName(),DistributionErrorEnum.endtimeNulll.getId());
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime = new Date();
        Date endTime = new Date();
        try {
            beginTime = sdf.parse(roomTypePriceRequest.getBegintime());
            endTime = sdf.parse(roomTypePriceRequest.getEndtime());
        } catch (Exception e) {
            logger.warn("时间格式转换错误",e);
            throw new OpenException(DistributionErrorEnum.dateFormatNull.getName(),DistributionErrorEnum.dateFormatNull.getId());
        }
        List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(Integer.valueOf(channelId));
        if(CollectionUtils.isNotEmpty(distributorConfigs)){
            roomTypePriceRequest.setOtatype(distributorConfigs.get(0).getOtatype());
        }else{
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(),DistributionErrorEnum.channelidNotExists.getId());
        }
        
        RoomTypePricesData rooTypePricesData = apiRoomtypePriceService.queryPriceByRoomTypeid(roomTypePriceRequest.getHotelid(),roomTypePriceRequest.getRoomtypeid(), roomTypePriceRequest.getOtatype(), beginTime, endTime);
        logger.info("rooTypePricesData"+gson.toJson(rooTypePricesData));
        
        
        if(rooTypePricesData!=null){
            openResponse.setResult("true");
            openResponse.setData(rooTypePricesData);
        }else{
            openResponse.setResult("false");
            openResponse.setErrorcode(DistributionErrorEnum.nofindRoomtypePrice.getId());
            openResponse.setErrormessage(DistributionErrorEnum.nofindRoomtypePrice.getName());
        }
        
        return new ResponseEntity<OpenResponse<RoomTypePricesData>>(openResponse,HttpStatus.OK);
    }
    

}
