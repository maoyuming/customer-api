package com.fangbaba.api.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.fangcang.Response;
import com.fangbaba.api.domain.fangcang.order.DeleteRatePlanRequest;
import com.fangbaba.api.domain.fangcang.order.SyncRateInfoRequest;
import com.fangbaba.api.domain.fangcang.order.SyncRatePlanRequest;
import com.fangbaba.api.enums.FangCangOverDraftEnum;
import com.fangbaba.api.enums.FangCangPaymethodEnum;
import com.fangbaba.api.enums.FangCangRequestTypeEnum;
import com.fangbaba.api.enums.FangCangRoomstateEnum;
import com.fangbaba.api.enums.RoomTypeFangCangBedEnum;
import com.fangbaba.api.service.FangCangHotelPriceService;
import com.fangbaba.api.util.BusinessUtil;
import com.fangbaba.api.util.DateUtil;
import com.fangbaba.basic.face.bean.RoomtypeModel;
import com.fangbaba.basic.face.service.RoomtypeService;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.fangbaba.gds.face.bean.OtaHotel;
import com.fangbaba.gds.face.bean.PriceJsonBean;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.face.service.IOtaHotelService;
import com.fangbaba.gds.face.service.IPriceService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.stock.face.base.RetInfo;
import com.fangbaba.stock.face.bean.RoomInfo;
import com.fangbaba.stock.face.bean.RoomTypeStock;
import com.fangbaba.stock.face.service.IStockService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * Created by nolan on 16/4/15.
 * description:
 * 天下房仓接口说明: http://www.fangcang.org/USP/
 */
@Service
public class FangCangHotelPriceServiceImpl implements FangCangHotelPriceService {
    public static final String CONSTANT_PLAN_ID1 = "10000";
    public static final String CONSTANT_PLAN_NAME = "标准预订计划";

    private static Logger logger = LoggerFactory.getLogger(FangCangHotelPriceServiceImpl.class);

    @Autowired
    private IOtaHotelService iOtaHotelService;

    @Autowired
    private RoomtypeService roomtypeService;

    @Autowired
    private BusinessUtil businessUtil;

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IStockService iStockService;



    @Autowired
    private IDistributorConfigService iDistributorConfigService;

    public Response doSyncRatePlan(Long hotelid) throws Exception {
        logger.info("Response syncRatePlan(Long hotelid:{})>>>>>>Start", hotelid);
        List<OtaHotel> itemList = iOtaHotelService.queryByHotelid(hotelid);
        //1. 基本数据校验
        Response response = new Response();
        if (itemList == null || itemList.size() == 0) {
            response.setResultMsg("酒店(" + hotelid + ")不存在.");
            return response;
        }
        List<RoomtypeModel> roomtypeModelList = roomtypeService.queryByHotelId(hotelid);
        if (roomtypeModelList == null || roomtypeModelList.size() == 0) {
            response.setResultMsg("酒店(" + hotelid + ")下房型列表为空.");
            return response;
        }

        //2. 远程方法交互
        StringBuffer outbuffer = new StringBuffer();
        for (RoomtypeModel roomtypeModel : roomtypeModelList) {
            Response tmpres = syncRatePlan(hotelid, roomtypeModel.getId());
            outbuffer.append(tmpres.getResultMsg());
        }

        //3. 构造返回数据
        response.setResultMsg(outbuffer.toString());
        logger.info("Response syncRatePlan(Long hotelid:{})>>>>>>End", hotelid);
        return response;
    }

