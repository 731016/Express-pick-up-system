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
    USERROLECHANGESUCCESS(2001, "用户身份切换成功,即将重新登录"),
    /**
     * 请求失败
     */
    REQUESTFAIL(4000, "操作失败"),
    LOGINERROR(4001, "用户名或密码不正确"),
    TOKENINVALID(4003, "token失效,请重新登录"),
    USERNAMEEXIST(4004, "用户名已存在！"),
    MENUPULLERROR(6001, "用户菜单拉取失败"),
    PWDEXIST(6002, "要修改的密码与原密码相同！"),
    IDNUMBEREXIST(6003,"身份证信息已存在"),
    PWDERROR(6004,"密码错误"),
    GETDELIVERYERR(6005,"获取配送人员信息失败"),
    NOEXISTUSER(6006,"无法查询到用户信息"),
    USERDISABLE(7001,"用户被禁用，无法登录!"),
    USERFREEZE(7002,"用户被冻结");
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
