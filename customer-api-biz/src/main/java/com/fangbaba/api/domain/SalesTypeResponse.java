package com.fangbaba.api.domain;

import java.io.Serializable;
import java.util.List;

public class SalesTypeResponse implements Serializable{
	private List<SalesType> salesType;

	public List<SalesType> getSalesType() {
		return salesType;
	}

	public void setSalesType(List<SalesType> salesType) {
		this.salesType = salesType;
	}
	
	
}
