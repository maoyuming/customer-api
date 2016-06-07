package com.fangbaba.api.controller.open;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.common.GlobalCache;
import com.fangbaba.api.domain.area.City;
import com.fangbaba.api.domain.area.District;
import com.fangbaba.api.domain.area.Province;
import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.api.util.PostUtils;
import com.fangbaba.basic.face.bean.CityModel;
import com.fangbaba.basic.face.bean.DistrictModel;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.service.IGdsChannelService;
import com.fangbaba.gds.po.GdsChannel;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/open/area")
public class AreaController {
	private static Logger logger = LoggerFactory.getLogger(AreaController.class);
	

	@Autowired
	private IGdsChannelService gdsChannelService;
	@Autowired
	private BusinessUtil businessUtil;
	
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
		

		CityModel openRequest = new Gson().fromJson(body, CityModel.class);
		OpenResponse<List<City>> openResponse = new OpenResponse<List<City>>();
		List<City>  list = GlobalCache.getInstance().getProvinceChildren().get(openRequest.getProid());
		openResponse.setResult(true+"");
		openResponse.setData(list);
		return new ResponseEntity<OpenResponse<List<City>>>(openResponse, HttpStatus.OK);
	}
	@RequestMapping(value = "/queryDistrict")
	public ResponseEntity<OpenResponse<List<District>>> queryDistrict(HttpServletRequest request,@RequestBody  String body) {
		logger.info("查询城市，{}",body);
		
		
		DistrictModel openRequest = new Gson().fromJson(body, DistrictModel.class);
		
		OpenResponse<List<District>> openResponse = new OpenResponse<List<District>>();
		List<District>  list = GlobalCache.getInstance().getCityChildren().get(openRequest.getCityid());
		openResponse.setResult(true+"");
		openResponse.setData(list);
		return new ResponseEntity<OpenResponse<List<District>>>(openResponse, HttpStatus.OK);
	}
	
}
