/**
 * 2016年3月22日下午4:52:08
 * zhaochuanbin
 */
package com.fangbaba.api.domain.open.hotel;

import java.util.List;

/**
 * @author zhaochuanbin
 *
 */
public class RoomTypePricesData {
    private Long hotelid;
    private List<RoomTypePrices> roomtypeprices;

	public List<RoomTypePrices> getRoomtypeprices() {
		return roomtypeprices;
	}

	public void setRoomtypeprices(List<RoomTypePrices> roomtypeprices) {
		this.roomtypeprices = roomtypeprices;
	}

    public Long getHotelid() {
        return hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }
    
}
