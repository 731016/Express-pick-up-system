package com.xiaofei.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 获取订单信息传输对象
 *
 * @date 2022/4/10 1:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoVo {
    private String id;
    private String userId;
    private String pickupName;
    private String trackNumber;
    private String contactNumber;
    private String trackDeliveryAddress;
    private String shipAddress;
    private String trackCompanyId;
    private String trackCompanyName;
    private String remark;
    private String deliveryManId;
    private String deliveryMainName;
    private String shippingReamrk;
    private Integer orderStatus;
    private String orderStatusName;
    private Integer isDel;
    private String delReason;
    private Integer orderStep;
    /**
     * 判断此订单是否已评价的标志
     * 用户已评价1，配送员已评价2，未评价0
     * 三个数求和
     * 1用户已评价
     * 2配送员已评价
     * 3用户和配送员都已评价
     */
    private Integer commentStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private PaymentVo paymentInfo;
}
