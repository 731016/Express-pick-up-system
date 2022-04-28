package com.xiaofei.service.order;

import com.github.pagehelper.PageInfo;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.entity.order.PaymentInfoEntity;

import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 15:29
 */
public interface PaymentInfoService {
    /**
     * 根据用户id，查询支付信息
     */
    PageInfo<PaymentInfoEntity> selectPaymentInfoByOrderIds(SearchCondition search, List<String> orderIds);
    List<PaymentInfoEntity> selectPaymentInfoByOrderIds(List<String> orderIds);
    /**
     * 过滤未支付订单数量
     */
    Integer filterUnPaidNumber(List<PaymentInfoEntity> list);
    /**
     * 过滤支付完成的
     */
    Integer filterPaymentSuccess(List<PaymentInfoEntity> list);
    /**
     * 创建订单支付信息
     */
    Boolean insertPayment(PaymentInfoEntity paymentInfo);
}
