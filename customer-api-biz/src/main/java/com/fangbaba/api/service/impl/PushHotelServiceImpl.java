/**
 * 2016年3月28日下午8:13:31
 * zhaochuanbin
 */
package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.open.hotel.HotelDetail;
import com.fangbaba.api.domain.open.hotel.Roomtype;
import com.fangbaba.api.service.PushHotelService;
import com.fangbaba.api.service.PushRoomtypeService;
import com.fangbaba.api.service.RoomtypeInfoService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.basic.face.bean.HotelExtension;
import com.fangbaba.basic.face.service.HotelExtensionService;
import com.fangbaba.basic.face.service.RoomtypeService;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.base.RetInfo;
import com.fangbaba.gds.face.bean.EsSearchBean;
import com.fangbaba.gds.face.bean.HotelModelEsBean;
import com.fangbaba.gds.face.service.IHotelSearchService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * @author zhaochuanbin
 *
 */
@Service
public class PushHotelServiceImpl implements PushHotelService {

    private static Logger logger = LoggerFactory.getLogger(PushHotelServiceImpl.class);
    @Autowired
    private RoomtypeService roomtypeService;
    @Autowired
    private IHotelSearchService iHotelSearchService;
    @Autowired
    private Mapper dozerMapper;
    @Autowired
    private RoomtypeInfoService roomtypeInfoService;
    @Autowired
    private HotelExtensionService hotelExtensionService;
    @Autowired
    private PushRoomtypeService pushRoomtypeService;
    @Autowired
    private BusinessUtil businessUtil;

	private ExecutorService executorService = null;

	

	private static final int THREAD_COUNT = 20;
	private static final int STEP = 1000;
	
    private static Gson gson = new Gson();
    

	//同步房型操作表示
	public static String ACTION_ADD = "add";
	public static String ACTION_MODIFY = "modify";
	public static String ACTION_DELETE = "delete";
    
	
	/**
	 * 全量推送某个渠道的酒店
	 */
    @Override
    public void otatypeRes(Long otatype,Integer channelId) {
        try{

        	this.executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        	
            HotelModelEsBean hotelModelEsBean = new HotelModelEsBean();
            hotelModelEsBean.setOtatype(otatype);
            int pagesize = 100;
            EsSearchBean bean = new EsSearchBean();
            bean.setPage(1);
            bean.setSize(999999999);
            Map<String, String> dynamicCondition = Maps.newHashMap();
            Integer totalnum = iHotelSearchService.searchHotelFromESCount(hotelModelEsBean, dynamicCondition, bean);
            Integer totalpage = totalnum/pagesize +1;
            logger.info("全量推送totalnum:"+totalnum+",totalpage:"+totalpage);
            
            if(totalpage>=1){
                for (int i = 1; i <= totalpage; i++) {
                	this.executorService.execute(new SendTask(hotelModelEsBean, dynamicCondition, i, pagesize, channelId, this));
                }
            }   
        }catch(Exception e){
            logger.info("error",e);
        }finally{
			executorService.shutdown();
        }
    }
    
    private class SendTask implements Runnable {

		private HotelModelEsBean hotelModelEsBean;
		private Map<String, String> dynamicCondition;
		private Integer channelId;
		private PushHotelServiceImpl pushHotelService;
		private int page;
		private int pageSize;

		public SendTask(HotelModelEsBean hotelModelEsBean, Map<String, String> dynamicCondition, int page,int pageSize,Integer channelId,PushHotelServiceImpl pushHotelService) {
			this.hotelModelEsBean = hotelModelEsBean;
			this.dynamicCondition = dynamicCondition;
			this.channelId = channelId;
			this.pushHotelService = pushHotelService;
			this.page = page;
			this.pageSize = pageSize;
		}

		@Override
		public void run() {

            EsSearchBean bean = new EsSearchBean();
            bean.setPage(page);
            bean.setSize(pageSize);
			List<HotelDetail> listHotelDetails2 = pushHotelService.querySendHotel(hotelModelEsBean, dynamicCondition, bean);
            StringBuffer hotelids  =new StringBuffer();
            for (HotelDetail hotelDetail:listHotelDetails2) {
            	hotelids.append(hotelDetail.getId()+",");
			}
            if(hotelids.length()>0){
            	hotelids.setLength(hotelids.length()-1);
            }
            logger.info("全量推送第"+page+"次发送日志开始,共:"+listHotelDetails2.size()+"个");
            businessUtil.push(GdsChannelUrlEnum.pushHotel.getId(), gson.toJson(listHotelDetails2),channelId,hotelids.toString(),null);
            logger.info("全量推送第"+page+"次发送日志结束,共:"+listHotelDetails2.size()+"个");
		}
	}
    
