package com.xiaofei.expresspickup.entity.DashBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 欢迎界面，普通用户看板信息
 *
 * @author 折腾的小飞
 * @date 2022/3/26 15:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryUserDashBoard {
    /*
     * 收到的评价
     */
    private Integer receivedAReviewTotal;
    /*
     * 综合评分
     */
    private Float overallRating;
    /*
     * 正在处理的反馈
     */
    private  Integer processing;
    /*
     * 未处理的反馈
     */
    private  Integer notProcessed;
    /*
     * 支付状态
     */
    private Integer unPaid;
    /*
     * 等待接单
     */
    private Integer waitingOrder;
    /*
     * 正在派送
     */
    private Integer dispatchingOrders;
}
