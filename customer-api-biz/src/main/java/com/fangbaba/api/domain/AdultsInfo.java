package com.fangbaba.api.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("AdultsInfo")
public class AdultsInfo {

	@XStreamAlias("FirstName")
	private String FirstName;
	@XStreamAlias("LastName")
	private String LastName;
	/*@XStreamAlias("AgeGroup")
	private String AgeGroup;*/
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	
}
