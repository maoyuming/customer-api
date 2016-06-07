package com.fangbaba.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by nolan on 16/2/15.
 * description:
 */
public class RoomOrder {
    private Long roomOrderId;//客单id
    private String roomTypeName;//房型名称
    private String roomName;//房号
    private String guestName;//宾客姓名：如果多个用逗号分隔如：张三，李四
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private String checkInTime;//入住时间或预抵时间
    private BigDecimal roomPrice;//价格
    private String status;//状态：已入住(IN)，已离店(OU)，未确认(NO)、已确认(YE)
    private String payType;//付款方式：到付(DF)，预付(YF)

    public Long getRoomOrderId() {
        return roomOrderId;
    }

    public void setRoomOrderId(Long roomOrderId) {
        this.roomOrderId = roomOrderId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "RoomOrder{" +
                "roomOrderId='" + roomOrderId + '\'' +
                ", roomTypeName='" + roomTypeName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", guestName='" + guestName + '\'' +
                ", checkInTime='" + checkInTime + '\'' +
                ", roomPrice='" + roomPrice + '\'' +
                ", status='" + status + '\'' +
                ", payType='" + payType + '\'' +
                '}';
    }
}
