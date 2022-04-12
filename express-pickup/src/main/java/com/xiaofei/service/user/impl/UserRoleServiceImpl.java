package com.xiaofei.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.user.UserRoleEntity;
import com.xiaofei.mapper.user.UserRoleMapper;
import com.xiaofei.service.user.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO 类描述
 *
 * @date 2022/4/4 12:29
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 根据用户角色id，查询用户角色名称
     *
     * @param userRoleId
     */
    @Override
    public String selectRoleNameByUserRoleId(String userRoleId) {
        QueryWrapper<UserRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userRoleId);
        UserRoleEntity role = userRoleMapper.selectOne(wrapper);
        return role.getRoleName();
    }
}

