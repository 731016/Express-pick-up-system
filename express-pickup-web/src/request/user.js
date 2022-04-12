import server from "./http";

//注册用户
export const register = params => server('post', '/user/register', params);
//用户登录
export const login = params => server('post', '/user/login',params);
//用户退出登录
export const logout = params => server('post', '/user/logout',params);