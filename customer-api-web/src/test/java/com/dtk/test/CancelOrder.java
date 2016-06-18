package com.dtk.test;

import com.alibaba.fastjson.JSON;
import com.duantuke.order.common.enums.CancelTypeEnum;
import com.duantuke.order.model.CancelOrderRequest;

/**
 * Created by jjh on 16/6/17.
 */
public class CancelOrder {

    // {"cancelType":0,"operatorId":"111","operatorName":"jjh","orderId":222,"reason":"reason","supplierId":333}
    public static void main(String[] args) {
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setReason("reason");
        cancelOrderRequest.setCancelType(CancelTypeEnum.common.getId());
        cancelOrderRequest.setOperatorId("111");
        cancelOrderRequest.setOperatorName("jjh");
        cancelOrderRequest.setOrderId(222L);
        cancelOrderRequest.setSupplierId(333L);

        System.out.println(JSON.toJSONString(cancelOrderRequest));
    }

}
