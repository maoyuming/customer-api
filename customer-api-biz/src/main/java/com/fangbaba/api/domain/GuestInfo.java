package com.fangbaba.api.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("GuestInfo")
public class GuestInfo {

	@XStreamAlias("GuestName")
	private String GuestName;
	@XStreamAlias("Nationality")
	private String Nationality;
	public String getGuestName() {
		return GuestName;
	}
	public void setGuestName(String guestName) {
		GuestName = guestName;
	}
	public String getNationality() {
		return Nationality;
	}
	public void setNationality(String nationality) {
		Nationality = nationality;
	}
	
	
}
