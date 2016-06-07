package com.fangbaba.api.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fangbaba.api.common.GlobalCache;
import com.fangbaba.api.domain.fangcang.Header;
import com.fangbaba.api.domain.fangcang.Request;
import com.fangbaba.api.domain.fangcang.Response;
import com.fangbaba.api.enums.FangCangRequestTypeEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.util.pushlog.PushLog;
import com.fangbaba.api.util.pushlog.PushLogUtil;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.service.IGdsChannelService;
import com.fangbaba.gds.face.service.IGdsChannelUrlService;
import com.fangbaba.gds.face.service.IHotelChannelSettingService;
import com.fangbaba.gds.po.GdsChannel;
import com.fangbaba.gds.po.GdsChannelUrl;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;


@Component
public class BusinessUtil<T> {


	private static Logger logger = LoggerFactory.getLogger(BusinessUtil.class);
	
	@Autowired
	private IGdsChannelUrlService iGdsChannelUrlService;
	@Autowired
	private IHotelChannelSettingService iHotelChannelSettingService;

    @Autowired
    private IGdsChannelService gdsChannelService;
    
    private static  ConcurrentHashMap<String,XStream> xstreamMap = new ConcurrentHashMap<String,XStream>();
    
    private Gson gson = new Gson();
    
    @Autowired
	private PushExceptionUtil pushExceptionUtil;
    
    @Resource
    private PushLogUtil pushLogUtil;
    
	

	
	/**
     * 线程队列的最大长度
     */
    private static int MAX_LENGTH = 2000;

    /**
     * 线程池的保存线程数
     */
    private static  int corePoolSize = 20;

    /**
     * 最大线程数
     */
    private  static int maximumPoolSize = 30;

    /**
     * 超过规模线程最大存活时间
     */
    private  static long keepAliveTime = 1L;


    /**
     * 线程最大等待时间
     */
    private  static int timeout = 10;

    /**
     * 线程休眠时间
     */
    private static  long threadSleep = 50;

    protected static ThreadPoolExecutor threadPool;
	
	
	static{
		ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(MAX_LENGTH);
	    threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
	}
	
	
	public  void push(final  Integer urlId, final String json,final Integer channelId,final String hotelids,final String orderid){
   	 	logger.info("推送数据开始");
        List<GdsChannel> gdsChannels = new ArrayList<GdsChannel>();
        //如果channelid 为一个
        if (channelId!=null) {
            GdsChannel gdsChannel =  gdsChannelService.queryByChannelId(channelId);
            if (gdsChannel!=null) {
            	 gdsChannels.add(gdsChannel);
			}
           
        }else{
            gdsChannels = gdsChannelService.queryAllGdsChannel();
        }
        logger.info("gdsChannels的长度:"+gdsChannels.size());
        
        if(CollectionUtils.isNotEmpty(gdsChannels)){
        	for (GdsChannel gdsChannel : gdsChannels) {
        		String result = null;
        		boolean flag = false;
        		try {
        			if(StringUtils.isBlank(json)){
        				continue;
        			}
        			result = doPushJson(urlId, json, gdsChannel.getChannelid());
        			flag = true;
				} catch (Exception e) {
					//异常存储到redis队列中
					pushExceptionUtil.cachePushErrorInfo(GdsChannelUrlEnum.getById(urlId), ChannelEnum.getById(channelId), hotelids, orderid);
					logger.error("推送数据error:",e);
				}finally{
					//存储推送日志到mongo
        			PushLog pushLog = new PushLog();
        	    	pushLog.setPushtype(GdsChannelUrlEnum.getById(urlId).getId()+"");
        	    	pushLog.setPushtypedesc(GdsChannelUrlEnum.getById(urlId).getName());
        	    	pushLog.setPushcontent(json);
        	    	pushLog.setResult(result);
        	    	pushLog.setChannelid(gdsChannel.getChannelid().toString());
        	    	pushLog.setResultflag(flag);
        			pushLogUtil.savePushLog(pushLog);
				}
        	}
        	
        }
        logger.info("推送数据完毕");
   }
	
	
    /**
     * 推送消息
     * @param urlId
     * @param json
     * @return
     * @throws Exception 
     */
    public String doPushJson(final  Integer urlId, final String json,final Integer channelId) throws Exception {
    	return doPush(urlId, json, channelId, "application/json");
    }
    /**
     * 推送消息
     * @param urlId
     * @param json
     * @return
     * @throws Exception 
     */
    public String doPushXml(final  Integer urlId, final String xml,final Integer channelId) throws Exception {
    	return doPush(urlId, xml, channelId, "application/xml");
    }
    
