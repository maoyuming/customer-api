package com.fangbaba.api.enums;


/**
 * 床型代码
	床型名称
	A000000
	单床
	1000000
	大床
	2000000
	双床
	3000000
	三床
	4000000
	四床
	5000000
	大/双床
 * @author tankai
 * 天下房仓的酒店床型类型枚举
 */
public enum RoomTypeFangCangBedEnum {
	
	singleBed("A000000","单床",1),
    bigBed("1000000","大床",1),
    doubleBed("2000000","双床",2),
    threeBed("3000000","三床",3),
    fourBed("4000000","四床"),
    big_doubleBed("5000000","大/双床"),
    ;
    
    private String id;
    private String name;
    private Integer fangbabaid;
    
    /**
     * 
     * @param id
     * @param name
     */
    private RoomTypeFangCangBedEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    
    private RoomTypeFangCangBedEnum(String id, String name, Integer fangbabaid) {
		this.id = id;
		this.name = name;
		this.fangbabaid = fangbabaid;
	}



	public Integer getFangbabaid() {
		return fangbabaid;
	}



	public void setFangbabaid(Integer fangbabaid) {
		this.fangbabaid = fangbabaid;
	}



	public String getId() {
        return id;
    }
    public void setId(String id) {
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
    public static RoomTypeFangCangBedEnum getById(String id){
        for (RoomTypeFangCangBedEnum temp : RoomTypeFangCangBedEnum.values()) {
            if(temp.getId().equals(id)){
                return temp;
            }
        }
        return null;
    }
    /**
     * 
     * @param id
     * @return
     */
    public static RoomTypeFangCangBedEnum getByFangbabaId(Integer fangbabaid){
    	for (RoomTypeFangCangBedEnum temp : RoomTypeFangCangBedEnum.values()) {
    		if(temp.getFangbabaid()!=null && temp.getFangbabaid().equals(fangbabaid)){
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
    public static RoomTypeFangCangBedEnum getByName(String name){
        for (RoomTypeFangCangBedEnum temp : RoomTypeFangCangBedEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
