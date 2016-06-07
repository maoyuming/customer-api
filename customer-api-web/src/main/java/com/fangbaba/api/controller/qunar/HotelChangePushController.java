package com.fangbaba.api.controller.qunar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.core.RedisCacheManager;
import com.fangbaba.api.domain.qunar.ChangeHotelInfo;
import com.fangbaba.api.domain.qunar.HotelChange;
import com.fangbaba.api.util.XstreamSingletonUtil;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;



@Controller
@RequestMapping(value = "/qunar/hotelchange")
public class HotelChangePushController {
	private static Logger logger = LoggerFactory.getLogger(HotelChangePushController.class);
	@Autowired
    private RedisCacheManager redisCacheManager;
    
    private static final String cacheName = "gds_hotel_change";
    
	/**
	 *酒店变价推送
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getChangeHotelInfo")
	public void pricechangepush(HttpServletRequest request, HttpServletResponse response,String lastupdate) throws IOException {
		logger.info("qunar 酒店变价推送传来的参数lastupdate:{}",lastupdate);
		
		ChangeHotelInfo changeHotelInfo = new ChangeHotelInfo();
		
		XStream xstream = XstreamSingletonUtil.getXstream(ChangeHotelInfo.class.getName());
		xstream.autodetectAnnotations(true);
		xstream.alias("changed", ChangeHotelInfo.class); 
		xstream.ignoreUnknownElements();
		try {
			
			List<HotelChange> hotels = new ArrayList<HotelChange>();
			
//			while(true){
			Gson gson = new Gson();
			for (int i = 0; i < 100; i++) {
				String result = redisCacheManager.rpop(cacheName);
				
				Map<String, String> map = gson.fromJson(result, Map.class);
				if(MapUtils.isNotEmpty(map)){
					for (Entry<String, String> entry : map.entrySet()) {
						HotelChange hotelChange = new HotelChange();
						hotelChange.setId(Long.valueOf(entry.getKey()));
						hotelChange.setUpdatetime(entry.getValue());
						//redisCacheManager.remove(cacheName, entry.getKey());//TODO:确定是否要删除缓存？？？
						hotels.add(hotelChange);
					}
				}
			}
				
//				if(result==null){
//					break;
//				}
//			}
			
//        Map<String, String> map = redisCacheManager.hgetAll(cacheName);
//        if(MapUtils.isNotEmpty(map)){
//        	for (Entry<String, String> entry : map.entrySet()) {
//        		HotelChange hotelChange = new HotelChange();
//        		hotelChange.setId(Long.valueOf(entry.getKey()));
//        		hotelChange.setUpdatetime(entry.getValue());
//        		//redisCacheManager.remove(cacheName, entry.getKey());//TODO:确定是否要删除缓存？？？
//        		hotels.add(hotelChange);
//			}
//        }
			changeHotelInfo.setHotels(hotels);
		} catch (Exception e) {
		}
        String xml =  xstream.toXML(changeHotelInfo);

		response.getWriter().write(xml);
		response.setContentType("application/xml");
//        return new ResponseEntity<String>(xml, HttpStatus.OK);
//        return new ResponseEntity<String>(xml, HttpStatus.OK);
        //return hotels;
//        return new ResponseEntity<String>("<list> <hotel id=\"1\" city=\"beijing_city\" name=\"北京大饭店\" address=\"苏州街\" tel=\"010-66666666\">    </hotel></list>", HttpStatus.OK);
	}
	
	/**
	 *测试
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/test")
	public void test(HttpServletRequest request, HttpServletResponse response,String hotelid) throws IOException {
		String[] hotelAarry = hotelid.split(",");
		
		for (String string : hotelAarry) {
			//放入redis
			Map<String, String> param = new HashMap<String, String>();
			param.put(string, new Date().getTime()+"");
//			redisCacheManager.hmset(cacheName, param, 10);
			
			
			redisCacheManager.lpush(cacheName, new Gson().toJson(param),60*60*24*7);
		}
		
//		redisCacheManager.lpush(cacheName, 11111+"");
//		while(true){
//			String result = redisCacheManager.rpop(cacheName);
//			if(result==null){
//				break;
//			}
//		}
//		Map<String, String> map = redisCacheManager.hgetAll(cacheName);
		
//		System.out.println(redisCacheManager.getExpires(cacheName, hotelid+""));
		
	}
	
}
