package com.xiaofei.expresspickup.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 响应工具类
 *
 * @date 2022/2/25 22:01
 */
@Data
@AllArgsConstructor
public class ResultUtils {
    /**
     * 请求成功
     */
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<T>(ActionStatus.REQUESTSUCCESS.getCode(), ActionStatus.REQUESTSUCCESS.getMsg(), data);
    }

    /**
     * 请求成功
     */
    public static <T> CommonResponse<T> success(String msg, T data) {
        return new CommonResponse<T>(ActionStatus.REQUESTSUCCESS.getCode(), msg, data);
    }

    /**
     * 请求失败
     */
    public static <T> CommonResponse<T> error(String data) {
        return new CommonResponse<>(ActionStatus.REQUESTFAIL.getCode(), ActionStatus.REQUESTFAIL.getMsg(), null);
    }
}
