/**
 * 2016年3月30日下午2:30:55
 * zhaochuanbin
 */
package com.fangbaba.api.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.bean.Order;
import com.fangbaba.api.util.Config;
import com.fangbaba.api.util.pushlog.PushLog;
import com.fangbaba.api.util.pushlog.PushLogUtil;
import com.fangbaba.gds.enums.GdsChannelUrlEnum;
import com.mk.config.client.MkConfigManager;

/**
 * @author zhaochuanbin
 *
 */
@Controller
@RequestMapping(value = "/util")
public class UtilController {
    
    private static Logger logger = LoggerFactory.getLogger(UtilController.class);
    
    @Resource
    private MkConfigManager mkConfigManager;
    @Resource
    private PushLogUtil pushLogUtil;
    
    @RequestMapping(value = "/testconfig")
    @ResponseBody
    public  ResponseEntity<RetInfo<String>> testconfig(String key){
        RetInfo<String> reInfo= new RetInfo<String>(); 
        String zkkey = Config.getValue(key);
        reInfo.setObj(zkkey);
        reInfo.setResult(true);
        logger.info("key:"+zkkey);
        return new ResponseEntity<RetInfo<String>>(reInfo, HttpStatus.OK);
    }
    @RequestMapping(value = "/testpushmongo")
    @ResponseBody
    public  ResponseEntity<String> testpushmongo(String key){
    	PushLog pushLog = new PushLog();
    	pushLog.setPushtype(GdsChannelUrlEnum.pushHotel.getId()+"");
    	pushLog.setPushtypedesc(GdsChannelUrlEnum.pushHotel.getName());
    	pushLog.setPushcontent("this is json chuan");
    	pushLog.setChannelid("11");
    	pushLogUtil.savePushLog(pushLog);
    	return new ResponseEntity<String>("ok", HttpStatus.OK);
    }
    
}