    /**
     * 推送消息
     * @param urlId
     * @param json
     * @return
     * @throws Exception 
     */
    public String doPush(final  Integer urlId, final String content,final Integer channelId,String contentType) throws Exception {

    	
		if(urlId==null){
			throw new OpenException("url标示为空");
		}
		if(StringUtils.isEmpty(content)){
			throw new OpenException("推送的content为空");
		}
		if(channelId==null){
			throw new OpenException("渠道标示为空");
		}
		
		//1、根据urlid查询所有要推送的渠道
		//2、推送
		
    	GdsChannelUrl channelUrl  = GlobalCache.getInstance().getGdsChannelUrlMap().get(channelId+"_"+urlId);
    	if(channelUrl==null){
    		channelUrl =  iGdsChannelUrlService.queryGdsChannelUrlByChannelAndUrlid(channelId, urlId); 
    	}
		
		
    	final GdsChannelUrl gdsChannelUrl = channelUrl;
		if(gdsChannelUrl!=null){
			
			if((channelId!=null&&gdsChannelUrl.getChannelid().equals(channelId))
					||channelId==null){
				//check酒店是否开通该渠道的分销
				
				final Map<String, String> headerMap = new HashMap<String, String>();
				if(StringUtils.isNotBlank(gdsChannelUrl.getToken())){
					headerMap.put("token", gdsChannelUrl.getToken());
				}
				//调用推送接口，推送数据
				logger.info("推送入参{}",content);
				String result   = PostUtils.doPost(content, gdsChannelUrl.getUrl(),headerMap,contentType);
				logger.info("推送结果：{}",result);
				return result;
			}
		}
		
		return null;
    }
    /**
     * 推送消息
     * @param urlId
     * @param json
     * @return
     * @throws Exception 
     */
    public String doPost(final  Integer urlId, final Map<String, String> param,final Integer channelId) {
    	
    	
    	if(urlId==null){
    		throw new OpenException("url标示为空");
    	}
    	if(MapUtils.isEmpty(param)){
    		throw new OpenException("推送的content为空");
    	}
    	if(channelId==null){
    		throw new OpenException("渠道标示为空");
    	}
    	
    	//1、根据urlid查询所有要推送的渠道
    	//2、推送
    	
    	GdsChannelUrl channelUrl  = GlobalCache.getInstance().getGdsChannelUrlMap().get(channelId+"_"+urlId);
    	if(channelUrl==null){
    		channelUrl =  iGdsChannelUrlService.queryGdsChannelUrlByChannelAndUrlid(channelId, urlId); 
    	}
    	
    	
    	final GdsChannelUrl gdsChannelUrl = channelUrl;
    	if(gdsChannelUrl!=null){
    		
    		if((channelId!=null&&gdsChannelUrl.getChannelid().equals(channelId))
    				||channelId==null){
    			//check酒店是否开通该渠道的分销
    			
    			final Map<String, String> headerMap = new HashMap<String, String>();
    			if(StringUtils.isNotBlank(gdsChannelUrl.getToken())){
    				headerMap.put("token", gdsChannelUrl.getToken());
    			}
    			logger.info("推送入参：{},{}",new Gson().toJson(gdsChannelUrl),new Gson().toJson(param));
    			//调用推送接口，推送数据
    			String result = HttpUtil.doPost(gdsChannelUrl.getUrl(),param,10000);
    			logger.info("推送结果：{}",result);
    			return result;
    		}
    	}
    	
    	return null;
    }
    
    
    /**
     * 获取天下房仓的header
     * @return
     */
    public Header getFangCangHeader(String requestType){
    	String securityCode = Config.getValue("fangbang.SecurityCode");
    	logger.info("加密前securityCode={}",securityCode); 
    	securityCode = MD5Util.encryption(securityCode).toUpperCase();
    	logger.info("加密后securityCode={}",securityCode);
    	Header header = new Header();
    	header.setTimeStamp(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
    	header.setRequestType(requestType);
    	header.setPartnerCode(Config.getValue("fangcang.PartnerCode"));
    	
    	String signature =  header.getTimeStamp() + header.getPartnerCode() + securityCode + requestType;
    	logger.info("加密前signature={}",signature); 
    	signature =  MD5Util.encryption(signature).toUpperCase();
    	logger.info("加密后signature={}",signature); 
    	header.setSignature(signature);
    	return header;
    }
    
    /**
     * @param header
     * 校验signature
     */
    public boolean isMatchFangCangSignature(Header header){
    	String securityCode = Config.getValue("fangbang.SecurityCode");
    	logger.info("X加密前securityCode={}",securityCode); 
    	securityCode = MD5Util.encryption(securityCode).toUpperCase();
    	logger.info("X加密后securityCode={}",securityCode);
    	String signature =  header.getTimeStamp() + header.getPartnerCode() + securityCode + header.getRequestType();
    	logger.info("X加密前signature={}",signature); 
    	signature =  MD5Util.encryption(signature).toUpperCase();
    	logger.info("X加密后signature={}",signature); 
    	return signature.equals(header.getSignature());
    }
    
    
    /**
     * 获取房仓的request
     * @param request
     * @param requestType
     * @return
     */
    public  Map<String, String> genFangCangRequest(Request request,FangCangRequestTypeEnum requestType){
    	request.setHeader(getFangCangHeader(requestType.getRequestType()));
    	/*XStream xstream = new XStream(); */
    	XStream xstream = XstreamSingletonUtil.getXstream(requestType.getRequestType());
        xstream.alias("Request", request.getClass());
        xstream.autodetectAnnotations(true);
        xstream.ignoreUnknownElements();
        xstream.toXML(request,new PrintWriter(System.out));  
        String xml =  xstream.toXML(request);
        xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+xml;
        
        
        Map<String, String> param = new HashMap<String, String>();
		param.put("xml", xml);
        return param;
    }
    /**
     * 获取房仓的response
     * @param response
     * @param requestType
     * @return
     */
    public  String genFangCangResponse(Response response,String objectName){
    	/*XStream xstream = new XStream(); */ 
    	XStream xstream = XstreamSingletonUtil.getXstream(objectName);
    	xstream.alias("Response", response.getClass());
    	xstream.autodetectAnnotations(true);
    	xstream.ignoreUnknownElements();
    	xstream.toXML(response,new PrintWriter(System.out));  
    	String xml =  xstream.toXML(response);
    	xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+xml;
    	
    	
    	return xml;
    }
    /**
     * 解析房仓 的xml
     * @param request
     * @param requestType
     * @return
     */
    public  T decodeRequestXml(String xml,Class<T> clazz){
    	XStream xstream = XstreamSingletonUtil.getXstream(clazz.getName());
    	/*XStream xstream = new XStream(); */ 
    	xstream.alias("Request", clazz);
    	xstream.ignoreUnknownElements();
    	xstream.autodetectAnnotations(true);
//    	xstream.fromXML(xml,new PrintWriter(System.out));  
    	Object obj =  xstream.fromXML(xml);
    	return (T)obj;
    }
    /**
     * 解析房仓 的xml
     * @param request
     * @param requestType
     * @return
     */
    public  T decodeResponseXml(String xml,Class<T> clazz){
    	XStream xstream = XstreamSingletonUtil.getXstream(clazz.getName());
    	/*XStream xstream = new XStream();  */
    	xstream.alias("Response", clazz);
    	xstream.autodetectAnnotations(true);
    	xstream.ignoreUnknownElements();
//    	xstream.fromXML(xml,new PrintWriter(System.out));  
    	Object obj =  xstream.fromXML(xml);
    	return (T)obj;
    }
    
    /**
     * @param xml
     * @return
     * 通过dom4j解析xml中的header
     */
    public Header ParseHeaderByDom4j(String xml) throws Exception{
    	Header header  = new Header();
    	Document document = DocumentHelper.parseText(xml);
    	Element root = document.getRootElement();
    	Element headerElement = root.element("Header");
    	header.setPartnerCode(headerElement.attribute("PartnerCode").getStringValue());
    	header.setRequestType(headerElement.attribute("RequestType").getStringValue());
    	header.setTimeStamp(headerElement.attribute("TimeStamp").getStringValue());
    	header.setSignature(headerElement.attribute("Signature").getStringValue());
    	return header;
    }

//    public static void main(String[] args) {
//
//    	String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?> <Response>     <GetHotelInfoResponse>       <HotelList>         <HotelInfo>          <FcHotelId>171102</HotelId>           <FcHotelChnName>中文名</HotelChnName>         <FcChnAddress>中文地址</ChnAddress>         <FcTelephone>电话</Telephone>           <FcWebSiteURL>网站</WebSiteURL>          <FcHotelStar>星级</HotelStar>           <FcCity>城市</City>           <FcDistinct>行政区</Distinct>           <FcBusiness>商业区</Business>         <Rooms>            <RoomType>              <FcRoomTypeId>房型id</RoomTypeId>               <FcRoomTypeName>房型名称</RoomTypeName>               <FcBedType>床型</BedType>           </RoomType>         </Rooms>        </HotelInfo>   </HotelList> </GetHotelInfoResponse>       <ResultCode>Success</ResultCode> 	<ResultNo>000</ResultNo> 	<ResultMsg>success</ResultMsg> </Response>";
//    	XStream xstream = new XStream();  
//    	xstream.alias("Response", HotelInfoResponse.class);
//    	xstream.autodetectAnnotations(true);
//    	HotelInfoResponse hotelInfoResponse =  (HotelInfoResponse)xstream.fromXML(xml);
//    	
//    	System.out.println(hotelInfoResponse);
//	}
    public static void main(String[] args) throws Exception {
    	
    	String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?> <Request>    "
    			+ "<Header TimeStamp=\"2014-02-16 15:36:48\" PartnerCode=\"F01202154\"  RequestType= \"cancelHotelOrder\" "
    			+ "Signature=\"484D30CBF4F167CBC803BF5A6AAEF1A8\"/>    "
    			+ "<CancelHotelOrderRequest>       <SpOrderId>SP234789547985</SpOrderId>       "
    			+ "<CancelReason>退改申请原因</CancelReason>    "
    			+ "</CancelHotelOrderRequest> "
    			+ "</Request>";
    	/*XStream xstream = new XStream();  
    	xstream.alias("Request", CancelOrderRequest.class);
    	xstream.autodetectAnnotations(true);
    	CancelOrderRequest obj =  (CancelOrderRequest)xstream.fromXML(xml);
    	
    	System.out.println(obj);*/
    	Document document = DocumentHelper.parseText(xml);
    	Element root = document.getRootElement();
    	Element header = root.element("Header");
    	System.out.println(header.attribute("TimeStamp").getStringValue());
    	System.out.println(header.attribute("PartnerCode").getStringValue());
    	System.out.println(header.attribute("RequestType").getStringValue());
    	System.out.println(header.attribute("Signature").getStringValue());
    }
    
    
    
    /**
     * 查询开通查询门市价的酒店
     * @return
     */
    public List<Integer> queryHotelChannelOpen(){
        String channelStr = Config.getValue("hotel_open_channel");
        List<Integer> list = new ArrayList<Integer>();
        if(StringUtils.isNotBlank(channelStr)){
            String[] array = channelStr.split("#");
            for (String channelId : array) {
                list.add(Integer.valueOf(channelId));
            }
        }
        return list;
    }
    
    
//    public static void main(String[] args) {
//    	String securityCode = "security_code_test";
//    	logger.info("加密前securityCode={}",securityCode); 
//    	securityCode = MD5Util.encryption(securityCode).toUpperCase();
//    	logger.info("加密后securityCode={}",securityCode);
//    	Header header = new Header();
//    	header.setTimeStamp("2015-05-16 10:25:38");
//    	header.setRequestType("syncOrderStatus");
//    	header.setPartnerCode("F0001TEST");
//    	
//    	String signature =  header.getTimeStamp() + header.getPartnerCode() + securityCode + "syncOrderStatus";
//    	logger.info("加密前signature={}",signature); 
//    	signature =  MD5Util.encryption(signature).toUpperCase();
//    	logger.info("加密后signature={}",signature); 
//    	header.setSignature(signature);
//    	System.out.println(new Gson().toJson(header));
//	}
}
