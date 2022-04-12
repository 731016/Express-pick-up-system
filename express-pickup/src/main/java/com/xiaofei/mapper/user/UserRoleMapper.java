package com.xiaofei.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofei.entity.user.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色
 *
 * @date 2022/4/4 12:26
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {
}