    public Response syncRatePlan(Long hotelid, Long roomtypeid) {
        logger.info("syncRatePlan(Long hotelid:{}, Long roomtypeid:{})>>>>>>Start", hotelid, roomtypeid);
        List<String> bedItemList = Lists.newArrayList();
        for (RoomTypeFangCangBedEnum tmp : RoomTypeFangCangBedEnum.values()) {
            bedItemList.add(tmp.getId());
        }
        String bedstr = Joiner.on(",").skipNulls().join(bedItemList);

        //1.组织请求参数
        SyncRatePlanRequest syncRatePlanRequest = new SyncRatePlanRequest();
        SyncRatePlanRequest.SyncRatePlanBodyRequest body = syncRatePlanRequest.newSyncRatePlanBody();
        body.setSpHotelId(String.valueOf(hotelid));
        body.setSpRoomTypeId(String.valueOf(roomtypeid));
        body.setOperateType("NEW");

        SyncRatePlanRequest.RatePlan ratePlan = syncRatePlanRequest.newRatePlan();
        ratePlan.setSpRatePlanId(roomtypeid+"");
        ratePlan.setSpRatePlanName(CONSTANT_PLAN_NAME);
        ratePlan.setSpBedType(bedstr);
        ratePlan.setPayMethod(String.valueOf(FangCangPaymethodEnum.YF.getId()));
        ratePlan.setCurrency("CNY");
        List rateList = Lists.newArrayList();
        rateList.add(ratePlan);
        body.setRatePlanList(rateList);


        //2. 远程方法请求
        Map<String, String> param = businessUtil.genFangCangRequest(syncRatePlanRequest, FangCangRequestTypeEnum.SyncRatePlan);
        logger.info("request-body: {}", param);
        String xml = businessUtil.doPost(GdsChannelUrlEnum.SyncRatePlan.getId(), param, ChannelEnum.fangcang.getId());
        logger.info("response-body: {}", xml);


        //3. 组织返回数据
        Response response = new Response();
        response.setResultMsg(xml);
        logger.info("syncRatePlan(Long hotelid:{}, Long roomtypeid:{})>>>>>>End", hotelid, roomtypeid);
        return response;
    }


    public Response doDeleteRatePlan(Long hotelid) throws Exception {
        logger.info("Response deleteRatePlan(Long hotelid:{})>>>>>>Start", hotelid);
        //1. 数据基础校验
        List<OtaHotel> itemList = iOtaHotelService.queryByHotelid(hotelid);
        if (itemList == null || itemList.size() == 0) {
            Response response = new Response();
            response.setResultMsg("酒店(" + hotelid + ")不存在.");
            return response;
        }

        List<RoomtypeModel> roomtypeModelList = roomtypeService.queryByHotelId(hotelid);
        if (roomtypeModelList == null || roomtypeModelList.size() == 0) {
            Response response = new Response();
            response.setResultMsg("酒店(" + hotelid + ")下房型列表为空.");
            return response;
        }


        //2. 远程方法交互
        StringBuffer outbuffer = new StringBuffer();
        for (RoomtypeModel roomtypeModel : roomtypeModelList) {
            Response tmpres = deleteRatePlan(hotelid, roomtypeModel.getId());
            outbuffer.append(tmpres.getResultMsg());
        }


        //3. 组织返回数据
        Response response = new Response();
        response.setResultMsg(outbuffer.toString());
        logger.info("Response deleteRatePlan(Long hotelid:{})>>>>>>Start", hotelid);
        return response;
    }

    public Response deleteRatePlan(Long hotelid, Long roomtypeid) {
        logger.info("Response deleteRatePlan(Long hotelid:{}, Long roomtypeid:{})>>>>>>Start", hotelid, roomtypeid);
        //1. 组织请求数据
        DeleteRatePlanRequest deleteRatePlanRequest = new DeleteRatePlanRequest();
        DeleteRatePlanRequest.DeleteRatePlanBodyRequest body = deleteRatePlanRequest.newDeleteRatePlanBodyRequest();
        body.setSpHotelId(String.valueOf(hotelid));
        body.setSpRoomTypeId(String.valueOf(roomtypeid));
        List itemList = Lists.newArrayList();
        DeleteRatePlanRequest.RatePlanInfo ratePlanInfo = deleteRatePlanRequest.newRatePlanInfo();
        ratePlanInfo.setSpRatePlanId(roomtypeid+"");
        itemList.add(ratePlanInfo);
        body.setRatePlanInfoList(itemList);

        //2. 远程方法交互
        Map<String, String> param = businessUtil.genFangCangRequest(deleteRatePlanRequest, FangCangRequestTypeEnum.DeleteRatePlan);
        logger.info("request-body: {}", param);
        String xml = businessUtil.doPost(GdsChannelUrlEnum.DeleteRatePlan.getId(), param, ChannelEnum.fangcang.getId());
        logger.info("response-body: {}", xml);


        //3. 组织返回数据
        Response response = new Response();
        response.setResultMsg(xml);
        logger.info("Response deleteRatePlan(Long hotelid:{}, Long roomtypeid:{})>>>>>>End", hotelid, roomtypeid);
        return response;
    }

