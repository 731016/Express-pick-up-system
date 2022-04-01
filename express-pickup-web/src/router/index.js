import VueRouter from 'vue-router';
import DashBoard from "../views/deliveryUser/DashBoard";
import OrderNow from "../views/deliveryUser/OrderNow";
import ConfirmOrder from "../views/deliveryUser/ConfirmOrder";
import DeliveryOrderList from "../views/deliveryUser/DeliveryOrderList";
import GeneralOrderList from "../views/generalUser/GeneralOrderList";
import RecycleBin from "../views/deliveryUser/RecycleBin";
import EvaluationCenter from "../views/deliveryUser/EvaluationCenter";
import PersonalCenter from "../views/deliveryUser/PersonalCenter";
import OrderHall from "../views/generalUser/OrderHall";
import GeneralPersonalCenter from "../views/generalUser/GeneralPersonalCenter";
import ManageDashBoard from "../views/ManagerUser/ManageDashBoard";
import OrderManager from "../views/ManagerUser/OrderManager";
import ManagerRecycleBin from "../views/ManagerUser/ManagerRecycleBin";
import UserManager from "../views/ManagerUser/UserManager";
import ManagerPersonalCenter from "../views/ManagerUser/ManagerPersonalCenter";
import Login from "../views/common/Login";
import Register from "../views/common/Register";

export default new VueRouter({
    routes: [
        {
            path: "/",
            name: "Login",
            component: Login,
        },
        {
            path: "/register",
            name: "Register",
            component: Register
        },
        {
            name: "DashBoard",
            path: '/DashBoard',
            component: DashBoard,
        },
        {
            name: "ManageDashBoard",
            path: "/ManageDashBoard",
            component: ManageDashBoard
        },
        {
            name: "OrderNow",
            path: '/OrderNow',
            component: OrderNow
        },
        {
            name: 'ConfirmOrder',
            path: '/ConfirmOrder',
            component: ConfirmOrder
        },
        {
            name: 'DeliveryOrderList',
            path: '/DeliveryOrderList',
            component: DeliveryOrderList
        }, {
            name: 'GeneralOrderList',
            path: '/GeneralOrderList',
            component: GeneralOrderList
        },
        {
            name: 'RecycleBin',
            path: '/RecycleBin',
            component: RecycleBin
        }, {
            name: 'EvaluationCenter',
            path: '/EvaluationCenter',
            component: EvaluationCenter
        },
        {
            name: 'PersonalCenter',
            path: '/PersonalCenter',
            component: PersonalCenter
        }, {
            name: 'OrderHall',
            path: '/OrderHall',
            component: OrderHall,
        }, {
            name: 'GeneralPersonalCenter',
            path: '/GeneralPersonalCenter',
            component: GeneralPersonalCenter
        }, {
            name: 'OrderManager',
            path: '/OrderManager',
            component: OrderManager
        }, {
            name: 'ManagerRecycleBin',
            path: '/ManagerRecycleBin',
            component: ManagerRecycleBin
        }, {
            name: 'UserManager',
            path: '/UserManager',
            component: UserManager
        }, {
            name: 'ManagerPersonalCenter',
            path: '/ManagerPersonalCenter',
            component: ManagerPersonalCenter
        }
    ]
})