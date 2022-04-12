package com.xiaofei.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @date 2022/4/10 15:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVo {
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
    private String paymentStatusName;
    /**
     * 收款方姓名
     */
    private Integer beneficiaryName;
    /**
     * 包裹数量
     */
    private Integer boxNumber;
    /**
     * 总重量
     */
    private Double weightTotal;
}
