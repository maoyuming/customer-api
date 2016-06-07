package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.core.RedisCacheManager;
import com.fangbaba.api.domain.fangcang.Response;
import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingAddRoomTypeRequest;
import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingDeleteRoomTypeRequest;
import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingRoomtypeRequest;
import com.fangbaba.api.domain.fangcang.hotelmapping.RoomType;
import com.fangbaba.api.domain.fangcang.hotelmapping.RoomTypeMappingDeleteRequest;
import com.fangbaba.api.domain.fangcang.hotelmapping.SpRoomtype;
import com.fangbaba.api.enums.FangCangRequestTypeEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.face.bean.RoomTypeMappingBean;
import com.fangbaba.api.service.RoomtypeMappingService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.api.util.Config;
import com.fangbaba.api.util.businesslog.BusinessLogUtil;
import com.fangbaba.api.util.businesslog.BussinssTypeEnum;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.enums.HotelMappingStateEnum;
import com.fangbaba.gds.face.base.RetInfo;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.fangbaba.gds.face.service.IRoomtypeMappingService;
import com.fangbaba.gds.po.RoomtypeMapping;
import com.google.gson.Gson;

@Service
public class RoomtypeMappingServiceImpl implements RoomtypeMappingService{
 
	@Autowired
	private BusinessUtil<Response> businessUtil;
	@Autowired
	private IRoomtypeMappingService iRoomtypeMappingService;
	@Autowired
	private IHotelSearchService iHotelSearchService;
	

    @Autowired
    private BusinessLogUtil businessLogUtil;

    @Autowired
    private RedisCacheManager redisCacheManager;
	
	private static Logger logger = LoggerFactory.getLogger(RoomtypeMappingServiceImpl.class);
	@Override
	public boolean addRoomTypeMappingBatch(List<HotelMappingRoomtypeRequest> hotelMappingRoomtypeRequests) {
		if(CollectionUtils.isNotEmpty(hotelMappingRoomtypeRequests)){
			for (HotelMappingRoomtypeRequest roomtypeRequest : hotelMappingRoomtypeRequests) {
				this.addRoomTypeMapping(roomtypeRequest);
			}
			
			return true;
		}else{
			logger.info("批量新增房型映射集合为空");
		}
		return false;
	}
	@Override
	public boolean deleteRoomTypeMapping(RoomTypeMappingBean roomtypemapping) {
		if (roomtypemapping==null) {
			logger.info("要删除房型映射对象为空");
			return false;
		}
		if (roomtypemapping.getSpHotelId()==null) {
			logger.info("要删除房型映射对象的酒店id为空");
			return false;
		}
		if (CollectionUtils.isEmpty(roomtypemapping.getRoomTypeList())) {
			logger.info("要删除房型映射对象为空");
			return false;
	    }
		HotelMappingDeleteRoomTypeRequest htotelMappingDeleteRoomTypeRequest = new HotelMappingDeleteRoomTypeRequest();
		
		RoomTypeMappingDeleteRequest roomTypeMappingDeleteRequest = new RoomTypeMappingDeleteRequest();
		roomTypeMappingDeleteRequest.setSpHotelId(roomtypemapping.getSpHotelId());
		
		List<String> SpRoomtypeidlist = new ArrayList<String>();
		 SpRoomtype sprt = new SpRoomtype();
		for (com.fangbaba.api.face.bean.SpRoomtype st : roomtypemapping.getRoomTypeList()) {
		//	SpRoomtype spRoomtype = new SpRoomtype();
			//spRoomtype.setSpRoomTypeId(st.getSpRoomTypeId());
			SpRoomtypeidlist.add(st.getSpRoomTypeId()+"");
		}
		sprt.setSpRoomTypeId(SpRoomtypeidlist);
		roomTypeMappingDeleteRequest.setSpRoomTypeId(sprt);
		htotelMappingDeleteRoomTypeRequest.setRoomTypeMappingDeleteRequest(roomTypeMappingDeleteRequest);
		String responseXml = "";
		try {
			 Map<String, String> param  = businessUtil.genFangCangRequest(htotelMappingDeleteRoomTypeRequest, FangCangRequestTypeEnum.DeleteRoomTypeMapping);
			 responseXml = businessUtil.doPost(GdsChannelUrlEnum.DeleteRoomTypeMapping.getId(), param, ChannelEnum.fangcang.getId());
			 logger.info("调用删除房型映射返回值:{}",responseXml);
		} catch (Exception e) {
		    logger.error("调用删除房型映射接口失败:",e);
	    }
		Response response =businessUtil.decodeResponseXml(responseXml,Response.class);
		if (response==null) {
			saveLog("删除映射的房型失败，返回null", roomtypemapping, BussinssTypeEnum.deleteRoomtypeMapping);
			throw new OpenException("删除映射的房型失败，返回null");
		}else if(!response.isSuccess()){
			saveLog(response.getResultMsg(), roomtypemapping, BussinssTypeEnum.deleteRoomtypeMapping);
			throw new OpenException("删除映射的房型失败，返回"+new Gson().toJson(response));
		}else{
			saveLog("保存房型成功", roomtypemapping, BussinssTypeEnum.deleteRoomtypeMapping);
		}
		return response.isSuccess();
	}
	
