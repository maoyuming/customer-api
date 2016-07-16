package com.duantuke.api.controller.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantuke.api.common.Constants;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.api.util.TokenUtil;
import com.duantuke.basic.face.service.PushLogService;
import com.duantuke.basic.po.LPushLog;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/customer/message")
public class CustomerMessageController {
	private static Logger logger = LoggerFactory.getLogger(CustomerMessageController.class);
	

	@Autowired
	private PushLogService pushLogService;
	
	/**
	 * 用户消息列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ResponseEntity<OpenResponse<List<LPushLog>>> list(HttpServletRequest request,LPushLog lPushLog) throws Exception {
		Long userId = TokenUtil.getUserIdByRequest(request);
        logger.info("用户{}查询消息列表",userId);
        OpenResponse<List<LPushLog>> openResponse = new OpenResponse<List<LPushLog>>();
		try {

			lPushLog.setMid(userId);
			lPushLog.setBegin(lPushLog.getIndex()-1);
			lPushLog.setEnd(lPushLog.getPageSize());
			List<LPushLog> list=pushLogService.queryPushLogByMid(lPushLog);
			
			openResponse.setResult(Constants.SUCCESS);
			openResponse.setData(list);
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
       
		return new ResponseEntity<OpenResponse<List<LPushLog>>>(openResponse, HttpStatus.OK);
	}
	
	
	/**
	 * 更新读取状态
	 * @param request
	 * @param logId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/udpateread")
	public ResponseEntity<OpenResponse<Boolean>> udpateread(HttpServletRequest request,Long logId) throws Exception {
		Long userId = TokenUtil.getUserIdByRequest(request);
		logger.info("用户{}消息读取，logId={}",userId,logId);
		OpenResponse<Boolean> openResponse = new OpenResponse<Boolean>();
		try {
			int count=pushLogService.updateReadStatus(userId,logId);
			openResponse.setResult(Constants.SUCCESS);
			
		} finally{
			logger.info("返回值openResponse：{}",new Gson().toJson(openResponse));
		}
		
		return new ResponseEntity<OpenResponse<Boolean>>(openResponse, HttpStatus.OK);
	}
	
	
}
