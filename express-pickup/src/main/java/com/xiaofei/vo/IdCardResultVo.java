package com.xiaofei.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @date 2022/5/13 23:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdCardResultVo {
    private String name;
    private String idcard;
    /**
     * 核验结果状态码，1 一致；2 不一致；3 无记录
     */
    private String res;
    private String description;
    private String sex;
    private String birthday;
    private String address;
}
