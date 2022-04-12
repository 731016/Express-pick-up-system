import server from "./http";

//获取普通用户仪表盘
export const dashBoardGeneral = params => server('post', '/dashboard/general', params);