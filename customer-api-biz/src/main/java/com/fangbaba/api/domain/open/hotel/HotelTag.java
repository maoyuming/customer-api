package com.fangbaba.api.domain.open.hotel;

import java.util.List;

import com.fangbaba.basic.face.bean.Tags;

public class HotelTag {
    private Long hotelid;
    private List<Tags> tags;
    private List<RoomtypeTag> roomtypeTags;
	public Long getHotelid() {
		return hotelid;
	}
	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}
	public List<Tags> getTags() {
		return tags;
	}
	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}
	public List<RoomtypeTag> getRoomtypeTags() {
		return roomtypeTags;
	}
	public void setRoomtypeTags(List<RoomtypeTag> roomtypeTags) {
		this.roomtypeTags = roomtypeTags;
	}
    
    
}
