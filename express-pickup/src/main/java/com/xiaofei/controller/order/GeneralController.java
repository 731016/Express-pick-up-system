package com.xiaofei.controller.order;

import com.github.pagehelper.PageInfo;
import com.xiaofei.common.ActionStatus;
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
import com.xiaofei.utils.OrderUtils;
import com.xiaofei.vo.OrderInfoVo;
import com.xiaofei.vo.PaymentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 20:58
 */
@Api(tags = "普通用户订单接口")
@RestController
@RequestMapping("/order")
public class GeneralController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private PaymentInfoService paymentInfoService;
    @Resource
    private OrderCommentService orderCommentService;

    @ApiOperation("发布订单")
    @PostMapping("/publishOrder")
    public CommonResponse<String> pulishOrder(@RequestHeader(value = "Authorization") String token, @RequestBody OrderInfoVo orderInfoVo) {
        OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
        //查询用户id
        String userNameByToken = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(userNameByToken);
        //设置订单信息
        orderInfoEntity.setUserId(userInfo.getUserId());
        orderInfoEntity.setTrackCompanyId(orderInfoVo.getTrackCompanyId());
        orderInfoEntity.setTrackNumber(orderInfoVo.getTrackNumber());
        orderInfoEntity.setPickupName(orderInfoVo.getPickupName());
        orderInfoEntity.setContactNumber(orderInfoVo.getContactNumber());
        orderInfoEntity.setShipAddress(orderInfoVo.getShipAddress());
        orderInfoEntity.setTrackDeliveryAddress(orderInfoVo.getTrackDeliveryAddress());
        orderInfoEntity.setRemark(orderInfoVo.getRemark());
        //返回插入的订单主键
        String id = orderInfoService.insertOrder(orderInfoEntity);
        if (StringUtils.isNotEmpty(id)) {
            PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
            PaymentVo pay = orderInfoVo.getPaymentInfo();
            paymentInfoEntity.setOrderid(id);
            paymentInfoEntity.setPaymentMethod(pay.getPaymentMethod());
            paymentInfoEntity.setPaymentAmount(pay.getPaymentAmount());
            paymentInfoEntity.setSerialNumber(OrderUtils.genSerialNumber());
            paymentInfoEntity.setPaymentStatus(1);
            paymentInfoEntity.setBoxNumber(pay.getBoxNumber());
            paymentInfoEntity.setWeightTotal(pay.getWeightTotal());
            Boolean insertFlag = paymentInfoService.insertPayment(paymentInfoEntity);
            if (insertFlag) {
                return ResultUtils.success("下单成功", null);
            }
        }
        return ResultUtils.error("下单失败", null);
    }

    @ApiOperation("查询普通用户，没有被删除和撤销的订单")
    @PostMapping("/selectAllOrder")
    public CommonResponse<List<OrderInfoVo>> selectOrderBelongGeneral(@RequestHeader(value = "Authorization") String token, @RequestBody SearchCondition searchConditions) {
        String name = JwtUtils.getUserNameByToken(token);
        PageInfo<OrderInfoEntity> ordersPage = orderInfoService.selectOrderBelongGeneral(searchConditions, name, true);
        List<OrderInfoEntity> orders = ordersPage.getList();
        List<OrderInfoVo> vos = new ArrayList<>();
        PageInfo<PaymentInfoEntity> paymentInfoEntities = new PageInfo<>();
        if (!CollectionUtils.isEmpty(orders)) {
            List<String> ids = orders.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
            paymentInfoEntities = paymentInfoService.selectPaymentInfoByOrderIds(searchConditions, ids);
            vos = orderInfoService.poToVo(orders, paymentInfoEntities.getList());
        }
        return ResultUtils.success(paymentInfoEntities.getPageNum(), (int) paymentInfoEntities.getTotal(), vos);
    }

    @ApiOperation("查询普通用户，被删除和撤销的订单")
    @PostMapping("/selectDelAndRevokeOrder")
    public CommonResponse<List<OrderInfoVo>> selectDelAndRevokeOrder(@RequestHeader(value = "Authorization") String token, @RequestBody SearchCondition searchConditions) {
        String name = JwtUtils.getUserNameByToken(token);
        PageInfo<OrderInfoEntity> ordersPage = orderInfoService.selectOrderBelongGeneral(searchConditions, name, false);
        List<OrderInfoEntity> orders = ordersPage.getList();
        List<OrderInfoVo> vos = new ArrayList<>();
        PageInfo<PaymentInfoEntity> paymentInfoEntities = new PageInfo<>();
        if (!CollectionUtils.isEmpty(orders)) {
            List<String> ids = orders.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
            paymentInfoEntities = paymentInfoService.selectPaymentInfoByOrderIds(searchConditions, ids);
            vos = orderInfoService.poToVo(orders, paymentInfoEntities.getList());
        }
        return ResultUtils.success(paymentInfoEntities.getPageNum(), (int) paymentInfoEntities.getTotal(), vos);
    }

    @ApiOperation("普通用户撤销订单")
    @PostMapping("/revokeOrder")
    public CommonResponse<String> revokeOrder(@RequestBody List<String> ids) {
        Boolean updateFlag = orderInfoService.revokeOrderStatus(ids);
        if (updateFlag) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("普通用户删除订单")
    @PostMapping("/deleteOrder")
    public CommonResponse<String> deleteOrder(@RequestBody List<String> ids) {
        Boolean updateFlag = orderInfoService.deleteOrderStatus(ids);
        if (updateFlag) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("普通用户还原订单")
    @PostMapping("/recyceOrder")
    public CommonResponse<String> recyceOrder(@RequestBody List<String> ids) {
        Boolean updateFlag = orderInfoService.recyceOrderStatus(ids);
        if (updateFlag) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("普通用户评价订单")
    @PostMapping("/evaluateOrder")
    public CommonResponse<String> evaluateOrder(@RequestHeader(value = "Authorization") String token, @RequestBody OrderCommentEntity comment) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);

        String orderId = comment.getOrderId();
        OrderCommentEntity entity = orderCommentService.selectAllByOrderId(orderId);
        Boolean insertOrUpdate = false;
        if (entity == null) {
            entity = new OrderCommentEntity();
            entity.setOrderId(comment.getOrderId());
            entity.setUserId(userInfo.getUserId());
            entity.setUserRating(comment.getUserRating());
            entity.setComment(comment.getComment());
            insertOrUpdate = orderCommentService.insert(entity);
        } else {
            entity.setUserId(userInfo.getUserId());
            entity.setUserRating(comment.getUserRating());
            entity.setComment(comment.getComment());
            insertOrUpdate = orderCommentService.update(entity, entity.getOrderId());
        }
        if (insertOrUpdate) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("查询所有评价信息，根据用户id,查找订单id")
    @PostMapping("/selectAllComment")
    public CommonResponse<List<OrderCommentEntity>> selectAllComment(@RequestHeader(value = "Authorization") String token,
                                                                     @RequestBody SearchCondition search) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        List<OrderInfoEntity> orderInfoEntityList = orderInfoService.selectOrderByUserId(userInfo.getUserId());
        List<String> orderIds = new ArrayList<>();
        for (OrderInfoEntity entity : orderInfoEntityList) {
            if (StringUtils.isNotEmpty(entity.getId())) {
                orderIds.add(entity.getId());
            }
        }
        PageInfo<OrderCommentEntity> pageInfo = orderCommentService.selectAllByOrderId(orderIds, search);
        List<OrderCommentEntity> list = pageInfo.getList();
        return ResultUtils.success(pageInfo.getPageNum(), (int) pageInfo.getTotal(), list);
    }

    @ApiOperation("查询综合评分")
    @PostMapping("/queryRate")
    public CommonResponse<Double> queryRate(@RequestHeader(value = "Authorization") String token) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        List<OrderInfoEntity> orderInfoEntityList = orderInfoService.selectOrderByUserId(userInfo.getUserId());
        List<String> orderIds = new ArrayList<>();
        for (OrderInfoEntity entity : orderInfoEntityList) {
            if (StringUtils.isNotEmpty(entity.getId())) {
                orderIds.add(entity.getId());
            }
        }
        PageInfo<OrderCommentEntity> commentEntities = orderCommentService.selectAllByOrderId(orderIds, null);
        List<OrderCommentEntity> entities = commentEntities.getList();
        Double endResult = 0.00;
        if (!CollectionUtils.isEmpty(entities)) {
            List<Double> list = entities.stream().map(OrderCommentEntity::getDeliveryRating).collect(Collectors.toList());
            for (Double aDouble : list) {
                endResult += aDouble;
            }
            endResult = endResult / list.size();
        }
        return ResultUtils.success(endResult);
    }
}
