/**
 * 2016年3月21日下午7:06:28
 * zhaochuanbin
 */
package com.fangbaba.api.domain.open.hotel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author zhaochuanbin
 *
 */
public class ApiRoomTypePriceRequest implements Serializable{
    
    private static final long serialVersionUID = 5713136590749662991L;

    private List<Long> roomtypeids;
    
    private Long hotelid;
    
    private Long roomtypeid;
    
    private Long otatype;
    
    private String begintime;

    private String endtime;
    

    public List<Long> getRoomtypeids() {
        return roomtypeids;
    }

    public void setRoomtypeids(List<Long> roomtypeids) {
        this.roomtypeids = roomtypeids;
    }

    public Long getOtatype() {
        return otatype;
    }

    public void setOtatype(Long otatype) {
        this.otatype = otatype;
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

    public Long getHotelid() {
        return hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public Long getRoomtypeid() {
        return roomtypeid;
    }

    public void setRoomtypeid(Long roomtypeid) {
        this.roomtypeid = roomtypeid;
    }

    
    
}
