package com.fangbaba.api.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XmlRootElement
@XStreamAlias("OccupancyInfo")
public class OccupancyInfo {

	@XStreamAlias("AdultsCount")
	private String adultsCount;
	@XStreamAlias("ChildrenCount")
	private String childrenCount;
	@XStreamAlias("ChildrenAges")
	private String childrenAges;
	
	@XStreamAlias("AdultsInfoList")
	private List<AdultsInfo> adultsInfoList;

	public String getAdultsCount() {
		return adultsCount;
	}

	public void setAdultsCount(String adultsCount) {
		this.adultsCount = adultsCount;
	}

	public String getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(String childrenCount) {
		this.childrenCount = childrenCount;
	}

	public String getChildrenAges() {
		return childrenAges;
	}

	public void setChildrenAges(String childrenAges) {
		this.childrenAges = childrenAges;
	}

	public List<AdultsInfo> getAdultsInfoList() {
		return adultsInfoList;
	}

	public void setAdultsInfoList(List<AdultsInfo> adultsInfoList) {
		this.adultsInfoList = adultsInfoList;
	}

	
	
}
