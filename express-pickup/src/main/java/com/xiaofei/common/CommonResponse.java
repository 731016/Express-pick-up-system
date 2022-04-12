package com.xiaofei.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用返回对象
 *
 * @date 2022/2/25 21:35
 */
@Data
@NoArgsConstructor
@ApiModel("通用返回对象")
public class CommonResponse<T> {
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
    @ApiModelProperty(value = "返回对象")
    private T data;
    @ApiModelProperty(value = "返回数组")
    private List<T> dataList;
    @ApiModelProperty(value = "当前页码")
    private Integer currentPage;
    @ApiModelProperty(value = "总页数")
    private Integer totalPage;

    public CommonResponse(Integer statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public CommonResponse(Integer statusCode, String message, List<T> dataList) {
        this.statusCode = statusCode;
        this.message = message;
        this.dataList = dataList;
    }

    public CommonResponse(Integer statusCode, String message, List<T> dataList, Integer currentSize, Integer totalPage) {
        this.statusCode = statusCode;
        this.message = message;
        this.dataList = dataList;
        this.currentPage = currentSize;
        this.totalPage = totalPage;
    }
}
