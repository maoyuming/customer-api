package com.fangbaba.api.domain.fangcang.hotel;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("HotelInfo")
public class HotelInfo  implements Serializable{
	@XStreamAlias("FcHotelId")
	private Long fcHotelId;//>187080</HotelId>
	@XStreamAlias("FcHotelChnName")
	private String fcHotelChnName;//>合肥新西苑旅社</HotelChnName>
	@XStreamAlias("FcChnAddress")
	private String fcChnAddress;//>梅山路中医学院24-108号</ChnAddress>
	@XStreamAlias("FcTelephone")
	private String fcTelephone;//>0551-62835935</Telephone>
	@XStreamAlias("FcWebSiteURL")
	private String fcWebSiteURL;//></WebSiteURL>
	@XStreamAlias("FcHotelStar")
	private String fcHotelStar;//>66</HotelStar>
	@XStreamAlias("FcCity")
	private String fcCity;//>HFE</City>
	@XStreamAlias("FcDistinct")
	private String fcDistinct;//></Distinct>
	@XStreamAlias("FcBusiness")
	private String fcBusiness;//></Business>
	
	@XStreamAlias("Rooms")
	private List<RoomType> rooms;
	
	
	
	public List<RoomType> getRooms() {
		return rooms;
	}
	public void setRooms(List<RoomType> rooms) {
		this.rooms = rooms;
	}
	public Long getFcHotelId() {
		return fcHotelId;
	}
	public void setFcHotelId(Long fcHotelId) {
		this.fcHotelId = fcHotelId;
	}
	public String getFcHotelChnName() {
		return fcHotelChnName;
	}
	public void setFcHotelChnName(String fcHotelChnName) {
		this.fcHotelChnName = fcHotelChnName;
	}
	public String getFcChnAddress() {
		return fcChnAddress;
	}
	public void setFcChnAddress(String fcChnAddress) {
		this.fcChnAddress = fcChnAddress;
	}
	public String getFcTelephone() {
		return fcTelephone;
	}
	public void setFcTelephone(String fcTelephone) {
		this.fcTelephone = fcTelephone;
	}
	public String getFcWebSiteURL() {
		return fcWebSiteURL;
	}
	public void setFcWebSiteURL(String fcWebSiteURL) {
		this.fcWebSiteURL = fcWebSiteURL;
	}
	public String getFcHotelStar() {
		return fcHotelStar;
	}
	public void setFcHotelStar(String fcHotelStar) {
		this.fcHotelStar = fcHotelStar;
	}
	public String getFcCity() {
		return fcCity;
	}
	public void setFcCity(String fcCity) {
		this.fcCity = fcCity;
	}
	public String getFcDistinct() {
		return fcDistinct;
	}
	public void setFcDistinct(String fcDistinct) {
		this.fcDistinct = fcDistinct;
	}
	public String getFcBusiness() {
		return fcBusiness;
	}
	public void setFcBusiness(String fcBusiness) {
		this.fcBusiness = fcBusiness;
	}
	
}
