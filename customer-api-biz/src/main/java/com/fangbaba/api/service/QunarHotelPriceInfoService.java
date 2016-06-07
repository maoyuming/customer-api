package com.fangbaba.api.service;

import java.util.Date;

import com.fangbaba.api.domain.qunar.HotelOfferPrice;
import com.fangbaba.api.face.base.RetInfo;

public interface QunarHotelPriceInfoService {

	/**
	 * 获取酒店房型的信息
	 * @param hotleid
	 * @param begintime
	 * @param entime
	 * @param roomtypeid
	 * @param usedFor
	 * @param count
	 * @return
	 */
	public RetInfo<HotelOfferPrice> getHotelPriceInfo(Long hotelId,Date begintime,Date endtime,Long roomtypeId,String usedFor,Integer count);
}
