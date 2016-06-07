package com.fangbaba.api.enums;


/**
 * @author zhaochuanbin
 * 酒店床型类型枚举
 */
public enum BedTypeEnum {
    
    bigBed(1,"大床"),
    doubleBed(2,"双床"),
    tripBed(3,"三床"),
    ;
    
    private Integer id;
    private String name;
    
    /**
     * 
     * @param id
     * @param name
     */
    private BedTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
    public static BedTypeEnum getById(Integer id){
        for (BedTypeEnum temp : BedTypeEnum.values()) {
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
    public static BedTypeEnum getByName(String name){
        for (BedTypeEnum temp : BedTypeEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
