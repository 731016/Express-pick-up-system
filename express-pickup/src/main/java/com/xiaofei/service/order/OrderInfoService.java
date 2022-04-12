package com.xiaofei.service.order;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.entity.dashBoard.DashBoardEntity;
import com.xiaofei.entity.order.OrderCommentEntity;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.vo.OrderInfoVo;

import java.util.List;
import java.util.Map;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 15:04
 */
public interface OrderInfoService {
    /**
     * 根据用户id，查询订单信息
     */
    List<OrderInfoEntity> selectOrderByUserId(String userId);

    /**
     * 从订单信息中汇总仪表板信息
     */
    List<String> collectOrderIdsByGeneral(List<OrderInfoEntity> orders);

    /**
     * 获取等待接单或正在派送的订单数
     */
    Map<String, Integer> filterWaitAndDispatchOrderNumber(List<OrderInfoEntity> orders);

    /**
     * 新增订单
     */
    String insertOrder(OrderInfoEntity orderInfo);

    /**
     * 获取没有被删除或撤销的订单，通过userName
     */
    PageInfo<OrderInfoEntity> selectOrderBelongGeneral(SearchCondition search, String suerName);

    /**
     * 将实体类转换为前台需要展示对象
     */
    List<OrderInfoVo> poToVo(List<OrderInfoEntity> orders, List<PaymentInfoEntity> payments);

    /**
     * 撤销订单
     */
    String revokeOrderStatus(List<String> ids);
    /**
     * 删除订单
     */
    String deleteOrderStatus(List<String> ids);
}
