package com.xiaofei.common;

import lombok.Getter;

/**
 * 第三方省份认证接口，返回状态码
 *
 * @date 2022/5/6 22:18
 */
@Getter
public enum IdCardResponseStatus {

    SUCCESS(0, "成功"),
    PARAMETER_ERROR(400, "参数错误"),
    IDCARD_ILLEGAL(20010, "身份证号为空或非法"),
    NAME_ILLEGAL(20310, "姓名为空或非法"),
    NOTFAND(404, "请求资源不存在"),
    INTERNAL_ERROR(500, "系统内部错误，请联系服务商"),
    SERVICE_EXCEPTION(501, "第三方服务异常"),
    API_STOP(604, "接口停用"),
    UNKNOWN_MISTAKE(1001, "未知错误");

    /**
     * 状态码
     */
    private int code;
    /**
     * 说明
     */
    private String message;

    IdCardResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMeaasge(int code) {
        String message = null;
        for (IdCardResponseStatus value : IdCardResponseStatus.values()) {
            int valueCode = value.getCode();
            if (valueCode == code) {
                message = value.getMessage();
            }
        }
        return message;
    }
    public static Boolean isSuccess(int code){
        return 200 == code;
    }
    public static Boolean isSuccess(String code){
        return "0".equals(code);
    }
}
