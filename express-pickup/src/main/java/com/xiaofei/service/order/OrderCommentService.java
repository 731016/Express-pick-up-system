package com.xiaofei.service.order;

import com.xiaofei.entity.order.OrderCommentEntity;

import java.util.List;
import java.util.Map;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 16:13
 */
public interface OrderCommentService {
    /**
     * 根据订单id，查找评价信息
     */
    List<OrderCommentEntity> selectAllByOrderId(List<String> orderIds);
    /**
     * 查询单个评价信息
     */
    OrderCommentEntity selectAllByOrderId(String orderId);
    /**
     * 收集所有评价的数量和综合评分
     */
    Map<String, Double> collectEvaluateAndRateNumber(List<OrderCommentEntity> list);
    /**
     * 插入评价信息
     */
    Boolean insert(OrderCommentEntity comment);
    /**
     * 更新评价信息
     */
    Boolean update(OrderCommentEntity comment,String orderId);
}
