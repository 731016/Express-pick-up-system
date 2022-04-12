package com.xiaofei.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付信息
 *
 * @date 2022/4/9 14:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("payment_info")
public class PaymentInfoEntity {
    private String orderid;
    /**
     * 支付方式
     */
    private String paymentMethod;
    /**
     * 支付金额
     */
    private Double paymentAmount;
    /**
     * 流水号
     */
    private String serialNumber;
    /**
     * 支付状态
     * 【0：等待支付】
     * 【1：支付成功】
     */
    private Integer paymentStatus;
    @TableField(exist = false)
    private Integer paymentStatusName;
    /**
     * 收款方姓名
     */
    private Integer beneficiaryName;
    /**
     * 包裹数量
     */
    @TableField(exist = false)
    private Integer boxNumber;
    /**
     * 总重量
     */
    @TableField(exist = false)
    private Double weightTotal;
}
