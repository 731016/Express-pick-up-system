package com.xiaofei.controller.order;

import com.github.pagehelper.PageInfo;
import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.common.SearchCondition;
import com.xiaofei.entity.order.OrderInfoEntity;
import com.xiaofei.entity.order.PaymentInfoEntity;
import com.xiaofei.entity.user.UserInfoEntity;
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
        PageInfo<OrderInfoEntity> ordersPage = orderInfoService.selectOrderBelongGeneral(searchConditions, name);
        List<OrderInfoEntity> orders = ordersPage.getList();
        List<OrderInfoVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orders)) {
            List<String> ids = orders.stream().map(OrderInfoEntity::getId).collect(Collectors.toList());
            List<PaymentInfoEntity> paymentInfoEntities = paymentInfoService.selectPaymentInfoByUserIds(ids);
            vos = orderInfoService.poToVo(orders, paymentInfoEntities);
        }
        return ResultUtils.success(ordersPage.getPageNum(), ordersPage.getPageSize(), (int) ordersPage.getTotal(), vos);
    }

    @ApiOperation("普通用户撤销订单")
    @PostMapping("/revokeOrder")
    public CommonResponse<String> revokeOrder(@RequestBody List<String> ids) {
        String result = orderInfoService.revokeOrderStatus(ids);
        return ResultUtils.success(result, "");
    }

    @ApiOperation("普通用户删除订单")
    @PostMapping("/deleteOrder")
    public CommonResponse<String> deleteOrder(@RequestBody List<String> ids) {
        String result = orderInfoService.deleteOrderStatus(ids);
        return ResultUtils.success(result, "");
    }
}
