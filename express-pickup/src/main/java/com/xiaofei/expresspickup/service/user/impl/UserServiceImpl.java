package com.xiaofei.expresspickup.service.user.impl;

import com.xiaofei.expresspickup.entity.User;
import com.xiaofei.expresspickup.mapper.user.UserMapper;
import com.xiaofei.expresspickup.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * TODO 类描述
 *
 * @date 2022/3/31 22:57
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    /*
     * description: 插入用户数据
     * @Param: [user] 
     * @Return: java.lang.Boolean 
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertUser(User user) {
        int insertFlag = userMapper.insert(user);
        if (insertFlag != 0) {
            return true;
        }
        return false;
    }
}
