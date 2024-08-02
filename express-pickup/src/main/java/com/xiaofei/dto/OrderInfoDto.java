package com.xiaofei.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaofei.vo.PaymentVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author tuaofei
 * @description TODO
 * @date 2024/8/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDto {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private PaymentVo paymentInfo;
}