    public Response syncRateInfo(Long otatype, Long hotelid) {
        return syncRateInfo(otatype, hotelid, null);
    }

    public Response syncRateInfo(Long otatype, Long hotelid, Long roomtype) {
        Gson gson = new Gson();
        Calendar startc = Calendar.getInstance();
        Calendar endc = Calendar.getInstance();
        endc.add(Calendar.DAY_OF_MONTH, 31);

        //1. 查询渠道商某家酒店所有房型价格
        Map<Long, Map<String, String>> resultmap = null;
		try {
			resultmap = priceService.queryChannelPricesFromRedis(otatype, hotelid, startc.getTime(), endc.getTime(), roomtype);
		} catch (Exception e) {
			logger.error("查询价格异常",e);
		}
        logger.info("查询渠道商({})对应酒店({})所有房型价格: {}", otatype, hotelid, resultmap);
        if (resultmap == null || resultmap.isEmpty()) {
            Response response = new Response();
            response.setResultMsg("天下房仓渠道关联酒店(" + hotelid + ")房型价格列表为空");
            return response;
        }

        //2. 查询渠道商某家酒店所有房型的库存情况
        Map<Long, Map<String, Integer>> roomTypeStockMap = Maps.newHashMap();
        List roomtypeids = Lists.newArrayList(resultmap.keySet());
        RetInfo<RoomTypeStock> stockRetInfo = iStockService.selectByDateAndRoomtype(roomtypeids, otatype, startc.getTime(), endc.getTime(),true);
        if (stockRetInfo.getList() != null && stockRetInfo.getList().size() > 0) {
            for (RoomTypeStock roomTypeStock : stockRetInfo.getList()) {
                Map<String, Integer> roomdaymap = Maps.newHashMap();
                List<RoomInfo> roomInfos = roomTypeStock.getRoomInfo();
                for (RoomInfo roomInfo : roomInfos) {
                    roomdaymap.put(roomInfo.getDate(), roomInfo.getNumber());
                }
                roomTypeStockMap.put(roomTypeStock.getRoomtypeid(), roomdaymap);
            }
        }
        logger.info("查询渠道商({})对应酒店({})所有房型的库存情况: {}", otatype, hotelid, roomTypeStockMap);

        //3. 构造与天下房仓的对接xml并交互
        StringBuffer outbuffer = new StringBuffer();
        for (Long roomtypeid : resultmap.keySet()) {
            SyncRateInfoRequest syncRateInfoRequest = new SyncRateInfoRequest();
            SyncRateInfoRequest.SyncRateInfoBodyRequest body = syncRateInfoRequest.newSyncRateInfoBodyRequest();
            body.setSpHotelId(String.valueOf(hotelid));
            body.setSpRoomTypeId(String.valueOf(roomtypeid));
            body.setSpRatePlanId(roomtypeid+"");

            List<SyncRateInfoRequest.SaleInfo> saleInfoList = Lists.newArrayList();
            Map<String, String> pricemap = resultmap.get(roomtypeid);
            Map<String, Integer> stockmap = roomTypeStockMap.get(roomtypeid);
            for (String day : pricemap.keySet()) {
            	Date date = DateUtil.strToDate(day, "yyyyMMdd");
                Integer stocknum = stockmap.get(DateUtil.dateToStr(date, "yyyy-MM-dd"));
                FangCangRoomstateEnum fangCangRoomstateEnum = FangCangRoomstateEnum.Have;
                if (stocknum == null || stocknum <= 0) {
                    fangCangRoomstateEnum = FangCangRoomstateEnum.Full;
                }
                PriceJsonBean bean = gson.fromJson(pricemap.get(day), PriceJsonBean.class);
                SyncRateInfoRequest.SaleInfo saleInfo = syncRateInfoRequest.newSaleInfo();
                saleInfo.setSaleDate(DateUtil.dateToStr(date, "yyyy-MM-dd"));                    //必填, 格式：YYYY-MM-DD，必须是当天(包含当天)以后的日期，系统才处理。格式：YYYY-MM-DD，必须是当天(包含当天)以后的日期，系统才处理。
                saleInfo.setSalePrice(bean.getChannelprice());// 必填, 保留小数点后2位
                saleInfo.setBreakfastType("3");      //早餐类型  |必填, 参考【早餐类型】章节。1:中早,2:西早,3:自助早
                saleInfo.setBreakfastNum(0);        //早餐数量  |必填, 参考【早餐数量】章节。-1:床位早,0:无早,1:单早,2:双早,3:三早,4:四早,5:五早,6:六早,7:七早,8:八早,9:九早,10:多早
                saleInfo.setFreeSale("0");          //是否自由售卖    |必填, 0：否；1:是；
                saleInfo.setRoomState(String.valueOf(fangCangRoomstateEnum.getId()));            //房态      |必填, 参考【房态】章节, 1有房, 2待查 3满房 TODO 需查询库存
                saleInfo.setOverdraft(FangCangOverDraftEnum.cannot.getId().toString());        //是否可超  |非必填, 默认值不可超；0：不可超；1：可超
                saleInfo.setOverDraftNum("");     //可超数额  |非必填, Overdraft=1的时候OverDraftNum为空时默认值9999，不为空时，必须是1~9999的整数。
                saleInfo.setQuotaNum(stocknum);          //配额数       |必填, 1~9999的整数
                saleInfo.setMinAdvHours(0);      //最少提前预订小时数 |非必填,格式为正整数（预订条款）
                saleInfo.setMinDays(1);          //最少入住天数    |非必填, 格式为正整数，不能与MaxDays同时有值，同时有值时，系统不做处理，提示“最少入住天数，最大入住天数不能同时有值。”，（入住条款）
                saleInfo.setMaxDays("");          //最大入住天数    |非必填, 格式为正整数，不能与MaxDays同时有值，同时有值时，系统不做处理，提示“最少入住天数，最大入住天数不能同时有值。”，（入住条款）
                saleInfo.setMinRooms(1);            //最少预订间数    |必填, 1~99
                saleInfo.setMinAdvCancelHours(1);                //取消最少提前小时数 |非必填,(取消条款). 值为0，则表示“一经预订不能取消”；值大于0，则表示“提前n点m点之前取消”程序根据小时数转换n天m点。
                saleInfo.setCancelDescription("1小时之后不可取消");  //取消说明          |非必填, 长度不超过500字符
                saleInfoList.add(saleInfo);
            }
            body.setSaleInfoList(saleInfoList);

            Map<String, String> param = businessUtil.genFangCangRequest(syncRateInfoRequest, FangCangRequestTypeEnum.SyncRateInfo);
            logger.info("request-body: {}", param);
            String xml = businessUtil.doPost(GdsChannelUrlEnum.SyncRateInfo.getId(), param, ChannelEnum.fangcang.getId());
            logger.info("response-body: {}", xml);
            outbuffer.append(xml).append("\n");
        }
        Response response = new Response();
        response.setResultCode("200");
        response.setResultMsg(outbuffer.toString());
        return response;
    }
    
    
    




