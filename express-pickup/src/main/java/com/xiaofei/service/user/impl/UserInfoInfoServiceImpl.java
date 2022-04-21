package com.xiaofei.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.entity.order.OrderCommentEntity;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.entity.user.UserRoleEntity;
import com.xiaofei.mapper.order.OrderCommentMapper;
import com.xiaofei.mapper.order.OrderInfoMapper;
import com.xiaofei.mapper.user.UserInfoMapper;
import com.xiaofei.mapper.user.UserRoleMapper;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.service.user.UserRoleService;
import com.xiaofei.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @date 2022/3/31 22:57
 */
@Service
public class UserInfoInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderCommentMapper orderCommentMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

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
                .eq("passWord", passWord);
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
        wrapper.eq("userId", userId);
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
        wrapper.in("userId", userIds);
        List<UserInfoEntity> entitys = userInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entitys)) {
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
        if (integer > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询配送用户
     */
    @Override
    public List<UserInfoEntity> selectDelivery() {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userRoleId", 'B');
        List<UserInfoEntity> entities = userInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities;
    }

    /**
     * 查询所有用户
     */
    @Override
    public PageInfo<UserInfoEntity> getAllUser(SearchCondition search) {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        Integer enableStatus = search.getEnableStatus();
        //是否启用
        if (enableStatus == 1) {
            wrapper.eq("disable", 1);
        } else if (enableStatus == -1) {
            wrapper.eq("disable", 0);
        }
        //是否冻结
        Integer accountStatus = search.getAccountStatus();
        if (accountStatus == 1) {
            wrapper.isNotNull("freezeTime");
        } else if (accountStatus == -1) {
            wrapper.isNull("freezeTime");
        }
        //实名
        Integer realNameStatus = search.getRealNameStatus();
        if (realNameStatus == 1) {
            wrapper.isNotNull("actualName").isNotNull("idNumber");
        } else if (realNameStatus == -1) {
            wrapper.isNull("actualName").isNull("idNumber");
        }
        //用户角色
        String userAuth = search.getUserAuth();
        if (!"0".equals(userAuth)) {
            wrapper.eq("userRoleId", userAuth);
        }
        String id = search.getId();
        if (StringUtils.isNotEmpty(id)) {
            wrapper.like("userId", id);
        }
        String userName = search.getUserName();
        if (StringUtils.isNotEmpty(userName)) {
            wrapper.like("userName", userName);
        }
        String phone = search.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            wrapper.like("phone", phone);
        }
        wrapper.ne("userRoleId", 'A');
        Integer curPage = search.getCurrentPage() == null || search.getCurrentPage() == 0 ? 1 : search.getCurrentPage();
        PageHelper.startPage(curPage, search.getPageSize());
        List<UserInfoEntity> entities = userInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new PageInfo<>();
        }
        //普通用户，根据userid查找订单id（自己发起的），计算配送员评分
        //配送用户，根据userid查找订单配送员id，计算普通用户评分
        List<String> userIds = new ArrayList<>();
        Map<String, String> rateMap = new HashMap<>();
        for (UserInfoEntity entity : entities) {
            String roleId = entity.getUserRoleId();
            rateMap.put(entity.getUserId(), roleId);
            userIds.add(entity.getUserId());
        }
        //查询订单id
        List<OrderInfoEntity> infoEntities = orderInfoMapper.selectOrderIdByUserId(userIds);
        if (CollectionUtils.isEmpty(infoEntities)) {
            return new PageInfo<>();
        }
        //收集订单id
        List<String> orderList = infoEntities.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
        QueryWrapper<OrderCommentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("orderId", orderList);
        List<OrderCommentEntity> entityList = orderCommentMapper.selectList(queryWrapper);

        Map<String, List<Double>> evaluateMap = new HashMap<>();
        //获取用户评分
        for (OrderCommentEntity entity : entityList) {
            String userId = entity.getUserId();
            if (evaluateMap.containsKey(userId)) {
                List<Double> doubles = evaluateMap.get(userId);
                doubles.add(entity.getUserRating());
            } else {
                List<Double> evalute = new ArrayList<>();
                evalute.add(entity.getUserRating());
                evaluateMap.put(userId, evalute);
            }
            String deliveryManId = entity.getDeliveryManId();
            if (evaluateMap.containsKey(deliveryManId)) {
                List<Double> doubles = evaluateMap.get(deliveryManId);
                doubles.add(entity.getDeliveryRating());
            } else {
                List<Double> evalute = new ArrayList<>();
                evalute.add(entity.getDeliveryRating());
                evaluateMap.put(userId, evalute);
            }
        }
        //用户权限
        List<UserRoleEntity> roleEntities = userRoleMapper.selectList(null);
        Map<String, String> userRoleMap = new HashMap<>();
        for (UserRoleEntity entity : roleEntities) {
            userRoleMap.put(entity.getId(), entity.getRoleName());
        }
        for (UserInfoEntity entity : entities) {
            entity.setUserRoleName("B".equals(entity.getUserRoleId()) ? "配送员" : "普通用户");
            entity.setDisableName(entity.getDisable() == 1 ? "启用" : "禁用");
            String actualName = entity.getActualName();
            String idNumber = entity.getIdNumber();
            String userRoleId = entity.getUserRoleId();
            entity.setUserRoleName(userRoleMap.get(userRoleId));
            if (StringUtils.isNotEmpty(actualName) && StringUtils.isNotEmpty(idNumber)) {
                entity.setRealNameStatus("已认证");
            } else {
                entity.setRealNameStatus("未认证");
            }
            if (!CollectionUtils.isEmpty(evaluateMap)) {
                List<Double> doubles = evaluateMap.get(entity.getUserId());
                if (!CollectionUtils.isEmpty(doubles)) {
                    entity.setRate(doubles.stream().reduce(Double::sum).orElse(0.0));
                }
            } else {
                entity.setRate(0.0);
            }
        }
        return new PageInfo<>(entities);
    }

    @Override
    public List<UserInfoEntity> selectAll() {
        List<UserInfoEntity> entities = userInfoMapper.selectList(null);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities;
    }
}
