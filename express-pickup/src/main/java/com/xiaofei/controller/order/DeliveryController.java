package com.xiaofei.controller.order;

import com.github.pagehelper.PageInfo;
import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.entity.order.OrderCommentEntity;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.service.order.OrderCommentService;
import com.xiaofei.service.order.OrderInfoService;
import com.xiaofei.service.order.PaymentInfoService;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.utils.JwtUtils;
import com.xiaofei.vo.OrderInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @date 2022/4/16 21:40
 */
@Api(tags = "配送员订单接口")
@RestController
@RequestMapping("/deliveryOrder")
public class DeliveryController {
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private OrderCommentService orderCommentService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private PaymentInfoService paymentInfoService;

    @ApiOperation("配送员接单")
    @PostMapping("/acceptOrder")
    public CommonResponse<String> acceptOrder(@RequestHeader(value = "Authorization") String token, @RequestBody List<String> ids) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        List<OrderInfoEntity> entityList = orderInfoService.selectOrderByOrderId(ids);
        for (OrderInfoEntity entity : entityList) {
            entity.setDeliveryManId(userInfo.getUserId());
        }
        Boolean batchUpdate = orderInfoService.batchAcceptOrder(entityList);
        if (batchUpdate) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("查询可以接单的订单")
    @PostMapping("/selectAllowAcceptOrder")
    public CommonResponse<List<OrderInfoVo>> selectAllowAcceptOrder(@RequestBody SearchCondition searchConditions) {
        /**
         * 订单状态10.支付状态1,isDel 0
         */
        PageInfo<OrderInfoEntity> ordersPage = orderInfoService.selectOrderBelongDelivery(searchConditions, true);
        List<OrderInfoEntity> list = ordersPage.getList();
        List<OrderInfoEntity> orders = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            orders = ordersPage.getList().stream().filter(item -> item.getOrderStatus() == 10).collect(Collectors.toList());
        }
        PageInfo<PaymentInfoEntity> paymentInfoEntities = new PageInfo<>();
        List<OrderInfoVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orders)) {
            List<String> ids = orders.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
            paymentInfoEntities = paymentInfoService.selectPaymentInfoByOrderIds(searchConditions, ids);
            vos = orderInfoService.poToVo(orders, paymentInfoEntities.getList());
            //接单大厅不能看到短信
            for (OrderInfoVo vo : vos) {
                vo.setRemark("");
            }
        }
        return ResultUtils.success(paymentInfoEntities.getPageNum(), (int) paymentInfoEntities.getTotal(), vos);
    }

    @ApiOperation("查询配送用户，没有被删除和撤销的订单")
    @PostMapping("/selectDeliveryOrder")
    public CommonResponse<List<OrderInfoVo>> selectDeliveryOrder(@RequestHeader(value = "Authorization") String token, @RequestBody SearchCondition searchConditions) {
        //查询订单的deliveryManId等于用户id
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        PageInfo<OrderInfoEntity> ordersPage = orderInfoService.selectOrderBydeliveryManId(searchConditions, userInfo.getUserId());
        List<OrderInfoEntity> orders = ordersPage.getList();
        List<OrderInfoVo> vos = new ArrayList<>();
        PageInfo<PaymentInfoEntity> paymentInfoEntityPageInfo = new PageInfo<>();
        if (!CollectionUtils.isEmpty(orders)) {
            List<String> ids = orders.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
            paymentInfoEntityPageInfo = paymentInfoService.selectPaymentInfoByOrderIds(searchConditions, ids);
            vos = orderInfoService.poToVo(orders, paymentInfoEntityPageInfo.getList());
        }
        return ResultUtils.success(paymentInfoEntityPageInfo.getPageNum(), (int) paymentInfoEntityPageInfo.getTotal(), vos);
    }

    @ApiOperation("完成订单")
    @PostMapping("/OrderSuccess")
    public CommonResponse<String> orderSuccess(@RequestBody OrderInfoEntity orderInfoEntity) {
        //订单id，订单状态，状态名称，配送备注
        String id = orderInfoEntity.getId();
        if (StringUtils.isNotEmpty(id)) {
            OrderInfoEntity entity = orderInfoService.selectOne(id);
            entity.setOrderStatus(orderInfoEntity.getOrderStatus());
            entity.setShippingReamrk(orderInfoEntity.getShippingReamrk());
            Boolean update = orderInfoService.update(entity);
            if (update) {
                return ResultUtils.success("");
            }
        }
        return ResultUtils.error("");
    }

    @ApiOperation("异常订单")
    @PostMapping("/OrderException")
    public CommonResponse<String> OrderException(@RequestBody OrderInfoEntity orderInfoEntity) {
        //订单id，订单状态，状态名称，配送备注
        String id = orderInfoEntity.getId();
        if (StringUtils.isNotEmpty(id)) {
            OrderInfoEntity entity = orderInfoService.selectOne(id);
            entity.setOrderStatus(orderInfoEntity.getOrderStatus());
            entity.setShippingReamrk(orderInfoEntity.getShippingReamrk());
            Boolean update = orderInfoService.update(entity);
            if (update) {
                return ResultUtils.success("");
            }
        }
        return ResultUtils.error("");
    }

    @ApiOperation("订单评价")
    @PostMapping("/OrderEvaluate")
    public CommonResponse<String> OrderEvaluate(@RequestHeader(value = "Authorization") String token, @RequestBody OrderCommentEntity commentEntity) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        //订单id，配送员id，配送评分，配送备注
        /**
         * 根据订单id，判断评价信息是否存在，不存在创建，已存在修改
         */
        String orderId = commentEntity.getOrderId();
        if (StringUtils.isNotEmpty(orderId)) {
            OrderCommentEntity orderCommentEntity = orderCommentService.selectAllByOrderId(orderId);
            if (orderCommentEntity != null) {
                //修改
                orderCommentEntity.setDeliveryManId(userInfo.getUserId());
                orderCommentEntity.setDeliveryRating(commentEntity.getDeliveryRating());
                orderCommentEntity.setDeliveryComment(commentEntity.getDeliveryComment());
                orderCommentEntity.setDeliveryTime(commentEntity.getDeliveryTime());
                Boolean update = orderCommentService.update(orderCommentEntity, orderId);
                if (update) {
                    return ResultUtils.success("");
                }
            } else {
                //创建
                OrderCommentEntity entity = new OrderCommentEntity();
                entity.setOrderId(orderId);
                entity.setDeliveryManId(userInfo.getUserId());
                entity.setDeliveryRating(commentEntity.getDeliveryRating());
                entity.setDeliveryComment(commentEntity.getDeliveryComment());
                entity.setDeliveryTime(commentEntity.getDeliveryTime());
                Boolean insert = orderCommentService.insert(entity);
                if (insert) {
                    return ResultUtils.success("");
                }
            }
        }
        return ResultUtils.error("");
    }

    @ApiOperation("查询所有评价信息，根据配送员id,查找订单id")
    @PostMapping("/selectDeliveryComment")
    public CommonResponse<List<OrderCommentEntity>> selectDeliveryComment(@RequestHeader(value = "Authorization") String token, @RequestBody SearchCondition search) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        List<OrderInfoEntity> orderInfoEntityList = orderInfoService.selectOrderByDeliveryManId(userInfo.getUserId());
        List<String> orderIds = new ArrayList<>();
        for (OrderInfoEntity entity : orderInfoEntityList) {
            if (StringUtils.isNotEmpty(entity.getId())) {
                orderIds.add(entity.getId());
            }
        }
        PageInfo<OrderCommentEntity> commentEntities = orderCommentService.selectAllByOrderId(orderIds, search,"C");
        List<OrderCommentEntity> list = commentEntities.getList();
        return ResultUtils.success(commentEntities.getPageNum(), (int) commentEntities.getTotal(), list);

    }

    @ApiOperation("查询综合评分")
    @PostMapping("/queryDeliveryRate")
    public CommonResponse<Double> queryDeliveryRate(@RequestHeader(value = "Authorization") String token) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        List<OrderInfoEntity> orderInfoEntityList = orderInfoService.selectOrderByDeliveryManId(userInfo.getUserId());
        List<String> orderIds = new ArrayList<>();
        for (OrderInfoEntity entity : orderInfoEntityList) {
            if (StringUtils.isNotEmpty(entity.getId())) {
                orderIds.add(entity.getId());
            }
        }
        PageInfo<OrderCommentEntity> commentEntities = orderCommentService.selectAllByOrderId(orderIds, null,"C");
        List<OrderCommentEntity> entitiesList = commentEntities.getList();
        Double endResult = 0.00;
        if (!CollectionUtils.isEmpty(entitiesList)) {
            List<Double> list = entitiesList.stream().map(OrderCommentEntity::getUserRating).collect(Collectors.toList());
            for (Double aDouble : list) {
                if (aDouble != null) {
                    endResult += aDouble;
                }
            }
            endResult = endResult / list.size();
        }
        return ResultUtils.success(endResult);
    }
}
