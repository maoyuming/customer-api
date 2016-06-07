package com.fangbaba.api.enums;


/**
 * @author zhagnyajun
 * 去哪儿的售卖渠道枚举
 */
public enum RoomTypeQunarChannelEnum {
	normal(0,"正常渠道"),
	nightSell(2,"夜销渠道"),
	wirelessSales(3,"无线促销"),
	vacationPackage(4,"度假打包"),
	HDSORB2B(5,"HDS(B2B)业务"),
	hourRoom(6,"钟点房"),
	;
    
    private int id;
    private String name;
    
    /**
     * 
     * @param id
     * @param name
     */
    private RoomTypeQunarChannelEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static RoomTypeQunarChannelEnum getById(int id){
        for (RoomTypeQunarChannelEnum temp : RoomTypeQunarChannelEnum.values()) {
            if(temp.getId() == id){
                return temp;
            }
        }
        return null;
    }
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static RoomTypeQunarChannelEnum getByName(String name){
        for (RoomTypeQunarChannelEnum temp : RoomTypeQunarChannelEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
