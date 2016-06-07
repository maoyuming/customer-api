package com.fangbaba.api.util.businesslog;

public enum BussinssTypeEnum {
    addHotelMapping("20001", "新增酒店映射"),
    updateHotelMapping("20002", "修改酒店映射"),
    deleteHotelMapping("20003", "删除酒店映射"),
    addRoomtypeMapping("30001", "新增房型映射"),
    updateRoomtypeMapping("30002", "修改房型映射"),
    deleteRoomtypeMapping("30003", "删除房型映射"),

    ;

    private String type;
    private String name;

    private BussinssTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
