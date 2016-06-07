/**
 * 2016年3月22日下午3:19:41
 * zhaochuanbin
 */
package com.fangbaba.api.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.open.hotel.HotelDetail;
import com.fangbaba.api.domain.open.hotel.PriceInfos;
import com.fangbaba.api.domain.open.hotel.RoomTypePrices;
import com.fangbaba.api.domain.open.hotel.RoomTypePricesData;
import com.fangbaba.api.service.ApiRoomtypePriceService;
import com.fangbaba.api.util.DateUtil;
import com.fangbaba.basic.face.base.RetInfo;
import com.fangbaba.basic.face.enums.HotelBusinessEnum;
import com.fangbaba.basic.face.service.HotelBusinessService;
import com.fangbaba.gds.face.bean.EsSearchBean;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.bean.PriceJsonBean;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.fangbaba.gds.face.service.IPriceService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * @author zhaochuanbin
 *
 */
@Service
public class ApiRoomtypePriceServiceImpl implements ApiRoomtypePriceService{
    
    private static Logger logger = LoggerFactory.getLogger(ApiRoomtypePriceServiceImpl.class);
    
    @Autowired
    private IPriceService iPriceService;
    @Autowired
    private HotelBusinessService hotelBusinessService;
    @Autowired
    private IHotelSearchService iHotelSearchService;
    private static Gson gson = new Gson();
    

    @Override
    public RoomTypePricesData queryPriceByRoomTypeid(
            Long hotelid, Long roomtypeid, Long otatype, Date begintime, Date endtime) {
        
        logger.info("hotelid:"+hotelid+"roomtypeid:"+roomtypeid+",otatype:"+otatype+",begintime:"+begintime+",endtime:"+endtime);
        RoomTypePricesData roomPricesData = new RoomTypePricesData();
        roomPricesData =  getRedisPrice(otatype,hotelid, begintime, endtime,roomtypeid);
        
//        HotelModelEsBean hotelModelEsBean = new HotelModelEsBean();
//        hotelModelEsBean.setOtatype(otatype);
//        Map<String, String> dynamicCondition = Maps.newHashMap();
//        EsSearchBean hotelbean = new EsSearchBean();
//        hotelbean.setSize(10);
//        List<HotelModelEsBean> listhotel = iHotelSearchService.searchHotelFromES(hotelModelEsBean, dynamicCondition, hotelbean);
//        if(CollectionUtils.isNotEmpty(listhotel)){
//            RetInfo<String>  retinfo = hotelBusinessService.queryBusinessStateByHotelpms(listhotel.get(0).getHotelpms(), HotelBusinessEnum.SWITCHS);
//            if(retinfo.isResult()){
//                roomPricesData =  getRedisPrice(otatype,hotelid, begintime, endtime,roomtypeid);
//            }else{
//                roomPricesData =  getBasicRackPrice(otatype,hotelid, begintime, endtime,roomtypeid);
//            }
//        }else{
//            roomPricesData = getBasicRackPrice(otatype,hotelid, begintime, endtime,roomtypeid);
//        }
        
         return roomPricesData;
    }


    /**
     * 
     *2016年5月25日上午11:09:48
     * 
     * 酒店未开通分销时查询门市价 
     */
    private RoomTypePricesData getBasicRackPrice(Long otatype, Long hotelid, Date begintime, Date endtime, Long roomtypeid) {
        RoomTypePricesData roomPricesData = new RoomTypePricesData();
        try {
            List<Long> roomtypeids = new ArrayList<Long>();
            roomtypeids.add(roomtypeid);
            Map<Long,Map<String,BigDecimal>>  priceMap = iPriceService.findRackRateByConditions(hotelid, roomtypeids , begintime, endtime);
            logger.info("map"+gson.toJson(priceMap));
            if(priceMap==null||priceMap.size()==0){
                return null;
            }else{
                List<RoomTypePrices> roomTypePriceslist = new ArrayList<RoomTypePrices>();
                for (Map.Entry<Long, Map<String, BigDecimal>> entry : priceMap.entrySet()) {
                    RoomTypePrices roomTypePrices = new RoomTypePrices();
                    roomTypePrices.setRoomtypeid(entry.getKey());
                    List<PriceInfos> priceInfoslist = new ArrayList<PriceInfos>(); 
                    for (Map.Entry<String, BigDecimal> entry2 : entry.getValue().entrySet()) {
                        PriceInfos priceInfos = new PriceInfos();
                        priceInfos.setDate(DateUtil.dateToStr(DateUtil.strToDate(entry2.getKey(), "yyyyMMdd"), "yyyy-MM-dd"));
                        priceInfos.setCost(entry2.getValue());
                        priceInfos.setPrice(entry2.getValue());
                        priceInfoslist.add(priceInfos);
                    }
                    roomTypePrices.setPriceinfos(priceInfoslist);
                    roomTypePriceslist.add(roomTypePrices);
                }
                roomPricesData.setRoomtypeprices(roomTypePriceslist);
            }
          
        } catch (Exception e) {
            logger.error("查询价格异常",e);
        }
        return roomPricesData;
    }


    private RoomTypePricesData getRedisPrice(Long otatype, Long hotelid, Date begintime, Date endtime, Long roomtypeid) {
        RoomTypePricesData roomPricesData = new RoomTypePricesData();
        try {
            //查询价格区间
            Map<Long,Map<String,String>>  priceMap = iPriceService.queryChannelPricesFromRedis(otatype,hotelid, 
                    begintime, endtime,roomtypeid);
            logger.info("map"+gson.toJson(priceMap));
            if(priceMap==null||priceMap.size()==0){
                return null;
            }else{
                List<RoomTypePrices> roomTypePriceslist = new ArrayList<RoomTypePrices>();
                for (Map.Entry<Long, Map<String, String>> entry : priceMap.entrySet()) {
                    RoomTypePrices roomTypePrices = new RoomTypePrices();
                    roomTypePrices.setRoomtypeid(entry.getKey());
                    List<PriceInfos> priceInfoslist = new ArrayList<PriceInfos>(); 
                    for (Map.Entry<String, String> entry2 : entry.getValue().entrySet()) {
                        PriceInfos priceInfos = new PriceInfos();
                        priceInfos.setDate(DateUtil.dateToStr(DateUtil.strToDate(entry2.getKey(), "yyyyMMdd"), "yyyy-MM-dd"));
                        PriceJsonBean bean = gson.fromJson(entry2.getValue(), PriceJsonBean.class);
                        priceInfos.setCost(bean.getSaleprice());
                        priceInfos.setPrice(bean.getChannelprice());
                        priceInfoslist.add(priceInfos);
                    }
                    roomTypePrices.setPriceinfos(priceInfoslist);
                    roomTypePriceslist.add(roomTypePrices);
                }
                roomPricesData.setHotelid(hotelid);
                roomPricesData.setRoomtypeprices(roomTypePriceslist);
            }
          
        } catch (Exception e) {
            logger.error("查询价格异常",e);
        }
        return roomPricesData;
    }
    
}
