package com.xiaofei.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 评价信息
 *
 * @date 2022/4/9 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_comment")
public class OrderCommentEntity {
    private String orderId;
    /**
     * 订单发布人id
     */
    private String userId;
    /**
     * 用户评分
     */
    private Double userRating;
    /**
     * 用户评价
     */
    private String comment;
    /**
     * 用户评价时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentTime;
    /**
     * 配送员id
     */
    private String deliveryManId;
    /**
     * 配送员评分
     */
    private Double deliveryRating;
    /**
     * 配送员评价
     */
    private String deliveryComment;
    /**
     * 配送员评价时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;
}
