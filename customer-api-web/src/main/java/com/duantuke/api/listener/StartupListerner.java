package com.duantuke.api.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.duantuke.api.service.impl.CacheService;
import com.duantuke.api.util.CacheUtils;
import com.duantuke.api.util.Config;


@Service("startupListerner")
public class StartupListerner implements ApplicationListener {

	private static final Log log = LogFactory.getLog(StartupListerner.class);

	@Autowired
	private CacheService cacheService;
	

	public void onApplicationEvent(ApplicationEvent event) {

		try {
			if (event instanceof ContextRefreshedEvent) {
				cacheService.initArea();
				CacheUtils.schedule(Config.getValue("cache_30_minute"),cacheService);
			}
		} catch (Exception e) {
			log.error("加载分类信息出错",e);
		}
	
	}
	

}
