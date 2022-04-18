package com.xiaofei.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.mapper.order.PaymentInfoMapper;
import com.xiaofei.service.order.PaymentInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 15:31
 */
@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {
    @Resource
    private PaymentInfoMapper paymentInfoMapper;

    /**
     * 根据订单id，查询支付信息
     */
    @Override
    public List<PaymentInfoEntity> selectPaymentInfoByOrderIds(List<String> orderIds) {
        QueryWrapper<PaymentInfoEntity> wrapper = new QueryWrapper<>();
        if (CollectionUtils.isEmpty(orderIds)) {
            return new ArrayList<>();
        } else {
            wrapper.in("orderId", orderIds);
            List<PaymentInfoEntity> entityList = paymentInfoMapper.selectList(wrapper);
            if (CollectionUtils.isEmpty(entityList)) {
                return new ArrayList<>();
            }
            return entityList;
        }
    }

    /**
     * 过滤未支付订单数量
     *
     * @param list
     */
    @Override
    public Integer filterUnPaidNumber(List<PaymentInfoEntity> list) {
        Integer unPaidNumber = 0;
        if (!CollectionUtils.isEmpty(list)) {
            for (PaymentInfoEntity entity : list) {
                if (entity.getPaymentStatus() == 0) {
                    unPaidNumber++;
                }
            }
        }
        return unPaidNumber;
    }

    /**
     * 过滤支付完成的
     *
     * @param list
     */
    @Override
    public Integer filterPaymentSuccess(List<PaymentInfoEntity> list) {
        Integer paymentSuccessNumber = 0;
        if (!CollectionUtils.isEmpty(list)) {
            for (PaymentInfoEntity entity : list) {
                if (entity.getPaymentStatus() == 1) {
                    paymentSuccessNumber++;
                }
            }
        }
        return paymentSuccessNumber;
    }

    /**
     * 创建订单支付信息
     *
     * @param paymentInfo
     */
    @Override
    public Boolean insertPayment(PaymentInfoEntity paymentInfo) {
        int insertFlag = paymentInfoMapper.insert(paymentInfo);
        if (insertFlag > 0) {
            return true;
        }
        return false;
    }
}
