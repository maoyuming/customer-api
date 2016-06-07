package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.fangcang.hotel.GetHotelInfoRequest;
import com.fangbaba.api.domain.fangcang.hotel.HotelInfo;
import com.fangbaba.api.domain.fangcang.hotel.HotelInfoRequest;
import com.fangbaba.api.domain.fangcang.hotel.HotelInfoResponse;
import com.fangbaba.api.domain.fangcang.hotel.RoomType;
import com.fangbaba.api.enums.FangCangRequestTypeEnum;
import com.fangbaba.api.enums.RoomTypeFangCangBedEnum;
import com.fangbaba.api.enums.RoomTypeQunarBedEnum;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.service.FangCangHotelService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.bean.RoomTypeInfo;
import com.fangbaba.gds.face.bean.Roomtype;
import com.google.gson.Gson;

@Service
public class FangCangHotelServiceImpl implements FangCangHotelService{

	private static Logger logger = LoggerFactory.getLogger(FangCangHotelServiceImpl.class);
	@Autowired
	private BusinessUtil<HotelInfoResponse> businessUtil;
	
	/**
	 * 酒店基本信息查询接口
	 * @param fcHotelIds
	 */
    public RetInfo<HotelModelEsBean> getHotelInfo(String fcHotelIds){
    	RetInfo<HotelModelEsBean> retInfo = new RetInfo<HotelModelEsBean>();
    	retInfo.setResult(false);
    	try {
    		logger.info("酒店基本信息查询接口入参：{}",fcHotelIds);
    		if(StringUtils.isBlank(fcHotelIds)){
    			retInfo.setMsg("酒店基本信息查询接口入参为空");
    			return retInfo;
    		}
			HotelInfoRequest hotelInfoRequest = new HotelInfoRequest();
			GetHotelInfoRequest getHotelInfoRequest = new GetHotelInfoRequest();
			getHotelInfoRequest.setFcHotelIds(fcHotelIds);
			
			hotelInfoRequest.setGetHotelInfoRequest(getHotelInfoRequest);
			
			Map<String, String> param = businessUtil.genFangCangRequest(hotelInfoRequest, FangCangRequestTypeEnum.GetHotelInfo);
			String responseXml = businessUtil.doPost(GdsChannelUrlEnum.GetHotelInfo.getId(), param, ChannelEnum.fangcang.getId());
			HotelInfoResponse hotelInfoResponse = businessUtil.decodeResponseXml(responseXml,HotelInfoResponse.class);
			
			
			List<HotelModelEsBean> hotelModels = new ArrayList<HotelModelEsBean>();
			if(hotelInfoResponse!=null){
				if(hotelInfoResponse.isSuccess()){
					logger.info("房仓返回成功");
					if(hotelInfoResponse.getGetHotelInfoResponse()!=null 
							&&hotelInfoResponse.getGetHotelInfoResponse().getHotelList()!=null
							&&CollectionUtils.isNotEmpty(hotelInfoResponse.getGetHotelInfoResponse().getHotelList())){
						logger.info("房仓返回酒店数:{}",hotelInfoResponse.getGetHotelInfoResponse().getHotelList().size());
						for (HotelInfo hotelInfo : hotelInfoResponse.getGetHotelInfoResponse().getHotelList()) {
							HotelModelEsBean hotelModel = new HotelModelEsBean();
							hotelModel.setId(hotelInfo.getFcHotelId());
							hotelModel.setHotelname(hotelInfo.getFcHotelChnName());
							hotelModel.setDetailaddr(hotelInfo.getFcChnAddress());
							hotelModel.setHotelphone(hotelInfo.getFcTelephone());
							
							//query roomtype
							List<RoomTypeInfo> roomtypes = new ArrayList<RoomTypeInfo>();
							if(CollectionUtils.isNotEmpty(hotelInfo.getRooms())){
								logger.info("房仓返回房型数:{}",hotelInfo.getRooms().size());
								for (RoomType roomType : hotelInfo.getRooms()) {
									RoomTypeInfo roomtypeLocal = new RoomTypeInfo();
									roomtypeLocal.setRoomTypeName(roomType.getRoomTypeName());
									roomtypeLocal.setId(roomType.getRoomtypeId());
									
									RoomTypeFangCangBedEnum bedEnum = RoomTypeFangCangBedEnum.getById(roomType.getBedType());
									if(bedEnum!=null){
										roomtypeLocal.setBedType(bedEnum.getFangbabaid());//此处需要转码
									}
									roomtypes.add(roomtypeLocal);
								}
								hotelModel.setRoomTypeInfos(roomtypes);
							}
							
							hotelModels.add(hotelModel);
							
						}
						
						retInfo.setList(hotelModels);
					}
					retInfo.setResult(true);
					
				}
				retInfo.setMsg(hotelInfoResponse.getResultMsg());
				retInfo.setCode(hotelInfoResponse.getResultCode());
			}
			logger.info("酒店基本信息查询接口返回：{}", new Gson().toJson(hotelInfoResponse));
		} catch (Exception e) {
			logger.error("酒店基本信息查询接口异常",e);
			retInfo.setMsg(e.getMessage());
		}
    	return retInfo;
    }
    
}
