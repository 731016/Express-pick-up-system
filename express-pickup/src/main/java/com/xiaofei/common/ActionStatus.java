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
    REQUESTSUCCESS(2000, "操作成功"),
    /**
     * 请求失败
     */
    REQUESTFAIL(4000, "操作失败"),
    LOGINERROR(4001, "用户名或密码不正确"),
    TOKENINVALID(4003, "token失效,请重新登录"),
    USERNAMEEXIST(4004,"用户名已存在！"),
    MENUPULLERROR(6001, "用户菜单拉取失败");
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
