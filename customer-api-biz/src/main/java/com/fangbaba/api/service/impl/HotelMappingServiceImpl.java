package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.core.RedisCacheManager;
import com.fangbaba.api.domain.fangcang.Response;
import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingAddRequest;
import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingDeleteRequest;
import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingHotelRequest;
import com.fangbaba.api.domain.fangcang.hotelmapping.HotelMappingInfo;
import com.fangbaba.api.enums.FangCangRequestTypeEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.service.HotelMappingService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.api.util.Config;
import com.fangbaba.api.util.businesslog.BusinessLogUtil;
import com.fangbaba.api.util.businesslog.BussinssTypeEnum;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.enums.HotelMappingStateEnum;
import com.fangbaba.gds.face.base.RetInfo;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.service.IHotelMappingService;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.fangbaba.gds.face.service.IRoomtypeMappingService;
import com.fangbaba.gds.po.HotelMapping;
import com.google.gson.Gson;

@Service
public class HotelMappingServiceImpl implements HotelMappingService{
 
	@Autowired
	private BusinessUtil<Response> businessUtil;
	@Autowired
	private IHotelMappingService iHotelMappingService;
	@Autowired
	private IRoomtypeMappingService iRoomtypeMappingService;
	@Autowired
	private IHotelSearchService iHotelSearchService;

    @Autowired
    private RedisCacheManager redisCacheManager;
	
    @Autowired
    private BusinessLogUtil businessLogUtil;
    
	
	private static final String dbListKey ="dbList";
	private static final String pushListKey ="pushList";
	private static final String channelIdKey ="channelId";
	private static Logger logger = LoggerFactory.getLogger(HotelMappingServiceImpl.class);
	@Override
	public boolean addHotelMapping(List<HotelMapping> hotelmapping) {
		if (CollectionUtils.isEmpty(hotelmapping)) {
			logger.info("新增映射的酒店为空");
			throw new OpenException("新增映射的酒店列表为空，可能没开通分销");
		}
		if (hotelmapping.size()>Integer.valueOf(Config.getValue("fangcang.listsize"))) {
			logger.info("新增映射的酒店集合长度大于10");
			throw new OpenException("新增映射的酒店集合长度大于10");
		}
		HotelMappingAddRequest hotelMappingRequest = new HotelMappingAddRequest();
		
		HotelMappingHotelRequest addHotelMappingRequest = new HotelMappingHotelRequest();
	    List<HotelMappingInfo> hotelMappingList = new ArrayList<HotelMappingInfo>();
		for (HotelMapping hotelMappingInfoRequest : hotelmapping) {
			logger.info("添加酒店映射对象:"+ToStringBuilder.reflectionToString(hotelMappingInfoRequest));
			
			
			if(StringUtils.isBlank(hotelMappingInfoRequest.getOtaHotelid())){
				logger.info("getOtaHotelid  为空");
				continue;
			}
			if(StringUtils.isBlank(hotelMappingInfoRequest.getHotelname())){
				logger.info("getHotelname  为空");
				continue;
			}
			if(hotelMappingInfoRequest.getHotelid()==null){
				logger.info("getHotelid  为空");
				continue;
			}
			HotelMappingInfo hotelMappingInfo = new HotelMappingInfo();
			hotelMappingInfo.setFcHotelId(Long.parseLong(hotelMappingInfoRequest.getOtaHotelid()));
			hotelMappingInfo.setFcHotelName(hotelMappingInfoRequest.getHotelname());
			hotelMappingInfo.setSpHotelId(hotelMappingInfoRequest.getHotelid()+"");
			hotelMappingList.add(hotelMappingInfo);
		}
		addHotelMappingRequest.setHotelMappingInfos(hotelMappingList);
		if(CollectionUtils.isEmpty(hotelMappingList)){
			logger.info("结果为空，不增加映射关系");
			throw new OpenException("新增映射的酒店结果为空，不增加映射关系");
		}
		hotelMappingRequest.setHotelMappingAllRequest(addHotelMappingRequest);;
		String responseXml = "";
		try {
			 Map<String, String> param  = businessUtil.genFangCangRequest(hotelMappingRequest, FangCangRequestTypeEnum.AddHotelMapping);
			 responseXml = businessUtil.doPost(GdsChannelUrlEnum.AddHotelMapping.getId(), param, ChannelEnum.fangcang.getId());
		     logger.info("调用添加酒店映射接口返回值:{}",responseXml);
		} catch (Exception e) {
			logger.error("调用添加酒店映射接口返回值:",e);
			throw new OpenException("调用添加酒店映射接口返回值失败，{}",e);
		}
		Response response =businessUtil.decodeResponseXml(responseXml,Response.class);
		if (response==null) {
			saveLog("新增映射的酒店失败，返回null", hotelmapping,BussinssTypeEnum.addHotelMapping);
			throw new OpenException("新增映射的酒店失败，返回null");
		}else if(!response.isSuccess()){
			saveLog(response.getResultMsg(), hotelmapping,BussinssTypeEnum.addHotelMapping);
			throw new OpenException("新增映射的酒店失败，返回"+new Gson().toJson(response));
		}else{
			saveLog("新增映射成功", hotelmapping,BussinssTypeEnum.addHotelMapping);
		}
		return response.isSuccess();
	}
	
	
	/**
	 * 保存日志
	 * @param msg
	 * @param hotelmappings
	 */
	private void saveLog(String msg,List<HotelMapping> hotelmappings,BussinssTypeEnum bussinssTypeEnum){
		try {
			for (HotelMapping hotelMapping : hotelmappings) {
				String content = "房仓酒店id："+hotelMapping.getOtaHotelid()+msg;
				businessLogUtil.saveLog(hotelMapping.getHotelid()+"", "system", content, bussinssTypeEnum);
			}
		} catch (Exception e) {
			logger.info("报错日志失败");
		}
	}
	