	/**
	 * 执行新增本地房型映射和房仓的房型映射
	 */
	@Override
	public boolean doAddRoomtypeMapping(
			List<RoomtypeMapping> roomtypeMappings) {

		Map<Integer, List<RoomtypeMapping>> map = new HashMap<Integer, List<RoomtypeMapping>>();
		//梳理下不同渠道的酒店mapping
		
		if(CollectionUtils.isNotEmpty(roomtypeMappings)){
			String cacheName = "roomtypeMappingAdd";
			map = queryCategoryMap(roomtypeMappings, cacheName);
			if(MapUtils.isNotEmpty(map)){
				for (Entry<Integer, List<RoomtypeMapping>> entry : map.entrySet()) {
					this.doAddRoomtypeMapping(entry.getValue(),entry.getKey());
				}
			}
		}
		return true;
	
	}
	
	 /**
     * 查询开通当前渠道分销的酒店，根据渠道分类
     * @param hotelMappings
     * @param cacheName
     * @return
     */
    private Map<Integer, List<RoomtypeMapping>> queryCategoryMap(List<RoomtypeMapping> roomtypeMappings ,String cacheName){
    	Map<Integer, List<RoomtypeMapping>> map = new HashMap<Integer, List<RoomtypeMapping>>();
    	for (RoomtypeMapping roomtypeMapping : roomtypeMappings) {
			
			//check token
			String key = roomtypeMapping.getChannelid()+"_"+roomtypeMapping.getHotelid()+"_"+roomtypeMapping.getRoomtypeid();
			Object obj = redisCacheManager.getExpires(cacheName,key );
			if(obj!=null){
				logger.error("重复的mapping数据，{}",new Gson().toJson(roomtypeMapping));
				continue;
			}else{
				if(map.containsKey(roomtypeMapping.getChannelid())){
					map.get(roomtypeMapping.getChannelid()).add(roomtypeMapping);
				}else{
					List<RoomtypeMapping> hotelMappingnew = new ArrayList<RoomtypeMapping>();
					hotelMappingnew.add(roomtypeMapping);
					map.put(roomtypeMapping.getChannelid(), hotelMappingnew);
				}
				redisCacheManager.setExpires(cacheName, key, 1, 10);
			}
			
		}
    	return map;
    }
	
