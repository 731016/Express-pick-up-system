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
    identifyCode: '',
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
}

//创建store
export default new Vuex.Store({
    actions,
    mutations,
    state,
    getters
});