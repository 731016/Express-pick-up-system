package com.xiaofei.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tuaofei
 * @description TODO
 * @date 2024/8/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardVo {
    private Integer unPaidNumber;
    private Integer waitOrderNumber;
    private Integer dispatchOrderNumber;
    /**
     * 收到的评价
     */
    private Integer receivedEvaluateNumber;
    /**
     * 综合得分
     */
    private Double overallRate;
    private Integer allowOrderNumber;
    private Integer newAddOrders;
    private Integer newAddRegister;
    private Integer userTotal;
    private Integer disableUser;
    private Integer freezeUser;
}