	/**
	 * 执行删除本地房型映射和房仓房型映射
	 */
	@Override
	public boolean doDeleteRoomtypeMapping(
			List<RoomtypeMapping> roomtypeMappings) {


		Map<Integer, List<RoomtypeMapping>> map = new HashMap<Integer, List<RoomtypeMapping>>();
		//梳理下不同渠道的酒店mapping
		
		if(CollectionUtils.isNotEmpty(roomtypeMappings)){
			String cacheName = "roomtypeMappingDelete";
			map = queryCategoryMap(roomtypeMappings, cacheName);
			if(MapUtils.isNotEmpty(map)){
				for (Entry<Integer, List<RoomtypeMapping>> entry : map.entrySet()) {
					try {
						this.doDeleteRoomtypeMapping(entry.getValue(),entry.getKey());
					} catch (Exception e) {
						logger.error("删除房型映射异常",e);
					}
				}
			}
		}
		return true;
	
	}
	@Override
	public boolean deleteRoomTypeMappingBatch(List<RoomTypeMappingBean> roomTypeMappingBean) {
		
		if(CollectionUtils.isEmpty(roomTypeMappingBean)){
			logger.info("批量删除房型映射集合为空");
			return false;
		}
		try {
			for (RoomTypeMappingBean rmb : roomTypeMappingBean) {
				this.deleteRoomTypeMapping(rmb);
			}
		} catch (Exception e) {
			logger.error("批量删除房型映射失败",e);
			return false;
		}
		
		logger.info("批量删除房型映射结束");
		return true;
	}

