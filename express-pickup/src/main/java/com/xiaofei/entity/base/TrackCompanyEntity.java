package com.xiaofei.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 快递公司
 *
 * @date 2022/4/9 14:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("track_company")
public class TrackCompanyEntity {
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 是否启用
     */
    private Integer disable;
}
