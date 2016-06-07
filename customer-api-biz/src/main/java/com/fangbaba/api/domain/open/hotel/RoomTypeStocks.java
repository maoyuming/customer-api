/**
 * 2016年3月22日下午4:29:08
 * zhaochuanbin
 */
package com.fangbaba.api.domain.open.hotel;

import java.util.List;

/**
 * @author he
 *
 */
public class RoomTypeStocks {
    private Long roomtypeid;

    private List<StockInfos> stockinfos;

	public Long getRoomtypeid() {
		return roomtypeid;
	}

	public void setRoomtypeid(Long roomtypeid) {
		this.roomtypeid = roomtypeid;
	}

	public List<StockInfos> getStockinfos() {
		return stockinfos;
	}

	public void setStockinfos(List<StockInfos> stockinfos) {
		this.stockinfos = stockinfos;
	}


}
