package com.fangbaba.api.util;

import java.util.concurrent.ConcurrentHashMap;

import com.thoughtworks.xstream.XStream;


/**
 * @author he
 * xstream按对象名单例
 */
public final class XstreamSingletonUtil {
    
    private static final ConcurrentHashMap<String,XStream> xstreamMap = new ConcurrentHashMap<String,XStream>();
    
    public static XStream getXstream(String objectName){
    	XStream xstream = null;
    	xstream = xstreamMap.get(objectName);
    	if(xstream==null){
    		xstream = new XStream();
    		xstreamMap.put(objectName, xstream);
    	}
    	return xstream;
    }
    
}
