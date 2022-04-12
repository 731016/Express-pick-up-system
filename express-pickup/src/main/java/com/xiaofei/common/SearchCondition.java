package com.xiaofei.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 查询条件
 *
 * @date 2022/4/10 16:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCondition {

    private Integer orderStatus;
    private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Date> startEndTime;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 页码大小
     */
    private Integer pageSize;
}
