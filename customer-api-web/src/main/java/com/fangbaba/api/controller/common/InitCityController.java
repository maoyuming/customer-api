package com.fangbaba.api.controller.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.face.service.IQunarCityService;



@Controller
@RequestMapping(value = "/cityinit")
public class InitCityController {
	private static Logger logger = LoggerFactory.getLogger(InitCityController.class);

	@Autowired
	private IQunarCityService iQunarCityService;

	/**
	 * 映射去哪儿城市列表
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/qunar/mapping")
	public ResponseEntity<Boolean> qunarmapping(HttpServletRequest request, HttpServletResponse response) throws IOException {
		iQunarCityService.initCity();
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		
	}
}