	/**
	 * 封装RoomTypeMappingBean集合
	 * @param roomtypeMappingMap
	 * @return
	 */
	private List<RoomTypeMappingBean> fillRoomTypeMappingBeanList(Map<Long, List<RoomtypeMapping>> roomtypeMappingMap){
		List<RoomTypeMappingBean> beans = new ArrayList<RoomTypeMappingBean>();
		for (Map.Entry<Long, List<RoomtypeMapping>> entry : roomtypeMappingMap.entrySet()) {
			Long hotelid = entry.getKey();
			
			RoomTypeMappingBean roomTypeMappingBean = new RoomTypeMappingBean();
			roomTypeMappingBean.setSpHotelId(hotelid+"");
			
			List<com.fangbaba.api.face.bean.SpRoomtype> spRoomtypes = new ArrayList<com.fangbaba.api.face.bean.SpRoomtype>();
			for (RoomtypeMapping roomTypeMapping : entry.getValue()) {
				com.fangbaba.api.face.bean.SpRoomtype spRoomtype = new com.fangbaba.api.face.bean.SpRoomtype();
				spRoomtype.setSpRoomTypeId(roomTypeMapping.getRoomtypeid()+"");
				spRoomtypes.add(spRoomtype);
			}
			roomTypeMappingBean.setRoomTypeList(spRoomtypes);
			beans.add(roomTypeMappingBean);
		}
		return beans;
	}
	/**
	 * 封装RoomTypeMappingBean集合
	 * @param roomtypeMappingMap
	 * @return
	 */
	private List<HotelMappingRoomtypeRequest> fillRoomTypeMappingRequestList(Map<String, List<RoomtypeMapping>> roomtypeMappingMap){
		List<HotelMappingRoomtypeRequest> beans = new ArrayList<HotelMappingRoomtypeRequest>();
		for (Map.Entry<String, List<RoomtypeMapping>> entry : roomtypeMappingMap.entrySet()) {
			String allhotelid = entry.getKey();
			String[] array = allhotelid.split("_");
			String sphotelid =array[0];
			String fchotelid =array[1];
			
			HotelMappingRoomtypeRequest roomTypeMappingBean = new HotelMappingRoomtypeRequest();
			roomTypeMappingBean.setSpHotelId(sphotelid);
			roomTypeMappingBean.setFcHotelId(fchotelid);
			
			List<RoomType> spRoomtypes = new ArrayList<RoomType>();
			for (RoomtypeMapping roomTypeMapping : entry.getValue()) {
				RoomType spRoomtype = new RoomType();
				spRoomtype.setSpRoomTypeId(roomTypeMapping.getRoomtypeid()+"");
				spRoomtype.setSpRoomTypeName(roomTypeMapping.getRoomtypename());
				spRoomtype.setFcRoomTypeId(Long.valueOf(roomTypeMapping.getOtaRoomtypeid()));
				spRoomtype.setFcRoomTypeName(roomTypeMapping.getOtaRoomtypename());
				spRoomtypes.add(spRoomtype);
			}
			roomTypeMappingBean.setRoomTypeList(spRoomtypes);
			beans.add(roomTypeMappingBean);
		}
		return beans;
	}
	@Override
	public boolean addRoomTypeMapping(HotelMappingRoomtypeRequest roomtypeRequest) {
		
		if (roomtypeRequest == null) {
			logger.info("添加房型映射接口参数为空");
			return false;
		}
		
		if (roomtypeRequest.getFcHotelId() == null) {
			logger.info("房仓酒店id为空");
			return false;
		}
		if (roomtypeRequest.getSpHotelId() == null) {
			logger.info("供应商酒店id为空");
			return false;
		}
		if (CollectionUtils.isEmpty(roomtypeRequest.getRoomTypeList())) {
			logger.info("添加映射房型集合为空");
			return false;
		}
		
        HotelMappingAddRoomTypeRequest hotelMappingAddRoomTypeRequest = new HotelMappingAddRoomTypeRequest();
		
		hotelMappingAddRoomTypeRequest.setHotelMappingRoomtypeRequest(roomtypeRequest);
		String responseXml = "";
		try {
			 Map<String, String> param  = businessUtil.genFangCangRequest(hotelMappingAddRoomTypeRequest, FangCangRequestTypeEnum.AddRoomTypeMapping);
			 responseXml = businessUtil.doPost(GdsChannelUrlEnum.AddRoomTypeMapping.getId(), param, ChannelEnum.fangcang.getId());
			 logger.info("添加房型映射接口返回值:{}",responseXml);
		} catch (Exception e) {
		    logger.error("添加房型映射接口失败:",e);
	     }
		Response response =businessUtil.decodeResponseXml(responseXml,Response.class);
		if (response==null) {
			saveLog("新增映射的房型失败，返回null", roomtypeRequest, BussinssTypeEnum.addRoomtypeMapping);
			logger.info("新增映射的房型失败，返回null");
			throw new OpenException("新增映射的房型失败，返回null");
		}else if(!response.isSuccess()){
			saveLog(response.getResultMsg(), roomtypeRequest, BussinssTypeEnum.addRoomtypeMapping);
			logger.info("新增映射的房型失败，返回{}",new Gson().toJson(response));
			throw new OpenException("新增映射的房型失败，返回"+new Gson().toJson(response));
		}else{
			saveLog("新增映射的房型成功", roomtypeRequest, BussinssTypeEnum.addRoomtypeMapping);
		}
		return response.isSuccess();
		
		
		
	}
	@Override
	public boolean doAddRoomtypeMapping(List<RoomtypeMapping> roomtypeMappings,
			Integer channelId) {

        boolean flag = false;
        if(channelId==null){
			logger.info("渠道id为空");
			throw new OpenException("渠道id为空");
		}
		if(CollectionUtils.isNotEmpty(roomtypeMappings)){
			Map<String, List<RoomtypeMapping>> roomtypeMappingMap = new HashMap<String, List<RoomtypeMapping>>();
			
			List<RoomtypeMapping> hotelMappingNew = new ArrayList<RoomtypeMapping>();
			for (RoomtypeMapping hotelMapping : roomtypeMappings) {
				if(StringUtils.isBlank(hotelMapping.getOtaHotelid())){
					logger.info("getOtaHotelid  为空");
					continue;
				}
				if(StringUtils.isBlank(hotelMapping.getOtaRoomtypeid())){
					logger.info("getOtaRoomtypeid  为空");
					continue;
				}
				if(StringUtils.isBlank(hotelMapping.getOtaRoomtypename())){
					logger.info("getOtaRoomtypename  为空");
					continue;
				}
				if(StringUtils.isBlank(hotelMapping.getRoomtypename())){
					logger.info("getRoomtypename  为空");
					continue;
				}
				if(StringUtils.isBlank(hotelMapping.getRoomtypename())){
					logger.info("getRoomtypename  为空");
					continue;
				}
				
				
				if(queryHotelById(hotelMapping.getHotelid(), hotelMapping.getChannelid()).isResult()){
					
					hotelMapping.setState(HotelMappingStateEnum.effect.getId());
				}else{
					hotelMapping.setState(HotelMappingStateEnum.notMapping.getId());
				}
				
				String key = hotelMapping.getHotelid()+"_"+hotelMapping.getOtaHotelid();
				
				if(!roomtypeMappingMap.containsKey(key)){
					List<RoomtypeMapping> hotelMappingList = new ArrayList<RoomtypeMapping>();
					hotelMappingList.add(hotelMapping);
					roomtypeMappingMap.put(key, hotelMappingList);
				}else{
					roomtypeMappingMap.get(key).add(hotelMapping);
				}
				
				hotelMappingNew.add(hotelMapping);
			}
			if(CollectionUtils.isEmpty(hotelMappingNew)){
				logger.info("待新增房型为空");
				throw new OpenException("待添加映射房型为空，参数可能有问题");
			}
			
			
			//save all to db
			iRoomtypeMappingService.saveRoomtypeMappingBatch(hotelMappingNew);
			logger.info("推送房型映射到数据库完成");
			if(channelId!=null){
				ChannelEnum channelEnum = ChannelEnum.getById(channelId);
				switch (channelEnum) {
				case fangcang:
					try {
						pushAddMappingToFangCang(roomtypeMappingMap, roomtypeMappings);
						flag= true;
					} catch (OpenException e) {//失败回滚
						
						//失败重新处理
						for (RoomtypeMapping hotelMapping : hotelMappingNew) {
							if(HotelMappingStateEnum.effect.getId() == hotelMapping.getState()){
								hotelMapping.setState(HotelMappingStateEnum.deployFail.getId());
							}
						}
						iRoomtypeMappingService.updateRoomtypeMappingBatch(hotelMappingNew);
						flag= false;
						throw e;
					}
					break;
					
				default:
					break;
				}
			}
		}
		return flag;
	
	}
	
	
	/**
	 * 推送新增mapping到第三方
	 * @param hotelMappingPush
	 * @param channelId
	 * @return
	 */
	public boolean pushAddMappingToFangCang(Map<String, List<RoomtypeMapping>> roomtypeMappingMap,List<RoomtypeMapping> roomtypeMappings){
		
		boolean flag =false;
		//check is push?
		List<RoomtypeMapping> hotelMappingList =  new ArrayList<RoomtypeMapping>();
		
		

		List<HotelMappingRoomtypeRequest> beanList = null;
		List<HotelMappingRoomtypeRequest> beans = fillRoomTypeMappingRequestList(roomtypeMappingMap);
		
		//10个一组，调用房仓的同步接口
		if(beans.size()<=Integer.valueOf(Config.getValue("fangcang.listsize"))){
			beanList = beans;
			this.addRoomTypeMappingBatch(beanList);
		}else{
			int interval = Integer.valueOf(Config.getValue("fangcang.listsize"));
			int begin=0;
			
			while(true){

				int end = begin+interval;
				if(end>roomtypeMappings.size()){
					end = roomtypeMappings.size();
				}
				hotelMappingList =  new ArrayList<RoomtypeMapping>();
				hotelMappingList = roomtypeMappings.subList(begin, end);
				if(CollectionUtils.isEmpty(hotelMappingList)){
					break;
				}
				this.addRoomTypeMappingBatch(beanList);
				if(end<=roomtypeMappings.size()){
					begin = end;
				}
			
			}
		}
		return flag;
	}
	@Override
	public boolean doDeleteRoomtypeMapping(
			List<RoomtypeMapping> roomtypeMappings, Integer channelId) {

		boolean flag =false;
		if(channelId==null){
			logger.info("渠道id为空");
			throw new OpenException("渠道id为空");
		}

		if(CollectionUtils.isNotEmpty(roomtypeMappings)){
			Map<Long, List<RoomtypeMapping>> roomtypeMappingMap = new HashMap<Long, List<RoomtypeMapping>>();
			
			List<RoomtypeMapping> hotelMappingNew = new ArrayList<RoomtypeMapping>();
			for (RoomtypeMapping hotelMapping : roomtypeMappings) {
				if(StringUtils.isBlank(hotelMapping.getOtaHotelid())){
					logger.info("getOtaHotelid  为空");
					continue;
				}
				if(StringUtils.isBlank(hotelMapping.getOtaRoomtypeid())){
					logger.info("getOtaRoomtypeid  为空");
					continue;
				}
				if(!roomtypeMappingMap.containsKey(hotelMapping.getHotelid())){
					List<RoomtypeMapping> hotelMappingList = new ArrayList<RoomtypeMapping>();
					hotelMappingList.add(hotelMapping);
					roomtypeMappingMap.put(hotelMapping.getHotelid(), hotelMappingList);
				}else{
					roomtypeMappingMap.get(hotelMapping.getHotelid()).add(hotelMapping);
				}
				
				hotelMappingNew.add(hotelMapping);
			}
			if(CollectionUtils.isEmpty(hotelMappingNew)){
				logger.info("待删除房型为空");
				throw new OpenException("待删除映射房型为空，参数可能有问题");
			}
			
			
			//save all to db
			roomtypeMappings = iRoomtypeMappingService.deleteRoomtypeMappingBatch(hotelMappingNew);
			
			if(channelId!=null){
				ChannelEnum channelEnum = ChannelEnum.getById(channelId);
				switch (channelEnum) {
				case fangcang:
					try {
						pushDeleteMappingToFangCang(roomtypeMappingMap);
						flag= true;
					} catch (OpenException e) {
						flag= false;
						throw e;
					}
					break;
					
				default:
					break;
				}
			}
			
			
		}else{
			logger.info("批量里删除房型入参为空");
			return false;
		}
		return flag;
	
	
	}
	
	
	public void pushDeleteMappingToFangCang(Map<Long, List<RoomtypeMapping>> roomtypeMappingMap){
		
		List<RoomTypeMappingBean> beanList = null;
		List<RoomTypeMappingBean> beans = fillRoomTypeMappingBeanList(roomtypeMappingMap);
		//10个一组，调用房仓的同步接口
		if(beans.size()<=Integer.valueOf(Config.getValue("fangcang.listsize"))){
			beanList = beans;
			
		    this.deleteRoomTypeMappingBatch(beanList); 
			
		}else{
			int interval = Integer.valueOf(Config.getValue("fangcang.listsize"));
			int begin=0;
			
			while(true){

				int end = begin+interval;
				if(end>beans.size()){
					end = beans.size();
				}
				beanList =  new ArrayList<RoomTypeMappingBean>();
				beanList = beans.subList(begin, end);
				if(CollectionUtils.isEmpty(beanList)){
					break;
				}
				
					
				this.deleteRoomTypeMappingBatch(beanList); 
				
				if(end<=beans.size()){
					begin = end;
				}
			
			}
		}
	
	}
	
