package com.xiaofei.service.user;

/**
 * TODO 类描述
 *
 * @date 2022/4/4 14:13
 */
public interface LoginPersistenceService {
    /**
     * 删除用户持久化信息
     */
    Boolean delToken(String userId);

    /**
     * 插入盐值
     */
    Boolean add(String userId, String userName, String salt);
    /**
     * 登录后，设置token
     */
    Boolean updateToken(String userId, String token);

    /**
     * 查询加密盐值
     */
    String querySalt(String userId);

    /**
     * 查询登录的token
     */
    String queryToken(String userId);
}
