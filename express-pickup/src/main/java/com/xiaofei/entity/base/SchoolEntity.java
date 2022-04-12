package com.xiaofei.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学校信息
 *
 * @date 2022/4/9 14:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("school")
public class SchoolEntity {
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    private String schoolName;
    private Integer provinceId;
    private String level;
    private String website;
    private String abbreviation;
    private Integer disable;
}
