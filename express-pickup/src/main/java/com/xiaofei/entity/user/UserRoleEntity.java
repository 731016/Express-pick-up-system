package com.xiaofei.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色
 *
 * @date 2022/4/4 12:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user_role")
public class UserRoleEntity {
    /**
     * 角色id
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 角色名称
     */
    private String roleName;
}
