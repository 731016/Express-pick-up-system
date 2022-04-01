package com.xiaofei.expresspickup.entity.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回对象
 *
 * @date 2022/2/25 21:35
 */
@Data
@NoArgsConstructor
@ApiModel("通用返回对象")
public class CommonResponse<T>{
    /*
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer statusCode;
    /*
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String message;
    @ApiModelProperty(value = "返回数据")
    private T data;

    public CommonResponse(Integer statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
