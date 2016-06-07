package com.fangbaba.api.enums;

/**
 * Created by nolan on 16/4/20.
 * description:
 */
public enum FangCangPaymethodEnum {
    YF(2, "预付");

    private Integer id;
    private String name;

    FangCangPaymethodEnum(Integer id, String name) {
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