	@Override
	public boolean deleteHotelMapping(List<HotelMapping> hotelmapping) {
		if (CollectionUtils.isEmpty(hotelmapping)) {
			logger.info("要删除的映射酒店为空");
			throw new OpenException("要删除的映射酒店为空");
		}
		if (hotelmapping.size()>10) {
			logger.info("删除的映射酒店集合长度大于10");
			throw new OpenException("删除的映射酒店集合长度大于10");
		}
		HotelMappingDeleteRequest hotelMappingDeleteRequest = new HotelMappingDeleteRequest();
		
		HotelMappingHotelRequest hotelMappingAllRequest = new HotelMappingHotelRequest();
	    List<HotelMappingInfo> hotelMappingList = new ArrayList<HotelMappingInfo>();
		for (HotelMapping hotelMappingInfoRequest : hotelmapping) {
			logger.info("删除酒店映射对象:"+ToStringBuilder.reflectionToString(hotelMappingInfoRequest));
			HotelMappingInfo hotelMappingInfo = new HotelMappingInfo();
			
			
			if(StringUtils.isBlank(hotelMappingInfoRequest.getOtaHotelid())){
				logger.info("getOtaHotelid  为空");
				continue;
			}
			if(hotelMappingInfoRequest.getHotelid()==null){
				logger.info("getHotelid  为空");
				continue;
			}
			
			hotelMappingInfo.setFcHotelId(Long.parseLong(hotelMappingInfoRequest.getOtaHotelid()));
//			hotelMappingInfo.setFcHotelName(hotelMappingInfoRequest.getHotelname());
			hotelMappingInfo.setSpHotelId(hotelMappingInfoRequest.getHotelid()+"");
			hotelMappingList.add(hotelMappingInfo);
		}
		hotelMappingAllRequest.setHotelMappingInfos(hotelMappingList);
		
		if(CollectionUtils.isEmpty(hotelMappingList)){
			logger.info("结果为空，不删除映射关系");
			throw new OpenException("删除的映射酒店结果为空，不删除映射关系");
		}
		
		hotelMappingDeleteRequest.setHotelMappingAllRequest(hotelMappingAllRequest);;
		String responseXml = "";
		try {
			 Map<String, String> param  = businessUtil.genFangCangRequest(hotelMappingDeleteRequest, FangCangRequestTypeEnum.DeleteHotelMapping);
			 responseXml = businessUtil.doPost(GdsChannelUrlEnum.DeleteHotelMapping.getId(), param, ChannelEnum.fangcang.getId());
			 logger.info("调用删除酒店映射接口返回值:{}",responseXml);
			} catch (Exception e) {
			 logger.error("调用删除酒店映射接口失败:",e);
		   }
		Response response =businessUtil.decodeResponseXml(responseXml,Response.class);
		if (response==null) {
			saveLog("删除映射的酒店失败，返回null", hotelmapping,BussinssTypeEnum.deleteHotelMapping);
			throw new OpenException("删除映射的酒店失败，返回null");
		}else if(!response.isSuccess()){
			saveLog(response.getResultMsg(), hotelmapping,BussinssTypeEnum.deleteHotelMapping);
			throw new OpenException("删除映射的酒店失败，返回{}",new Gson().toJson(response));
		}else{
			saveLog("新增映射成功", hotelmapping,BussinssTypeEnum.deleteHotelMapping);
		}
		return response.isSuccess();
	}
	
	
	public boolean  doAddHotelMapping(List<HotelMapping> hotelMappings){
		Map<Integer, List<HotelMapping>> map = new HashMap<Integer, List<HotelMapping>>();
		//梳理下不同渠道的酒店mapping
		
		if(CollectionUtils.isNotEmpty(hotelMappings)){
			String cacheName = "hotelMappingAdd";
			map = queryCategoryMap(hotelMappings, cacheName);
			if(MapUtils.isNotEmpty(map)){
				for (Entry<Integer, List<HotelMapping>> entry : map.entrySet()) {
					try {
						this.doAddHotelMapping(entry.getValue(),entry.getKey());
					} catch (Exception e) {
						logger.error("新增酒店映射异常",e);
					}
				}
			}
		}
		return true;
	}

	
	/**
	 * 批量保存酒店映射 本地的mapping和ota的mapping
	 */
	@Override
	public boolean doAddHotelMapping(List<HotelMapping> hotelMappings,Integer channelId) {
		boolean flag =false;
		if(channelId==null){
			logger.info("渠道id为空");
			throw new OpenException("渠道id为空");
		}
		logger.info("酒店映射列表：{},渠道id：{}", new Gson().toJson(hotelMappings),channelId);
		List<HotelMapping> hotelMappingNew = new ArrayList<HotelMapping>();
		List<HotelMapping> hotelMappingPush = new ArrayList<HotelMapping>();
		if(CollectionUtils.isNotEmpty(hotelMappings)){
			
			
			Map<String, List<HotelMapping>> map = getHotelMappingMap(hotelMappings);
			hotelMappingNew = map.get(dbListKey);
			hotelMappingPush = map.get(pushListKey);
			
			//save all
			List<HotelMapping> hotelMappingDb = new ArrayList<HotelMapping>();
			
			hotelMappingDb.addAll(hotelMappingNew);
			hotelMappingDb.addAll(hotelMappingPush);
			
			
			if(CollectionUtils.isEmpty(hotelMappings)){
				logger.info("待添加酒店 为空");
				throw new OpenException("待添加映射酒店为空，酒店可能没有开通该渠道的分销");
				
			}
			
			iHotelMappingService.saveHotelMappingBatch(hotelMappingDb);
			flag= true;
			if(channelId!=null){
				ChannelEnum channelEnum = ChannelEnum.getById(channelId);
				switch (channelEnum) {
				case fangcang:
					try {
						pushAddMappingToFangCang(hotelMappingPush);
						flag= true;
					} catch (OpenException e) {//失败回滚
						//失败重新处理
						for (HotelMapping hotelMapping : hotelMappingPush) {
							if(HotelMappingStateEnum.effect.getId() == hotelMapping.getState()){
								hotelMapping.setState(HotelMappingStateEnum.deployFail.getId());
							}
						}
						iHotelMappingService.updateHotelMappingBatch(hotelMappingPush);
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
	public boolean pushAddMappingToFangCang(List<HotelMapping> hotelMappingPush){
		
		boolean flag =false;
		//check is push?
		List<HotelMapping> hotelMappingList =  new ArrayList<HotelMapping>();
		
		//10个一组，调用房仓的同步接口
		if(hotelMappingPush.size()<=Integer.valueOf(Config.getValue("fangcang.listsize"))){
			hotelMappingList = hotelMappingPush;
			flag = this.addHotelMapping(hotelMappingList);
		}else{
			int interval = Integer.valueOf(Config.getValue("fangcang.listsize"));
			int begin=0;
			
			while(true){

				int end = begin+interval;
				if(end>hotelMappingPush.size()){
					end = hotelMappingPush.size();
				}
				hotelMappingList =  new ArrayList<HotelMapping>();
				hotelMappingList = hotelMappingPush.subList(begin, end);
				if(CollectionUtils.isEmpty(hotelMappingList)){
					break;
				}
				flag = this.addHotelMapping(hotelMappingList);
				if(end<=hotelMappingPush.size()){
					begin = end;
				}
			
			}
		}
		return flag;
	}
	
	
	public boolean  doDeleteHotelMapping(List<HotelMapping> hotelMappings){
		Map<Integer, List<HotelMapping>> map = new HashMap<Integer, List<HotelMapping>>();
    	//梳理下不同渠道的酒店mapping

    	if(CollectionUtils.isNotEmpty(hotelMappings)){
    		String cacheName = "hotelMappingDelete";
    		map = queryCategoryMap(hotelMappings, cacheName);
    		if(MapUtils.isNotEmpty(map)){
    			for (Entry<Integer, List<HotelMapping>> entry : map.entrySet()) {
    				
    				try {
						this.doDeleteHotelMapping(entry.getValue(),entry.getKey());
					} catch (Exception e) {
						logger.error("删除酒店映射异常",e);
					}
    			}
    		}
    	}
    	return true;
	}
	
	/**
	 * 删除本地的mapping和ota的mapping
	 */
	@Override
	public boolean doDeleteHotelMapping(List<HotelMapping> hotelMappings,Integer channelId) {

		if(channelId==null){
			logger.info("渠道id为空");
			throw new OpenException("渠道id为空");
		}
		List<HotelMapping> hotelMappingNew = new ArrayList<HotelMapping>();
		if(CollectionUtils.isNotEmpty(hotelMappings)){
			for (HotelMapping hotelMapping : hotelMappings) {
				if(hotelMapping.getChannelid()==null){
					logger.info("getChannelid  为空");
					continue;
				}
//				if(StringUtils.isBlank(hotelMapping.getOtaHotelid())){
//					logger.info("getOtaHotelid  为空");
//					continue;
//				}
				if(StringUtils.isBlank(hotelMapping.getHotelname())){
					logger.info("getHotelname  为空");
					continue;
				}
				if(hotelMapping.getHotelid()==null){
					logger.info("getHotelid  为空");
					continue;
				}
				
				
				hotelMapping.setState(HotelMappingStateEnum.notMapping.getId());
				hotelMappingNew.add(hotelMapping);
				
				
			}
			if(CollectionUtils.isEmpty(hotelMappingNew)){
				logger.info("待删除酒店 为空");
				throw new OpenException("待删除酒店为空");
				
			}
			//save all
			iHotelMappingService.deleteHotelMappingBatch(hotelMappingNew);
			if(CollectionUtils.isEmpty(hotelMappingNew)){
				logger.info("待删除酒店 为空");
				throw new OpenException("待删除映射酒店为空，酒店可能不存在映射关系");
				
			}
			
			if(channelId!=null){
				ChannelEnum channelEnum = ChannelEnum.getById(channelId);
				switch (channelEnum) {
				case fangcang:
					pushDeleteMappingToFangCang(hotelMappingNew);
					break;
					
				default:
					break;
				}
			}
		}
		return true;
	
	}
	/**
	 * 推送删除映射到房仓
	 * @param hotelMappings
	 */
	public void pushDeleteMappingToFangCang(List<HotelMapping> hotelMappings){
		List<HotelMapping> hotelMappingList =  new ArrayList<HotelMapping>();
		
		//10个一组，调用房仓的同步接口
		if(hotelMappings.size()<=Integer.valueOf(Config.getValue("fangcang.listsize"))){
			hotelMappingList = hotelMappings;
			this.deleteHotelMapping(hotelMappingList);
		}else{
			int interval = Integer.valueOf(Config.getValue("fangcang.listsize"));
			int begin=0;
			
			while(true){

				int end = begin+interval;
				if(end>hotelMappings.size()){
					end = hotelMappings.size();
				}
				hotelMappingList =  new ArrayList<HotelMapping>();
				hotelMappingList = hotelMappings.subList(begin, end);
				if(CollectionUtils.isEmpty(hotelMappingList)){
					break;
				}
				this.deleteHotelMapping(hotelMappingList);
				if(end<=hotelMappings.size()){
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
	
	/**
	 * 获取两个list：
	 * 1、不推送给ota的的mapping
	 * 2、推送给ota的mapping
	 * @param hotelMappings
	 * @return
	 */
	private  Map<String, List<HotelMapping>> getHotelMappingMap(List<HotelMapping> hotelMappings){
		Map<String, List<HotelMapping>> map = new HashMap<String, List<HotelMapping>>();
		List<HotelMapping> hotelMappingNew = new ArrayList<HotelMapping>();
		List<HotelMapping> hotelMappingPush = new ArrayList<HotelMapping>();
		map.put("dbList", hotelMappingNew);
		map.put("pushList", hotelMappingPush);
		boolean flag =false;
		Integer channelId = null;
		if(CollectionUtils.isNotEmpty(hotelMappings)){
			for (HotelMapping hotelMapping : hotelMappings) {
				if(hotelMapping.getChannelid()==null){
					logger.info("getChannelid  为空");
					continue;
				}
				if(hotelMapping.getHotelid()==null){
					logger.info("getHotelid  为空");
					continue;
				}
//				if(StringUtils.isBlank(hotelMapping.getOtaHotelid())){
//					logger.info("getOtaHotelid  为空");
//					continue;
//				}
				if(StringUtils.isBlank(hotelMapping.getHotelname())){
					logger.info("getHotelname  为空");
					continue;
				}
//				if(StringUtils.isBlank(hotelMapping.getHotelphone())){
//					logger.info("getHotelphone  为空");
//					continue;
//				}
				
				
				if(queryHotelById(hotelMapping.getHotelid(), hotelMapping.getChannelid()).isResult()){
					
					hotelMapping.setState(HotelMappingStateEnum.effect.getId());
					hotelMappingPush.add(hotelMapping);
				}else{
					hotelMapping.setState(HotelMappingStateEnum.notMapping.getId());
					hotelMappingNew.add(hotelMapping);
				}
				
				if(channelId==null){
					channelId = hotelMapping.getChannelid();
				}
			}
			
		}
		logger.info("酒店映射两个list：{}",new Gson().toJson(map));
		return map;
	}
	
	

    
    /**
     * 查询开通当前渠道分销的酒店，根据渠道分类
     * @param hotelMappings
     * @param cacheName
     * @return
     */
    private Map<Integer, List<HotelMapping>> queryCategoryMap(List<HotelMapping> hotelMappings ,String cacheName){
    	Map<Integer, List<HotelMapping>> map = new HashMap<Integer, List<HotelMapping>>();
    	for (HotelMapping hotelMapping : hotelMappings) {
			//check token
			String key = hotelMapping.getChannelid()+"_"+hotelMapping.getHotelid();
			Object obj = redisCacheManager.getExpires(cacheName,key );
			if(obj!=null){
				logger.error("重复的mapping数据，{}",new Gson().toJson(hotelMapping));
				continue;
			}else{
				if(map.containsKey(hotelMapping.getChannelid())){
					map.get(hotelMapping.getChannelid()).add(hotelMapping);
				}else{
					List<HotelMapping> hotelMappingnew = new ArrayList<HotelMapping>();
					hotelMappingnew.add(hotelMapping);
					map.put(hotelMapping.getChannelid(), hotelMappingnew);
				}
				redisCacheManager.setExpires(cacheName, key, 1, 10);
			}
			
		}
    	return map;
    }
	@Override
	public boolean doUpdateHotelMapping(List<HotelMapping> hotelMappings) {

		Map<Integer, List<HotelMapping>> map = new HashMap<Integer, List<HotelMapping>>();
		//梳理下不同渠道的酒店mapping
		
		if(CollectionUtils.isNotEmpty(hotelMappings)){
			String cacheName = "hotelMappingUpdate";
			map = queryCategoryMap(hotelMappings, cacheName);
			if(MapUtils.isNotEmpty(map)){
				for (Entry<Integer, List<HotelMapping>> entry : map.entrySet()) {
					try {
						this.pushDeleteMappingToFangCang(entry.getValue());
					} catch (Exception e) {
						logger.error("调用房仓酒店映射异常",e);
					}
					try {
						this.doAddHotelMapping(entry.getValue(),entry.getKey());
					} catch (Exception e) {
						logger.error("新增酒店映射异常",e);
					}
				}
			}
		}
		return true;
	
	}

}
