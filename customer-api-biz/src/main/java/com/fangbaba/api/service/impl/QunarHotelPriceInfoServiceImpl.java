package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fangbaba.api.common.GlobalCache;
import com.fangbaba.api.domain.qunar.HotelOfferPrice;
import com.fangbaba.api.domain.qunar.HotelOfferRoom;
import com.fangbaba.api.enums.RoomTypeQunarBedEnum;
import com.fangbaba.api.enums.RoomTypeQunarBreakfastEnum;
import com.fangbaba.api.enums.RoomTypeQunarBroadbandEnum;
import com.fangbaba.api.enums.RoomTypeQunarChannelEnum;
import com.fangbaba.api.enums.RoomTypeQunarPayEnum;
import com.fangbaba.api.enums.RoomTypeQunarStatusEnum;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.service.QunarHotelPriceInfoService;
import com.fangbaba.api.util.DateUtil;
import com.fangbaba.basic.face.bean.HotelModel;
import com.fangbaba.basic.face.bean.RoomtypeModel;
import com.fangbaba.basic.face.bean.RoomtypeinfoModel;
import com.fangbaba.basic.face.service.RoomtypeService;
import com.fangbaba.basic.face.service.RoomtypeinfoService;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.face.bean.CityMapping;
import com.fangbaba.gds.face.bean.EsSearchBean;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.service.ICityMappingService;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.fangbaba.gds.face.service.IOtaHotelFullFlagService;
import com.fangbaba.gds.face.service.IPriceService;
import com.fangbaba.gds.face.service.IRoomtypeMappingService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.gds.po.RoomtypeMapping;
import com.fangbaba.stock.face.bean.RoomInfo;
import com.fangbaba.stock.face.bean.RoomTypeStock;
import com.fangbaba.stock.face.service.IStockService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 获取酒店房型信息
 * @author zhangyajun
 *
 */
@Service
public class QunarHotelPriceInfoServiceImpl implements QunarHotelPriceInfoService{
	
	@Autowired
	private IDistributorConfigService iDistributorConfigService;

	@Autowired
	private IHotelSearchService iHotelSearchService;
	@Autowired
	private RoomtypeService roomtypeService;
	@Autowired
	private IPriceService iPriceService;
	@Autowired
	private IStockService iStockService;
	@Autowired
	private RoomtypeinfoService roomtypeInfoService;
	@Autowired
	private IOtaHotelFullFlagService iOtaHotelFullFlagService;
	@Autowired
	private ICityMappingService iCityMappingService;
	@Autowired
	private IRoomtypeMappingService iRoomtypeMappingService;
	
	private static Logger logger = LoggerFactory.getLogger(QunarHotelPriceInfoServiceImpl.class);
	
