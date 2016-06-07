package com.duantuke.api.util;

public class TokenUtil {
    public static boolean checkOmsToken(String token){
    	String systemToken = MD5Util.encryption(Config.getValue("oms.token"));
    	if(systemToken.equals(token)){
    		return true;
    	}
    	
    	return false;
    }
    
    
    
   
}
