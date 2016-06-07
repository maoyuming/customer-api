package com.fangbaba.api.controller.push;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.common.GlobalCache;
import com.fangbaba.api.domain.area.Province;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.service.IGdsChannelService;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/push/area")
public class PushAreaController {
	private static Logger logger = LoggerFactory.getLogger(PushAreaController.class);
	
	@Autowired
	private IGdsChannelService gdsChannelService;
	@Autowired
	private BusinessUtil businessUtil;
	
	/**
	 * 推送城市全量信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/push")
    public ResponseEntity<Boolean> push(HttpServletRequest request) {
		Integer channelid = Integer.valueOf(request.getHeader(com.fangbaba.api.common.Constants.CHANNEL_ID));
		logger.info("查询省份");
		List<Province>  list = GlobalCache.getInstance().getAreaInfo().getProvinceList();
		
		String json  = new Gson().toJson(list);
		businessUtil.push(GdsChannelUrlEnum.pushCity.getId(), json,channelid,null,null);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	
}
