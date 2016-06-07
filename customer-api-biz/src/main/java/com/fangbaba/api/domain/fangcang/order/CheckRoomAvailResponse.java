package com.fangbaba.api.domain.fangcang.order;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fangbaba.api.domain.fangcang.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class CheckRoomAvailResponse extends Response {

	private static final long serialVersionUID = 8171843103454844386L;
	@XStreamAlias("SpRatePlanId")
	private String spRatePlanId;
	@XStreamAlias("CanBook")
	private String canBook;
	@XStreamAlias("CanImmediate")
	private String canImmediate;
	@XStreamAlias("SaleItemList")
	private List<SaleItem> saleItemList;
	public String getSpRatePlanId() {
		return spRatePlanId;
	}
	public void setSpRatePlanId(String spRatePlanId) {
		this.spRatePlanId = spRatePlanId;
	}
	public String getCanBook() {
		return canBook;
	}
	public void setCanBook(String canBook) {
		this.canBook = canBook;
	}
	public String getCanImmediate() {
		return canImmediate;
	}
	public void setCanImmediate(String canImmediate) {
		this.canImmediate = canImmediate;
	}
	public List<SaleItem> getSaleItemList() {
		return saleItemList;
	}
	public void setSaleItemList(List<SaleItem> saleItemList) {
		this.saleItemList = saleItemList;
	}

}
