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
import com.fangbaba.api.domain.open.hotel.HotelListBean;
import com.fangbaba.api.domain.open.hotel.HotelRequest;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.gds.face.bean.EsSearchBean;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.fangbaba.gds.po.DistributorConfig;
import com.google.common.collect.Maps;
import com.google.gson.Gson;


@Controller
@RequestMapping(value = "/open/hotel/list")
public class HotelListController {
	private static Logger logger = LoggerFactory.getLogger(HotelListController.class);
	@Autowired
	private IHotelSearchService iHotelSearchService;
	@Autowired
	private IDistributorConfigService iDistributorConfigService;
    @Autowired
    private Mapper dozerMapper;
    
	/**
	 * 查询房型信息
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/query")
    public ResponseEntity<OpenResponse<Map<String,List<HotelListBean>>>> query(HttpServletRequest request,@RequestBody  String body) {
		logger.info("查询房型，{}",body);
		
		String channelId = request.getHeader("channelid");
		
		HotelRequest openRequest = new Gson().fromJson(body, HotelRequest.class);
		
		if (openRequest==null) {
			logger.info("请求参数为空");
			throw new OpenException(DistributionErrorEnum.argsNull.getName(),DistributionErrorEnum.argsNull.getId());
		}
//		if (openRequest.getLongitude()==null) {
//			logger.info("经度为空");
//			throw new OpenException(DistributionErrorEnum.longitudeNulll.getName(),DistributionErrorEnum.longitudeNulll.getId());
//		}
//		if (openRequest.getLatitude()==null) {
//			logger.info("纬度为空");
//			throw new OpenException(DistributionErrorEnum.latitudeNulll.getName(),DistributionErrorEnum.latitudeNulll.getId());
//		}
		if (openRequest.getPage()==null) {
			openRequest.setPage(1);
		}
		if (openRequest.getPagesize()==null) {
			openRequest.setPagesize(10);
		}else{
			if(openRequest.getPagesize()>100){
				openRequest.setPagesize(100);
			}
		}
		

		HotelModelEsBean hotelModelEsBean = new HotelModelEsBean();
//		hotelModelEsBean.setRange(5000000D);
		hotelModelEsBean.setLatitude(openRequest.getLatitude());
		hotelModelEsBean.setLongitude(openRequest.getLongitude());
		if(openRequest.getCitycode()!=null){
			hotelModelEsBean.setCitycode(openRequest.getCitycode());
		}

		EsSearchBean bean = new EsSearchBean();
		
		
		//根据channelid查询otatype
		List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(Integer.valueOf(channelId));
		if(CollectionUtils.isNotEmpty(distributorConfigs)){
			hotelModelEsBean.setOtatype(distributorConfigs.get(0).getOtatype());
		}else{
			throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(),DistributionErrorEnum.channelidNotExists.getId());
		}

		bean.setPage(openRequest.getPage());
		bean.setSize(openRequest.getPagesize());
//		bean.setSortby(hotelQuery.getOrderBy());
//		bean.setSortorder(hotelQuery.getOrderByType());

        Map<String, String> dynamicCondition = Maps.newHashMap();
        List<HotelModelEsBean> list = iHotelSearchService.searchHotelFromES(hotelModelEsBean, dynamicCondition, bean);
        List<HotelListBean> listBean = new ArrayList<HotelListBean>();
        if(CollectionUtils.isNotEmpty(list)){
            for(HotelModelEsBean hotelModelEsBean2 :list ){
                listBean.add(dozerMapper.map(hotelModelEsBean2, HotelListBean.class));
            }
        }
        
		OpenResponse<Map<String,List<HotelListBean>>> openResponse = new OpenResponse<Map<String,List<HotelListBean>>>();

		
		openResponse.setResult(true+"");
		Map<String,List<HotelListBean>> datamapresult= new HashMap<String,List<HotelListBean>>();
		datamapresult.put("hotels", listBean);
		openResponse.setData(datamapresult);
		
		
		return new ResponseEntity<OpenResponse<Map<String,List<HotelListBean>>>>(openResponse, HttpStatus.OK);
	}
}
