package com.xiaofei.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.order.OrderCommentEntity;
import com.xiaofei.mapper.order.OrderCommentMapper;
import com.xiaofei.service.order.OrderCommentService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public List<OrderCommentEntity> selectAllByOrderId(List<String> orderIds) {
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

    @Override
    public OrderCommentEntity selectAllByOrderId(String orderId) {
        QueryWrapper<OrderCommentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("orderId", orderId);
        OrderCommentEntity entity = orderCommentMapper.selectOne(wrapper);
        return entity;
    }

    /**
     * 收集所有评价的数量和综合评分
     *
     * @param list
     */
    @Override
    public Map<String, Double> collectEvaluateAndRateNumber(List<OrderCommentEntity> list) {
        Map<String, Double> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
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

    /**
     * 插入评价信息
     *
     * @param comment
     */
    @Override
    public Boolean insert(OrderCommentEntity comment) {
        Date today = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = formatDate.format(today);
        Date timeDate = null;
        try {
            timeDate = formatDate.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp dateTime = new Timestamp(timeDate.getTime());
        comment.setCommentTime(dateTime);
        int insert = orderCommentMapper.insert(comment);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    /**
     * 更新评价信息
     *
     * @param comment
     */
    @Override
    public Boolean update(OrderCommentEntity comment, String orderId) {
        QueryWrapper<OrderCommentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("orderId", orderId);
        int updateById = orderCommentMapper.update(comment, wrapper);
        if (updateById > 0) {
            return true;
        }
        return false;
    }
}
