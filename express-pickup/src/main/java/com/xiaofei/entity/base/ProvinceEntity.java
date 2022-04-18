package com.xiaofei.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 省份
 *
 * @date 2022/4/9 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("province")
public class ProvinceEntity {
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private Integer id;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 是否启用
     */
    private Integer disable;
}
