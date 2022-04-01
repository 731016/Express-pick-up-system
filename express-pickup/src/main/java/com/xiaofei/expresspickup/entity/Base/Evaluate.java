package com.xiaofei.expresspickup.entity.Base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @author 折腾的小飞
 * @date 2022/3/26 15:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluate{
    /*
     * 收到的评价
     */
    private Integer receivedAReviewTotal;
    /*
     * 综合评分
     */
    private Float overallRating;
}
