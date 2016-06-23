package com.dtk.test;

import com.alibaba.fastjson.JSON;
import com.duantuke.api.domain.common.OpenResponse;
import com.duantuke.basic.enums.SkuTypeEnum;
import com.duantuke.order.common.enums.OrderErrorEnum;
import com.duantuke.order.common.enums.OrderStatusEnum;
import com.duantuke.order.common.enums.PayStatusEnum;
import com.duantuke.order.common.enums.PayTypeEnum;
import com.duantuke.order.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jjh on 16/6/22.
 */
public class OpenResponseJson {

    public static void main(String[] args) {
        OpenResponse<Order> openResponse = new OpenResponse<Order>();

        List<Order> orderList = new ArrayList<Order>();
        Order order = new Order();
        order.setId(11111L);
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
        order.setBeginTime(new Date());
        order.setEndTime(new Date());

        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setId(2222L);
        orderDetail1.setSkuId(789L);
        orderDetail1.setSkuName("商品");
        orderDetail1.setSkuType(SkuTypeEnum.roomtype.getCode());
        orderDetail1.setNum(1);
        orderDetail1.setTotalPrice(new BigDecimal(33333));

        List<OrderDetailPrice> priceDetails = new ArrayList<OrderDetailPrice>();
        OrderDetailPrice orderDetailPrice = new OrderDetailPrice();
        orderDetailPrice.setActionTime(new Date());
        orderDetailPrice.setCreateBy("11111");
        orderDetailPrice.setCreateTime(new Date());
        orderDetailPrice.setOrderDetailId(2222L);
        orderDetailPrice.setOrderId(11111L);
        orderDetailPrice.setPrice(new BigDecimal(123));
        orderDetailPrice.setSkuId(789L);
        orderDetailPrice.setSkuName("商品");
        orderDetailPrice.setUpdateBy("11111");
        orderDetailPrice.setUpdateTime(new Date());
        priceDetails.add(orderDetailPrice);


        orderDetail1.setPriceDetails(priceDetails);
        orderDetailList.add(orderDetail1);

        order.setOrderDetails(orderDetailList);
        orderList.add(order);

        openResponse.setResult("true");
        openResponse.setData(order);
        openResponse.setErrorCode(OrderErrorEnum.paramsError.getErrorCode());
        openResponse.setErrorMessage(OrderErrorEnum.paramsError.getErrorMsg());

        System.out.println(JSON.toJSONString(openResponse));
    }

}
