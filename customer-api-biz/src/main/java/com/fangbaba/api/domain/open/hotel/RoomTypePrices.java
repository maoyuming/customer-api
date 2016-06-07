/**
 * 2016年3月22日下午4:29:08
 * zhaochuanbin
 */
package com.fangbaba.api.domain.open.hotel;

import java.util.List;

/**
 * @author zhaochuanbin
 *
 */
public class RoomTypePrices {
    private Long roomtypeid;

    private List<PriceInfos> priceinfos;

    public void setRoomtypeid(Long roomtypeid) {
        this.roomtypeid = roomtypeid;
    }

    public Long getRoomtypeid() {
        return this.roomtypeid;
    }

	public List<PriceInfos> getPriceinfos() {
		return priceinfos;
	}

	public void setPriceinfos(List<PriceInfos> priceinfos) {
		this.priceinfos = priceinfos;
	}

}
