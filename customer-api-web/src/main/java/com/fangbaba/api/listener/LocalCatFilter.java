//package com.fangbaba.api.listener;
//
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
//import com.dianping.cat.servlet.CatFilter;
//import com.fangbaba.api.service.CacheService;
//import com.fangbaba.api.util.CacheUtils;
//import com.fangbaba.api.util.Config;
//
//public class LocalCatFilter extends CatFilter implements ApplicationContextAware{
//	
//
//	private static final Log log = LogFactory.getLog(LocalCatFilter.class);
//	ApplicationContext context = null;
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    	super.init(filterConfig);
//    	initCache();
//    }
//    
//    
//    private void initCache(){
//    	try {
//    		CacheService cacheService= (CacheService)context.getBean("cacheService");
//			cacheService.initArea();
//			CacheUtils.schedule(Config.getValue("cache_30_minute"),cacheService);
//		} catch (Exception e) {
//			log.error("加载分类信息出错",e);
//		}
//    }
//
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext)
//			throws BeansException {
//		this.context = applicationContext;
//	}
//}
