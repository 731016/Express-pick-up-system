package com.xiaofei.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofei.entity.order.OrderInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单信息
 *
 * @date 2022/4/9 14:32
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfoEntity> {
    /**
     * 撤销订单
     */
    Integer revokeOrderStatus(List<String> ids);
    /**
     * 删除订单
     */
    Integer deleteOrderStatus(List<String> ids);
    /**
     * 还原订单
     */
    Integer recyceOrderStatus(List<String> ids);
}
