package com.fangbaba.api.domain.qunar;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 
 * @author zhangyajun
 *
 */
@XmlRootElement
@XStreamAlias("room")
public class HotelOfferRoom implements Serializable{
	

	private static final long serialVersionUID = -7052436721173022915L;
	@XStreamAsAttribute
	private Long id;//房型id
	@XStreamAsAttribute
	private String name;//房型名字
	@XStreamAsAttribute
	private String breakfast;//早餐
	@XStreamAsAttribute
	private Integer bed;//床型
	@XStreamAsAttribute
	private Integer broadband;//宽带
	@XStreamAsAttribute
	private Integer prepay;//支付类型
	@XStreamAsAttribute
	private String prices;//渠道价
	@XStreamAsAttribute
	private String status;//房型状态
	@XStreamAsAttribute
	private String counts;//房量
	@XStreamAsAttribute
	private String last;//连住天数
	@XStreamAsAttribute
	private String advance;//提前预订天数
	@XStreamAsAttribute
	private String refusestate;//房量不足是否拒绝预订
	@XStreamAsAttribute
	private Integer channel;//房量不足是否拒绝预订
	public HotelOfferRoom(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBreakfast() {
		return breakfast;
	}
	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}
	
	public Integer getBed() {
		return bed;
	}

	public void setBed(Integer bed) {
		this.bed = bed;
	}

	public Integer getPrepay() {
		return prepay;
	}

	public void setPrepay(Integer prepay) {
		this.prepay = prepay;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCounts() {
		return counts;
	}
	public void setCounts(String counts) {
		this.counts = counts;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getAdvance() {
		return advance;
	}
	public void setAdvance(String advance) {
		this.advance = advance;
	}
	public String getRefusestate() {
		return refusestate;
	}
	public void setRefusestate(String refusestate) {
		this.refusestate = refusestate;
	}

	public String getPrices() {
		return prices;
	}

	public void setPrices(String prices) {
		this.prices = prices;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Integer getBroadband() {
		return broadband;
	}

	public void setBroadband(Integer broadband) {
		this.broadband = broadband;
	}

	
	
}