    /**
     * 同步价格计划接口-SyncRatePlan
     *    根据合作方的价格计划信息同步到房仓价格计划信息。
     *    
     *    1.房仓的价格计划数据结构分为三层：酒店、房型、价格计划，接口调用方需要自行封装此数据结构；
     *    2.每次调用接口时，只传递一个房型下面最多10个价格计划数据；
     *    3.房仓保存双方的价格计划映射关系并维护；
     *    4.处理结果：全部成功或全部失败。
     * @return
     */
    public List<Response> syncRatePlan(Long hotelid) {
        logger.info("同步价格计划......>>>>>开始");
        List<Response> resList = Lists.newArrayList();

        Long otatype = getFangCangOtatype();
        List<OtaHotel> otahotelList = new ArrayList<OtaHotel>();
        if(hotelid!=null){
            OtaHotel otaHotel =  iOtaHotelService.queryByOtatypeAndHotelid(otatype, hotelid);
            if(otaHotel==null){
                return resList;
            }else{
                otahotelList.add(otaHotel);
            }
        }else{
            otahotelList = iOtaHotelService.queryByOtatype(otatype);
        }
        logger.info("需同步价格计划的酒店共计: {}", otahotelList != null ? otahotelList.size() : 0);

        if (otahotelList != null && otahotelList.size() > 0) {
            for (OtaHotel otaHotel : otahotelList) {
                logger.info("正在同步酒店({})的价格计划......", otaHotel.getHotelid());
                try {
                    Response response = this.doSyncRatePlan(otaHotel.getHotelid());
                    logger.info("同步酒店({})的价格计划结果: {}", otaHotel.getHotelid(), response);
                    resList.add(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("同步酒店({})的价格计划>>>>>结束", otaHotel.getHotelid());
            }
        }
        logger.info("同步价格计划......>>>>>结束");
        return resList;
    }

    /**
     * 删除价格计划接口-deleteRatePlan
     *    1.房仓的价格计划数据结构分为三层：酒店、房型、价格计划，接口调用方需要自行封装此数据结构；
     *    2.每次调用接口时，只传递一个房型要删除的价格计划信息；
     *    3.商品产品关系表不处理，价格计划映射、价格计划表设置无效。
     * @return
     */
    public List<Response> deleteRatePlan(Long hotelid) {
        logger.info("删除价格计划......>>>>>开始");
        List<Response> resList = Lists.newArrayList();

        Long otatype = getFangCangOtatype();
        List<OtaHotel> otahotelList = new ArrayList<OtaHotel>();
        if(hotelid!=null){
            OtaHotel otaHotel =  iOtaHotelService.queryByOtatypeAndHotelid(otatype, hotelid);
            if(otaHotel==null){
                return resList;
            }else{
                otahotelList.add(otaHotel);
            }
        }else{
            otahotelList = iOtaHotelService.queryByOtatype(otatype);
        }
        logger.info("删除价格计划的酒店共计: {}", otahotelList != null ? otahotelList.size() : 0);

        if (otahotelList != null && otahotelList.size() > 0) {
            for (OtaHotel otaHotel : otahotelList) {
                logger.info("正在删除酒店({})的价格计划......", otaHotel.getHotelid());
                try {
                    Response response = this.doDeleteRatePlan(otaHotel.getHotelid());
                    logger.info("删除酒店({})的价格计划结果: {}", otaHotel.getHotelid(), response);
                    resList.add(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("删除酒店({})的价格计划>>>>>结束", otaHotel.getHotelid());
            }
        }
        logger.info("删除价格计划......>>>>>结束");
        return resList;
    }

    /**
     * 同步价格信息接口-syncRateInfo
     *
     *      1.根据价格计划id（合作方价格计划id）同步酒店房型的定价信息;每次接口调用最多传递30天的价格信息;
     *      2.合作方可多次推送同一价格计划同一日期的价格房态等，后推送的会覆盖先推送的，只保留最近一次调用的数据信息。
     *      MinDays和MaxDays需为正整数--modify1.6
     *      
     * @return
     */
    public List<Response> syncRateInfo(Long hotelid) {
        logger.info("同步价格信息......>>>>>开始");
        List<Response> resList = Lists.newArrayList();

        Long otatype = getFangCangOtatype();
        List<OtaHotel> otahotelList = new ArrayList<OtaHotel>();
        if(hotelid!=null){
            OtaHotel otaHotel =  iOtaHotelService.queryByOtatypeAndHotelid(otatype, hotelid);
            if(otaHotel==null){
                return resList;
            }else{
                otahotelList.add(otaHotel);
            }
        }else{
            otahotelList = iOtaHotelService.queryByOtatype(otatype);
        }
        logger.info("同步价格信息的酒店共计: {}", otahotelList != null ? otahotelList.size() : 0);

        if (otahotelList != null && otahotelList.size() > 0) {
            for (OtaHotel otaHotel : otahotelList) {
                logger.info("正在同步酒店({})的价格信息......", otaHotel.getHotelid());
                try {
                    Response response = this.syncRateInfo(otatype, otaHotel.getHotelid());
                    logger.info("同步酒店({})的价格信息结果: {}", otaHotel.getHotelid(), response);
                    resList.add(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("同步酒店({})的价格信息>>>>>结束", otaHotel.getHotelid());
            }
        }
        logger.info("同步酒店({})的价格信息......>>>>>结束");
        return resList;
    }

    private Long getFangCangOtatype() {
        List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(ChannelEnum.fangcang.getId());
        if (distributorConfigs == null || distributorConfigs.size() == 0) {
            throw new IllegalArgumentException("当前渠道otatype不存在.");
        }
        return distributorConfigs.get(0).getOtatype();
    }

    
}
