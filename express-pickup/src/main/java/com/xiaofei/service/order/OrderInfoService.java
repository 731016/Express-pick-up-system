package com.xiaofei.service.order;

import com.github.pagehelper.PageInfo;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.dto.OrderInfoDto;

import java.util.List;
import java.util.Map;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 15:04
 */
public interface OrderInfoService {
    /**
     * 根据普通用户，查询订单信息
     */
    List<OrderInfoEntity> selectOrderByUserId(String userId);

    /**
     * 根据配送用户，查询订单信息
     */
    List<OrderInfoEntity> selectOrderByDeliveryManId(String deliveryManId);

    /**
     * 从订单信息中汇总仪表板信息
     */
    List<String> collectOrderIds(List<OrderInfoEntity> orders);

    /**
     * 获取等待接单或正在派送的订单数
     */
    Map<String, Integer> filterWaitAndDispatchOrderNumber(List<OrderInfoEntity> orders);

    /**
     * 新增订单
     */
    String insertOrder(OrderInfoEntity orderInfo);

    /**
     * 获取没有被删除或撤销的订单，通过userName,true未被删除，false删除和撤销
     */
    PageInfo<OrderInfoEntity> selectOrderBelongGeneral(SearchCondition search, String userName, Boolean flag);

    /**
     * 获取没有被删除或撤销的订单，通过true未被删除，false删除和撤销
     */
    PageInfo<OrderInfoEntity> selectOrderBelongDelivery(SearchCondition search, Boolean flag);

    /**
     *  管理员查询被删除和撤销的订单
     */
    PageInfo<OrderInfoEntity> selectAllOrder(SearchCondition search, Boolean flag);

    /**
     * 查询订单的deliveryManId等于用户id，没有被删除或撤销的订单
     */
    PageInfo<OrderInfoEntity> selectOrderBydeliveryManId(SearchCondition search, String deliveryManId);

    List<OrderInfoEntity> selectOrderBydeliveryManId(String deliveryManId);

    /**
     * 将实体类转换为前台需要展示对象
     */
    List<OrderInfoDto> poToVo(List<OrderInfoEntity> orders, List<PaymentInfoEntity> payments);

    /**
     * 撤销订单
     */
    Boolean revokeOrderStatus(List<String> ids);

    /**
     * 删除订单
     */
    Boolean deleteOrderStatus(List<String> ids);

    /**
     * 还原订单
     */
    Boolean recyceOrderStatus(List<String> ids);

    /**
     * 查询订单信息，通过订单id
     */
    List<OrderInfoEntity> selectOrderByOrderId(List<String> orderIds);

    /**
     * 查询订单信息，通过订单id
     */
    OrderInfoEntity selectOne(String id);

    /**
     * 批量更新
     */
    Boolean batchAcceptOrder(List<OrderInfoEntity> entities);

    /**
     * 根据订单状态，查询所有订单
     */
    List<OrderInfoEntity> selectAllOrderByOrderStatus(Integer... orderStatus);

    /**
     * 更新单个对象
     */
    Boolean update(OrderInfoEntity entity);

    Boolean successOrder(List<String> orderIds);
    Boolean execptionOrder(List<String> orderIds);

    /**
     * 查询订单id根据用户id
     */
    List<String> selectOrderIdByUserId(List<String> userIds);
    /**
     * 查询所有订单
     */
    List<OrderInfoEntity> selectAll();
}
