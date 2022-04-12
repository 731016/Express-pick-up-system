package com.xiaofei.common;

import io.swagger.models.auth.In;
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
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<T>(ActionStatus.REQUESTSUCCESS.getCode(), ActionStatus.REQUESTSUCCESS.getMsg(), data);
    }

    public static <T> CommonResponse<T> success(String msg, T data) {
        return new CommonResponse<T>(ActionStatus.REQUESTSUCCESS.getCode(), msg, data);
    }

    public static <T> CommonResponse<T> success(Integer code, String msg, T data) {
        return new CommonResponse<T>(code, msg, data);
    }

    public static <T> CommonResponse<T> error(T data) {
        return new CommonResponse<>(ActionStatus.REQUESTFAIL.getCode(), ActionStatus.REQUESTFAIL.getMsg(), data);
    }

    public static <T> CommonResponse<T> error(String msg, T data) {
        return new CommonResponse<>(ActionStatus.REQUESTFAIL.getCode(), msg, data);
    }

    public static <T> CommonResponse<T> error(Integer code, String msg, T data) {
        return new CommonResponse<>(code, msg, data);
    }

    public static <T> CommonResponse<List<T>> success(List<T> dataList) {
        return new CommonResponse<List<T>>(ActionStatus.REQUESTSUCCESS.getCode(), ActionStatus.REQUESTSUCCESS.getMsg(), dataList);
    }

    public static <T> CommonResponse<List<T>> success(Integer currentSize, Integer pageSize, Integer totalPage, List<T> dataList) {
        return new CommonResponse(ActionStatus.REQUESTSUCCESS.getCode(), ActionStatus.REQUESTSUCCESS.getMsg(), dataList, currentSize, pageSize, totalPage);
    }
}
