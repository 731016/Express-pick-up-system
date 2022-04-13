package com.xiaofei.service.user;

import com.xiaofei.entity.user.UserInfoEntity;

/**
 * 用户信息，service
 *
 * @date 2022/3/31 22:54
 */
public interface UserInfoService {
    /**
     * 注册用户
     */
    Boolean insertUserInfo(UserInfoEntity userInfoEntity);

    /**
     * 检查用户名和密码是否正确
     */
    Boolean checkUserNameAndPwd(String userName, String passWord);
    /**
     * 根据用户名，查询用户信息
     */
    UserInfoEntity selectOneUserInfo(String userName);
    /**
     * 更新用户信息
     */
    Boolean update(UserInfoEntity userInfoEntity);
}
