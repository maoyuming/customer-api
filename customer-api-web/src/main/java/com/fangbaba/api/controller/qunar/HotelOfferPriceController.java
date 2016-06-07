package com.fangbaba.api.controller.qunar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.domain.qunar.Hotel;
import com.fangbaba.api.domain.qunar.HotelOfferPrice;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.service.QunarHotelPriceInfoService;
import com.fangbaba.api.util.DateUtil;
import com.fangbaba.api.util.XstreamSingletonUtil;
import com.google.common.base.Strings;
import com.thoughtworks.xstream.XStream;



@Controller
@RequestMapping(value = "/qunar/offer")
public class HotelOfferPriceController {
	private static Logger logger = LoggerFactory.getLogger(HotelOfferPriceController.class);

	@Autowired
	private QunarHotelPriceInfoService qunarHotelPriceInfoService;
	public static final int DEFAULTDAYS = 90;
	
	
	/**
	 *酒店变价推送
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getPriceInfo")
	public void pricechangepush(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//酒店id
		String hotelId = request.getParameter("hotelId");
		
		//入住日期
		String fromDate = request.getParameter("fromDate");
		//离店时间
		String toDate = request.getParameter("toDate");
		//代理商房型ID
		String roomtypeid = request.getParameter("roomId");
		//订单报价标识
		String usedFor = request.getParameter("usedFor");
		//房间数	
		String count = request.getParameter("count");
		logger.info("qunar 酒店报接口传来的参数hotelId:{},fromDate:{},toDate:{},roomId:{},usedFor:{}",hotelId,fromDate,toDate,roomtypeid,usedFor,count);
		Long hotelIdLong  = null;
		Long roomtypeidLong = null;
		RetInfo<HotelOfferPrice> retInfo = new RetInfo<HotelOfferPrice>();
		retInfo.setObj(new HotelOfferPrice());
		try {
			if (Strings.isNullOrEmpty(hotelId)) {
				logger.info("酒店id为空");
				returnXml(response);
				return ;
			}
			if (Strings.isNullOrEmpty(fromDate)) {
				logger.info("入住时间为空");
				returnXml(response);
				return ;
			}
			if (Strings.isNullOrEmpty(toDate)) {
				logger.info("离店时间为空");
				returnXml(response);
				return ;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date begintime= null;
			try {
				begintime = sdf.parse(fromDate);
			} catch (Exception e) {
				logger.error("时间格式转换错误",e);
				returnXml(response);
				return;
			}
			Date endtime= null;
			try {
				endtime = sdf.parse(toDate);
			} catch (Exception e) {
				logger.error("时间格式转换错误",e);
				returnXml(response);
				return;
			}
			
			try {
				dealTimeSpace(begintime,endtime);
			} catch (Exception e) {
				logger.error("时间区间处理异常",e);
				returnXml(response);
				return;
			}
			
			try {
				hotelIdLong = Long.parseLong(hotelId);
				if (!Strings.isNullOrEmpty(usedFor)&&"ORDER".equals(usedFor.toUpperCase())) {
					if (!Strings.isNullOrEmpty(roomtypeid)) {
					    roomtypeidLong = Long.parseLong(roomtypeid);
					}else {
						logger.info("房型参数为空");
						returnXml(response);
						return;
					}
					
				}
			} catch (Exception e) {
				logger.info("参数类型转换异常:",e);
				returnXml(response);
				return;
			}
			retInfo = qunarHotelPriceInfoService.getHotelPriceInfo(hotelIdLong, begintime, endtime, roomtypeidLong, usedFor, null);
			 if (!retInfo.isResult()) {
				logger.info("获取酒店房型信息的错误信息："+retInfo.getMsg());
				returnXml(response);
				return;
			}
		} catch (Exception e) {
			logger.error("qunar报价查询异常",e);
		}
		XStream xstream = XstreamSingletonUtil.getXstream(HotelOfferPrice.class.getName());
		xstream.autodetectAnnotations(true);
        xstream.alias("hotel", HotelOfferPrice.class); 
        xstream.ignoreUnknownElements();
        String xml =  xstream.toXML(retInfo.getObj());
		response.getWriter().write(xml);
		response.setContentType("application/xml");
	}
	
	private void returnXml(HttpServletResponse response){
		XStream xstream = XstreamSingletonUtil.getXstream(HotelOfferPrice.class.getName());
		HotelOfferPrice aHotelOfferPrice = new HotelOfferPrice();
		 xstream.alias("hotel", HotelOfferPrice.class); 
		 xstream.ignoreUnknownElements();
	     String xml =  xstream.toXML(aHotelOfferPrice);
		 try {
			response.getWriter().write(xml);
		} catch (IOException e) {
		}
		 response.setContentType("application/xml");
	}
	private void dealTimeSpace(Date begintime,Date endtime ){
		int beginAndEndtimeSpace = 0;
		try {
			beginAndEndtimeSpace = DateUtil.daysBetween(begintime, endtime);
			if (beginAndEndtimeSpace<0) {
				logger.info("开始时间大于结束时间");
				throw new RuntimeException("开始时间大于结束时间");
			}
		} catch (Exception e) {
			logger.info("获取时间间隔异常");
			throw new RuntimeException("获取时间间隔异常");
		}
		
		Date nowDate = new Date();
		int beginTimeCompare = 0;
		int endTimeCompare = 0;
		int beginAndendTimeCompare = 0;
		try {
			beginTimeCompare = DateUtil.daysBetween(nowDate, begintime);
			endTimeCompare = DateUtil.daysBetween(nowDate, endtime);
			beginAndendTimeCompare =DateUtil.daysBetween(begintime, endtime);
		} catch (Exception e1) {
			logger.info("获取时间间隔异常");
			throw new RuntimeException("获取时间间隔异常");
		}
		if (beginAndendTimeCompare==0) {
			logger.info("开始时间结束时间不能相同");
			throw new RuntimeException("开始时间结束时间不能相同");
		}
		/*结束小于当天的时间*/ 
		if (endTimeCompare<0) {
			logger.info("结束时间小于等于当前时间");
			throw new RuntimeException("结束 时间小于当前时间");
		}else {
			/*结束大于等于当天的时间*/
			//1：开始时间大于等于当前时间且和结束时间差大于90天 ,则结束时间在开始时间基础上增加90天
			if (beginTimeCompare>=0&&beginAndEndtimeSpace > DEFAULTDAYS) {
				//endtime = DateUtil.addDateOneDay(begintime, DEFAULTDAYS);
				endtime.setTime(DateUtil.addDateOneDay(begintime, DEFAULTDAYS).getTime());
			//2：开始时间小于当前时间且和结束时间差小于等于90天 ,则开始时间等于当前时间的前一天
			}else if (beginTimeCompare<0) {
				begintime.setTime(DateUtil.addDateOneDay(nowDate, -1).getTime());
				if (beginAndEndtimeSpace>DEFAULTDAYS) {
					try {
						int timeCompare = DateUtil.daysBetween(begintime, endtime);
						if (timeCompare > DEFAULTDAYS) {
							//endtime = DateUtil.addDateOneDay(begintime, DEFAULTDAYS);
							endtime.setTime(DateUtil.addDateOneDay(begintime, DEFAULTDAYS).getTime());
						}
						
					} catch (Exception e) {
						logger.error("获取时间间隔异常",e);
						throw new RuntimeException("获取时间间隔异常");
					}
				}
			}
		}
		
	}
	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begintime= null;
		try {
			begintime = sdf.parse("2016-03-24");
		} catch (Exception e) {
		}
		
		int timespace = 0;
		try {
			 timespace = DateUtil.daysBetween(begintime, new Date());
			 System.out.println(timespace);
		} catch (Exception e1) {
		}

	}
}
