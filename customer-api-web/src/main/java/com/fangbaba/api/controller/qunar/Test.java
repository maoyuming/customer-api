package com.fangbaba.api.controller.qunar;

import java.io.PrintWriter;

import com.fangbaba.api.domain.RoomOrder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Test {  
    public static void main(String[] args) {  
                  //JavaBean --> XML  
    	RoomOrder address=new RoomOrder();  
    	address.setGuestName("12321");
    	address.setRoomOrderId(2222L);
        XStream xstream = new XStream(new DomDriver());  
        xstream.alias("RoomOrder", RoomOrder.class);  
        xstream.toXML(address,new PrintWriter(System.out));  
        
        System.out.println();
    }
}