    /**
     * 查询待推送酒店
     * @param hotelModelEsBean
     * @param dynamicCondition
     * @param bean
     * @return
     */
    public List<HotelDetail> querySendHotel(HotelModelEsBean hotelModelEsBean, Map<String, String> dynamicCondition, EsSearchBean bean){
        List<HotelDetail> listhoteDetails = new ArrayList<HotelDetail>();
        List<HotelModelEsBean> listhotel = iHotelSearchService.searchHotelFromES(hotelModelEsBean, dynamicCondition, bean);
        for(HotelModelEsBean hotelModelEsBean2:listhotel){
            HotelDetail hotelDetail   = getHotelDetail(hotelModelEsBean2);
            logger.info("全量推送hotelDetail:"+gson.toJson(hotelDetail));
            listhoteDetails.add(hotelDetail);
        }
        return listhoteDetails;
    }

    
    /**
     * 推送酒店信息
     */
    @Override
    public void pushHotel(Long hotelId,Integer channelId) {
    	
//    	if(action==null || ACTION_ADD.equals(action) || ACTION_MODIFY.equals(action)){
    		pushSaveOrUpdateHotel(hotelId, channelId);
//		}else if(ACTION_DELETE.equals(action)){
			//delete hotel
//			pushDeleteHotel(hotelId, channelId);
//		}
        
    }
    
    /**
     * 推送新增和修改酒店
     * @param hotelId
     * @param channelId
     * @param action
     */
    private void pushSaveOrUpdateHotel(Long hotelId,Integer channelId){
    	try {
            //查询酒店信息

    		HotelModelEsBean hotelModelEsBean = new HotelModelEsBean();
    		hotelModelEsBean.setId(hotelId);
    		hotelModelEsBean.setChannelId(channelId);
    		RetInfo<HotelModelEsBean> retInfo = iHotelSearchService.queryHotelByHotelId(hotelModelEsBean);
    		if(!retInfo.isResult()){
    			logger.info("查询酒店:{}返回false",hotelId);
    			return;
    		}
    		HotelModelEsBean hotelModelEsBean2 = retInfo.getObj();
    		
    		if(hotelModelEsBean2==null){
    			logger.info("查询酒店返回为空");
    			return;
    		}
             
    		List<HotelDetail> listHotelDetails2 = new ArrayList<HotelDetail>();
            HotelDetail hotelDetail   = getHotelDetail(hotelModelEsBean2);
            listHotelDetails2.add(hotelDetail);
            String json  = new Gson().toJson(listHotelDetails2);
            //推送酒店信息
            businessUtil.push(GdsChannelUrlEnum.pushHotel.getId(), json,channelId,hotelDetail.getId().toString(),null);
        } catch (Exception e) {
            logger.info("HotelConsumer apipush_hotel_channel_open error:",e);
        }
    }
    
    

    /**
     * 推送删除酒店
     * @param hotelId
     * @param channelId
     * @param action
     */
    public void pushDeleteHotel(Long hotelId,Integer channelId){

		Map<String,Object> hotelMap = new HashMap<String,Object>();
		hotelMap.put("hotelid", hotelId);
		/*格式说明：
		{
	    	"hotelid": "2811, 2311,22333"
	    }
	    */
		String json = new Gson().toJson(hotelMap);
    	businessUtil.push(GdsChannelUrlEnum.pushDeleteHotel.getId(), json,channelId,hotelId+"",null);
    }
    
    
    
    /**
     * 获取对外的酒店bean
     * @param hotelModelEsBean2
     * @return
     */
    private  HotelDetail getHotelDetail(HotelModelEsBean hotelModelEsBean2){
    	 List<Roomtype> roomtypes = pushRoomtypeService.queryRoomtypeByHotelId(hotelModelEsBean2.getId());
 		
    	
         HotelDetail hotelDetail = dozerMapper.map(hotelModelEsBean2,HotelDetail.class);
         if (Strings.isNullOrEmpty(hotelDetail.getHotelpics())) {
        	 HotelExtension hotelExtension = hotelExtensionService.getHotelExtension(hotelModelEsBean2.getId());
             if (hotelExtension!=null&&hotelExtension.getHotelpic()!=null) {
            	 hotelDetail.setHotelpics(hotelExtension.getHotelpic());
             }
		 }
        
         hotelDetail.setRoomtypes(roomtypes);
         return hotelDetail;
    }
    
}
