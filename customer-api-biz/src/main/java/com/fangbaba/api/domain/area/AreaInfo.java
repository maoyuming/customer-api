package com.fangbaba.api.domain.area;

import java.util.List;

/**
 * 区域信息
 * @author tankai
 *
 */
public class AreaInfo {
    private List<Province> provinceList;

	public List<Province> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<Province> provinceList) {
		this.provinceList = provinceList;
	}
    
}
