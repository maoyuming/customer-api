//package com.fangbaba.api.kafka;
//
//import java.util.List;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.fangbaba.api.util.BusinessUtil;
//import com.fangbaba.basic.face.bean.HotelModel;
//import com.fangbaba.basic.face.bean.RoomtypeModel;
//import com.fangbaba.basic.face.service.HotelService;
//import com.fangbaba.basic.face.service.RoomtypeService;
//import com.fangbaba.gds.enums.GdsChannelUrlEnum;
//import com.fangbaba.gds.face.service.IHotelChannelSettingService;
//import com.fangbaba.gds.po.HotelChannelSetting;
//import com.google.common.base.Strings;
//import com.google.gson.Gson;
//import com.mk.kafka.client.stereotype.MkMessageService;
//import com.mk.kafka.client.stereotype.MkTopicConsumer;
//
///**
// * zhangyajun
// * description:
// */
//@MkMessageService
//public class HotelChangePushConsumer {
//
//    private Logger logger = LoggerFactory.getLogger(HotelChangePushConsumer.class);
//
//    @Autowired
//    private HotelService hotelService;
//    @Autowired
//    private RoomtypeService roomtypeService;
//    @Autowired
//	private BusinessUtil businessUtil;
//    @Autowired
//    private IHotelChannelSettingService hotelChannelSettingService;
//    
//    /**
//     * 酒店增量推送
//     *
//     * @param hotelTag
//     */
//    @MkTopicConsumer(topic = "Basic_hotel_sync_change", group = "Hotel_IncrementPush", serializerClass = "com.mk.kafka.client.serializer.StringDecoder")
//    public void incrementPush(String hotelid) {
//    	logger.info("酒店增量推送传来参数hotelid:{}",hotelid);
//    	HotelModel hotelModel = null;
//    	List<RoomtypeModel> roomtypeModels = null;
//        try {
//        	hotelModel = hotelService.queryById(Long.parseLong(hotelid));
//        	roomtypeModels = roomtypeService.queryByHotelId(hotelModel.getId());
//		} catch (Exception e) {
//			logger.error("查询酒店或房型出错",e);
//		}
//        if (hotelModel == null) {
//			logger.info("酒店对象为空");
//			return ;
//		}
//        String roomtypejson ="";
//        String json  = new Gson().toJson(hotelModel);
//        if (CollectionUtils.isNotEmpty(roomtypeModels)) {
//        	roomtypejson = new Gson().toJson(roomtypeModels);
//		}else {
//			logger.info("该房型下未能查询到酒店");
//		}
//        List<HotelChannelSetting> hotelChannelSettings = hotelChannelSettingService.queryHotelChannelSettingByHotelid(Long.parseLong(hotelid));
//        if (hotelChannelSettings == null || hotelChannelSettings.size() == 0) {
//            logger.warn("该渠道下无酒店. hotelid:{}", hotelid);
//        }else{
//        	if (Strings.isNullOrEmpty(roomtypejson)) {
//        		 for (HotelChannelSetting hotelChannelSetting : hotelChannelSettings) {
//                     businessUtil.push(GdsChannelUrlEnum.pushHotel.getId(), json,hotelChannelSetting.getChannelid(),hotelid,null);
//                 }
//			}else {
//				 for (HotelChannelSetting hotelChannelSetting : hotelChannelSettings) {
//		                businessUtil.push(GdsChannelUrlEnum.pushHotel.getId(), json,hotelChannelSetting.getChannelid(),hotelid,null);
//		                businessUtil.push(GdsChannelUrlEnum.pushRoomtype.getId(), roomtypejson,hotelChannelSetting.getChannelid(),hotelid,null);
//		           }
//			}
//           
//        }    
//    }
//    
//    
//    
//}
