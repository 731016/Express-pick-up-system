package com.xiaofei.controller.dashboard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.common.ActionStatus;
import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.entity.dashBoard.DashBoardEntity;
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
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

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
    public CommonResponse<DashBoardEntity> getGeneralDashBoard(@RequestHeader(value = "Authorization") String token) {
        String userName = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(userName);
        String userId = userInfo.getUserId();
        //订单信息
        List<OrderInfoEntity> orderInfoEntityList = orderInfoService.selectOrderByUserId(userId);
        DashBoardEntity dashBoard = new DashBoardEntity();
        if (!CollectionUtils.isEmpty(orderInfoEntityList)) {
            //所有订单的id
            List<String> orderIds = orderInfoService.collectOrderIdsByGeneral(orderInfoEntityList);
            //支付信息
            List<PaymentInfoEntity> paymentInfoEntities = paymentInfoService.selectPaymentInfoByUserIds(orderIds);
            Integer unPaidNumber = paymentInfoService.filterUnPaidNumber(paymentInfoEntities);
            Map<String, Integer> map = orderInfoService.filterWaitAndDispatchOrderNumber(orderInfoEntityList);
            //查询评价信息
            List<OrderCommentEntity> orderCommentEntityList = orderCommentService.selectAllByUserId(orderIds);
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
}
