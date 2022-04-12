package com.xiaofei.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录信息，持久化
 *
 * @date 2022/4/4 13:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "login_persistence")
public class LoginPersistenceEntity {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 密码加密盐值
     */
    private String salt;
    /**
     * token
     */
    private String token;
}
