package com.xiaofei.expresspickup.service.user;

import com.xiaofei.expresspickup.entity.User;
import com.xiaofei.expresspickup.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户，service
 *
 * @date 2022/3/31 22:54
 */
public interface UserService {
    /*
     * description: 注册用户
     * @Param: [user] 
     * @Return: java.lang.Boolean 
     **/
    Boolean insertUser(User user);
}
