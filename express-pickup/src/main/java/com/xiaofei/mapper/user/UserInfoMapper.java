package com.xiaofei.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofei.entity.user.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户，数据交互
 *
 * @date 2022/3/31 22:50
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {
}
