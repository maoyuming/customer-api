package com.fangbaba.api.domain.open.hotel;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单请求实体类
 * <p/>
 * Created by nolan on 16/3/25.
 * description:
 */
public class OrderRequest {

	private Long orderid;
    private Long hotelid;
    private String begintime;
    private String endtime;
    private String contact;
    private String contactsphone;
    private String channelorderid;
    private String paytype;
    private List<NestGuestInfo> guestinfo;
    private List<NestOrderDetail> createorderdetails;

    public Long getHotelId() {
        return hotelid;
    }

    public void setHotelId(Long hotelId) {
        this.hotelid = hotelId;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactsphone() {
        return contactsphone;
    }

    public void setContactsphone(String contactsphone) {
        this.contactsphone = contactsphone;
    }

    public String getChannelorderid() {
        return channelorderid;
    }

    public void setChannelorderid(String channelorderid) {
        this.channelorderid = channelorderid;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public List<NestGuestInfo> getGuestinfo() {
        return guestinfo;
    }

    public void setGuestinfo(List<NestGuestInfo> guestinfo) {
        this.guestinfo = guestinfo;
    }

    public List<NestOrderDetail> getCreateorderdetails() {
        return createorderdetails;
    }

    public void setCreateorderdetails(List<NestOrderDetail> createorderdetails) {
        this.createorderdetails = createorderdetails;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "hotelid='" + hotelid + '\'' +
                ", begintime='" + begintime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", contact='" + contact + '\'' +
                ", contactsphone='" + contactsphone + '\'' +
                ", channelorderid='" + channelorderid + '\'' +
                ", paytype='" + paytype + '\'' +
                ", guestinfo=" + guestinfo +
                '}';
    }

    public class NestGuestInfo {
        private String name;

        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public String toString() {
            return "NestGuestInfo{" +
                    "name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }

    public class NestOrderDetail {
        private int num;

        private Long roomtypeid;

        private List<NestSaleRange> salelist;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public Long getRoomtypeid() {
            return roomtypeid;
        }

        public void setRoomtypeid(Long roomtypeid) {
            this.roomtypeid = roomtypeid;
        }

        public List<NestSaleRange> getSalelist() {
            return salelist;
        }

        public void setSalelist(List<NestSaleRange> salelist) {
            this.salelist = salelist;
        }

        @Override
        public String toString() {
            return "NestOrderDetail{" +
                    "num=" + num +
                    ", roomtypeid=" + roomtypeid +
                    ", salelist=" + salelist +
                    '}';
        }
    }

    public class NestSaleRange {
        private String actiondate;
        private BigDecimal price;

        public String getActiondate() {
            return actiondate;
        }

        public void setActiondate(String actiondate) {
            this.actiondate = actiondate;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "NestSaleRange{" +
                    "actiondate='" + actiondate + '\'' +
                    ", price=" + price +
                    '}';
        }
    }

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Long getHotelid() {
		return hotelid;
	}

	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}
    
    
    
}
