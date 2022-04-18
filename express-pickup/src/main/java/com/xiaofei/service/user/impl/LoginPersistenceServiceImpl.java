package com.xiaofei.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.user.LoginPersistenceEntity;
import com.xiaofei.mapper.user.LoginPersistenceMapper;
import com.xiaofei.service.user.LoginPersistenceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO 类描述
 *
 * @date 2022/4/4 14:13
 */
@Service
public class LoginPersistenceServiceImpl implements LoginPersistenceService {
    @Resource
    private LoginPersistenceMapper loginPersistenceMapper;

    /**
     * 删除用户持久化信息
     *
     * @param userId
     */
    @Override
    public Boolean delToken(String userId) {
        int deleteFlag = loginPersistenceMapper.delToken(userId);
        if (deleteFlag > 0) {
            return true;
        }
        return false;
    }

    /**
     * 插入盐值
     *
     * @param userId
     * @param salt
     */
    @Override
    public Boolean add(String userId, String userName, String salt) {
        LoginPersistenceEntity persistence = new LoginPersistenceEntity();
        persistence.setUserId(userId);
        persistence.setUserName(userName);
        persistence.setSalt(salt);
        int insert = loginPersistenceMapper.insert(persistence);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询加密盐值
     *
     * @param userId
     */
    @Override
    public String querySalt(String userId) {
        QueryWrapper<LoginPersistenceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        LoginPersistenceEntity loginPersistenceEntity = loginPersistenceMapper.selectOne(wrapper);
        if (loginPersistenceEntity != null) {
            return loginPersistenceEntity.getSalt();
        }
        return null;
    }

    /**
     * 查询登录的token
     *
     * @param userId
     */
    @Override
    public String queryToken(String userId) {
        LoginPersistenceEntity loginPersistenceEntity = loginPersistenceMapper.selectById(userId);
        if (loginPersistenceEntity != null) {
            return loginPersistenceEntity.getToken();
        }
        return null;
    }

    /**
     * 登录后，设置token
     *
     * @param userId
     * @param token
     */
    @Override
    public Boolean updateToken(String userId, String token) {
        QueryWrapper<LoginPersistenceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        LoginPersistenceEntity loginPersistenceEntity = loginPersistenceMapper.selectOne(wrapper);
        loginPersistenceEntity.setToken(token);
        int updateFlag = loginPersistenceMapper.update(loginPersistenceEntity, wrapper);
        if (updateFlag > 0) {
            return true;
        }
        return false;
    }
}
