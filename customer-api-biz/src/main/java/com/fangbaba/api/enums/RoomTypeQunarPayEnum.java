package com.fangbaba.api.enums;


/**
 * @author zhagnyajun
 * 去哪儿的支付类型枚举
 */
public enum RoomTypeQunarPayEnum {
	prePay(0,"预付"),
	cashPay(1,"现付"),
	;
    
    private int id;
    private String name;
    
    /**
     * 
     * @param id
     * @param name
     */
    private RoomTypeQunarPayEnum(int id, String name) {
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
	public static RoomTypeQunarPayEnum getById(int id){
        for (RoomTypeQunarPayEnum temp : RoomTypeQunarPayEnum.values()) {
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
	public static RoomTypeQunarPayEnum getByName(String name){
        for (RoomTypeQunarPayEnum temp : RoomTypeQunarPayEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
