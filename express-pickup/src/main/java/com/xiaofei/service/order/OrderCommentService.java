package com.xiaofei.service.order;

import com.github.pagehelper.PageInfo;
import com.xiaofei.common.SearchCondition;
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
    PageInfo<OrderCommentEntity> selectAllByOrderId(List<String> orderIds, SearchCondition search,String roleId);
    /**
     * 查询单个评价信息
     */
    OrderCommentEntity selectAllByOrderId(String orderId);
    /**
     * 收集所有评价的数量和综合评分
     * sign :true用于普通用户，false用于配送用户
     */
    Map<String, Double> collectEvaluateAndRateNumber(List<OrderCommentEntity> list,Boolean sign);
    /**
     * 插入评价信息
     */
    Boolean insert(OrderCommentEntity comment);
    /**
     * 更新评价信息
     */
    Boolean update(OrderCommentEntity comment,String orderId);
}
