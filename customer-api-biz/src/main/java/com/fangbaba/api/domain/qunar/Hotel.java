package com.fangbaba.api.domain.qunar;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XmlRootElement
public class Hotel implements Serializable {
	
	@XStreamAlias("id")
    @XStreamAsAttribute
	private Long	id;//代理商酒店的唯一标识 最小长度为1字符，最大长度为16字符
	@XStreamAsAttribute
	private	String name;//酒店名称 最小长度为1字符，最大长度为100字符
	@XStreamAsAttribute
	private String city;//酒店城市，支持城市的中文名称、拼音以及城市的英文名称
	@XStreamAsAttribute
	private String address;//酒店地址，最小长度为1字符，最大长度为100字符
	@XStreamAsAttribute
	private String	tel;//酒店联系电话，格式：区号-电话，最大30字符
	private	String[] remarks;//酒店特殊说明信息，最多配置5条
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
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
	public String[] getRemarks() {
		return remarks;
	}
	public void setRemarks(String[] remarks) {
		this.remarks = remarks;
	}
	public Hotel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
