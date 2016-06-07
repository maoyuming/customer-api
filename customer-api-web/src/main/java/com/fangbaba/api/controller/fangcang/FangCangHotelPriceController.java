package com.fangbaba.api.controller.fangcang;

import com.fangbaba.api.domain.fangcang.Response;
import com.fangbaba.api.service.FangCangHotelPriceService;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.face.bean.OtaHotel;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IOtaHotelService;
import com.fangbaba.gds.po.DistributorConfig;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nolan on 16/4/15.
 * description:
 * 天下房仓接口说明: http://www.fangcang.org/USP/
 */
@Controller
@RequestMapping(value = "/fangcang/hotel/price")
public class FangCangHotelPriceController {
    private Logger logger = LoggerFactory.getLogger(FangCangHotelPriceController.class);

    @Autowired
    private FangCangHotelPriceService fangCangHotelPriceService;

    @Autowired
    private IOtaHotelService iOtaHotelService;

    @Autowired
    private IDistributorConfigService iDistributorConfigService;


    /**
     * 同步价格计划接口-SyncRatePlan
     *    根据合作方的价格计划信息同步到房仓价格计划信息。
     *    
     *    1.房仓的价格计划数据结构分为三层：酒店、房型、价格计划，接口调用方需要自行封装此数据结构；
     *    2.每次调用接口时，只传递一个房型下面最多10个价格计划数据；
     *    3.房仓保存双方的价格计划映射关系并维护；
     *    4.处理结果：全部成功或全部失败。
     * @return
     */
    @RequestMapping("/syncrateplan")
    public ResponseEntity<List<Response>> syncRatePlan(Long hotelid) {
        logger.info("同步价格计划......>>>>>开始");
        List<Response> resList =  fangCangHotelPriceService.syncRatePlan(hotelid);
        logger.info("同步价格计划......>>>>>结束");
        return new ResponseEntity<List<Response>>(resList, HttpStatus.OK);
    }

    /**
     * 删除价格计划接口-deleteRatePlan
     *    1.房仓的价格计划数据结构分为三层：酒店、房型、价格计划，接口调用方需要自行封装此数据结构；
     *    2.每次调用接口时，只传递一个房型要删除的价格计划信息；
     *    3.商品产品关系表不处理，价格计划映射、价格计划表设置无效。
     * @return
     */
    @RequestMapping("/deleterateplan")
    public ResponseEntity<List<Response>> deleteRatePlan(Long hotelid) {
        logger.info("删除价格计划......>>>>>开始");
        List<Response> resList = fangCangHotelPriceService.deleteRatePlan(hotelid);
        logger.info("删除价格计划......>>>>>结束");
        return new ResponseEntity<List<Response>>(resList, HttpStatus.OK);
    }

    /**
     * 同步价格信息接口-syncRateInfo
     *
     *      1.根据价格计划id（合作方价格计划id）同步酒店房型的定价信息;每次接口调用最多传递30天的价格信息;
     *      2.合作方可多次推送同一价格计划同一日期的价格房态等，后推送的会覆盖先推送的，只保留最近一次调用的数据信息。
     *      MinDays和MaxDays需为正整数--modify1.6
     *      
     * @return
     */
    @RequestMapping("/syncrateinfo")
    public ResponseEntity<List<Response>> syncRateInfo(Long hotelid) {
        logger.info("同步价格信息......>>>>>开始");
        List<Response> resList = fangCangHotelPriceService.syncRateInfo(hotelid);
        logger.info("同步酒店({})的价格信息......>>>>>结束");
        return new ResponseEntity<List<Response>>(resList, HttpStatus.OK);
    }

}
