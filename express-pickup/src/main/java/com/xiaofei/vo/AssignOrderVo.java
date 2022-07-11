package com.xiaofei.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/16 22:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignOrderVo implements Serializable {

    private static final long serialVersionUID = 5745721901721925241L;
    /**
     * 分配配送员id
     */
    private String deliveryId;
    /**
     * 分配订单id
     */
    private List<String> orderIds;
}
