package com.fangbaba.api.domain.fangcang.hotelmapping;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XmlRootElement
public class SpRoomtype implements Serializable{
    @XStreamImplicit(itemFieldName="SpRoomTypeId")
	private List<String> SpRoomTypeId;

	public List<String> getSpRoomTypeId() {
		return SpRoomTypeId;
	}

	public void setSpRoomTypeId(List<String> spRoomTypeId) {
		SpRoomTypeId = spRoomTypeId;
	}

	
	
}
