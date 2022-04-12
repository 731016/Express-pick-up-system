package com.xiaofei.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaofei.entity.user.LoginPersistenceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO 类描述
 *
 * @date 2022/4/4 14:12
 */
@Mapper
public interface LoginPersistenceMapper extends BaseMapper<LoginPersistenceEntity> {
    /**
     * 删除token
     */
    Integer delToken(String userId);
}

