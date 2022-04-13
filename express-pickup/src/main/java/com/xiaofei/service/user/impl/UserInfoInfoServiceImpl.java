package com.xiaofei.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.mapper.user.UserInfoMapper;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.utils.CodecUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO 类描述
 *
 * @date 2022/3/31 22:57
 */
@Service
public class UserInfoInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Boolean insertUserInfo(UserInfoEntity userInfoEntity) {
        String passWord = userInfoEntity.getPassWord();
        //设置用户级别【C普通，B配送,A管理】
        userInfoEntity.setUserRoleId("C");
        //密码加密
        userInfoEntity.setPassWord(CodecUtils.encrypt(passWord));
        int insertFlag = userInfoMapper.insert(userInfoEntity);
        if (insertFlag != 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkUserNameAndPwd(String userName, String passWord) {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", userName)
                .eq("passWord", passWord)
                .eq("disable", 1);
        Integer count = userInfoMapper.selectCount(wrapper);
        if (count == 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据用户名，查询用户信息
     *
     * @param userName
     */
    @Override
    public UserInfoEntity selectOneUserInfo(String userName) {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", userName);
        UserInfoEntity userInfoEntity = userInfoMapper.selectOne(wrapper);
        return userInfoEntity;
    }

    /**
     * 更新用户信息
     *
     * @param userInfoEntity
     */
    @Override
    public Boolean update(UserInfoEntity userInfoEntity) {
        int update = userInfoMapper.updateById(userInfoEntity);
        if (update > 0) {
            return true;
        }
        return false;
    }
}
