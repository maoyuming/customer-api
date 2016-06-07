package com.fangbaba.api.service;

import java.util.List;

import com.fangbaba.api.domain.fangcang.Response;

/**
 * Created by nolan on 16/4/15.
 * description:
 */
public interface FangCangHotelPriceService {

    /**
     * 同步酒店所有房型价格计划
     *
     * @param hotelid 酒店id
     * @return
     */
	List<Response> syncRatePlan(Long hotelid);

    /**
     * 同步酒店单房型价格计划
     *
     * @param hotelid    酒店id
     * @param roomtypeid 房型id
     * @return
     * @throws Exception
     */
    Response syncRatePlan(Long hotelid, Long roomtypeid) ;

    /**
     * 删除酒店所有房型价格计划
     *
     * @param hotelid 酒店id
     * @return
     */
    List<Response> deleteRatePlan(Long hotelid) ;

    /**
     * 删除酒店单房型价格计划
     *
     * @param hotelid    酒店id
     * @param roomtypeid 房型id
     * @return
     * @throws Exception
     */
    Response deleteRatePlan(Long hotelid, Long roomtypeid) ;

    /**
     * 同步酒店所有房型价格信息
     *
     * @param otatype 分销商id
     * @param hotelid 酒店id
     * @return
     * @throws Exception
     */
    Response syncRateInfo(Long otatype, Long hotelid,Long roomtypeid) ;

    /**
     * 同步酒店单房型价格
     *
     * @param otatype  分销商id
     * @param hotelid  酒店id
     * @param roomtype 房型id
     * @return
     * @throws Exception
     */
    public List<Response> syncRateInfo(Long hotelid) ;
}
