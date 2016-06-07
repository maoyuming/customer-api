package com.fangbaba.api.util.businesslog;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fangbaba.api.util.Config;
import com.lz.mongo.bislog.BisLog;
import com.lz.mongo.bislog.BisLogDelegate;

@Component
public class BusinessLogUtil {

	private static final Logger logger = LoggerFactory.getLogger(BusinessLogUtil.class);
	@Autowired
	private BisLogDelegate bisLogDelegate;
	
	/**
	 *  BussinessId 业务id ，例如订单号
	 *  BussinssType 业务类型，例如创建订单
	 * @param bisLog
	 */
	public void saveLog(String businessId,String operator,String content,BussinssTypeEnum bussinssTypeEnum){
		try {
			logger.info("保存业务日志开始,businessId={},operator={},content={}",businessId,operator,content);
			if(businessId==null){
				return;
			}
			if(operator==null){
				operator = "system";
			}
			if(content==null){
				return;
			}
			if(bussinssTypeEnum==null){
				return;
			}
			BisLog bisLog = new BisLog();
			bisLog.setSystem(Config.getValue("api.system"));
			
			bisLog.setOperator(operator);
			bisLog.setBussinessId(businessId);
			bisLog.setBussinssType(bussinssTypeEnum.getType());
			bisLog.setContent(content);
			bisLog.setCreateTime(new Date());

			
			bisLogDelegate.saveBigLog(bisLog);
			logger.info("保存业务日志完成");
		} catch (Exception e) {
			logger.error("记录日志异常",e);
		}

	}

}
