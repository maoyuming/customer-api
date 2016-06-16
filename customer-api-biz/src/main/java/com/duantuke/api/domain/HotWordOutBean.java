package com.duantuke.api.domain;

import java.io.Serializable;

public class HotWordOutBean implements Serializable{
	private static final long serialVersionUID = 1L;

    private Integer provcode;

    private Integer citycode;

    private Integer discode;

    private String businessType;

    private String word;

	public Integer getProvcode() {
		return provcode;
	}

	public void setProvcode(Integer provcode) {
		this.provcode = provcode;
	}

	public Integer getCitycode() {
		return citycode;
	}

	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}

	public Integer getDiscode() {
		return discode;
	}

	public void setDiscode(Integer discode) {
		this.discode = discode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
    
    

}
