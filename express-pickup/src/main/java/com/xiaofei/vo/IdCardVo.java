package com.xiaofei.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @date 2022/5/9 20:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdCardVo {

    private int code;
    private String message;

    private IdCardResultVo result;
}
