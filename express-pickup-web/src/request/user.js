import server from "./http";

//注册用户
export const register = params => server('post', '/user/register', params);