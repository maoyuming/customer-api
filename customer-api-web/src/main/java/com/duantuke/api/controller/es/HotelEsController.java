package com.duantuke.api.controller.es;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.enums.BusinessTypeEnum;
import com.duantuke.basic.face.esbean.output.HotelOutputBean;
import com.duantuke.basic.face.esbean.output.TeamSkuOutputBean;
import com.duantuke.basic.face.esbean.query.HotelQueryBean;
import com.duantuke.basic.face.esbean.query.MealQueryBean;
import com.duantuke.basic.face.esbean.query.TeamSkuQueryBean;
import com.duantuke.basic.face.service.DuantukeLikeService;
import com.duantuke.basic.face.service.HotelSearchService;
import com.duantuke.basic.face.service.TeamSkuSearchService;
import com.duantuke.basic.po.DuantukeLike;
import com.google.gson.Gson;

/**
 * @author he
 * 农家院
 */
@Controller
@RequestMapping(value = "/es/hotel")
public class HotelEsController {
	
	private static Logger logger = LoggerFactory.getLogger(HotelEsController.class);
	
	@Autowired
	private HotelSearchService hotelSearchService;
	
	@Autowired
	private DuantukeLikeService duantukeLikeService;
	
	@Autowired
	private TeamSkuSearchService teamSkuSearchService;
	
	
	/**
	 * @param hotelQueryBean
	 * 搜索农家院es
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<HotelOutputBean>>> search(HotelQueryBean hotelQueryBean) {
		logger.info("CustomerHotelController search：{}",new Gson().toJson(hotelQueryBean));
		OpenResponse<List<HotelOutputBean>> openResponse = new OpenResponse<List<HotelOutputBean>>();
		try {
			List<HotelOutputBean> list = hotelSearchService.searchHotelsFromEs(hotelQueryBean,null,null);
			if(CollectionUtils.isNotEmpty(list)){
				for (HotelOutputBean hotelOutputBean : list) {
					DuantukeLike duantukeLike = new DuantukeLike();
					duantukeLike.setBusinessType(Short.valueOf(BusinessTypeEnum.hotel.getCode()+""));
					duantukeLike.setFid(hotelOutputBean.getHotelId());
					int like = duantukeLikeService.countDuantukeLike(duantukeLike);
					hotelOutputBean.setLike(like);
				}
			}
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerHotelController search error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<HotelOutputBean>>> (openResponse, HttpStatus.OK);
		
		
	}
	
	/**
	 * @param hotelQueryBean
	 * 通过吃搜索农家院es
	 */
	@RequestMapping(value = "/searchbymeal", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<HotelOutputBean>>> searchbymeal(MealQueryBean mealQueryBean,BigDecimal longitude,BigDecimal latitude) {
		logger.info("CustomerHotelController searchbymeal：{}",new Gson().toJson(mealQueryBean));
		OpenResponse<List<HotelOutputBean>> openResponse = new OpenResponse<List<HotelOutputBean>>();
		try {
			//查出餐饮对应的酒店ids
			Integer queryPage = mealQueryBean.getPage();
			Integer queryPageSize = mealQueryBean.getPagesize();
			//mealQueryBean.setPage(1);
			//mealQueryBean.setPagesize(10);
			HotelQueryBean hotelQueryBean = new HotelQueryBean();
			hotelQueryBean.setPage(queryPage);
			hotelQueryBean.setPagesize(queryPageSize);
			hotelQueryBean.setLatitude(latitude);
			hotelQueryBean.setLongitude(longitude);
			List<HotelOutputBean> list = hotelSearchService.searchHotelsFromEs(hotelQueryBean,mealQueryBean,null);
			if(CollectionUtils.isNotEmpty(list)){
				for (HotelOutputBean hotelOutputBean : list) {
					DuantukeLike duantukeLike = new DuantukeLike();
					duantukeLike.setBusinessType(Short.valueOf(BusinessTypeEnum.hotel.getCode()+""));
					duantukeLike.setFid(hotelOutputBean.getHotelId());
					int like = duantukeLikeService.countDuantukeLike(duantukeLike);
					hotelOutputBean.setLike(like);
				}
			}
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
			/*List<MealOutputBean> meals = mealSearchService.searchMealsFromEs(mealQueryBean);
			if(CollectionUtils.isNotEmpty(meals)){
				List<String> hotelids = new ArrayList<String>();
				for (MealOutputBean meal:meals) {
					hotelids.add(meal.getSupplierId());
				}
				HotelQueryBean hotelQueryBean = new HotelQueryBean();
				hotelQueryBean.setPage(queryPage);
				hotelQueryBean.setPagesize(queryPageSize);
				hotelQueryBean.setHotelIds(hotelids);
				hotelQueryBean.setLatitude(latitude);
				hotelQueryBean.setLongitude(longitude);
				
				List<HotelOutputBean> list = hotelSearchService.searchHotelsFromEs(hotelQueryBean,mealQueryBean);
				if(CollectionUtils.isNotEmpty(list)){
					for (HotelOutputBean hotelOutputBean : list) {
						DuantukeLike duantukeLike = new DuantukeLike();
						duantukeLike.setBusinessType(Short.valueOf(BusinessTypeEnum.hotel.getCode()+""));
						duantukeLike.setFid(hotelOutputBean.getHotelId());
						int like = duantukeLikeService.countDuantukeLike(duantukeLike);
						hotelOutputBean.setLike(like);
					}
				}
				openResponse.setData(list);
				openResponse.setResult(Constants.SUCCESS);
			}*/
		} catch (Exception e) {
			logger.error("CustomerHotelController searchbymeal error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<HotelOutputBean>>> (openResponse, HttpStatus.OK);
		
		
	}
	
	/**
	 * @param hotelQueryBean
	 * 通过团体搜索农家院es
	 */
	@RequestMapping(value = "/searchbyteam", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<HotelOutputBean>>> searchbyteam(TeamSkuQueryBean teamSkuQueryBean,BigDecimal longitude,BigDecimal latitude) {
		logger.info("CustomerHotelController searchbyteam：{}",new Gson().toJson(teamSkuQueryBean));
		OpenResponse<List<HotelOutputBean>> openResponse = new OpenResponse<List<HotelOutputBean>>();
		try {
			//查出餐饮对应的酒店ids
			Integer queryPage = teamSkuQueryBean.getPage();
			Integer queryPageSize = teamSkuQueryBean.getPagesize();
			//teamSkuQueryBean.setPage(1);
			//teamSkuQueryBean.setPagesize(10);
			HotelQueryBean hotelQueryBean = new HotelQueryBean();
			hotelQueryBean.setPage(queryPage);
			hotelQueryBean.setPagesize(queryPageSize);
			hotelQueryBean.setLatitude(latitude);
			hotelQueryBean.setLongitude(longitude);
			List<HotelOutputBean> list = hotelSearchService.searchHotelsFromEs(hotelQueryBean,null,teamSkuQueryBean);
			if(CollectionUtils.isNotEmpty(list)){
				for (HotelOutputBean hotelOutputBean : list) {
					DuantukeLike duantukeLike = new DuantukeLike();
					duantukeLike.setBusinessType(Short.valueOf(BusinessTypeEnum.hotel.getCode()+""));
					duantukeLike.setFid(hotelOutputBean.getHotelId());
					int like = duantukeLikeService.countDuantukeLike(duantukeLike);
					hotelOutputBean.setLike(like);
				}
			}
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
			/*List<TeamSkuOutputBean> teams = teamSkuSearchService.searchTeamSkusFromEs(teamSkuQueryBean);
			if(CollectionUtils.isNotEmpty(teams)){
				List<String> hotelids = new ArrayList<String>();
				for (TeamSkuOutputBean team:teams) {
					hotelids.add(team.getSupplierId());
				}
				HotelQueryBean hotelQueryBean = new HotelQueryBean();
				hotelQueryBean.setPage(queryPage);
				hotelQueryBean.setPagesize(queryPageSize);
				hotelQueryBean.setHotelIds(hotelids);
				hotelQueryBean.setLatitude(latitude);
				hotelQueryBean.setLongitude(longitude);
				
				List<HotelOutputBean> list = hotelSearchService.searchHotelsFromEs(hotelQueryBean,null,teamSkuQueryBean);
				if(CollectionUtils.isNotEmpty(list)){
					for (HotelOutputBean hotelOutputBean : list) {
						DuantukeLike duantukeLike = new DuantukeLike();
						duantukeLike.setBusinessType(Short.valueOf(BusinessTypeEnum.hotel.getCode()+""));
						duantukeLike.setFid(hotelOutputBean.getHotelId());
						int like = duantukeLikeService.countDuantukeLike(duantukeLike);
						hotelOutputBean.setLike(like);
					}
				}
				openResponse.setData(list);
				openResponse.setResult(Constants.SUCCESS);
			}*/
		} catch (Exception e) {
			logger.error("CustomerHotelController searchbyteam error",e);
			openResponse.setResult(Constants.FAIL);
			throw e;
		}
		return new ResponseEntity<OpenResponse<List<HotelOutputBean>>> (openResponse, HttpStatus.OK);
		
		
	}
	
	
	/*public static void main(String[]args){
		Map<String,List<String>> tagmap = new HashMap<String,List<String>>();
		List<String> list1 = new ArrayList<String>();
		list1.add("旅游景区");
		List<String> list2 = new ArrayList<String>();
		list2.add("商务会议");
		list2.add("温泉度假");
		tagmap.put("taggroup_2", list2);
		tagmap.put("taggroup_1", list1);
		String s = new Gson().toJson(tagmap);
		System.out.println(s);
		Map<String,List<String>> tagmap1 = new Gson().fromJson(s, Map.class);
		System.out.println(tagmap1.get("taggroup_2").get(1));
	}*/
}
