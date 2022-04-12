package com.xiaofei.common;

import lombok.Getter;

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
    REQUESTFAIL(4000, "请求失败"),
    LOGINERROR(4001, "用户名或密码不正确"),
    MENUPULLERROR(6001, "用户菜单拉取失败"),
    TOKENINVALID(4003, "token失效,请重新登录");
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
