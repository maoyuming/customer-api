package com.fangbaba.api.domain.fangcang;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
public class Request implements Serializable{
	@XStreamAlias("Header")
    private Header header;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}
    
}
