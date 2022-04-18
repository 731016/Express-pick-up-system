package com.xiaofei.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.mapper.user.UserInfoMapper;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.utils.CodecUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * 查询所有用户的身份证id
     */
    @Override
    public Boolean selectAllIdNumber(String actualName, String idNumber) {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("idNumber").isNotNull("actualName");
        List<UserInfoEntity> list = userInfoMapper.selectList(wrapper);
        Boolean flag = false;
        for (UserInfoEntity user : list) {
            if (user.getIdNumber().equals(idNumber)) {
                flag = true;
            }
            if (user.getActualName().equals(actualName)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 通过用户id，查询
     *
     * @param userId
     */
    @Override
    public UserInfoEntity selectUserById(String userId) {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userId",userId);
        UserInfoEntity entity = userInfoMapper.selectOne(wrapper);
        return entity;
    }

    /**
     * 查询多个用户信息，通过id
     *
     * @param userIds
     */
    @Override
    public List<UserInfoEntity> selectUserByIds(List<String> userIds) {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.in("userId",userIds);
        List<UserInfoEntity> entitys = userInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entitys)){
            return new ArrayList<>();
        }
        return entitys;
    }

    /**
     * 批量禁用用户
     *
     * @param entities
     */
    @Override
    public Boolean disableUser(List<UserInfoEntity> entities) {
//        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
//        wrapper.in("userId",userIds);
        Integer integer = userInfoMapper.disableUser(entities);
        if (integer>0){
            return true;
        }
        return false;
    }

    /**
     * 查询所有用户
     */
    @Override
    public List<UserInfoEntity> selectDelivery() {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userRoleId",101);
        List<UserInfoEntity> entities = userInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entities)){
            return new ArrayList<>();
        }
        return entities;
    }
}