	/**
	 * 根据酒店id查询酒店信息
	 * @param hotelId
	 * @param channelId
	 * @return
	 */
	private RetInfo<HotelModelEsBean> queryHotelById(Long hotelId,Integer channelId){
		HotelModelEsBean hotelModelEsBean = new HotelModelEsBean();
		hotelModelEsBean.setId(hotelId);
		hotelModelEsBean.setChannelId(channelId);
		RetInfo<HotelModelEsBean> retInfo = iHotelSearchService.queryHotelByHotelId(hotelModelEsBean);
		return retInfo;
	}
	@Override
	public boolean doUpdateRoomtypeMapping(
			List<RoomtypeMapping> roomtypeMappings) {


		Map<Integer, List<RoomtypeMapping>> map = new HashMap<Integer, List<RoomtypeMapping>>();
		//梳理下不同渠道的酒店mapping
		
		if(CollectionUtils.isNotEmpty(roomtypeMappings)){
			String cacheName = "roomtypeMappingUpdate";
			map = queryCategoryMap(roomtypeMappings, cacheName);
			if(MapUtils.isNotEmpty(map)){
				try {
					this.pushDeleteMappingToFangCang(fillRoomtypeMappingMap(roomtypeMappings));
				} catch (Exception e) {
					logger.error("调用房仓酒店映射异常",e);
				}
				for (Entry<Integer, List<RoomtypeMapping>> entry : map.entrySet()) {
					
					try {
						this.doAddRoomtypeMapping(entry.getValue(),entry.getKey());
					} catch (Exception e) {
						logger.error("调用房仓酒店映射异常",e);
					}
				}
			}
		}
		return true;
	
	
	}
	
