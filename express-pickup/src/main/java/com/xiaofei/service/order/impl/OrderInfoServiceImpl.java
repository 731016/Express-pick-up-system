package com.xiaofei.service.order.impl;

import com.alibaba.fastjson.util.AntiCollisionHashMap;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaofei.common.OrderStatus;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.entity.base.TrackCompanyEntity;
import com.xiaofei.entity.dashBoard.DashBoardEntity;
import com.xiaofei.entity.order.OrderCommentEntity;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.mapper.order.OrderInfoMapper;
import com.xiaofei.mapper.order.PaymentInfoMapper;
import com.xiaofei.mapper.user.UserInfoMapper;
import com.xiaofei.service.base.TrackCompanyService;
import com.xiaofei.service.order.OrderInfoService;
import com.xiaofei.vo.OrderInfoVo;
import com.xiaofei.vo.PaymentVo;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 15:05
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private TrackCompanyService trackCompanyService;

    @Override
    public List<OrderInfoEntity> selectOrderByUserId(String userName) {
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userName);
        List<OrderInfoEntity> list = orderInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * 从订单信息中汇总仪表板信息
     *
     * @param orders
     */
    @Override
    public List<String> collectOrderIdsByGeneral(List<OrderInfoEntity> orders) {
        List<String> orderIds = new ArrayList<>();
        for (OrderInfoEntity order : orders) {
            orderIds.add(order.getId());
        }
        if (CollectionUtils.isEmpty(orderIds)) {
            return new ArrayList<>();
        }
        return orderIds;
    }

    /**
     * 获取等待接单或正在派送的订单数
     *
     * @param orders
     */
    @Override
    public Map<String, Integer> filterWaitAndDispatchOrderNumber(List<OrderInfoEntity> orders) {
        Map<String, Integer> map = new HashMap<>();
        map.put("wait", 0);
        map.put("dispatch", 0);
        for (OrderInfoEntity entity : orders) {
            Integer status = entity.getOrderStatus();
            if (status == 10) {
                map.put("wait", map.get("wait") + 1);
            } else if (status == 20) {
                map.put("dispatch", map.get("dispatch") + 1);
            }
        }
        return map;
    }

    /**
     * 新增订单
     *
     * @param orderInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertOrder(OrderInfoEntity orderInfo) {
        int insertFlag = orderInfoMapper.insert(orderInfo);
        if (insertFlag > 0) {
            return orderInfo.getId();
        }
        return "";
    }

    /**
     * 获取【没有被删除或撤销】或者被删除或撤销的订单
     */
    @Override
    public PageInfo<OrderInfoEntity> selectOrderBelongGeneral(SearchCondition search, String userName, Boolean flag) {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", userName);
        UserInfoEntity entity = userInfoMapper.selectOne(wrapper);
        Integer curPage = search.getCurrentPage() == 0 ? 1 : search.getCurrentPage();
        PageHelper.startPage(curPage, search.getPageSize());
        QueryWrapper<OrderInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", entity.getUserId());
        if (flag) {
            queryWrapper.eq("isDel", 0);
        } else {
            queryWrapper.in("isDel", 1, -1);
        }
        //订单号
        if (StringUtils.isNotEmpty(search.getId())) {
            queryWrapper.like("id", search.getId());
        }
        //订单状态
        if (search.getOrderStatus() != null && search.getOrderStatus() != 0) {
            queryWrapper.eq("orderStatus", search.getOrderStatus());
        }
        //起始日期
        if (!CollectionUtils.isEmpty(search.getStartEndTime())) {
            List<Date> startEndTime = search.getStartEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = startEndTime.get(0);
            Date end = startEndTime.get(1);
            if (start != null) {
                queryWrapper
                        .ge("createTime", format.format(start))
                        .le("createTime", format.format(end));
            }
        }
        List<OrderInfoEntity> entityList = orderInfoMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageInfo<OrderInfoEntity>();
        }
        return new PageInfo<OrderInfoEntity>(entityList);
    }

    /**
     * 将实体类转换为前台需要展示对象
     * 将订单，支付 => 订单对象
     */
    @Override
    public List<OrderInfoVo> poToVo(List<OrderInfoEntity> orders, List<PaymentInfoEntity> payments) {
        List<OrderInfoVo> vos = new ArrayList<>();
        //查询对应的快递公司名称(jedis)
        Map<String, String> compNameMap = new HashMap<>();
        List<String> compIds = new ArrayList<>();
        for (OrderInfoEntity order : orders) {
            if (StringUtils.isNotEmpty(order.getTrackCompanyId())) {
                compIds.add(order.getTrackCompanyId());
            }
        }
        if (!CollectionUtils.isEmpty(compIds)) {
            List<TrackCompanyEntity> trackCompanyEntities = trackCompanyService.batchSelectAll(compIds);
            for (TrackCompanyEntity entity : trackCompanyEntities) {
                compNameMap.put(entity.getId(), entity.getCompanyName());
            }
        }
        //查询而配送员名称
        Map<String, String> deliveryNameMap = new HashMap<>();
        List<String> deliveryIds = orders.stream().map(OrderInfoEntity::getDeliveryManId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(deliveryIds)) {
            QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
            wrapper.in("userId", deliveryIds);
            List<UserInfoEntity> userList = userInfoMapper.selectList(wrapper);
            for (UserInfoEntity entity : userList) {
                deliveryNameMap.put(entity.getUserId(), entity.getUserName());
            }
        }
        //设置支付信息和对应的订单
        Map<String, PaymentInfoEntity> payMap = new HashMap<>();
        for (PaymentInfoEntity payment : payments) {
            payMap.put(payment.getOrderid(), payment);
        }
        for (OrderInfoEntity order : orders) {
            OrderInfoVo vo = new OrderInfoVo();
            BeanUtils.copyProperties(order, vo);
            vo.setTrackCompanyName(compNameMap.get(order.getTrackCompanyId()));
            if (StringUtils.isNotBlank(order.getDeliveryManId())) {
                vo.setDeliveryMainName(deliveryNameMap.get(order.getDeliveryManId()));
            }
            vo.setOrderStatusName(OrderStatus.getNameByStatus(order.getOrderStatus()));
            PaymentInfoEntity pay = payMap.get(order.getId());
            if (pay != null) {
                PaymentVo paymentVo = new PaymentVo();
                BeanUtils.copyProperties(pay, paymentVo);
                vo.setPaymentInfo(paymentVo);
                vo.getPaymentInfo().setPaymentStatusName(OrderStatus.getNameByStatus(pay.getPaymentStatus()));
            }
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 修改订单为撤销状态
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean revokeOrderStatus(List<String> ids) {
        Integer revokeNumber = orderInfoMapper.revokeOrderStatus(ids);
        if (revokeNumber > 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除订单
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteOrderStatus(List<String> ids) {
        Integer delNumber = orderInfoMapper.deleteOrderStatus(ids);
        if (delNumber > 0) {
            return true;
        }
        return false;
    }

    /**
     * 还原订单
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean recyceOrderStatus(List<String> ids) {
        Integer recyceNumber = orderInfoMapper.recyceOrderStatus(ids);
        if (recyceNumber > 0) {
            return true;
        }
        return false;
    }
}
