package com.fangbaba.api.controller.qunar;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.common.GlobalCache;
import com.fangbaba.api.domain.qunar.ChangeHotelInfo;
import com.fangbaba.api.domain.qunar.Hotel;
import com.fangbaba.api.util.PageItem;
import com.fangbaba.api.util.XstreamSingletonUtil;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.face.bean.CityMapping;
import com.fangbaba.gds.face.bean.EsSearchBean;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.service.ICityMappingService;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.fangbaba.gds.po.DistributorConfig;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;



@Controller
@RequestMapping(value = "/qunar/hotel")
public class HotelController {
	private static Logger logger = LoggerFactory.getLogger(HotelController.class);

	@Autowired
	private ICityMappingService iCityMappingService;
	@Autowired
	private IHotelSearchService hotelSearchService;
	@Autowired
	private IDistributorConfigService iDistributorConfigService;
	

	/**
	 * 查询分销信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getFullHotelInfo")
	public void queryHotel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("qunar 开始查询基本信息hotel");
		HotelModelEsBean hotelModel =  new HotelModelEsBean();
		hotelModel.setRange(5000000D);

		
		List<Hotel> hotels = new ArrayList<Hotel>();
		//根据渠道查询otatype
		List<DistributorConfig>  list =iDistributorConfigService.queryByChannelId(ChannelEnum.QUNAR.getId());
		if(CollectionUtils.isNotEmpty(list)){
			Long otatype = list.get(0).getOtatype();
			hotelModel.setOtatype(otatype);

//					bean.setSize(hotelQuery.getPageSize());
//					bean.setSortorder("asc");
			int pageSize = 100;
			EsSearchBean esSearchBean = new EsSearchBean();
			//ES必须有默认值，设置成最大数
			esSearchBean.setSize(900000000);
			esSearchBean.setSortby("id");
			Integer count = hotelSearchService.searchHotelFromESCount(hotelModel, null, esSearchBean);
			PageItem pageItem = new PageItem();
			pageItem.setIndex(1);
			pageItem.setPageSize(pageSize);
			pageItem.setTotalItem(count);
			
			logger.info("查询酒店总页数：{},总记录数：{}",pageItem.getTotalPage(),count);
			for (int i = 0; i < pageItem.getTotalPage(); i++) {
				logger.info("开始查询第{}页数据",i+1);
				EsSearchBean bean = new EsSearchBean();
				bean.setPage(i+1);
				bean.setSize(pageSize);
				bean.setSortby("id");
				
				List<HotelModelEsBean> hotelModels =  hotelSearchService.searchHotelFromES(hotelModel,null, bean);
				
				
				if(CollectionUtils.isNotEmpty(hotelModels)){
					logger.info("查询酒店数量,{}",hotelModels.size());
					for (HotelModelEsBean hotelModel2 : hotelModels) {
						try {
							Hotel hotel = new Hotel();
							hotel.setAddress(hotelModel2.getDetailaddr());
//								hotel.setCity(hotelModel2.getCitycode()+"");
							hotel.setCity(GlobalCache.getInstance().getOatCityMap().get(ChannelEnum.QUNAR.getId()+"_"+hotelModel2.getCitycode()));
							if(StringUtils.isBlank(hotel.getCity())){
								logger.info("缓存没有该城市信息,"+new Gson().toJson(hotel));
								continue;
							}else{
								CityMapping cityMapping = iCityMappingService.queryCityMappingByCityCodeAndChannelId(ChannelEnum.QUNAR.getId(),hotelModel2.getCitycode()+"");
								if(cityMapping==null){
									logger.info("数据库没有该城市信息,"+new Gson().toJson(hotel));
									continue;
								}else{
									hotel.setCity(cityMapping.getOtacode());
								}
							}
							hotel.setId(hotelModel2.getId());
							hotel.setTel(hotelModel2.getOtaHotelphone());
							hotel.setName(hotelModel2.getOtaHotelname());
							
							
							if(StringUtils.isBlank(hotelModel2.getOtaHotelname())){
								hotel.setName(hotelModel2.getHotelname());
				        	}else{
				        		hotel.setName(hotelModel2.getOtaHotelname());
				        	}
				        	if(StringUtils.isBlank(hotelModel2.getOtaDetailaddr())){
				        		hotel.setAddress(hotelModel2.getDetailaddr());
				        	}else{
				        		hotel.setAddress(hotelModel2.getOtaDetailaddr());
				        	}
				        	if(StringUtils.isBlank(hotelModel2.getOtaHotelphone())){
				        		hotel.setTel(hotelModel2.getHotelphone());
				        	}else{
				        		hotel.setTel(hotelModel2.getOtaHotelphone());
				        	}
							
							hotels.add(hotel);
						} catch (Exception e) {
							logger.error("查询酒店失败",e);
						}
					}
				}else{
					logger.info("酒店列表为空");
				}
			}
			
		}
		
		XStream xstream = XstreamSingletonUtil.getXstream(Hotel.class.getName());
        xstream.alias("hotel", Hotel.class);
        xstream.autodetectAnnotations(true);
        xstream.ignoreUnknownElements();
        xstream.toXML(hotels,new PrintWriter(System.out));  
        String xml =  xstream.toXML(hotels);

		response.getWriter().write(xml);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/xml;charset=UTF-8");
//        return new ResponseEntity<String>(xml, HttpStatus.OK);
//        return new ResponseEntity<String>(xml, HttpStatus.OK);
        //return hotels;
//        return new ResponseEntity<String>("<list> <hotel id=\"1\" city=\"beijing_city\" name=\"北京大饭店\" address=\"苏州街\" tel=\"010-66666666\">    </hotel></list>", HttpStatus.OK);
	}
}
