package com.fangbaba.api.domain.open.hotel;


public class RoomtypeStcokRequest {

	private Long hotelid;
	//房型id
	private String roomtypeid;
	//开始时间
	private String begintime;
	//结束时间
	private String endtime;
	//下单还是每天库存
	private Boolean flag;


	public Long getHotelid() {
		return hotelid;
	}

	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}

	public String getRoomtypeid() {
		return roomtypeid;
	}

	public void setRoomtypeid(String roomtypeid) {
		this.roomtypeid = roomtypeid;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
	
	
	
}
