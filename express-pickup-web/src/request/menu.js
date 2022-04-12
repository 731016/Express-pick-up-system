import server from "./http";

//查询单个用户信息，进入菜单
export const selectOneUserInfo = params => server('post', '/user/selectOneUserInfo', params);