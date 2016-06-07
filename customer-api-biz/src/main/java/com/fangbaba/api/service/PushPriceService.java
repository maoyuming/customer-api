package com.fangbaba.api.service;

import java.util.List;

import com.fangbaba.api.face.service.IPushPriceService;
import com.fangbaba.gds.face.bean.OtaHotel;

public interface PushPriceService extends IPushPriceService{
	public void pushAllPriceInfos(Integer channelId);
	public  void pushAllPriceInfos(List<OtaHotel> list,Integer channelId);
}
