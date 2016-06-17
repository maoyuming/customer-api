package com.duantuke.api.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.dtk.token.TokenHttpUtils;
import com.duantuke.api.common.Constants;
import com.duantuke.api.enums.ErrorEnum;
import com.duantuke.api.exception.OpenException;
import com.duantuke.basic.face.UserTokenTypeEnum;
import com.duantuke.basic.face.service.CustomerService;
import com.duantuke.basic.po.Boss;
import com.duantuke.basic.po.Customer;

public class TokenUtil {

	
	

	private static CustomerService customerService = SpringContextUtil.getBean("customerService");
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
    
    /**
     * 根据request请求获取用户id
     * @param request
     * @return
     */
    public static Customer getUserInfoByRequest(HttpServletRequest request){
    	Object userId =  request.getAttribute(Constants.USER_ID);
    	
    	Long  customerId = (Long)userId;
    	return customerService.queryCustomerById(customerId);
    	
    }
    
    /**
     * 获取token
     * @param phone
     * @return
     * @throws Exception
     */
	public static String getTokenByPhone(String phone) throws Exception{
		

		Customer customer = customerService.queryCustomerByPhone(phone);
		if(customer==null){
			throw new OpenException(ErrorEnum.userUnExists);
		}
		
		return getTokenByUserId(customer.getCustomerId());
		
    }
	/**
	 * 获取token
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public static String getTokenByUserId(Long userId) throws Exception{
		
		String token = TokenHttpUtils.getToken(Config.getValue("cas.server"), userId+"");
		if(StringUtils.isEmpty(token)){
			throw new OpenException(ErrorEnum.tokenError);
		}
		
		return token;
		
	}
    
	/**
	 * 生成token
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static String createTokenByUserId(Long userId) throws Exception{
		
		String token = TokenUtil.getTokenByUserId(userId);
		if(StringUtils.isEmpty(token)){
			token = TokenHttpUtils.createToken(Config.getValue("cas.server"),userId+"", 
					UserTokenTypeEnum.C.getId()+"",  Long.valueOf(Config.getValue("token.expiredTime")));
		}
		
		return token;
		
	}
	/**
	 * 生成token
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static String createTokenByPhone(String phone) throws Exception{
		Customer customer = customerService.queryCustomerByPhone(phone);
		if(customer==null){
			throw new OpenException(ErrorEnum.userUnExists);
		}
		
		return createTokenByUserId(customer.getCustomerId());
		
	}
   

}
