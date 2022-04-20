package com.xiaofei.controller.dashboard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.common.ActionStatus;
import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.ResultUtils;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
            List<OrderCommentEntity> orderCommentEntityList = orderCommentService.selectAllByOrderId(orderIds);
            Map<String, Double> evaluateAndRateNumber = orderCommentService.collectEvaluateAndRateNumber(orderCommentEntityList);
            dashBoard.setUnPaidNumber(unPaidNumber);
            if (!CollectionUtils.isEmpty(map)) {
                dashBoard.setWaitOrderNumber(map.get("wait"));
                dashBoard.setDispatchOrderNumber(map.get("dispatch"));
            }
            if (!CollectionUtils.isEmpty(evaluateAndRateNumber)) {
                dashBoard.setReceivedEvaluateNumber(Integer.valueOf(String.valueOf(evaluateAndRateNumber.get("evaluate"))));
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
        //订单信息
        List<OrderInfoEntity> orderInfoEntityList = orderInfoService.selectAllOrderByOrderStatus(10, 20, 30);
        /**
         * 组装订单信息：可以接单=>orderStatus，【10：等待接单】paymentStatus，【1：支付成功】，一条订单对应一条支付信息，暂时不存在只有订单信息，没有支付信息的
         * 需要派送订单：【20：派送中】【30：订单异常】
         * 评价信息：deliveryManId=>orderid，评价信息
         */
        DashBoardVo dashBoard = new DashBoardVo();
        if (!CollectionUtils.isEmpty(orderInfoEntityList)) {
            //允许接单
            Integer allowOrderNumber = 0;
            //订单状态为10，等待接单
            List<String> orderIds = new ArrayList<>();
            //需要派送
            Integer dispatchOrderNumber = 0;
            for (OrderInfoEntity entity : orderInfoEntityList) {
                if (entity.getOrderStatus() == 20 || entity.getOrderStatus() == 30) {
                    dispatchOrderNumber++;
                }
                //收集订单id，第一次过滤
                if (entity.getOrderStatus() == 10) {
                    orderIds.add(entity.getId());
                }
            }
            //支付信息
            List<PaymentInfoEntity> paymentInfoEntities = paymentInfoService.selectPaymentInfoByOrderIds(orderIds);
            //过滤支付完成的
            dispatchOrderNumber = paymentInfoService.filterPaymentSuccess(paymentInfoEntities);

            //查询评价信息
            List<OrderCommentEntity> orderCommentEntityList = orderCommentService.selectAllByOrderId(orderIds);
            Map<String, Double> evaluateAndRateNumber = orderCommentService.collectEvaluateAndRateNumber(orderCommentEntityList);
            dashBoard.setAllowOrderNumber(allowOrderNumber);
            dashBoard.setDispatchOrderNumber(dispatchOrderNumber);

            if (!CollectionUtils.isEmpty(evaluateAndRateNumber)) {
                dashBoard.setReceivedEvaluateNumber(Integer.valueOf(String.valueOf(evaluateAndRateNumber.get("evaluate"))));
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
        for (OrderInfoEntity entity : entityList) {
            Integer orderStatus = entity.getOrderStatus();
            switch (orderStatus) {
                case 10:
                    waitOrderNumber++;
                    break;
                case 20:
                    dispatchOrderNumber++;
                    break;
            }
            Date createTime = entity.getCreateTime();

        }
        List<UserInfoEntity> entities = userInfoService.selectAll();
        userTotal = entities.size();
        for (UserInfoEntity entity : entities) {
            Integer status = entity.getDisable();
            if (status == 0) {
                disableUser++;
            }
            Date freezeTime = entity.getFreezeTime();
            if (freezeTime != null) {
                freezeUser++;
            }
        }
        return null;
    }
}
