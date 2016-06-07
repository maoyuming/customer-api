package com.fangbaba.api.domain.open.hotel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by nolan on 16/3/25.
 * description:
 */
public class OrderResponse {
    private Long orderid;
    private Integer orderstatus;
    private Long hotelid;
    private Integer paytype;//支付类型，1预付、2到付
    private String arrivetime;// ’yyyyMMdd’, // 预抵时间
    private String leavetime;// ’yyyyMMdd’’, // 预离时间
    private String contact; //联系人
    private String phone; //联系电话
    private String memo; //备注
    private List<Map<String, String>> user; //map(name:'') //入住人姓名
    private List<NestOrderDetailResponse> orderdetails;

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public Integer getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(Integer orderstatus) {
        this.orderstatus = orderstatus;
    }

    public Long getHotelid() {
        return hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public Integer getPaytype() {
        return paytype;
    }

    public void setPaytype(Integer paytype) {
        this.paytype = paytype;
    }

    public String getArrivetime() {
        return arrivetime;
    }

    public void setArrivetime(String arrivetime) {
        this.arrivetime = arrivetime;
    }

    public String getLeavetime() {
        return leavetime;
    }

    public void setLeavetime(String leavetime) {
        this.leavetime = leavetime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<Map<String, String>> getUser() {
        return user;
    }

    public void setUser(List<Map<String, String>> user) {
        this.user = user;
    }

    public List<NestOrderDetailResponse> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(List<NestOrderDetailResponse> orderdetails) {
        this.orderdetails = orderdetails;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderid=" + orderid +
                ", orderstatus=" + orderstatus +
                ", hotelid=" + hotelid +
                ", paytype=" + paytype +
                ", arrivetime='" + arrivetime + '\'' +
                ", leavetime='" + leavetime + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", memo='" + memo + '\'' +
                ", user=" + user +
                ", orderdetails=" + orderdetails +
                '}';
    }

    public class NestOrderDetailResponse {
        private Long roomtypeid;
        private Integer booknum;
        private List<NestRoomTypePriceResponse> cost;

        public Long getRoomtypeid() {
            return roomtypeid;
        }

        public void setRoomtypeid(Long roomtypeid) {
            this.roomtypeid = roomtypeid;
        }

        public Integer getBooknum() {
            return booknum;
        }

        public void setBooknum(Integer booknum) {
            this.booknum = booknum;
        }

        public List<NestRoomTypePriceResponse> getCost() {
            return cost;
        }

        public void setCost(List<NestRoomTypePriceResponse> cost) {
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "NestOrderDetailResponse{" +
                    "roomtypeid=" + roomtypeid +
                    ", booknum=" + booknum +
                    ", cost=" + cost +
                    '}';
        }
    }

    public class NestRoomTypePriceResponse {
        private String time;
        private BigDecimal cost;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public BigDecimal getCost() {
            return cost;
        }

        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "NestRoomTypePriceResponse{" +
                    "time='" + time + '\'' +
                    ", cost=" + cost +
                    '}';
        }
    }
}
