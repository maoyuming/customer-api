package com.fangbaba.api.enums;


/**
 * @author zhagnyajun
 * 去哪儿的房量不足是否拒绝预订枚举
 */
public enum RoomTypeQunarRefusestateEnum {
	confirm(0,"库存不足需要确认"),
	noAdvance(1,"库存不足不可预定")
	;
    
    private int id;
    private String name;
    
    /**
     * 
     * @param id
     * @param name
     */
    private RoomTypeQunarRefusestateEnum(int id, String name) {
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
	public static RoomTypeQunarRefusestateEnum getById(int id){
        for (RoomTypeQunarRefusestateEnum temp : RoomTypeQunarRefusestateEnum.values()) {
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
	public static RoomTypeQunarRefusestateEnum getByName(String name){
        for (RoomTypeQunarRefusestateEnum temp : RoomTypeQunarRefusestateEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
