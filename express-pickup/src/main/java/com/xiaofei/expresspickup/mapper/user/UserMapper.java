package com.xiaofei.expresspickup.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofei.expresspickup.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户，数据交互
 *
 * @date 2022/3/31 22:50
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
