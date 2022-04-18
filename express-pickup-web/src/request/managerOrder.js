import server from "./http";
//查询回收站订单
export const selectAllAndRevokeOrder = params => server('post', '/managerOrder/selectAllAndRevokeOrder', params);
//查询订单管理
export const selectAllUnDelAndRevokeOrder = params => server('post', '/managerOrder/selectAllUnDelAndRevokeOrder', params);
//完成订单
export const successOrder = params => server('post', '/managerOrder/successOrder', params);
//异常订单
export const execptionOrder = params => server('post', '/managerOrder/execptionOrder', params);
//分配订单
export const assignOrder = params => server('post', '/managerOrder/assignOrder', params);
//获取所有的配送人员id和姓名
export const getDeliveryPeople = params => server('post', '/managerOrder/getDeliveryPeople', params);