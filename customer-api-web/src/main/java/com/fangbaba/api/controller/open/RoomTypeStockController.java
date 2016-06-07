package com.fangbaba.api.controller.open;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.domain.open.hotel.PriceInfos;
import com.fangbaba.api.domain.open.hotel.RoomTypePricesData;
import com.fangbaba.api.domain.open.hotel.RoomTypeStocks;
import com.fangbaba.api.domain.open.hotel.RoomTypeStocksData;
import com.fangbaba.api.domain.open.hotel.RoomtypeStcokRequest;
import com.fangbaba.api.domain.open.hotel.StockInfos;
import com.fangbaba.api.enums.DistributionErrorEnum;
import com.fangbaba.api.exception.OpenException;
import com.fangbaba.api.util.DateUtil;
import com.fangbaba.basic.face.bean.RoomtypeModel;
import com.fangbaba.basic.face.service.RoomtypeService;
import com.fangbaba.gds.face.bean.PriceJsonBean;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.po.DistributorConfig;
import com.fangbaba.stock.face.base.RetInfo;
import com.fangbaba.stock.face.bean.RoomInfo;
import com.fangbaba.stock.face.bean.RoomTypeStock;
import com.fangbaba.stock.face.service.IStockService;
import com.google.common.base.Strings;
import com.google.gson.Gson;


@Controller
@RequestMapping(value = "/open/stock/roomtype")
public class RoomTypeStockController {
	private static Logger logger = LoggerFactory.getLogger(RoomTypeStockController.class);
	@Autowired
	private IStockService iStockService;
	@Autowired
	private IDistributorConfigService iDistributorConfigService;

	@Autowired
	private RoomtypeService roomtypeService;
    @Autowired
    private Mapper dozerMapper;
    
    private Gson gson = new Gson();
    
