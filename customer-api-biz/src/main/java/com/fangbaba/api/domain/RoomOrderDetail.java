package com.fangbaba.api.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by nolan on 16/2/15.
 * description:
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class RoomOrderDetail extends RoomOrder {
    private String guestPhone; //预订人电话
    
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private String checkOutTime;//离店时间或预离时间
    
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private String beginTime;//预抵时间
    
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private String endTime;//预离时间
    
    public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private String bookRoomTime;//下单时间
    
    @JsonProperty("CHANNEL")
    private String CHANNEL;//来源

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getBookRoomTime() {
        return bookRoomTime;
    }

    public void setBookRoomTime(String bookRoomTime) {
        this.bookRoomTime = bookRoomTime;
    }

    @JsonIgnoreProperties
    public String getCHANNEL() {
        return CHANNEL;
    }

    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

    @Override
    public String toString() {
        return super.toString() + ", RoomOrderDetail{" +
                "guestPhone='" + guestPhone + '\'' +
                ", checkOutTime=" + checkOutTime +
                ", bookRoomTime=" + bookRoomTime +
                ", CHANNEL='" + CHANNEL + '\'' +
                '}';
    }
}