	@Override
	public RetInfo<HotelOfferPrice> getHotelPriceInfo(Long hotelId,
			Date begintime, Date endtime, Long roomtypeId, String usedFor,
			Integer count) {
		
		RetInfo<HotelOfferPrice> retInfo = new RetInfo<HotelOfferPrice>();
		logger.info("qunar 酒店报接口传来的参数hotelId:{},fromDate:{},toDate:{},roomId:{},usedFor:{}",hotelId,begintime,endtime,roomtypeId,usedFor,count);
		Long otatype = null;
		Integer qunar_channelid = ChannelEnum.QUNAR.getId();
		Integer qunar_roomdechannelid = RoomTypeQunarChannelEnum.HDSORB2B.getId();
		//根据渠道查询otatype
		logger.info("去哪儿room节点属性默认值channel:{}",qunar_roomdechannelid);
		logger.info("去哪儿渠道id:{}",qunar_channelid);
			
		List<DistributorConfig>  list =iDistributorConfigService.queryByChannelId(qunar_channelid);
		if(CollectionUtils.isNotEmpty(list)){
			otatype = list.get(0).getOtatype();
			logger.info("获取去哪儿otaype:{}",otatype);
		}else {
			logger.info("获取去哪儿otaype为空");
			retInfo.setResult(false);
			retInfo.setMsg("获取去哪儿otaype为空");
			return retInfo;
		}
		if (hotelId==null) {
			logger.info("酒店id为空");
			retInfo.setResult(false);
			retInfo.setMsg("酒店id为空");
			return retInfo;
		}
		HotelModel hotelModel = new HotelModel();
		try {
			HotelModelEsBean hotelModelEsBean = new HotelModelEsBean();
			hotelModelEsBean.setId(hotelId);

			EsSearchBean bean = new EsSearchBean();

			//根据channelid查询otatype
			hotelModelEsBean.setOtatype(otatype);
			bean.setPage(1);
			bean.setSize(1);
//			bean.setSortby(hotelQuery.getOrderBy());
//			bean.setSortorder(hotelQuery.getOrderByType());

	        Map<String, String> dynamicCondition = Maps.newHashMap();
	        List<HotelModelEsBean> hotelModelEsBeans = iHotelSearchService.searchHotelFromES(hotelModelEsBean, dynamicCondition, bean);
	        if(CollectionUtils.isNotEmpty(hotelModelEsBeans)){
	        	HotelModelEsBean modelEsBean  =hotelModelEsBeans.get(0);
	        	hotelModel.setId(modelEsBean.getId());
	        	
	        	if(StringUtils.isBlank(modelEsBean.getOtaHotelname())){
	        		hotelModel.setHotelname(modelEsBean.getHotelname());
	        	}else{
	        		hotelModel.setHotelname(modelEsBean.getOtaHotelname());
	        	}
	        	if(StringUtils.isBlank(modelEsBean.getOtaDetailaddr())){
	        		hotelModel.setDetailaddr(modelEsBean.getDetailaddr());
	        	}else{
	        		hotelModel.setDetailaddr(modelEsBean.getOtaDetailaddr());
	        	}
	        	if(StringUtils.isBlank(modelEsBean.getOtaHotelphone())){
	        		hotelModel.setHotelphone(modelEsBean.getHotelphone());
	        	}else{
	        		hotelModel.setHotelphone(modelEsBean.getOtaHotelphone());
	        	}
	        	
	        	hotelModel.setCityname(GlobalCache.getInstance().getOatCityMap().get(ChannelEnum.QUNAR.getId()+"_"+modelEsBean.getCitycode()));
				if(StringUtils.isBlank(hotelModel.getCityname())){
					logger.info("缓存没有该城市信息,"+new Gson().toJson(hotelModel));
					hotelModel = null;
				}else{
					CityMapping cityMapping = iCityMappingService.queryCityMappingByCityCodeAndChannelId(ChannelEnum.QUNAR.getId(),modelEsBean.getCitycode()+"");
					if(cityMapping==null){
						logger.info("数据库没有该城市信息,"+new Gson().toJson(hotelModel));
						hotelModel = null;
					}else{
						hotelModel.setCityname(cityMapping.getOtacode());
					}
				}
	        }else{
	        	hotelModel = null;
	        }
//			hotelModel = hotelService.queryById(hotelId);
		} catch (Exception e) {
			logger.error("查询酒店失败",e);
			retInfo.setResult(false);
			retInfo.setMsg("查询酒店失败");
			return retInfo;
		}
		if(hotelModel==null){
			logger.info("查询酒店为空");
			retInfo.setResult(false);
			retInfo.setMsg("查询酒店为空");
			return retInfo;
		}
		int timespace = 0;
		try {
			 timespace = DateUtil.daysBetween(begintime, endtime);
			 logger.info("时间间隔天数:{}",timespace);
		} catch (Exception e) {
			retInfo.setResult(false);
			retInfo.setMsg("时间间隔计算错误");
			return retInfo;
		}
		//所有房型
		List<RoomtypeModel> roomtypeList = roomtypeService.queryByHotelId(hotelId);
		//将所有房型存放在list中
		List<Long> roomtypelist = new ArrayList<Long>();
		if (CollectionUtils.isEmpty(roomtypeList)) {
			logger.info("酒店id：{},查询房型结果为空",hotelId);
			retInfo.setResult(false);
			retInfo.setMsg("查询房型结果为空");
			return retInfo;
		}
		for (RoomtypeModel rm : roomtypeList) {
			roomtypelist.add(rm.getId());
		}
		//获取渠道价
		Map<Long, Map<String, String>> priceMap = null;
		try {
			priceMap = iPriceService.queryChannelPricesFromRedis(otatype, hotelId, begintime, endtime,roomtypeId);
		} catch (Exception e) {
			logger.error("获取酒店房型渠道价错误",e);
			retInfo.setResult(false);
			retInfo.setMsg("获取酒店房型渠道价错误");
			return retInfo;
		}
		
		List<HotelOfferRoom> rooms = new ArrayList<HotelOfferRoom>();
		List<RoomtypeModel> roomtypeModels = new ArrayList<RoomtypeModel>();
		
        if (priceMap.isEmpty()) {
			logger.info("获取渠道价为空");
		}
        List<RoomtypeModel> roomtypes = roomtypeService.queryByIds(roomtypelist);
        if (CollectionUtils.isNotEmpty(roomtypes)) {
			for (RoomtypeModel rm : roomtypes) {
				roomtypeModels.add(rm);
			}
		}
        //获取房型床的类型
        List<RoomtypeinfoModel> roomtypeinfos = roomtypeInfoService.findRoomtypeinfoByRoomtypeIds(roomtypelist);
        Map<Long, RoomtypeinfoModel> roomtypeinfoMap = new HashMap<Long, RoomtypeinfoModel>();
        for (RoomtypeinfoModel rti : roomtypeinfos) {
        	roomtypeinfoMap.put(rti.getRoomtypeid(), rti);
		}
        //从房型映射表中获取渠道映射的房型名字
        List<RoomtypeMapping> roomtypeMappings = iRoomtypeMappingService.queryByRoomtypeids(roomtypelist, qunar_channelid);
        Map<Long, RoomtypeMapping> roomtypeMappingMap =  new HashMap<Long, RoomtypeMapping>();
		if (CollectionUtils.isNotEmpty(roomtypeMappings)) {
			for (RoomtypeMapping rm : roomtypeMappings) {
				roomtypeMappingMap.put(rm.getRoomtypeid(), rm);
			}
		}else {
			logger.info("查询的房型映射为空");
		}
        
        //获取房型有效房量
    	com.fangbaba.stock.face.base.RetInfo<RoomTypeStock> roomtypestock = iStockService.selectByDateAndRoomtype(roomtypelist, otatype, begintime, endtime,true);
  		if (!roomtypestock.isResult()) {
  			logger.info("获取房型有效房量:{}",roomtypestock.getMsg());
  			roomtypestock = new com.fangbaba.stock.face.base.RetInfo<RoomTypeStock>();
  		}
  	    //将获取的房量信息发在map中方便取
  		List<RoomTypeStock> roomtypestocklist = roomtypestock.getList();
  		Map<Long, RoomTypeStock> roomTypeStockMap =  new HashMap<Long, RoomTypeStock>();
		if (CollectionUtils.isNotEmpty(roomtypestocklist)) {
			for (RoomTypeStock rs : roomtypestocklist) {
				roomTypeStockMap.put(rs.getRoomtypeid(), rs);
			}
		}else {
			logger.info("未查到可用的房型房量信息");
		}
		Boolean flag = iOtaHotelFullFlagService.queryFullFlag(otatype, hotelModel.getId());
		for (RoomtypeModel roomtypeModel : roomtypeModels) {
			 HotelOfferRoom hotelOfferRoom = new HotelOfferRoom();
			 RoomtypeMapping roomtype_Mapping = roomtypeMappingMap.get(roomtypeModel.getId());
			 //查询床型
			 Integer bed = null;
			 if (roomtype_Mapping!=null&&roomtype_Mapping.getBedtype()!=null) {
				 RoomTypeQunarBedEnum bedEnum = RoomTypeQunarBedEnum.getByFangbabaId(roomtype_Mapping.getBedtype().intValue());
				 if(bedEnum  == null){
					 bed = RoomTypeQunarBedEnum.unknown.getId();
				 }else{
					 bed = bedEnum.getId();
				 }
				 
			 }else{
				 RoomtypeinfoModel roomtypeInfo = roomtypeinfoMap.get(roomtypeModel.getId());
				 if(roomtypeInfo==null){
					 logger.info("房型id:{},对应的roomtypeinfo表中的bedtype为:{}",roomtypeModel.getId(),null);
					 bed = RoomTypeQunarBedEnum.unknown.getId();
				 }else{
					 logger.info("房型id:{},对应的roomtypeinfo表中的bedtype为:{}",roomtypeModel.getId(),roomtypeInfo.getBedtype());
					 if(roomtypeInfo.getBedtype()==null){
						 bed = RoomTypeQunarBedEnum.unknown.getId();
					 }else{
						 RoomTypeQunarBedEnum bedEnum = RoomTypeQunarBedEnum.getByFangbabaId(Integer.valueOf(roomtypeInfo.getBedtype()+""));
						 if(bedEnum  == null){
							 bed = RoomTypeQunarBedEnum.unknown.getId();
						 }else{
							 bed = bedEnum.getId();
						 }
					 }
				 }
			 }
			 
			 hotelOfferRoom.setBed(bed);
			 hotelOfferRoom.setBroadband(RoomTypeQunarBroadbandEnum.free.getId());
			 hotelOfferRoom.setPrepay(RoomTypeQunarPayEnum.prePay.getId());
			// hotelOfferRoom.setAdvance("0");
			 hotelOfferRoom.setChannel(qunar_roomdechannelid);
			 //获取房型基本属性
			 hotelOfferRoom.setId(roomtypeModel.getId());
			 if (roomtype_Mapping!=null) {
				 if (!Strings.isNullOrEmpty(roomtype_Mapping.getOtaRoomtypename())) {
				     hotelOfferRoom.setName(roomtype_Mapping.getOtaRoomtypename());
				}else{
					 hotelOfferRoom.setName(roomtypeModel.getName());
				}
			 }else{
				 hotelOfferRoom.setName(roomtypeModel.getName());
			 }
			 
			 if(flag){//TODO:暂时写死
				 hotelOfferRoom.setRefusestate("1");
			 }
			 StringBuffer channelpricestr = new StringBuffer("");//渠道价
			 StringBuffer breakfaststr = new StringBuffer("");//早餐
			 StringBuffer roomStatusStr = new StringBuffer("");//房型状态
			 StringBuffer roomtypecountstr = new StringBuffer("");//房型下有效的房间数
			 boolean channelpriceflag = false;
			 boolean roomStatusflag = false;
			  //设置房量,将该房型key：日期，value:房量放进map中
			  Map<String, Integer> roomNumMap = new HashMap<String, Integer>();
			  if (roomTypeStockMap.get(roomtypeModel.getId())!=null) {
				  RoomTypeStock rs =  roomTypeStockMap.get(roomtypeModel.getId());
				  List<RoomInfo>  roomInfoList =  rs.getRoomInfo();
				  if (CollectionUtils.isNotEmpty(roomInfoList)) {
					  for (RoomInfo roomInfo : roomInfoList) {
						  roomNumMap.put(roomInfo.getDate().replace("-", ""),roomInfo.getNumber());
					}
				}
				  
			  }else {
				logger.info("根据房型id:{}并未在map查到对象",roomtypeModel.getId());
			  }
			 
			 //获取该房型多天的渠道价
			  if (priceMap.get(roomtypeModel.getId())!=null) {
				  channelpriceflag = true;
				  roomStatusflag = true;
				  Map<String, String> channelPricesMap =  priceMap.get(roomtypeModel.getId());
				  for (int i = 0; i < timespace; i++) {
						 //设置渠道价
						 String timestr = DateUtil.dateToStr(DateUtil.addDateOneDay(begintime, i), "yyyyMMdd");
						 if (channelPricesMap.get(timestr)!=null) {
							 JSONObject jsonObject = JSON.parseObject(channelPricesMap.get(timestr));
							 String channelprice = jsonObject.getString("channelprice");
							 channelpricestr.append("|"+channelprice);
							 Integer roomNum = roomNumMap.get(timestr);
							 logger.debug("房型:{},时间:{},渠道价:{},库存:{}",roomtypeModel.getId(),timestr,channelprice,roomNum);
							 if (roomNumMap.get(timestr)==null||roomNumMap.get(timestr)>0) {
								 roomStatusStr.append("|"+RoomTypeQunarStatusEnum.yes.getId());
							 }else {
								 roomStatusStr.append("|"+RoomTypeQunarStatusEnum.no.getId());
							 }
							
						 }else {
							 logger.debug("房型id:{}并未在map查到该房型的时间:{}渠道价",roomtypeModel.getId(),timestr);
							 channelpricestr.append("|0");
							 roomStatusStr.append("|"+RoomTypeQunarStatusEnum.no.getId());
						 }
					 }
			  }else {
				  logger.debug("根据房型id:{}并未在map查到该房型的渠道价",roomtypeModel.getId());
			}
			  
		  for (int i = 0; i < timespace; i++) {
				 if (!channelpriceflag) {
					 channelpricestr.append("|0");
				 }
				 if (!roomStatusflag) {
					 roomStatusStr.append("|"+RoomTypeQunarStatusEnum.no.getId());
				 }
				 
				 //设置房型下有效房间数
				 /*if (roomNumMap.containsKey(timestr)) {
					 //??????roomtypecountstr.append("|"+roomNumMap.get(timestr));
					 roomtypecountstr.append("|0");
				 }else {
					 roomtypecountstr.append("|0");
				 }*/
				 roomtypecountstr.append("|0");
				 //设置早餐
				 breakfaststr.append("|"+RoomTypeQunarBreakfastEnum.no.getId());
			 }
		     //设置渠道价
			 hotelOfferRoom.setPrices(channelpricestr.substring(1));
			 //设置早餐
			 hotelOfferRoom.setBreakfast(breakfaststr.substring(1));
			 //设置房型状态
			 hotelOfferRoom.setStatus(roomStatusStr.substring(1));
			 //设置房量
			 hotelOfferRoom.setCounts(roomtypecountstr.substring(1));
			 rooms.add(hotelOfferRoom);
		}
		//设置酒店信息
		HotelOfferPrice hotelOfferPrice = new HotelOfferPrice();
		hotelOfferPrice.setId(hotelModel.getId());
		hotelOfferPrice.setCity(hotelModel.getCityname());
		hotelOfferPrice.setName(hotelModel.getHotelname());
		//hotelOfferPrice.setPromotion("");
		hotelOfferPrice.setTel(hotelModel.getHotelphone());
		hotelOfferPrice.setAddress(hotelModel.getDetailaddr());
		hotelOfferPrice.setRooms(rooms);
		
		retInfo.setObj(hotelOfferPrice);
		retInfo.setResult(true);
		return retInfo;
	}


}
