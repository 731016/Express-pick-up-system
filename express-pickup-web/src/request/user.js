import server from "./http";

//注册用户
export const register = params => server('post', '/user/register', params);
//用户登录
export const login = params => server('post', '/user/login', params);
//用户退出登录
export const logout = params => server('post', '/user/logout', params);
//查询用户信息
export const queryUserInfo = params => server('post', '/user/queryUserInfo', params);
//修改密码
export const updatePwd = params => server('post', '/user/updatePwd', params);
//修改手机号
export const updatePhoneAjax = params => server('post', '/user/updatePhoneAjax', params);
//修改性别
export const updateSexAjax = params => server('post', '/user/updateSexAjax', params);
//修改学校信息。包括学校id、学号、学校名称
export const updateSchoolInfo = params => server('post', '/user/updateSchoolInfo', params);
//实名认证
export const updateIdCard =  params => server('post', '/user/updateIdCard', params);
//验证密码并切换用户,为配送用户
export const verifyPwdAndChange = params => server('post', '/user/verifyPwdAndChange', params);
//验证密码并切换用户,为普通用户
export const exchangeRoleToGeneral = params => server('post', '/user/exchangeRoleToGeneral', params);
