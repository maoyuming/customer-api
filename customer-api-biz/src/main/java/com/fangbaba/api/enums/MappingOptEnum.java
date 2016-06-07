package com.fangbaba.api.enums;

public enum MappingOptEnum {


    insert("insert","新增"),
    update("update","修改"),
    delete("delete","删除"),
	;
	
	private final String opt;
	private final String name;
	



	private MappingOptEnum(String opt, String name) {
		this.opt = opt;
		this.name = name;
	}




	public String getOpt() {
		return opt;
	}




	public String getName() {
		return name;
	}




	public static MappingOptEnum findByOpt(String opt){
		for (MappingOptEnum value : MappingOptEnum.values()) {
			if(value.getOpt().equals(opt)){
				return value;
			}
		}
		return null;
	}


}