	public Map<Long, List<RoomtypeMapping>> fillRoomtypeMappingMap(List<RoomtypeMapping> roomtypeMappings){
		Map<Long, List<RoomtypeMapping>> roomtypeMappingMap = new HashMap<Long, List<RoomtypeMapping>>();
		
		List<RoomtypeMapping> hotelMappingNew = new ArrayList<RoomtypeMapping>();
		for (RoomtypeMapping hotelMapping : roomtypeMappings) {
			if(StringUtils.isBlank(hotelMapping.getOtaHotelid())){
				logger.info("getOtaHotelid  为空");
				continue;
			}
			if(StringUtils.isBlank(hotelMapping.getOtaRoomtypeid())){
				logger.info("getOtaRoomtypeid  为空");
				continue;
			}
			if(!roomtypeMappingMap.containsKey(hotelMapping.getHotelid())){
				List<RoomtypeMapping> hotelMappingList = new ArrayList<RoomtypeMapping>();
				hotelMappingList.add(hotelMapping);
				roomtypeMappingMap.put(hotelMapping.getHotelid(), hotelMappingList);
			}else{
				roomtypeMappingMap.get(hotelMapping.getHotelid()).add(hotelMapping);
			}
			
			hotelMappingNew.add(hotelMapping);
		}
		return roomtypeMappingMap;
	}
	
