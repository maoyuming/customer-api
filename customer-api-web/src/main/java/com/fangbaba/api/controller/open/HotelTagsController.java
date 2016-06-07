package com.fangbaba.api.controller.open;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.domain.open.hotel.HotelTag;
import com.fangbaba.api.domain.open.hotel.HotelTagRequest;
import com.fangbaba.basic.face.bean.Hoteltags;
import com.fangbaba.basic.face.bean.Tags;
import com.fangbaba.basic.face.service.HotelTagsService;
import com.google.gson.Gson;



@Controller
@RequestMapping(value = "/open/hotel/tags")
public class HotelTagsController {

	
	
	/**
	 * 查询标签
	 */
	@Autowired
	private HotelTagsService hotelTagsService;
	@RequestMapping(value = "/querytags")
    public ResponseEntity<OpenResponse<HotelTag>> query(HttpServletRequest request,@RequestBody  String body) {

		HotelTagRequest rHotelTagRequest = new Gson().fromJson(body, HotelTagRequest.class);
		Long hotelid = rHotelTagRequest.getHotelid();
		List<Tags> tagses = hotelTagsService.queryTagsByHotelid(hotelid);

		OpenResponse<HotelTag> openResponse = new OpenResponse<HotelTag>();
		
		HotelTag hotelTag = new HotelTag();
		hotelTag.setHotelid(hotelid);
		hotelTag.setTags(tagses);
		
		
		openResponse.setResult(true+"");
		openResponse.setData(hotelTag);
		
		return new ResponseEntity<OpenResponse<HotelTag>>(openResponse, HttpStatus.OK);
		
	}
	
}
