package com.xiaofei.expresspickup.entity.Base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单反馈
 *
 * @author 折腾的小飞
 * @date 2022/3/26 15:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback{
    /**
     * 正在处理的反馈
     */
    private  Integer processing;
    /**
     * 未处理的反馈
     */
    private  Integer notProcessed;
}
