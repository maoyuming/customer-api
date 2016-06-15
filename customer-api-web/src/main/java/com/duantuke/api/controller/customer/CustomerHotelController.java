package com.duantuke.api.controller.customer;

import java.util.List;

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
import com.duantuke.basic.face.esbean.output.HotelOutputBean;
import com.duantuke.basic.face.esbean.query.HotelQueryBean;
import com.duantuke.basic.face.service.HotelSearchService;
import com.google.gson.Gson;

/**
 * @author he
 * 农家院
 */
@Controller
@RequestMapping(value = "/customer/hotel")
public class CustomerHotelController {
	
	private static Logger logger = LoggerFactory.getLogger(CustomerJournalController.class);
	
	@Autowired
	private HotelSearchService hotelSearchService;
	
	
	/**
	 * @param hotelQueryBean
	 * 搜索农家院es
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<OpenResponse<List<HotelOutputBean>>> search(HotelQueryBean hotelQueryBean) {
		logger.info("CustomerHotelController search：{}",new Gson().toJson(hotelQueryBean));
		OpenResponse<List<HotelOutputBean>> openResponse = new OpenResponse<List<HotelOutputBean>>();
		try {
			List<HotelOutputBean> list = hotelSearchService.searchHotelsFromEs(hotelQueryBean);
			openResponse.setData(list);
			openResponse.setResult(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("CustomerHotelController search error",e);
			openResponse.setResult(Constants.FAIL);
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
