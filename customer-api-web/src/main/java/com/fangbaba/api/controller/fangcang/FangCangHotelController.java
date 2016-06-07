package com.fangbaba.api.controller.fangcang;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.service.IFangCangHotelService;
import com.fangbaba.gds.face.bean.HotelModelEsBean;



@Controller
@RequestMapping(value = "/fangcang/hotel")
public class FangCangHotelController {

	private static Logger logger = LoggerFactory.getLogger(FangCangHotelController.class);
    @Autowired
    private IFangCangHotelService fangCangHotelService;
    
    /**
	 * 查询房仓酒店信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getHotelInfo")
    public ResponseEntity<RetInfo<HotelModelEsBean>> getHotelInfo(HttpServletRequest request,String fcHotelIds) {
		logger.info("查询房仓酒店信息");
		RetInfo<HotelModelEsBean> retInfo = fangCangHotelService.getHotelInfo(fcHotelIds);
		logger.info("查询房仓酒店信息result:{}",retInfo.isResult());
		return new ResponseEntity<RetInfo<HotelModelEsBean>>(retInfo, HttpStatus.OK);
	}
	
}
