package com.fangbaba.api.domain.fangcang;

import java.io.PrintWriter;

import com.fangbaba.api.domain.fangcang.hotel.GetHotelInfoRequest;
import com.fangbaba.api.domain.fangcang.hotel.HotelInfoRequest;
import com.fangbaba.api.enums.FangCangRequestTypeEnum;
import com.fangbaba.api.util.BusinessUtil;
import com.thoughtworks.xstream.XStream;

public class Main {
    public static void main(String[] args) {
    	
    	
    	
    	
    	HotelInfoRequest hotelInfoRequest = new HotelInfoRequest();
    	GetHotelInfoRequest getHotelInfoRequest = new GetHotelInfoRequest();
    	getHotelInfoRequest.setFcHotelIds("121212");
    	
    	hotelInfoRequest.setGetHotelInfoRequest(getHotelInfoRequest);
    	
    	BusinessUtil businessUtil = new BusinessUtil();
    	businessUtil.genFangCangRequest(hotelInfoRequest, FangCangRequestTypeEnum.AddHotelMapping);
    	
//    	HotelInfoRequest hotelInfoRequest = new HotelInfoRequest();
//    	GetHotelInfoRequest getHotelInfoRequest = new GetHotelInfoRequest();
//    	getHotelInfoRequest.setFcHotelIds("121212");
//    	
//    	hotelInfoRequest.setGetHotelInfoRequest(getHotelInfoRequest);
//    	
//    	Header header = new Header();
//    	header.setPartnerCode("123213");
//    	header.setRequestType(FangCangRequestTypeEnum.AddHotelMapping.getRequestType());
//    	header.setSignature(111111+"");
//    	hotelInfoRequest.setHeader(header);
//    	
//
//    	genFangCangRequest(hotelInfoRequest);
	}
    
    
    public static String genFangCangRequest(Request request){
    	XStream xstream = new XStream();  
        xstream.alias("request", HotelInfoRequest.class);
        xstream.autodetectAnnotations(true);
        xstream.ignoreUnknownElements();
        xstream.toXML(request,new PrintWriter(System.out));  
        String xml =  xstream.toXML(request);
        
        return xml;
    }
}
