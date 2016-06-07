package com.fangbaba.api.domain.qunar;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XmlRootElement
@XStreamAlias("hotel")
public class HotelChange implements Serializable {
	
	private static final long serialVersionUID = -2274647004696821567L;
    @XStreamAsAttribute
	private Long	id;
    @XStreamAsAttribute
	private String updatetime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public HotelChange() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	
	
}
