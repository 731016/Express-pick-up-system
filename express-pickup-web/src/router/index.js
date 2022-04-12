import VueRouter from 'vue-router';
import DashBoard from "../views/generalUser/DashBoard";
import OrderNow from "../views/generalUser/OrderNow";
import ConfirmOrder from "../views/generalUser/ConfirmOrder";
import DeliveryOrderList from "../views/deliveryUser/DeliveryOrderList";
import GeneralOrderList from "../views/generalUser/GeneralOrderList";
import RecycleBin from "../views/generalUser/RecycleBin";
import EvaluationCenter from "../views/generalUser/EvaluationCenter";
import PersonalCenter from "../views/generalUser/PersonalCenter";
import OrderHall from "../views/deliveryUser/OrderHall";
import DeliveryPersonalCenter from "../views/deliveryUser/DeliveryPersonalCenter";
import ManageDashBoard from "../views/ManagerUser/ManageDashBoard";
import OrderManager from "../views/ManagerUser/OrderManager";
import ManagerRecycleBin from "../views/ManagerUser/ManagerRecycleBin";
import UserManager from "../views/ManagerUser/UserManager";
import ManagerPersonalCenter from "../views/ManagerUser/ManagerPersonalCenter";
import Login from "../views/common/Login";
import Register from "../views/common/Register";
import Home from "../views/common/Home";
import GeneralDashBoard from "../views/deliveryUser/DeliveryDashBoard";
import ResultSuccess from "../views/common/ResultSuccess";

const router = new VueRouter({
    routes: [
        {
            path: '/',
            redirect: {name: 'Login'}
        },
        {
            path: "/Login",
            name: "Login",
            component: Login,
            meta: {
                title: "登录页",
            }
        },
        {
            path: "/Register",
            name: "Register",
            component: Register,
            meta: {
                title: "注册页",
            }
        },
        {
            path: "/Home",
            name: "Home",
            component: Home,
            meta: {
                title: "主页"
            },
            children: [
                {
                    name: "DashBoard",
                    path: 'DashBoard',
                    component: DashBoard,
                    meta: {
                        title: "普通用户仪表盘"
                    }
                },
                {
                    name: "GeneralDashBoard",
                    path: 'GeneralDashBoard',
                    component: GeneralDashBoard,
                    meta: {
                        title: "配送员仪表盘"
                    }
                },
                {
                    name: "ManageDashBoard",
                    path: "ManageDashBoard",
                    component: ManageDashBoard,
                    meta: {
                        title: "管理员仪表盘"
                    }
                },
                {
                    name: "OrderNow",
                    path: 'OrderNow',
                    component: OrderNow,
                    meta: {
                        title: "下单"
                    }
                },
                {
                    name: 'ConfirmOrder',
                    path: 'ConfirmOrder',
                    component: ConfirmOrder,
                    meta: {
                        title: "订单确认"
                    }
                },
                {
                    name: 'GeneralOrderList',
                    path: 'GeneralOrderList',
                    component: GeneralOrderList,
                    meta: {
                        title: "普通用户订单管理"
                    }
                }, {
                    name: 'DeliveryOrderList',
                    path: 'DeliveryOrderList',
                    component: DeliveryOrderList,
                    meta: {
                        title: "配送员订单管理"
                    }
                },
                {
                    name: 'RecycleBin',
                    path: 'RecycleBin',
                    component: RecycleBin,
                    meta: {
                        title: "回收中心"
                    }
                }, {
                    name: 'EvaluationCenter',
                    path: 'EvaluationCenter',
                    component: EvaluationCenter,
                    meta: {
                        title: "评价中心"
                    }
                },
                {
                    name: 'PersonalCenter',
                    path: 'PersonalCenter',
                    component: PersonalCenter,
                    meta: {
                        title: "个人中心"
                    }
                }, {
                    name: 'OrderHall',
                    path: 'OrderHall',
                    component: OrderHall,
                }, {
                    name: 'DeliveryPersonalCenter',
                    path: 'DeliveryPersonalCenter',
                    component: DeliveryPersonalCenter,
                    meta: {
                        title: "配送员个人中心"
                    }
                }, {
                    name: 'OrderManager',
                    path: 'OrderManager',
                    component: OrderManager,
                    meta: {
                        title: "管理员订单管理"
                    }
                }, {
                    name: 'ManagerRecycleBin',
                    path: 'ManagerRecycleBin',
                    component: ManagerRecycleBin,
                    meta: {
                        title: "管理员订单回收"
                    }
                }, {
                    name: 'UserManager',
                    path: 'UserManager',
                    component: UserManager,
                    meta: {
                        title: "管理员用户管理"
                    }
                }, {
                    name: 'ManagerPersonalCenter',
                    path: 'ManagerPersonalCenter',
                    component: ManagerPersonalCenter,
                    meta: {
                        title: "管理员个人中心"
                    }
                },{
                    name: 'ResultSuccess',
                    path: '/ResultSuccess',
                    component: ResultSuccess,
                    meta: {
                        title: '下单成功'
                    }
                }
            ]
        },
    ]
})
router.beforeEach((to, from, next) => {
    let token = localStorage.getItem("token");
    if (to.name === 'Login' || to.name === 'Register') {
        if (token) {
            next({name: 'Home'})
        } else {
            next();
        }
    } else {
        if (token) {
            next();
        } else {
            next({name: 'Login'})
        }
    }
})
export default router