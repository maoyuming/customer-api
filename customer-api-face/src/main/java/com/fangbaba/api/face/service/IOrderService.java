package com.fangbaba.api.face.service;

import com.fangbaba.api.domain.open.hotel.OrderRequest;
import com.fangbaba.api.domain.open.hotel.OrderResponse;
import com.fangbaba.api.face.base.RetInfo;
import com.fangbaba.api.face.bean.OptOrder;
import com.fangbaba.api.face.bean.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单业务服务类
 */
public interface IOrderService {
    /**
     * 查询订单
     *
     * @param channelId 渠道id
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    RetInfo<Order> queryOrder(Integer channelId, Date beginTime, Date endTime);

    /**
     * 订单操作
     *
     * @param channelId 渠道id
     * @param optOrder
     * @return
     */
    RetInfo<Order> optOrder(Integer channelId, OptOrder optOrder);


    /**
     * 创建订单
     *
     * @param orderRequest
     * @param channelId    渠道id
     * @param channelName  渠道名称
     * @param token        验证token,header参数
     * @return
     */
    public Map createOrder(OrderRequest orderRequest, Integer channelId, String token) ;


    /**
     * 取消订单
     *
     * @param orderid      订单id
     * @param cancelReason 取消原因,非必填
     * @param token        验证token,header参数
     * @param channelid    渠道id
     * @return
     */
    RetInfo<Map> cancelOrder(Long orderid, String cancelReason, String token, Integer channelid);

    /**
     * 查询订单
     *
     * @param ids       订单ids
     * @param token     验证token,header参数
     * @param channelid 渠道id
     * @return
     */
    List<OrderResponse> queryOrderByIds(List<Long> ids, String token, Integer channelid);
    
    
    public  RetInfo<Boolean> updateOrder(OrderRequest order, Integer channelId, String token);
}
