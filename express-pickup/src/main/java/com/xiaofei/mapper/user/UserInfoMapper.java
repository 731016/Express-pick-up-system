package com.xiaofei.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofei.entity.user.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户，数据交互
 *
 * @date 2022/3/31 22:50
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {
    /**
     * 查询所有用户的身份证id
     */
    List<String> selectAllIdNumber();
    /**
     * 批量禁用用户
     */
    Integer disableUser(List<UserInfoEntity> userIds);
}
