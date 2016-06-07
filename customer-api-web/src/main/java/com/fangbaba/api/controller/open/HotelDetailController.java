package com.fangbaba.api.controller.open;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.domain.open.hotel.HotelDetail;
import com.fangbaba.api.domain.open.hotel.HotelListBean;
import com.fangbaba.api.domain.open.hotel.Roomtype;
import com.fangbaba.api.domain.open.hotel.RoomtypeRequest;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.enums.open.BreakfastEnum;
import com.fangbaba.api.enums.open.PrepayEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.basic.face.bean.RoomtypeModel;
import com.fangbaba.basic.face.bean.RoomtypeinfoModel;
import com.fangbaba.basic.face.service.RoomtypeService;
import com.fangbaba.basic.face.service.RoomtypeinfoService;
import com.fangbaba.gds.face.bean.EsSearchBean;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.fangbaba.gds.po.DistributorConfig;
import com.google.common.collect.Maps;
import com.google.gson.Gson;


@Controller
@RequestMapping(value = "/open/hotel/detail")
public class HotelDetailController {
	private static Logger logger = LoggerFactory.getLogger(HotelDetailController.class);
	@Autowired
	private RoomtypeService roomtypeService;
   @Autowired
    private IDistributorConfigService iDistributorConfigService;
   @Autowired
   private IHotelSearchService iHotelSearchService;
   @Autowired
   private RoomtypeinfoService roomtypeinfoService;
    @Autowired
    private Mapper dozerMapper;
    private static Gson gson = new Gson();
	/**
	 * 查询房型信息
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/query")
    public ResponseEntity<OpenResponse<Map<String,HotelDetail>>> query(HttpServletRequest request,@RequestBody  String body) {
		logger.info("查询房型，{}",body);
		String channelId = request.getHeader("channelid");
		RoomtypeRequest openRequest = new Gson().fromJson(body, RoomtypeRequest.class);
		OpenResponse<Map<String,HotelDetail>> openResponse = new OpenResponse<Map<String,HotelDetail>>();
		
		if (openRequest==null) {
			logger.info("酒店id为空");
			throw new OpenException(DistributionErrorEnum.hotelNull.getName(),DistributionErrorEnum.hotelNull.getId());
		}
		if (openRequest.getHotelid()==null) {
			logger.info("酒店id为空");
			throw new OpenException(DistributionErrorEnum.hotelNull.getName(),DistributionErrorEnum.hotelNull.getId());
		}
		
		HotelModelEsBean hotelModelEsBean = new HotelModelEsBean();
		//根据channelid查询otatype
        List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(Integer.valueOf(channelId));
        if(CollectionUtils.isNotEmpty(distributorConfigs)){
            hotelModelEsBean.setOtatype(distributorConfigs.get(0).getOtatype());
        }else{
            throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(),DistributionErrorEnum.channelidNotExists.getId());
        }
        
		Long hotelid = openRequest.getHotelid();
		hotelModelEsBean.setId(hotelid);
		
       EsSearchBean bean = new EsSearchBean();
       bean.setPage(1);
       bean.setSize(10);
	   Map<String, String> dynamicCondition = Maps.newHashMap();
       List<HotelModelEsBean> listhotel = iHotelSearchService.searchHotelFromES(hotelModelEsBean, dynamicCondition, bean);
		logger.info("listhotel"+gson.toJson(listhotel));
       
		if(CollectionUtils.isEmpty(listhotel)){
		    throw new OpenException(DistributionErrorEnum.hotelidNotExists.getName(),DistributionErrorEnum.hotelidNotExists.getId());
		}
		HotelModelEsBean hotelBean = listhotel.get(0);
		
		List<RoomtypeModel> list = roomtypeService.queryByHotelId(hotelid);
		HotelDetail hotelDetail = new HotelDetail();

		List<Roomtype> roomtypes = new ArrayList<Roomtype>();
		if(CollectionUtils.isNotEmpty(list)){
			for (RoomtypeModel roomtypeModel : list) {
				Roomtype roomtype =  dozerMapper.map(roomtypeModel, Roomtype.class);
				RoomtypeinfoModel roomtypeinfoModel = roomtypeinfoService.findRoomtypeinfoByRoomtypeId(roomtype.getId());
				roomtype.setPrepay(PrepayEnum.yufu.getId().toString());
				roomtype.setBreakfast(BreakfastEnum.no.getId().toString());
				if(roomtypeinfoModel!=null){
					roomtype.setArea(roomtypeinfoModel.getMaxarea()+"");
					roomtype.setBedtype(roomtypeinfoModel.getBedtype()+"");
					roomtype.setRoomtypepics(roomtypeinfoModel.getPics());
					roomtype.setBedsize(roomtypeinfoModel.getBedsize());
				}
				roomtypes.add(roomtype);
			}
		}
		hotelDetail = dozerMapper.map(hotelBean,HotelDetail.class);
		hotelDetail.setRoomtypes(roomtypes);
		openResponse.setResult("true");
		Map<String,HotelDetail> datamapresult= new HashMap<String,HotelDetail>();
		datamapresult.put("hotel", hotelDetail);
		openResponse.setData(datamapresult);
		
		
		return new ResponseEntity<OpenResponse<Map<String,HotelDetail>>>(openResponse, HttpStatus.OK);
	}
}
