import server from "./http";
//查询所有学校信息
export const selectAllSchool = params => server('post', '/school/selectAllSchool', params);
//查询单个学校信息
export const selectOneSchool = params => server('post', '/school/selectOneSchool', params);
//查询所有省份信息
export const selectAllProvince = params => server('post', '/province/selectAllProvince', params);
//查询单个省份信息
export const selectOneProvince = params => server('post', '/province/selectOneProvince', params);