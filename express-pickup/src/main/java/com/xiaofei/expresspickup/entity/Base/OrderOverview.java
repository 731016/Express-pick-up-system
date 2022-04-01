package com.xiaofei.expresspickup.entity.Base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单状态
 *
 * @author 折腾的小飞
 * @date 2022/3/26 15:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverview{
    /**
     * 支付状态
     */
    private Integer unPaid;
    /**
     * 等待接单
     */
    private Integer waitingOrder;
    /**
     * 正在派送
     */
    private Integer dispatchingOrders;

}
