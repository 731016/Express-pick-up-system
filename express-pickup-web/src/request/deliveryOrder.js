import server from "./http";

//查询配送员订单信息
export const selectDeliveryOrder = params => server('post', '/deliveryOrder/selectDeliveryOrder', params);
//查询所有订单状态为10，支付状态为1的订单
export const selectAllowAcceptOrder = params => server('post', '/deliveryOrder/selectAllowAcceptOrder', params);
//接单
export const acceptOrder = params => server('post', '/deliveryOrder/acceptOrder', params);
//订单完成
export const orderSuccessAjax = params => server('post', '/deliveryOrder/OrderSuccess', params);
//订单异常
export const orderExceptionAjax = params => server('post', '/deliveryOrder/OrderException', params);
//订单评价
export const orderEvaluate = params => server('post', '/deliveryOrder/OrderEvaluate', params);
//获取评价信息
export const selectDeliveryComment = params => server('post', '/deliveryOrder/selectDeliveryComment', params);
//获取综合评分
export const queryDeliveryRate = params => server('post', '/deliveryOrder/queryDeliveryRate', params);