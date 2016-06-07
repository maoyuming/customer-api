package com.fangbaba.api.domain.qunar;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XmlRootElement
@XStreamAlias("city")
@XStreamConverter(ToAtrributedValueConverter.class)
public class City {
	@XStreamAsAttribute
	private String  code;//="10th_of_ramadan_city" 
	@XStreamAsAttribute
	private String  name;//="10th of Ramadan City" 
	@XStreamAsAttribute
	private String 	countryName;//="埃及" 
	@XStreamAsAttribute
	private String 	countryPy;//="aiji" 
	@XStreamAsAttribute
	private String 	provinceName;//="开罗" 
	@XStreamAsAttribute
	private String 	provincePy;//="kailuo"
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryPy() {
		return countryPy;
	}
	public void setCountryPy(String countryPy) {
		this.countryPy = countryPy;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getProvincePy() {
		return provincePy;
	}
	public void setProvincePy(String provincePy) {
		this.provincePy = provincePy;
	}
	
	
}
