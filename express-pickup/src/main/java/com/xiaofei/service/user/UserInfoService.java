package com.xiaofei.service.user;

import com.xiaofei.entity.user.UserInfoEntity;
import org.w3c.dom.ls.LSInput;

import java.util.List;

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
     * 通过用户id，查询
     */
    UserInfoEntity selectUserById(String userId);
    /**
     * 查询多个用户信息，通过id
     */
    List<UserInfoEntity> selectUserByIds(List<String> userIds);

    /**
     * 更新用户信息
     */
    Boolean update(UserInfoEntity userInfoEntity);

    /**
     * 查询所有用户的身份证id,是否和传入的相同
     */
    Boolean selectAllIdNumber(String actualName,String idNumber);
    /**
     * 批量禁用用户
     */
    Boolean disableUser(List<UserInfoEntity> entities);
    /**
     * 查询配送用户
     */
    List<UserInfoEntity> selectDelivery();
}
