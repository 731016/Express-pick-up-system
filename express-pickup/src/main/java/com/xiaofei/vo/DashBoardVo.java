package com.xiaofei.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 欢迎界面，看板信息
 *
 * @author 折腾的小飞
 * @date 2022/3/26 15:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardVo {

    /**
     * 未支付订单
     */
    private Integer unPaidNumber;
    /**
     * 等待接单数量
     */
    private Integer waitOrderNumber;
    /**
     * 可以接单的数量
     */
    private Integer allowOrderNumber;
    /**
     * 正在（需要）派送的订单数量
     */
    private Integer dispatchOrderNumber;
    /**
     * 收到的评价数量
     */
    private Integer receivedEvaluateNumber;
    /**
     * 综合评分
     */
    private Double overallRate;
    /**
     * 今日新增订单数
     */
    private Integer newAddOrders;
    /**
     * 今日注册用户
     */
    private Integer newAddRegister;
    /**
     * 总用户数
     */
    private Integer userTotal;
    /**
     * 禁用用户数
     */
    private Integer disableUser;
    /**
     * 冻结用户数
     */
    private Integer freezeUser;
}
