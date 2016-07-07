package com.dtk.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.duantuke.basic.enums.SkuTypeEnum;
import com.duantuke.order.common.enums.OrderStatusEnum;
import com.duantuke.order.common.enums.PayStatusEnum;
import com.duantuke.order.common.enums.PayTypeEnum;
import com.duantuke.order.model.Order;
import com.duantuke.order.model.OrderDetail;
import com.duantuke.order.model.OrderDetailPrice;

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
        order.setSupplierId(456L);
        order.setSupplierName("sky");
        order.setMemo("beizhu");
        order.setBeginTime(new Date());
        order.setEndTime(new Date());

        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setSkuId(1L);
        orderDetail1.setSkuName("商品");
        orderDetail1.setSkuType(SkuTypeEnum.roomtype.getCode());
        orderDetail1.setNum(1);
        orderDetail1.setTotalPrice(new BigDecimal(100));
//        orderDetail1.setPrice(new BigDecimal(100));

        List<OrderDetailPrice> priceDetails = new ArrayList<OrderDetailPrice>();
        OrderDetailPrice orderDetailPrice = new OrderDetailPrice();
        orderDetailPrice.setActionTime(new Date());
        orderDetailPrice.setPrice(new BigDecimal(123));
        orderDetailPrice.setSkuId(789L);
        orderDetailPrice.setSkuName("商品");
        priceDetails.add(orderDetailPrice);

        orderDetail1.setPriceDetails(priceDetails);

        orderDetailList.add(orderDetail1);

        order.setOrderDetails(orderDetailList);

        System.out.println(JSON.toJSONString(order));

        // {"beginTime":1466241251395,"contact":"jjh","contactPhone":"123456","customerId":123,"endTime":1466241251395,"memo":"beizhu","orderDetails":[{"num":1,"price":100,"skuId":789,"skuName":"商品","skuType":1}],"payStatus":10,"payType":100,"promotionId":321,"status":50,"supplierId":456,"supplierName":"sky","totalPrice":100,"type":0}
    }

}
