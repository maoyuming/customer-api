package com.duantuke.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantuke.api.common.GlobalCache;
import com.duantuke.api.domain.area.City;
import com.duantuke.api.domain.area.District;
import com.duantuke.api.domain.area.Province;
import com.duantuke.api.domain.common.OpenResponse;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/area")
public class AreaController {
	private static Logger logger = LoggerFactory.getLogger(AreaController.class);
	

	
	@RequestMapping(value = "/queryProvince")
    public ResponseEntity<OpenResponse<List<Province>>> queryAllProvinces(HttpServletRequest request) {
		logger.info("查询省份");
		OpenResponse<List<Province>> openResponse = new OpenResponse<List<Province>>();
		List<Province>  list = GlobalCache.getInstance().getAreaInfo().getProvinceList();
		openResponse.setResult(true+"");
		openResponse.setData(list);
		return new ResponseEntity<OpenResponse<List<Province>>>(openResponse, HttpStatus.OK);
	}
	@RequestMapping(value = "/queryCity")
	public ResponseEntity<OpenResponse<List<City>>> queryCity(HttpServletRequest request,@RequestBody  String body) {
		logger.info("查询城市，{}",body);
		

		City openRequest = new Gson().fromJson(body, City.class);
		OpenResponse<List<City>> openResponse = new OpenResponse<List<City>>();
		List<City>  list = GlobalCache.getInstance().getProvinceChildren().get(openRequest.getProid());
		openResponse.setResult(true+"");
		openResponse.setData(list);
		return new ResponseEntity<OpenResponse<List<City>>>(openResponse, HttpStatus.OK);
	}
	@RequestMapping(value = "/queryDistrict")
	public ResponseEntity<OpenResponse<List<District>>> queryDistrict(HttpServletRequest request,@RequestBody  String body) {
		logger.info("查询城市，{}",body);
		
		
		District openRequest = new Gson().fromJson(body, District.class);
		
		OpenResponse<List<District>> openResponse = new OpenResponse<List<District>>();
		List<District>  list = GlobalCache.getInstance().getCityChildren().get(openRequest.getCityid());
		openResponse.setResult(true+"");
		openResponse.setData(list);
		return new ResponseEntity<OpenResponse<List<District>>>(openResponse, HttpStatus.OK);
	}
	
}
