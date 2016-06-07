package com.fangbaba.api.enums;
/**
 * 接口返回标识：
 * @author zhangyajun
 *
 */
public enum FangCangResultFlagEnum {
	failure(0, "失败"), 
	success(1, "成功"),
	;
	
	private Integer id;
	private String name;
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
	
	
	
	private FangCangResultFlagEnum(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	public static FangCangResultFlagEnum findByCode(Integer code){
		for (FangCangResultFlagEnum value : FangCangResultFlagEnum.values()) {
			if(value.id.equals(code)){
				return value;
			}
		}
		return null;
	}
	public static String findNameByCode(Integer code){
		for (FangCangResultFlagEnum value : FangCangResultFlagEnum.values()) {
			if(value.id.equals(code)){
				return value.getName();
			}
		}
		return null;
	}
	
}
