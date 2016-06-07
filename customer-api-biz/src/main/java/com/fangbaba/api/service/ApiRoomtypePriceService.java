/**
 * 2016年3月22日下午3:18:39
 * zhaochuanbin
 */
package com.fangbaba.api.service;

import java.util.Date;
import com.fangbaba.api.domain.open.hotel.RoomTypePricesData;

/**
 * @author zhaochuanbin
 *
 */
public interface ApiRoomtypePriceService {
    public RoomTypePricesData  queryPriceByRoomTypeid(Long hotelid,Long roomtypeid, Long otatype,  Date begintime, Date endtime);
}