	/**
	 * 查询房型信息
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/query")
    public ResponseEntity<OpenResponse<RoomTypeStocksData>> query(HttpServletRequest request,@RequestBody  String body) {
//		logger.info("查询库存入参，{}",body);
		String channelId = request.getHeader("channelid");
		RoomtypeStcokRequest openRequest = new Gson().fromJson(body, RoomtypeStcokRequest.class);
		if (openRequest==null) {
			logger.info("查询房型库存参数为空");
			throw new OpenException(DistributionErrorEnum.argsNull.getName(),DistributionErrorEnum.argsNull.getId());
		}
		
		if (Strings.isNullOrEmpty(channelId)) {
			logger.info("渠道id为空");
			throw new OpenException(DistributionErrorEnum.channelidNulll.getName(),DistributionErrorEnum.channelidNulll.getId());
		}
		if (Strings.isNullOrEmpty(openRequest.getBegintime())) {
			logger.info("开始时间为空");
			throw new OpenException(DistributionErrorEnum.begintimeNulll.getName(),DistributionErrorEnum.begintimeNulll.getId());
		}
	  if (Strings.isNullOrEmpty(openRequest.getEndtime())) {
            logger.info("结束时间为空");
            throw new OpenException(DistributionErrorEnum.endtimeNulll.getName(),DistributionErrorEnum.endtimeNulll.getId());
        }
	  if (openRequest.getHotelid()==null) {
            logger.info("酒店id为空");
            throw new OpenException(DistributionErrorEnum.hotelNull.getName(),DistributionErrorEnum.hotelNull.getId());
        }
//		if (openRequest.getFlag()!=null) {
//			logger.info("库存类型应为true或false");
//			throw new OpenException(DistributionErrorEnum.noStocktype.getName(),DistributionErrorEnum.noStocktype.getId());
//		}
		
		boolean flag = false;
		if (openRequest.getFlag()==null) {
		    flag= false;
        }else if(openRequest.getFlag()==true){
            flag = true;
        }else if(openRequest.getFlag()==false){
            flag = false;
        }
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begintime= null;
		try {
			begintime = sdf.parse(openRequest.getBegintime());
		} catch (Exception e) {
			logger.info("开始时间格式转换错误");
			throw new OpenException(DistributionErrorEnum.timeParseNulll.getName(),DistributionErrorEnum.timeParseNulll.getId());
		}
		Date endtime= null;
		try {
			endtime = sdf.parse(openRequest.getEndtime());
		} catch (Exception e) {
			logger.info("结束时间格式转换错误");
			throw new OpenException(DistributionErrorEnum.timeParseNulll.getName(),DistributionErrorEnum.timeParseNulll.getId());
		}
		
		if (Strings.isNullOrEmpty(openRequest.getRoomtypeid())) {
			List<RoomtypeModel> roomtypes = roomtypeService.queryByHotelId(openRequest.getHotelid());
			if (CollectionUtils.isEmpty(roomtypes)) {
				logger.info("根据酒店id:{}未能查到房型",openRequest.getHotelid());
				throw new OpenException(DistributionErrorEnum.nofindRoomtype.getName(),DistributionErrorEnum.nofindRoomtype.getId());
			}else {
				StringBuffer roomtypeidStr = new StringBuffer("");
				for (RoomtypeModel rt : roomtypes) {
					roomtypeidStr.append(","+rt.getId());
				}
				openRequest.setRoomtypeid(roomtypeidStr.substring(1));
			}
		}
		
		
		//根据channelid查询otatype
		Long otatype = null ;
		List<DistributorConfig> distributorConfigs = iDistributorConfigService.queryByChannelId(Integer.valueOf(channelId));
		if(CollectionUtils.isNotEmpty(distributorConfigs)){
			otatype = distributorConfigs.get(0).getOtatype();
		}else{
			throw new OpenException(DistributionErrorEnum.channelidNotExists.getName(),DistributionErrorEnum.channelidNotExists.getId());
		}
		List<Long> roomtypeids = new ArrayList<Long>();
		String[] roomtype = openRequest.getRoomtypeid().split(",");
		for (String id : roomtype) {
			try {
				roomtypeids.add(Long.parseLong(id));
			} catch (Exception e) {
				logger.info("房型id不是整数");
				throw new OpenException(DistributionErrorEnum.roomtypesidTypeError.getName(),DistributionErrorEnum.roomtypesidTypeError.getId());
			}
			
		}
		//获取房型有效房量
		RetInfo<RoomTypeStock> roomtypestock = iStockService.selectByDateAndRoomtype(roomtypeids, otatype, begintime,endtime,flag);
		if (!roomtypestock.isResult()) {
			throw new OpenException(roomtypestock.getMsg(),DistributionErrorEnum.findroomtypestockError.getId());
		}
		if (CollectionUtils.isEmpty(roomtypestock.getList())) {
			logger.info("查询库存为空");
		}
		OpenResponse<RoomTypeStocksData> openResponse = new OpenResponse<RoomTypeStocksData>();
		openResponse.setResult(true+"");
		
		//begin
		RoomTypeStocksData roomTypeStocksData = new RoomTypeStocksData();
		 List<RoomTypeStocks> roomTypeStockslist = new ArrayList<RoomTypeStocks>();
         for (RoomTypeStock roomTypeStock:roomtypestock.getList()) {
             RoomTypeStocks roomTypeStocks = new RoomTypeStocks();
             roomTypeStocks.setRoomtypeid(roomTypeStock.getRoomtypeid());
             List<StockInfos> stockInfoslist = new ArrayList<StockInfos>(); 
             for (RoomInfo roomInfo:roomTypeStock.getRoomInfo()) {
                 StockInfos stockInfos = new StockInfos();
                 stockInfos.setDate(roomInfo.getDate());
                 stockInfos.setNum(roomInfo.getNumber().toString());
                 stockInfoslist.add(stockInfos);
             }
             roomTypeStocks.setStockinfos(stockInfoslist);
             roomTypeStockslist.add(roomTypeStocks);
         }
         roomTypeStocksData.setRoomtypestocks(roomTypeStockslist);
         //end
		
		openResponse.setData(roomTypeStocksData);
		
//		logger.info("查询库存结果:"+gson.toJson(roomTypeStocksData));
		
		return new ResponseEntity<OpenResponse<RoomTypeStocksData>>(openResponse, HttpStatus.OK);
	}
	
	
}
