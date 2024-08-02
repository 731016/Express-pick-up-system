package com.xiaofei.common;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;

/**
 * TODO 类描述
 *
 * @date 2022/4/10 14:19
 */
@Getter
public enum OrderStatus {

    WAIT_ACCEPT(10, "等待接单"),
    WAIT_DISPAT(20, "派送中"),
    WAIT_FAIL(30, "订单异常"),
    WAIT_SUCCESS(40, "订单完成"),
    WAIT_PAYMENT(0,"等待支付"),
    SUCCESS_PAYMENT(1,"支付成功");

    private Integer orderStatus;
    private String orderStatusName;

    OrderStatus(Integer orderStatus, String orderStatusName) {
        this.orderStatus = orderStatus;
        this.orderStatusName = orderStatusName;
    }

    public static String getNameByStatus(Integer status) {
        for (OrderStatus value : OrderStatus.values()) {
            if (value.getOrderStatus().equals(status)) {
                return value.getOrderStatusName();
            }
        }
        return "";
    }
}
