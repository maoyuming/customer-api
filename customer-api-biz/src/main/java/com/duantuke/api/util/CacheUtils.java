package com.duantuke.api.util;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.duantuke.api.service.impl.CacheService;


/**
 * 缓存工具类
 * @author tankai
 *
 */
public class CacheUtils {

	private static final Log log = LogFactory.getLog(CacheUtils.class);
	private static final String TOP_REGEX="#";
	private static final String REGEX=";";
	public static void schedule(String cacheMethodKey,final CacheService cacheService){
		if(cacheMethodKey==null){
			return;
		}
		String[] tempMethods =null; 
		long tempMinute =0; 
		String[] cache = cacheMethodKey.split(TOP_REGEX);
		    if(cache!=null&&cache.length==2){
		    	tempMethods = cache[1].split(REGEX);
		    	tempMinute = Long.valueOf(cache[0]);
		    }
		final String[] methods =tempMethods;
		long minute = tempMinute;
		if(minute==0){
			return;
		}
    	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    	executor.scheduleWithFixedDelay(new Runnable() {
    		
    		@Override
    		public void run() {
    			Class clazz = CacheService.class;
    			if(!ArrayUtils.isEmpty(methods)){
    				for (String method : methods) {
    					Method m;
						try {
							m = clazz.getDeclaredMethod(method);
							Object obj = m.invoke(cacheService);
						} catch (Exception e) {
							log.error("加载类反射出错",e);
						}  
    				}
    			}
    		}
    	}, 0L,minute, TimeUnit.MINUTES);
    }
}
