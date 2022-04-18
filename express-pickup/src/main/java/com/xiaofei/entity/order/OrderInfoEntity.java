package com.xiaofei.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单信息
 *
 * @date 2022/4/9 14:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_info")
public class OrderInfoEntity {
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    private String userId;
    /**
     * 快递公司id
     */
    private String trackCompanyId;
    @TableField(exist = false)
    private String trackCompanyName;
    /**
     * 快递单号
     */
    private String trackNumber;
    /**
     * 收货人
     */
    private String pickupName;
    /**
     * 收货手机号
     */
    private String contactNumber;
    /**
     * 本平台的配送地址
     */
    private String shipAddress;
    /**
     * 驿站地址（短信中的地址）
     */
    private String trackDeliveryAddress;
    /**
     * 备注
     */
    private String remark;
    /**
     * 配送员id
     */
    private String deliveryManId;
    @TableField(exist = false)
    private String deliveryMainName;
    /**
     * 配送备注,用于记录配送员，修改订单状态时设置
     */
    private String shippingReamrk;
    /**
     * 订单状态
     * 【10：等待接单】
     * 【20：派送中】
     * 【30：订单异常】
     * 【40：订单完成】
     */
    private Integer orderStatus;
    @TableField(exist = false)
    private String orderStatusName;
    /**
     * 订单是否删除
     * 【0：未删除 1：撤销 -1：删除】
     */
    private Integer isDel;
    /**
     * 删除原因
     */
    private String delReason;
    /**
     * 订单创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
