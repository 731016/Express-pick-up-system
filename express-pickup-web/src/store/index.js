//引入vue
import Vue from 'vue';
//引入vuex
import Vuex from 'vuex';
// import axios from "axios";
//应用vuex插件
Vue.use(Vuex);

//准备actions对象-响应组件中用户的动作
const actions = {}
//准备mutations对象-修改state中的数据
const mutations = {
    updateUserInfo(state, value) {
        state.userId = value.userId;
        state.userName = value.userName;
        state.userRoleId = value.userRoleId;
        state.userRoleName = value.userRoleName;
    }
}
//准备state对象-保存具体的数据
const getters = {
    getOverallRating() {
        return state.evaluate.overallRating;
    },
    getRateCommitTotal() {
        return state.tableData.filter(item => item.rangeInfo.id != '').length;
    },
    getUserRoleId() {
        return state.userRoleId;
    },
    getUserId() {
        return state.userId;
    },
    getUserName() {
        return state.userName;
    },
    getUserRoleName() {
        return state.userRoleName;
    }
}
const state = {
    identifyCode:'',
    userId: '',
    userName: '',
    userRoleId: '',
    userRoleName: '',
    orderStatusOptions: [
        {
            value: 0,
            label: '所有状态'
        },
        {
            value: 10,
            label: '等待接单',
        },
        {
            value: 20,
            label: '派送中'
        },
        {
            value: 30,
            label: '订单完成'
        },
        {
            value: 40,
            label: '订单异常'
        }],
    paymentStatusOptions: [
        {
            value: -1,
            label: '所有状态'
        },
        {
            value: 0,
            label: '等待支付'
        },
        {
            value: 1,
            label: '支付成功'
        }
    ],
    /*tableData: [
        {
            id: 'qwed-34253-fsgdf-42354',
            orderStep: 2,
            // 订单号
            orderNumber: '1111111111',
            // 收件姓名
            pickupName: '涂鏊飞',
            // 收件短信
            contactNumber: '17685585594',
            // 寄达地址
            trackDeliveryAddress: '武汉市xxx',
            // 收件地址
            shipAddress: '碧海花园',
            // 快递单号
            trackNumber: '156461564496',
            // 快递公司
            trackCompany: 'JD',
            trackCompanyName: '京东',
            //配送员id
            deliveryManId: '4923853458943',
            // 配送员
            deliveryMan: '色色色',
            // 配送备注
            shippingReamrk: '',
            // 备注
            remark: '放到菜鸟驿站',
            //是否删除【1撤销，0未删除,-1删除】
            isDel: '0',
            //删除原因
            delReason: '',
            // 订单状态
            orderStatus: '派送中',
            createTime: '2022/3/12',
            rangeInfo: {
                id: '3442',
                userRatings: 4.5,
                comment: '参数方程无法',
                completeEvaluationFlag: '0'
            },
            paymentInfo: {
                id: '1-1234',
                // 支付方式
                paymentMethod: '支付宝',
                // 流水号
                serialNumber: '3346587897967568689797',
                // 支付金额
                paymentAmount: 4.0,
                // 支付状态
                paymentStatus: '等待支付',
            }
        },
        {
            id: 'd223423-edwede-d2wedwe-e2r23f-dwedf',
            orderStep: 3,
            // 订单号
            orderNumber: '22222222222',
            // 收件姓名
            pickupName: '涂鏊飞',
            // 收件短信
            contactNumber: '17685585594',
            // 寄达地址
            trackDeliveryAddress: '武汉市xxx',
            // 收件地址
            shipAddress: '碧海花园',
            // 快递单号
            trackNumber: '454375698709-09-90-890',
            // 快递公司
            trackCompany: 'JD',
            trackCompanyName: '京东',
            //配送员id
            deliveryManId: '43657876876',
            // 配送员
            deliveryMan: '规划和',
            // 配送备注
            shippingReamrk: '已送达',
            // 备注
            remark: '放到菜鸟驿站',
            //是否删除【1删除，0未删除】
            isDel: '0',
            //删除原因
            delReason: '',
            // 订单状态
            orderStatus: '订单完成',
            createTime: '2022/3/14',
            rangeInfo: {
                id: '2-45345',
                userRatings: 4.5,
                comment: '服务好',
                completeEvaluationFlag: '1'
            },
            paymentInfo: {
                id: '2-43242',
                // 支付方式
                paymentMethod: '支付宝',
                // 流水号
                serialNumber: '3346587897967568689797',
                // 支付金额
                paymentAmount: 42.0,
                // 支付状态
                paymentStatus: '等待支付',
            }
        },
        {
            id: '345435-435-45646-54654-7574',
            orderStep: 0,
            // 订单号
            orderNumber: '333333333333',
            // 收件姓名
            pickupName: '涂鏊飞',
            // 收件短信
            contactNumber: '17685585594',
            // 寄达地址
            trackDeliveryAddress: '武汉市xxx',
            // 收件地址
            shipAddress: '碧海花园',
            // 快递单号
            trackNumber: '454375698709-09-90-890',
            // 快递公司
            trackCompany: 'JD',
            trackCompanyName: '京东',
            //配送员id
            deliveryManId: '4923853458943',
            // 配送员
            deliveryMan: '色色色',
            // 配送备注
            shippingReamrk: '',
            // 备注
            remark: '放到菜鸟驿站',
            //是否删除【1撤销，0未删除,-1删除】
            isDel: '0',
            //删除原因
            delReason: '',
            // 订单状态
            orderStatus: '等待接单',
            createTime: '2022/3/12',
            rangeInfo: {
                id: '3-45345',
                userRatings: 6.7,
                comment: '发个体哥哥',
                completeEvaluationFlag: '0'
            },
            paymentInfo: {
                id: '3-4534',
                // 支付方式
                paymentMethod: '支付宝',
                // 流水号
                serialNumber: '3346587897967568689797',
                // 支付金额
                paymentAmount: 4.0,
                // 支付状态
                paymentStatus: '支付成功',
            }
        },
        {
            id: '2343254-2432423-234324-2423',
            orderStep: 3,
            // 订单号
            orderNumber: '444444444',
            // 收件姓名
            pickupName: '涂鏊飞',
            // 收件短信
            contactNumber: '17685585594',
            // 寄达地址
            trackDeliveryAddress: '武汉市xxx',
            // 收件地址
            shipAddress: '碧海花园',
            // 快递单号
            trackNumber: '454375698709-09-90-890',
            // 快递公司
            trackCompany: 'JD',
            trackCompanyName: '京东',
            //配送员id
            deliveryManId: '475678687686',
            // 配送员
            deliveryMan: '嘎嘎嘎',
            // 配送备注
            shippingReamrk: '已送达',
            // 备注
            remark: '放到菜鸟驿站',
            //是否删除【1撤销，0未删除,-1删除】
            isDel: '0',
            //删除原因
            delReason: '',
            // 订单状态
            orderStatus: '派送中',
            createTime: '2022/3/14',
            rangeInfo: {
                id: '',
                userRatings: 0,
                comment: '',
                completeEvaluationFlag: '0'
            },
            paymentInfo: {
                id: '4-433',
                // 支付方式
                paymentMethod: '支付宝',
                // 流水号
                serialNumber: '3346587897967568689797',
                // 支付金额
                paymentAmount: 42.0,
                // 支付状态
                paymentStatus: '支付成功',
            }
        },
        {
            id: '2343254-2432423-234324-2423',
            orderStep: 3,
            // 订单号
            orderNumber: '444444444',
            // 收件姓名
            pickupName: '涂鏊飞',
            // 收件短信
            contactNumber: '17685585594',
            // 寄达地址
            trackDeliveryAddress: '武汉市xxx',
            // 收件地址
            shipAddress: '碧海花园',
            // 快递单号
            trackNumber: '454375698709-09-90-890',
            // 快递公司
            trackCompany: 'JD',
            trackCompanyName: '京东',
            //配送员id
            deliveryManId: '4923853458943',
            // 配送员
            deliveryMan: '色色色',
            // 配送备注
            shippingReamrk: '已送达',
            // 备注
            remark: '放到菜鸟驿站',
            //是否删除【1撤销，0未删除,-1删除】
            isDel: '-1',
            //删除原因
            delReason: '手动删除',
            // 订单状态
            orderStatus: '订单完成',
            createTime: '2022/3/14',
            rangeInfo: {
                id: '4-4534',
                userRatings: 7.8,
                comment: '一款具有口语',
                completeEvaluationFlag: '0'
            },
            paymentInfo: {
                id: '4-433',
                // 支付方式
                paymentMethod: '支付宝',
                // 流水号
                serialNumber: '3346587897967568689797',
                // 支付金额
                paymentAmount: 42.0,
                // 支付状态
                paymentStatus: '支付成功',
            },
        },
        {
            id: '345435-435-45646-54654-7574',
            orderStep: 3,
            // 订单号
            orderNumber: '333333333333',
            // 收件姓名
            pickupName: '涂鏊飞',
            // 收件短信
            contactNumber: '17685585594',
            // 寄达地址
            trackDeliveryAddress: '武汉市xxx',
            // 收件地址
            shipAddress: '碧海花园',
            // 快递单号
            trackNumber: '454375698709-09-90-890',
            // 快递公司
            trackCompany: 'JD',
            trackCompanyName: '京东',
            //配送员id
            deliveryManId: '4923853458943',
            // 配送员
            deliveryMan: '色色色',
            // 配送备注
            shippingReamrk: '',
            // 备注
            remark: '放到菜鸟驿站',
            //是否删除【1撤销，0未删除,-1删除】
            isDel: '0',
            //删除原因
            delReason: '',
            // 订单状态
            orderStatus: '订单异常',
            createTime: '2022/3/12',
            rangeInfo: {
                id: '',
                userRatings: 0,
                comment: '',
                completeEvaluationFlag: '0'
            },
            paymentInfo: {
                id: '3-4534',
                // 支付方式
                paymentMethod: '支付宝',
                // 流水号
                serialNumber: '3346587897967568689797',
                // 支付金额
                paymentAmount: 4.0,
                // 支付状态
                paymentStatus: '支付成功',
            }
        }
    ],*/
}

//创建store
export default new Vuex.Store({
    actions,
    mutations,
    state,
    getters
});