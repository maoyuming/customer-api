package com.fangbaba.api.enums.open;


/**
 * 支付类型枚举
 */
public enum PrepayEnum {
    
    yufu(1,"预付"),
    daofu(2,"到付"),
    ;
    
    private Integer id;
    private String name;
    
    /**
     * 
     * @param id
     * @param name
     */
    private PrepayEnum(Integer id, String name) {
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
    public static PrepayEnum getById(Integer id){
        for (PrepayEnum temp : PrepayEnum.values()) {
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
    public static PrepayEnum getByName(String name){
        for (PrepayEnum temp : PrepayEnum.values()) {
            if(temp.getName().equals(name)){
                return temp;
            }
        }
        return null;
    }
}
