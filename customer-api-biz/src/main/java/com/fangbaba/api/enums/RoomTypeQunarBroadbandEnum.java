package com.fangbaba.api.enums;


/**
 * @author zhagnyajun
 * 去哪儿的酒店宽带类型枚举
 */
public enum RoomTypeQunarBroadbandEnum {
	no(0,"无宽带"),
	free(2,"免费"),
	charge(3,"收费"),
    partialCharge(4,"部分收费"),
    partialHaveCharge(5,"部分有且收费"),
    partialHaveFree(6,"部分有且免费"),
	;
    
    private int id;
    private String name;
    
    /**
     * 
     * @param id
     * @param name
     */
    private RoomTypeQunarBroadbandEnum(int id, String name) {
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
	public static RoomTypeQunarBroadbandEnum getById(int id){
        for (RoomTypeQunarBroadbandEnum temp : RoomTypeQunarBroadbandEnum.values()) {
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
	public static RoomTypeQunarBroadbandEnum getByName(String name){
        for (RoomTypeQunarBroadbandEnum temp : RoomTypeQunarBroadbandEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
