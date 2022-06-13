package com.xiaofei.controller.dashboard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.xiaofei.common.ActionStatus;
import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.utils.DateUtils;
import com.xiaofei.vo.DashBoardVo;
import com.xiaofei.entity.order.OrderCommentEntity;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.service.order.OrderCommentService;
import com.xiaofei.service.order.OrderInfoService;
import com.xiaofei.service.order.PaymentInfoService;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 仪表盘
 *
 * @date 2022/4/9 14:59
 */
@Api(tags = "仪表盘接口")
@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private PaymentInfoService paymentInfoService;
    @Resource
    private OrderCommentService orderCommentService;

    @ApiOperation("普通用户仪表盘")
    @PostMapping("/general")
    public CommonResponse<DashBoardVo> getGeneralDashBoard(@RequestHeader(value = "Authorization") String token) {
        String userName = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(userName);
        String userId = userInfo.getUserId();
        //订单信息
        List<OrderInfoEntity> orderInfoEntityList = orderInfoService.selectOrderByUserId(userId);
        DashBoardVo dashBoard = new DashBoardVo();
        if (!CollectionUtils.isEmpty(orderInfoEntityList)) {
            //所有订单的id
            List<String> orderIds = orderInfoService.collectOrderIds(orderInfoEntityList);
            //支付信息
            List<PaymentInfoEntity> paymentInfoEntities = paymentInfoService.selectPaymentInfoByOrderIds(orderIds);
            Integer unPaidNumber = paymentInfoService.filterUnPaidNumber(paymentInfoEntities);
            Map<String, Integer> map = orderInfoService.filterWaitAndDispatchOrderNumber(orderInfoEntityList);
            //查询评价信息
            PageInfo<OrderCommentEntity> orderCommentEntityList = orderCommentService.selectAllByOrderId(orderIds, null, null);
            List<OrderCommentEntity> list = orderCommentEntityList.getList();
            Map<String, Double> evaluateAndRateNumber = orderCommentService.collectEvaluateAndRateNumber(list,true);
            dashBoard.setUnPaidNumber(unPaidNumber);
            if (!CollectionUtils.isEmpty(map)) {
                dashBoard.setWaitOrderNumber(map.get("wait"));
                dashBoard.setDispatchOrderNumber(map.get("dispatch"));
            }
            if (!CollectionUtils.isEmpty(evaluateAndRateNumber)) {
                dashBoard.setReceivedEvaluateNumber(evaluateAndRateNumber.get("evaluate").intValue());
                dashBoard.setOverallRate(evaluateAndRateNumber.get("rate"));
            }
        }
        if (dashBoard == null) {
            return ResultUtils.error(ActionStatus.REQUESTFAIL.getCode(), ActionStatus.REQUESTFAIL.getMsg(), null);
        }
        return ResultUtils.success(dashBoard);
    }

    @ApiOperation("配送用户仪表盘")
    @PostMapping("/delivery")
    public CommonResponse<DashBoardVo> getDeliveryDashBoard(@RequestHeader(value = "Authorization") String token) {
        /**
         * 可以接单 等待接单、支付完成、未删除、撤销
         * 需要派送 接单用户id，派送中
         * 评价 接单用户id，订单完成、异常。存在评价信息且用户评价不为空
         */
        //允许接单
        Integer allowOrderNumber = 0;

        //需要派送
        Integer dispatchOrderNumber = 0;

        DashBoardVo dashBoard = new DashBoardVo();

        //订单状态为10，等待接单
        List<String> orderIds = new ArrayList<>();

        List<OrderInfoEntity> orderInfoEntityList = orderInfoService.selectAllOrderByOrderStatus(10);
        for (OrderInfoEntity entity : orderInfoEntityList) {
            if (entity.getIsDel() == 0) {
                orderIds.add(entity.getId());
            }
        }
        //支付信息
        List<PaymentInfoEntity> paymentInfoEntities = paymentInfoService.selectPaymentInfoByOrderIds(orderIds);
        //过滤支付完成的
        allowOrderNumber = paymentInfoService.filterPaymentSuccess(paymentInfoEntities);

        String userName = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(userName);
        String userId = userInfo.getUserId();

        List<String> deliveryOrderIds = new ArrayList<>();
        List<OrderInfoEntity> entities = orderInfoService.selectOrderBydeliveryManId(userId);
        for (OrderInfoEntity entity : entities) {
            deliveryOrderIds.add(entity.getId());
            Integer isDel = entity.getIsDel();
            Integer orderStatus = entity.getOrderStatus();
            if (isDel == 0 && orderStatus == 20) {
                dispatchOrderNumber++;
            }
        }

        dashBoard.setAllowOrderNumber(allowOrderNumber == null ? 0 : allowOrderNumber);
        dashBoard.setDispatchOrderNumber(dispatchOrderNumber);

        if (!CollectionUtils.isEmpty(entities)) {
            //查询评价信息
            PageInfo<OrderCommentEntity> orderCommentEntityList = orderCommentService.selectAllByOrderId(deliveryOrderIds, null, null);
            List<OrderCommentEntity> list = orderCommentEntityList.getList();
            Map<String, Double> evaluateAndRateNumber = orderCommentService.collectEvaluateAndRateNumber(list,false);

            if (!CollectionUtils.isEmpty(evaluateAndRateNumber)) {
                dashBoard.setReceivedEvaluateNumber(evaluateAndRateNumber.get("evaluate").intValue());
                dashBoard.setOverallRate(evaluateAndRateNumber.get("rate"));
            }
        }
        if (dashBoard == null) {
            return ResultUtils.error(ActionStatus.REQUESTFAIL.getCode(), ActionStatus.REQUESTFAIL.getMsg(), null);
        }
        return ResultUtils.success(dashBoard);
    }

    @ApiOperation("管理员仪表盘")
    @PostMapping("/manager")
    public CommonResponse<DashBoardVo> getManagerDashBoard() {
        Date date = new Date();
        List<OrderInfoEntity> entityList = orderInfoService.selectAll();
        //总等待接单数
        Integer waitOrderNumber = 0;
        //正在派送数
        Integer dispatchOrderNumber = 0;
        //今日新增订单数
        Integer newAddOrders = 0;
        //今日注册用户数
        Integer newAddRegister = 0;
        // 总用户数
        Integer userTotal = 0;
        // 其中禁用用户数
        Integer disableUser = 0;
        // 冻结用户数：0
        Integer freezeUser = 0;
        userTotal = entityList.size();
        for (OrderInfoEntity entity : entityList) {
            Integer orderStatus = entity.getOrderStatus();
            switch (orderStatus) {
                case 10:
                    waitOrderNumber++;
                    break;
                case 20:
                    dispatchOrderNumber++;
                    break;
                default:
                    break;
            }
            Date createTime = entity.getCreateTime();
            if (DateUtils.isNowDay(createTime)) {
                newAddOrders++;
            }
        }
        List<UserInfoEntity> entities = userInfoService.selectAll();
        for (UserInfoEntity entity : entities) {
            Integer status = entity.getDisable();
            if (status == 0) {
                disableUser++;
            }
            Date freezeTime = entity.getFreezeTime();
            if (freezeTime != null) {
                freezeUser++;
            }
            Date registerTime = entity.getRegisterTime();
            if (DateUtils.isNowDay(registerTime)) {
                newAddRegister++;
            }
        }
        DashBoardVo vo = new DashBoardVo();
        vo.setNewAddOrders(newAddOrders);
        vo.setWaitOrderNumber(waitOrderNumber);
        vo.setDispatchOrderNumber(dispatchOrderNumber);

        vo.setNewAddRegister(newAddRegister);
        vo.setUserTotal(userTotal);
        vo.setDisableUser(disableUser);
        vo.setFreezeUser(freezeUser);
        return ResultUtils.success(vo);
    }
}
