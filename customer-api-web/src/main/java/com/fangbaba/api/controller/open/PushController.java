package com.fangbaba.api.controller.open;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.core.RedisCacheManager;
import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.domain.open.hotel.PushExceptionOutputBean;
import com.fangbaba.api.domain.open.hotel.PushExceptionOutputInfoBean;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.util.PushExceptionUtil;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Controller
@RequestMapping(value = "/open/push")
public class PushController {
	private static Logger logger = LoggerFactory.getLogger(PushController.class);
	
	@Autowired
    private RedisCacheManager redisCacheManager;    
	@Autowired
	private PushExceptionUtil pushExceptionUtil;
	
	private Gson gson = new Gson();
	/**
	 * 查询push错误信息
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/queryfaillist")
    public ResponseEntity<OpenResponse<PushExceptionOutputBean>> queryfaillist(HttpServletRequest request,@RequestBody  String body) {
		logger.info("queryfaillist begin {}",body);
		
		String channelid = request.getHeader("channelid");
		
		Map<String, String> retMap = gson.fromJson(body,  
				new TypeToken<Map<String, String>>() {  
		}.getType());
		if (retMap==null || retMap.get("exceptiontype")==null) {
			logger.info("请求参数为空");
			throw new OpenException(DistributionErrorEnum.argsNull.getName(),DistributionErrorEnum.argsNull.getId());
		}
		String exceptiontype = retMap.get("exceptiontype");
        
		OpenResponse<PushExceptionOutputBean> openResponse = new OpenResponse<PushExceptionOutputBean>();

		
		openResponse.setResult(true+"");
		PushExceptionOutputBean pushExceptionOutputBean = new PushExceptionOutputBean();
		boolean isorder = false;
		isorder = GdsChannelUrlEnum.getById(Integer.parseInt(exceptiontype)).equals(GdsChannelUrlEnum.pushOrderStatus);
		for (int i = 0; i < 100; i++) {
			String result = redisCacheManager.rpop(PushExceptionUtil.getQueueRedisName(GdsChannelUrlEnum.getById(Integer.parseInt(exceptiontype)), ChannelEnum.getById(Integer.parseInt(channelid))));
			
			Map<String, String> map = gson.fromJson(result,  
					new TypeToken<Map<String, String>>() {  
			}.getType());
			if(MapUtils.isNotEmpty(map)){
				for (Entry<String, String> entry : map.entrySet()) {
					PushExceptionOutputInfoBean pushExceptionOutputInfoBean= new PushExceptionOutputInfoBean();
					pushExceptionOutputInfoBean.setId(entry.getKey());
					pushExceptionOutputInfoBean.setUpdatetime(entry.getValue());
					if(isorder){
						pushExceptionOutputBean.getOrders().add(pushExceptionOutputInfoBean);
					}else{
						pushExceptionOutputBean.getHotels().add(pushExceptionOutputInfoBean);
					}
				}
			}else{
				break;
			}
		}
		openResponse.setData(pushExceptionOutputBean);
		logger.info("queryfaillist return:{}",gson.toJson(openResponse));
		return new ResponseEntity<OpenResponse<PushExceptionOutputBean>>(openResponse, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/testpush")
    public ResponseEntity<String> testpush(HttpServletRequest request,@RequestBody  String body) {
		String channelid = request.getHeader("channelid");
		
		Map<String, String> retMap = gson.fromJson(body,  
				new TypeToken<Map<String, String>>() {  
		}.getType());
		if (retMap==null || retMap.get("exceptiontype")==null) {
			logger.info("请求参数为空");
			throw new OpenException(DistributionErrorEnum.argsNull.getName(),DistributionErrorEnum.argsNull.getId());
		}
		String exceptiontype = retMap.get("exceptiontype");
		String hotelid = retMap.get("hotelid");
		String orderid = retMap.get("orderid");
		
		pushExceptionUtil.cachePushErrorInfo(GdsChannelUrlEnum.getById(Integer.parseInt(exceptiontype)), ChannelEnum.getById(Integer.parseInt(channelid)), hotelid, orderid);
		
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
}
