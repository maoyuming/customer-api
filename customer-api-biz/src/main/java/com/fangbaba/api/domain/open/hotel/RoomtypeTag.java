package com.fangbaba.api.domain.open.hotel;

import java.util.List;

import com.fangbaba.basic.face.bean.Tags;

public class RoomtypeTag {
    private Long roomtypeid;
    private List<Tags> tags;
	public Long getRoomtypeid() {
		return roomtypeid;
	}
	public void setRoomtypeid(Long roomtypeid) {
		this.roomtypeid = roomtypeid;
	}
	public List<Tags> getTags() {
		return tags;
	}
	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}
    
    
}
