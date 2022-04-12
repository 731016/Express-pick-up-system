package com.xiaofei.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.order.OrderCommentEntity;
import com.xiaofei.mapper.order.OrderCommentMapper;
import com.xiaofei.service.order.OrderCommentService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 16:14
 */
@Service
public class OrderCommentServiceImpl implements OrderCommentService {
    @Resource
    private OrderCommentMapper orderCommentMapper;

    /**
     * 根据订单id，查找评价信息
     *
     * @param orderIds
     */
    @Override
    public List<OrderCommentEntity> selectAllByUserId(List<String> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return new ArrayList<>();
        } else {
            QueryWrapper<OrderCommentEntity> wrapper = new QueryWrapper<>();
            wrapper.in("orderId", orderIds);
            List<OrderCommentEntity> list = orderCommentMapper.selectList(wrapper);
            if (CollectionUtils.isEmpty(list)) {
                return new ArrayList<>();
            }
            return list;
        }
    }

    /**
     * 收集所有评价的数量和综合评分
     *
     * @param list
     */
    @Override
    public Map<String, Double> collectEvaluateAndRateNumber(List<OrderCommentEntity> list) {
        Map<String, Double> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)){
            map.put("evaluate", (double) list.size());
            map.put("rate", 0.00);
            Double rateTotal = 0.00;
            for (OrderCommentEntity entity : list) {
                rateTotal += entity.getUserRating();
            }
            map.put("rate", rateTotal / list.size());
        }
        return map;
    }
}
