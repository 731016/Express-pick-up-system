import server from "./http";

//发布代取订单
export const publishOrder = params => server('post', '/order/publishOrder', params);
//获取没有被删除或撤销的订单，通过userName
export const selectAllOrder = params => server('post', '/order/selectAllOrder', params);
//获取没有被删除或撤销的订单，通过userName
export const selectDelAndRevokeOrder = params => server('post', '/order/selectDelAndRevokeOrder', params);
//撤销订单
export const revokeOrder = params => server('post', '/order/revokeOrder', params);
//删除订单
export const deleteOrder = params => server('post', '/order/deleteOrder', params);
//还原订单
export const recyceOrder = params => server('post', '/order/recyceOrder', params);
//订单评价
export const evaluateOrder = params => server('get', '/order/evaluateOrder', params);