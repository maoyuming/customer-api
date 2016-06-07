package com.fangbaba.api.enums;


/**
 * @author zhagnyajun
 * 去哪儿的酒店房量状态类型枚举
 */
public enum RoomTypeQunarStatusEnum {
    
    yes(0,"有房"),
    no(1,"满房"),
    ;
    
    private int id;
    private String name;
    
    /**
     * 
     * @param id
     * @param name
     */
    private RoomTypeQunarStatusEnum(int id, String name) {
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
    public static RoomTypeQunarStatusEnum getById(int id){
        for (RoomTypeQunarStatusEnum temp : RoomTypeQunarStatusEnum.values()) {
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
    public static RoomTypeQunarStatusEnum getByName(String name){
        for (RoomTypeQunarStatusEnum temp : RoomTypeQunarStatusEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
