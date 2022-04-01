package com.xiaofei.expresspickup.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 操作状态
 *
 * @date 2022/3/31 23:14
 */
@Getter
public enum ActionStatus {
    /**
     * 请求成功
     */
    REQUESTSUCCESS(2000, "请求成功"),
    /**
     * 请求失败
     */
    REQUESTFAIL(4000, "请求失败");
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 说明
     */
    private String msg;

    ActionStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
