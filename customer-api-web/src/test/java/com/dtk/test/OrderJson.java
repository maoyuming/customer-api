package com.dtk.test;

import com.alibaba.fastjson.JSON;
import com.duantuke.basic.enums.SkuTypeEnum;
import com.duantuke.order.common.enums.OrderStatusEnum;
import com.duantuke.order.common.enums.PayStatusEnum;
import com.duantuke.order.common.enums.PayTypeEnum;
import com.duantuke.order.model.Order;
import com.duantuke.order.model.OrderDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jjh on 16/6/17.
 */
public class OrderJson {

    public static void main(String[] args) {
        Order order = new Order();
        order.setType(0);
        order.setStatus(OrderStatusEnum.confirmed.getId());
        order.setPayType(PayTypeEnum.prepay.getId());
        order.setPayStatus(PayStatusEnum.noNeedToPay.getId());
        order.setTotalPrice(new BigDecimal(100));
        order.setContact("jjh");
        order.setContactPhone("123456");
        order.setCustomerId(123L);
        order.setPromotionId(321L);
        order.setSupplierId(456L);
        order.setSupplierName("sky");
        order.setMemo("beizhu");

        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setSkuId(789L);
        orderDetail1.setSkuName("商品");
        orderDetail1.setSkuType(SkuTypeEnum.roomtype.getCode());
        orderDetail1.setNum(1);
        orderDetail1.setPrice(new BigDecimal(100));
        orderDetail1.setBeginTime(new Date());
        orderDetail1.setEndTime(new Date());
        orderDetailList.add(orderDetail1);

        order.setOrderDetails(orderDetailList);

        System.out.println(JSON.toJSONString(order));

        // {"contact":"jjh","contactPhone":"123456","customerId":123,"memo":"beizhu","orderDetails":[{"beginTime":1466147711970,"endTime":1466147711970,"num":1,"price":100,"skuId":789,"skuName":"商品","skuType":1}],"payStatus":10,"payType":100,"promotionId":321,"status":50,"supplierId":456,"supplierName":"sky","totalPrice":100,"type":0}
    }

}
