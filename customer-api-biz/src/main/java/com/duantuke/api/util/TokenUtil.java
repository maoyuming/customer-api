package com.duantuke.api.util;

import javax.servlet.http.HttpServletRequest;

import com.duantuke.api.common.Constants;

public class TokenUtil {
    public static boolean checkOmsToken(String token){
    	String systemToken = MD5Util.encryption(Config.getValue("oms.token"));
    	if(systemToken.equals(token)){
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * 根据request请求获取用户id
     * @param request
     * @return
     */
    public static Long getUserIdByRequest(HttpServletRequest request){
    	 Object userId =  request.getAttribute(Constants.USER_ID);
    	 return (Long)userId;
    	 
    }
    
    
    
   
}
