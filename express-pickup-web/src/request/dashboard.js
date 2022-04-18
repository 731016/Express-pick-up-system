import server from "./http";

//获取普通用户仪表盘
export const dashBoardGeneral = params => server('post', '/dashboard/general', params);
//获取配送用户仪表盘
export const dashBoardDelivery = params => server('post', '/dashboard/delivery', params);