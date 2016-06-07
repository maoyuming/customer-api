package com.fangbaba.api.domain.qunar;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 酒店信息
 * @author zhangyajun
 *
 */
@XmlRootElement
public class HotelOfferPrice implements Serializable{


	private static final long serialVersionUID = 2516961440414076616L;
	@XStreamAsAttribute
	private Long id;
	@XStreamAsAttribute
	private String city;
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String address;
	@XStreamAsAttribute
	private String tel;
	@XStreamAsAttribute
	private String promotion;
	@XStreamAsAttribute
	List<HotelOfferRoom> rooms;

	public HotelOfferPrice(){}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public List<HotelOfferRoom> getRooms() {
		return rooms;
	}

	public void setRooms(List<HotelOfferRoom> rooms) {
		this.rooms = rooms;
	}

	
}
