package com.fangbaba.api.enums;


/**
 * @author zhagnyajun
 * 去哪儿的酒店床型类型枚举
 */
public enum RoomTypeQunarBedEnum {
    
    bigBed(0,"大床",1),
    doubleBed(1,"双床",2),
    big_doubleBed(2,"大/双床"),
    threeBed(3,"三床",3),
    oneSingleOneDoubleBed(4,"一单一双"),
    singleBed(5,"单人床"),
    bunkBed(6,"上下铺"),
    shopBed(7,"通铺"),
    tatamiBed(8,"榻榻米"),
    waterBed(9,"水床"),
    roundBed(10,"圆床"),
    togetherBed(11,"拼床"),
    unknown(99,"拼床"),
    ;
    
    private Integer id;
    private String name;
    private Integer fangbabaid;
    
    /**
     * 
     * @param id
     * @param name
     */
    private RoomTypeQunarBedEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    
    private RoomTypeQunarBedEnum(Integer id, String name, Integer fangbabaid) {
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
    public static RoomTypeQunarBedEnum getById(int id){
        for (RoomTypeQunarBedEnum temp : RoomTypeQunarBedEnum.values()) {
            if(temp.getId() == id){
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
    public static RoomTypeQunarBedEnum getByFangbabaId(Integer fangbabaid){
    	for (RoomTypeQunarBedEnum temp : RoomTypeQunarBedEnum.values()) {
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
    public static RoomTypeQunarBedEnum getByName(String name){
        for (RoomTypeQunarBedEnum temp : RoomTypeQunarBedEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
