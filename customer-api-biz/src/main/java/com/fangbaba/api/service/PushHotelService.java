/**
 * 2016年3月28日下午8:11:56
 * zhaochuanbin
 */
package com.fangbaba.api.service;

/**
 * @author zhaochuanbin
 *
 */
public interface PushHotelService {
    public void otatypeRes(Long otatype,Integer channelId);
    public void pushHotel(Long hotelId,Integer channelId);
    public void pushDeleteHotel(Long hotelId,Integer channelId);
}
