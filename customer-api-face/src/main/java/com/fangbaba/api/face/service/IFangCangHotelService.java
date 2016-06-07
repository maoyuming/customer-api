package com.fangbaba.api.face.service;

import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.gds.face.bean.HotelModelEsBean;

public interface IFangCangHotelService {
	public RetInfo<HotelModelEsBean> getHotelInfo(String fcHotelIds);
}
