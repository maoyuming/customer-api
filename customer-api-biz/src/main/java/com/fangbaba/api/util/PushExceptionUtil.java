package com.fangbaba.api.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fangbaba.api.core.RedisCacheManager;
import com.fangbaba.basic.face.bean.HotelModel;
import com.fangbaba.basic.face.bean.RoomtypeModel;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.base.OtaDeployStatusEnum;
import com.fangbaba.gds.face.bean.OtaHotel;
import com.fangbaba.gds.face.bean.OtaRoomtype;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.gds.po.HotelOnsale;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * 推送失败存储redis队列util
 * 
 * @author jianghe
 *
 */
@Component
public class PushExceptionUtil {

	private static Logger logger = LoggerFactory.getLogger(PushExceptionUtil.class);

	@Autowired
	private RedisCacheManager redisCacheManager;

	private static final String cacheName = "api_push_error_info";

	public static ExecutorService pool = Executors.newFixedThreadPool(10);

	private Gson gson = new Gson();

	public static String getQueueRedisName(GdsChannelUrlEnum gdsChannelUrlEnum, ChannelEnum channelEnum) {
		return cacheName + "@" + channelEnum.getId() + "@" + gdsChannelUrlEnum.getId();
	}

	/**
	 * 推送失败后存储到redis队列
	 * 
	 * @param hotelid
	 *            酒店id
	 * @param orderid
	 *            订单id
	 */
	public void cachePushErrorInfo(final GdsChannelUrlEnum gdsChannelUrlEnum, final ChannelEnum channelEnum, final String hotelids,
			final String orderid) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String pushbusinessconfig = Config.getValue("push_business_config");
					Map<String, String> retMap = gson.fromJson(pushbusinessconfig,
							new TypeToken<Map<String, String>>() {
							}.getType());
					String channelid = retMap.get("channelid");
					String pushbusiness = retMap.get("pushbusiness");
					String queuename = getQueueRedisName(gdsChannelUrlEnum, channelEnum);
					// 判断是否配置此业务推送
					if (channelid.contains(channelEnum.getId() + "")
							&& pushbusiness.contains(gdsChannelUrlEnum.getId() + "")) {
						if (gdsChannelUrlEnum.equals(GdsChannelUrlEnum.pushOrderStatus)) {// 订单
							// 放入redis
							Map<String, String> param = new HashMap<String, String>();
							param.put(orderid, DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
							String queuevalue = gson.toJson(param);
							redisCacheManager.lpush(queuename, queuevalue, 60 * 60 * 24 * 7);
							logger.info("cachePushErrorInfo push queuename:{},queuevalue:{}", queuename, queuevalue);
						} else if (gdsChannelUrlEnum.equals(GdsChannelUrlEnum.pushHotel)
								|| gdsChannelUrlEnum.equals(GdsChannelUrlEnum.pushRoomtype)
								|| gdsChannelUrlEnum.equals(GdsChannelUrlEnum.pushPrice)
								|| gdsChannelUrlEnum.equals(GdsChannelUrlEnum.pushTags)) {
							// 放入redis
							Map<String, String> param = new HashMap<String, String>();
							if (hotelids != null) {
								for (String hotelid : hotelids.split(",")) {
									param.put(hotelid, DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
								}
							}
							String queuevalue = gson.toJson(param);
							redisCacheManager.lpush(queuename, queuevalue, 60 * 60 * 24 * 7);
							logger.info("cachePushErrorInfo push queuename:{},queuevalue:{}", queuename, queuevalue);
						}
					}
				} catch (Exception e) {
					logger.error("cachePushErrorInfo:", e);
				}
			}
		});

	}
}
