package com.xiaofei.common;

import io.swagger.models.auth.In;
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
    /**
     * 是否启用 1启用 -1禁用
     */
    private Integer enableStatus;
    /**
     * 是否冻结 1冻结 -1未冻结
     */
    private Integer accountStatus;
    /**
     * 实名认证 1认证 -1未认证
     */
    private Integer realNameStatus;
    /**
     * C B A
     */
    private String userAuth;
    private Integer orderStatus;
    private Integer paymentStatus;
    private String id;
    private String userName;
    private String phone;
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
