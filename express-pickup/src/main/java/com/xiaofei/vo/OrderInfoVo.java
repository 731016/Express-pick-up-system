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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private PaymentVo paymentInfo;
}
