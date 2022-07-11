package com.xiaofei.controller.order;

import com.github.pagehelper.PageInfo;
import com.xiaofei.common.ActionStatus;
import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.controller.user.UserController;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.service.order.OrderInfoService;
import com.xiaofei.service.order.PaymentInfoService;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.utils.DateUtils;
import com.xiaofei.utils.JwtUtils;
import com.xiaofei.vo.AssignOrderVo;
import com.xiaofei.vo.OrderInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @date 2022/4/16 22:24
 */
@Api(tags = "管理员订单接口")
@RestController
@RequestMapping("/managerOrder")
public class ManagerController {
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private PaymentInfoService paymentInfoService;

    @ApiOperation("管理员分配订单")
    @PostMapping("/assignOrder")
    public CommonResponse<String> assignOrder(@RequestBody AssignOrderVo assignOrderVo) {
        List<OrderInfoEntity> entities = orderInfoService.selectOrderByOrderId(assignOrderVo.getOrderIds());
        String deliveryId = assignOrderVo.getDeliveryId();
        for (OrderInfoEntity entity : entities) {
            entity.setOrderStatus(20);
            entity.setDeliveryManId(deliveryId);
        }
        Boolean batchAcceptOrder = orderInfoService.batchAcceptOrder(entities);
        if (batchAcceptOrder) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("完成订单")
    @PostMapping("/successOrder")
    public CommonResponse<String> successOrder(@RequestBody List<String> orderIds) {
        Boolean aBoolean = orderInfoService.successOrder(orderIds);
        if (aBoolean) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("异常订单")
    @PostMapping("/execptionOrder")
    public CommonResponse<String> execptionOrder(@RequestBody List<String> orderIds) {
        Boolean aBoolean = orderInfoService.execptionOrder(orderIds);
        if (aBoolean) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("查询所有用户，被删除和撤销的订单")
    @PostMapping("/selectAllAndRevokeOrder")
    public CommonResponse<List<OrderInfoVo>> selectAllAndRevokeOrder(@RequestBody SearchCondition searchConditions) {
        PageInfo<OrderInfoEntity> ordersPage = orderInfoService.selectAllOrder(searchConditions, false);
        List<OrderInfoEntity> orders = ordersPage.getList();
        List<OrderInfoVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orders)) {
            List<String> ids = orders.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
            List<PaymentInfoEntity> paymentInfoEntities = paymentInfoService.selectPaymentInfoByOrderIds(ids);
            vos = orderInfoService.poToVo(orders, paymentInfoEntities);
        }
        return ResultUtils.success(ordersPage.getPageNum(), (int) ordersPage.getTotal(), vos);
    }

    @ApiOperation("查询所有用户，没有删除和撤销的订单")
    @PostMapping("/selectAllUnDelAndRevokeOrder")
    public CommonResponse<List<OrderInfoVo>> selectAllUnDelAndRevokeOrder(@RequestBody SearchCondition searchConditions) {
        PageInfo<OrderInfoEntity> ordersPage = orderInfoService.selectAllOrder(searchConditions, true);
        List<OrderInfoEntity> orders = ordersPage.getList();
        List<OrderInfoVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orders)) {
            List<String> ids = orders.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
            List<PaymentInfoEntity> paymentInfoEntities = paymentInfoService.selectPaymentInfoByOrderIds(ids);
            vos = orderInfoService.poToVo(orders, paymentInfoEntities);
        }
        return ResultUtils.success(ordersPage.getPageNum(), (int) ordersPage.getTotal(), vos);
    }

    @ApiOperation("获取配送人员信息")
    @PostMapping("/getDeliveryPeople")
    public CommonResponse<List<UserInfoEntity>> getDeliveryPeople() {
        List<UserInfoEntity> entities = userInfoService.selectDelivery();
        logger.info("获取配送人员信息如下：" + entities.toString());
        if (CollectionUtils.isEmpty(entities)) {
            return ResultUtils.error(ActionStatus.GETDELIVERYERR.getCode(), ActionStatus.GETDELIVERYERR.getMsg(), null);
        }
        return ResultUtils.success(entities);
    }

    @ApiOperation("管理员获取所有用户，除了管理员")
    @PostMapping("/getAllUser")
    public CommonResponse<List<UserInfoEntity>> getAllUser(@RequestBody SearchCondition search) {
        PageInfo<UserInfoEntity> allUser = userInfoService.getAllUser(search);
        List<UserInfoEntity> list = allUser.getList();
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.error(ActionStatus.NOEXISTUSER.getCode(), ActionStatus.NOEXISTUSER.getMsg(), null);
        }
        return ResultUtils.success(allUser.getPageNum(), (int) allUser.getTotal(), list);
    }

    @ApiOperation("禁用用户")
    @GetMapping("/disableUser")
    public CommonResponse<String> disableUser(@RequestParam(value = "userId", required = false) String userId) {
        UserInfoEntity userInfoEntity = userInfoService.selectUserById(userId);
        if (userInfoEntity != null) {
            userInfoEntity.setDisable(0);
            Boolean update = userInfoService.update(userInfoEntity);
            if (update) {
                return ResultUtils.success("");
            }
        }
        return ResultUtils.error("");
    }

    @ApiOperation("启用用户")
    @GetMapping("/enableUser")
    public CommonResponse<String> enableUser(@RequestParam(value = "userId", required = false) String userId) {
        UserInfoEntity userInfoEntity = userInfoService.selectUserById(userId);
        if (userInfoEntity != null) {
            userInfoEntity.setDisable(1);
            Boolean update = userInfoService.update(userInfoEntity);
            if (update) {
                return ResultUtils.success("");
            }
        }
        return ResultUtils.error("");
    }

    @ApiOperation("冻结用户")
    @GetMapping("/freezeUser")
    public CommonResponse<Date> freezeUser(@RequestParam(value = "userId", required = false) String userId,
                                           @RequestParam(value = "freezeTime", required = false) Integer freezeTime) {
        UserInfoEntity userInfoEntity = userInfoService.selectUserById(userId);
        if (userInfoEntity != null) {
            Date freezeUserTime = DateUtils.addHour(new Date(), freezeTime);
            userInfoEntity.setFreezeTime(freezeUserTime);
            Boolean update = userInfoService.update(userInfoEntity);
            if (update) {
                return ResultUtils.success(freezeUserTime);
            }
        }
        return ResultUtils.error(null);
    }

    @ApiOperation("解封用户")
    @GetMapping("/unfreezeUser")
    public CommonResponse<String> unfreezeUser(@RequestParam(value = "userId", required = false) String userId) {
        UserInfoEntity userInfoEntity = userInfoService.selectUserById(userId);
        if (userInfoEntity != null) {
            Boolean update = userInfoService.unfreezeUser(userInfoEntity);
            if (update) {
                return ResultUtils.success("");
            }
        }
        return ResultUtils.error("");
    }
}