	/**
	 * 保存日志
	 * @param msg
	 * @param hotelmappings
	 */
	private void saveLog(String msg,HotelMappingRoomtypeRequest hotelMappingRoomtypeRequest,BussinssTypeEnum bussinssTypeEnum){

		try {
			if(hotelMappingRoomtypeRequest!=null){
				if(CollectionUtils.isNotEmpty(hotelMappingRoomtypeRequest.getRoomTypeList())){
					for (RoomType roomType : hotelMappingRoomtypeRequest.getRoomTypeList()) {
						StringBuilder sf = new StringBuilder();
						sf.append("房仓酒店id：");
						sf.append(hotelMappingRoomtypeRequest.getFcHotelId());
						sf.append(",");
						sf.append("房仓房型id：");
						sf.append(roomType.getFcRoomTypeId());
						sf.append(",");
						sf.append("房爸爸房型id：");
						sf.append(roomType.getSpRoomTypeId());
						sf.append(",");
						sf.append(msg);
						
						String content = sf.toString();
						businessLogUtil.saveLog(hotelMappingRoomtypeRequest.getSpHotelId(), "system", content, bussinssTypeEnum);
					}
				}
			}
		
		} catch (Exception e) {
			logger.info("保存日志失败");
		}
	
	}
	/**
	 * 保存日志
	 * @param msg
	 * @param hotelmappings
	 */
	private void saveLog(String msg,RoomTypeMappingBean roomTypeMappingBean,BussinssTypeEnum bussinssTypeEnum){
		try {
			if(roomTypeMappingBean!=null){
				if(CollectionUtils.isNotEmpty(roomTypeMappingBean.getRoomTypeList())){
					for (com.fangbaba.api.face.bean.SpRoomtype spRoomtype : roomTypeMappingBean.getRoomTypeList()) {
						StringBuilder sf = new StringBuilder();
						sf.append("房爸爸房型id：");
						sf.append(spRoomtype.getSpRoomTypeId());
						sf.append(",");
						sf.append(msg);
						
						String content = sf.toString();
						businessLogUtil.saveLog(roomTypeMappingBean.getSpHotelId(), "system", content, bussinssTypeEnum);
					}
				}
			}
		
		} catch (Exception e) {
			logger.info("保存日志失败");
		}
	}

}
