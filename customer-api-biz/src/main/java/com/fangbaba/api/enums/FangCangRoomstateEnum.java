package com.fangbaba.api.enums;

/**
 * Created by nolan on 16/4/20.
 * description:
 */
public enum FangCangRoomstateEnum {
    Have(1, "有房"),
    Pending(2, "待查"),
    Full(3, "满房");

    private Integer id;
    private String name;

    FangCangRoomstateEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
