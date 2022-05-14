package com.xiaofei.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaofei.common.OrderStatus;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.entity.base.TrackCompanyEntity;
import com.xiaofei.entity.order.OrderCommentEntity;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.mapper.order.OrderCommentMapper;
import com.xiaofei.mapper.order.OrderInfoMapper;
import com.xiaofei.mapper.order.PaymentInfoMapper;
import com.xiaofei.mapper.user.UserInfoMapper;
import com.xiaofei.service.base.TrackCompanyService;
import com.xiaofei.service.order.OrderInfoService;
import com.xiaofei.service.order.PaymentInfoService;
import com.xiaofei.utils.OrderUtils;
import com.xiaofei.vo.OrderInfoVo;
import com.xiaofei.vo.PaymentVo;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.ls.LSInput;

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
    private OrderCommentMapper orderCommentMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private TrackCompanyService trackCompanyService;
    @Resource
    private PaymentInfoMapper paymentInfoMapper;

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
     * 根据配送用户，查询订单信息
     *
     * @param deliveryManId
     */
    @Override
    public List<OrderInfoEntity> selectOrderByDeliveryManId(String deliveryManId) {
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deliveryManId", deliveryManId);
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
    public List<String> collectOrderIds(List<OrderInfoEntity> orders) {
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = startEndTime.get(0);
            Date end = startEndTime.get(1);
            if (start != null) {
                String formatStart = format.format(start);
                String formatEnd = format.format(end);
                if (StringUtils.compare(formatStart, formatEnd) == 0) {
                    formatEnd = formatEnd + " 23:59:59";
                } else {
                    queryWrapper
                            .ge("createTime", format.format(start))
                            .le("createTime", format.format(end));
                }
            }
        }
        List<OrderInfoEntity> entityList = orderInfoMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageInfo<OrderInfoEntity>();
        }
        return new PageInfo<OrderInfoEntity>(entityList);
    }

    /**
     * 管理员查询被删除和撤销的订单
     *
     * @param search
     * @param flag
     */
    @Override
    public PageInfo<OrderInfoEntity> selectAllOrder(SearchCondition search, Boolean flag) {
        //支付状态
        QueryWrapper<PaymentInfoEntity> wrapper = new QueryWrapper<>();
        List<PaymentInfoEntity> paymentInfoEntities = new ArrayList<>();
        if (search.getPaymentStatus() != null && search.getPaymentStatus() != -1) {
            wrapper.eq("paymentStatus", search.getPaymentStatus());
            paymentInfoEntities = paymentInfoMapper.selectList(wrapper);
            //当存在支付状态信息，但是查询无结果时，直接返回
            if (CollectionUtils.isEmpty(paymentInfoEntities)) {
                return new PageInfo<OrderInfoEntity>();
            }
        }
        //收集订单id
        List<String> orderIds = new ArrayList<>();
        for (PaymentInfoEntity entity : paymentInfoEntities) {
            if (StringUtils.isNotEmpty(entity.getOrderid())) {
                orderIds.add(entity.getOrderid());
            }
        }
        QueryWrapper<OrderInfoEntity> queryWrapper = new QueryWrapper<>();
        Integer curPage = search.getCurrentPage() == 0 ? 1 : search.getCurrentPage();
        PageHelper.startPage(curPage, search.getPageSize());
        if (!CollectionUtils.isEmpty(paymentInfoEntities)) {
            queryWrapper.in("id", orderIds);
        }
        if (flag) {
            queryWrapper.eq("isDel", 0);
        } else {
            queryWrapper.in("isDel", 1, -1);
        }
        //订单状态
        if (search.getOrderStatus() != null && search.getOrderStatus() != 0) {
            queryWrapper.eq("orderStatus", search.getOrderStatus());
        }
        //起始日期
        if (!CollectionUtils.isEmpty(search.getStartEndTime())) {
            List<Date> startEndTime = search.getStartEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = startEndTime.get(0);
            Date end = startEndTime.get(1);
            if (start != null) {
                String formatStart = format.format(start);
                String formatEnd = format.format(end);
                if (StringUtils.compare(formatStart, formatEnd) == 0) {
                    formatEnd = formatEnd + " 23:59:59";
                } else {
                    queryWrapper
                            .ge("createTime", format.format(start))
                            .le("createTime", format.format(end));
                }
            }
        }
        List<OrderInfoEntity> entityList = orderInfoMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageInfo<OrderInfoEntity>();
        }
        return new PageInfo<OrderInfoEntity>(entityList);
    }

    @Override
    public PageInfo<OrderInfoEntity> selectOrderBelongDelivery(SearchCondition search, Boolean flag) {
        Integer curPage = search.getCurrentPage() == 0 ? 1 : search.getCurrentPage();
        PageHelper.startPage(curPage, search.getPageSize());
        QueryWrapper<OrderInfoEntity> queryWrapper = new QueryWrapper<>();
        if (flag) {
            queryWrapper.eq("isDel", 0);
        } else {
            queryWrapper.in("isDel", 1, -1);
        }
        //订单号
        if (StringUtils.isNotEmpty(search.getId())) {
            queryWrapper.like("id", search.getId());
        }
        //起始日期
        if (!CollectionUtils.isEmpty(search.getStartEndTime())) {
            List<Date> startEndTime = search.getStartEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = startEndTime.get(0);
            Date end = startEndTime.get(1);
            if (start != null) {
                String formatStart = format.format(start);
                String formatEnd = format.format(end);
                if (StringUtils.compare(formatStart, formatEnd) == 0) {
                    formatEnd = formatEnd + " 23:59:59";
                } else {
                    queryWrapper
                            .ge("createTime", format.format(start))
                            .le("createTime", format.format(end));
                }
            }
        }
        List<OrderInfoEntity> entityList = orderInfoMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageInfo<OrderInfoEntity>();
        }
        return new PageInfo<OrderInfoEntity>(entityList);
    }

    /**
     * 查询订单的deliveryManId等于用户id，没有被删除或撤销的订单
     *
     * @param deliveryManId
     */
    @Override
    public PageInfo<OrderInfoEntity> selectOrderBydeliveryManId(SearchCondition search, String deliveryManId) {
        Integer curPage = search.getCurrentPage() == 0 ? 1 : search.getCurrentPage();
        PageHelper.startPage(curPage, search.getPageSize());
        QueryWrapper<OrderInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deliveryManId", deliveryManId);
        queryWrapper.eq("isDel", 0);
        //订单状态
        if (search.getOrderStatus() != 0) {
            queryWrapper.eq("orderStatus", search.getOrderStatus());
        }
        //订单号
        if (StringUtils.isNotEmpty(search.getId())) {
            queryWrapper.like("id", search.getId());
        }
        //起始日期
        if (!CollectionUtils.isEmpty(search.getStartEndTime())) {
            List<Date> startEndTime = search.getStartEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = startEndTime.get(0);
            Date end = startEndTime.get(1);
            if (start != null) {
                String formatStart = format.format(start);
                String formatEnd = format.format(end);
                if (StringUtils.compare(formatStart, formatEnd) == 0) {
                    formatEnd = formatEnd + " 23:59:59";
                } else {
                    queryWrapper
                            .ge("createTime", format.format(start))
                            .le("createTime", format.format(end));
                }
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

        //计算是否评价，判断id是否存在
        List<String> orderIds = orders.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
        QueryWrapper<OrderCommentEntity> wrapper = new QueryWrapper<>();
        wrapper.in("orderId", orderIds);
        List<OrderCommentEntity> entities = orderCommentMapper.selectList(wrapper);
        Map<String, Integer> evaluateStatusMap = new HashMap<>();
        for (OrderCommentEntity entity : entities) {
            String orderId = entity.getOrderId();
            if (StringUtils.isNotEmpty(orderId)) {
                Integer evaluateStatus = OrderUtils.calEvaluateStatus(entity);
                evaluateStatusMap.put(orderId, evaluateStatus);
            }
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
            if (order.getOrderStatus() != null) {
                Integer orderStep = OrderUtils.calOrderStep(order.getOrderStatus());
                vo.setOrderStep(orderStep);
            }
            //设置评价状态
            vo.setCommentStatus(evaluateStatusMap.get(order.getId()));
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

    /**
     * 查询订单信息，通过订单id
     *
     * @param orderIds
     */
    @Override
    public List<OrderInfoEntity> selectOrderByOrderId(List<String> orderIds) {
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.in("id", orderIds);
        List<OrderInfoEntity> entities = orderInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities;
    }

    /**
     * 批量更新
     *
     * @param entities
     */
    @Override
    public Boolean batchAcceptOrder(List<OrderInfoEntity> entities) {
        Integer batchUpdate = orderInfoMapper.batchAcceptOrder(entities);
        if (batchUpdate > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据订单状态，查询所有订单
     *
     * @param orderStatus
     */
    @Override
    public List<OrderInfoEntity> selectAllOrderByOrderStatus(Integer... orderStatus) {
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        List<Integer> arrayList = new ArrayList<>();
        for (Integer status : orderStatus) {
            arrayList.add(status);
        }
        wrapper.in("orderStatus", arrayList);
        List<OrderInfoEntity> entities = orderInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities;
    }

    /**
     * 查询订单信息，通过订单id
     *
     * @param id
     */
    @Override
    public OrderInfoEntity selectOne(String id) {
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        OrderInfoEntity entity = orderInfoMapper.selectOne(wrapper);
        return entity;
    }

    /**
     * 更新单个对象
     *
     * @param entity
     */
    @Override
    public Boolean update(OrderInfoEntity entity) {
        int update = orderInfoMapper.updateById(entity);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean successOrder(List<String> orderIds) {
        Integer integer = orderInfoMapper.successOrder(orderIds);
        if (integer > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean execptionOrder(List<String> orderIds) {
        Integer integer = orderInfoMapper.execptionOrder(orderIds);
        if (integer > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询订单id根据用户id
     *
     * @param userIds
     */
    @Override
    public List<String> selectOrderIdByUserId(List<String> userIds) {
        List<OrderInfoEntity> list = orderInfoMapper.selectOrderIdByUserId(userIds);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<String> collect = list.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<OrderInfoEntity> selectOrderBydeliveryManId(String deliveryManId) {
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deliveryManId", deliveryManId);
        List<OrderInfoEntity> list = orderInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 查询所有订单
     */
    @Override
    public List<OrderInfoEntity> selectAll() {
        List<OrderInfoEntity> entities = orderInfoMapper.selectList(null);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities;
    }
}
