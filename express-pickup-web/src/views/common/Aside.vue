<template>
    <el-aside style="width: 210px" v-show="showUserRoleMenu" v-loading.fullscreen.lock="loading">
        <div v-if="showGeneralMenu">
            <el-menu
                    default-active="1"
                    class="navigation-menu">
                <el-menu-item index="1" @click="toDashBoard()">
                    <i class="el-icon-star-on"></i>
                    <span slot="title">仪表盘</span>
                </el-menu-item>
                <el-menu-item index="2" @click="toOrderNow()">
                    <i class="el-icon-sold-out"></i>
                    <span slot="title">立即下单</span>
                </el-menu-item>
                <el-menu-item index="3" @click="toGeneralOrderList()">
                    <i class="el-icon-s-order"></i>
                    <span slot="title">我的订单</span>
                </el-menu-item>
                <el-menu-item index="4" @click="toRecycleBin()">
                    <i class="el-icon-delete"></i>
                    <span slot="title">回收站</span>
                </el-menu-item>
                <el-menu-item index="5" @click="toEvaluationCenter()">
                    <i class="el-icon-edit"></i>
                    <span slot="title">评价中心</span>
                </el-menu-item>
                <el-menu-item index="6" @click="toPersonalCenter()">
                    <i class="el-icon-user-solid"></i>
                    <span slot="title">个人中心</span>
                </el-menu-item>
                <el-menu-item index="7" @click="exitLogIn()">
                    <i class="el-icon-remove"></i>
                    <span slot="title">退出登录</span>
                </el-menu-item>
            </el-menu>
        </div>
        <div v-if="showDeliveryMenu">
            <el-menu
                    default-active="1"
                    class="navigation-menu">
                <el-menu-item index="1" @click="toDashBoard()">
                    <i class="el-icon-star-on"></i>
                    <span slot="title">仪表盘</span>
                </el-menu-item>
                <el-menu-item index="2" @click="toOrderHall()">
                    <i class="el-icon-sold-out"></i>
                    <span slot="title">接单大厅</span>
                </el-menu-item>
                <el-menu-item index="3" @click="toDeliveryOrderList()">
                    <i class="el-icon-s-order"></i>
                    <span slot="title">派送订单</span>
                </el-menu-item>
                <el-menu-item index="5" @click="toEvaluationCenter()">
                    <i class="el-icon-edit"></i>
                    <span slot="title">评价中心</span>
                </el-menu-item>
                <el-menu-item index="6" @click="toPersonalCenter()">
                    <i class="el-icon-user-solid"></i>
                    <span slot="title">个人中心</span>
                </el-menu-item>
                <el-menu-item index="7" @click="exitLogIn()">
                    <i class="el-icon-remove"></i>
                    <span slot="title">退出登录</span>
                </el-menu-item>
            </el-menu>
        </div>
        <div v-if="showManagerMenu">
            <el-menu
                    default-active="1"
                    class="navigation-menu">
                <el-menu-item index="1" @click="toDashBoard()">
                    <i class="el-icon-star-on"></i>
                    <span slot="title">仪表盘</span>
                </el-menu-item>
                <el-menu-item index="2" @click="toOrderManager()">
                    <i class="el-icon-s-order"></i>
                    <span slot="title">订单管理</span>
                </el-menu-item>
                <el-menu-item index="3" @click="toRecycleBin()">
                    <i class="el-icon-delete"></i>
                    <span slot="title">回收站</span>
                </el-menu-item>
                <el-menu-item index="4" @click="toUserManager()">
                    <i class="el-icon-s-custom"></i>
                    <span slot="title">用户管理</span>
                </el-menu-item>
                <el-menu-item index="6" @click="toPersonalCenter()">
                    <i class="el-icon-user-solid"></i>
                    <span slot="title">个人中心</span>
                </el-menu-item>
                <el-menu-item index="7" @click="exitLogIn()">
                    <i class="el-icon-remove"></i>
                    <span slot="title">退出登录</span>
                </el-menu-item>
            </el-menu>
        </div>
    </el-aside>
</template>

<script>
    import {selectOneUserInfo} from "../../request/menu";
    import {logout} from "../../request/user";

    export default {
        name: "Aside",
        data() {
            return {
                loading: false
            }
        },
        methods: {
            exitLogIn() {
                this.loading = true;
                let userInfoEntity = {};
                userInfoEntity.userId = this.getUserId;
                logout(userInfoEntity).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        localStorage.removeItem("token");
                        this.$router.push({
                            name: 'Login'
                        })
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            toDashBoard() {
                switch (this.getUserRoleId) {
                    case 'A':
                        this.$router.push({
                            name: 'ManageDashBoard',
                        })
                        break;
                    case 'B':
                        this.$router.push({
                            name: 'DeliveryDashBoard',
                        })
                        break;
                    case 'C':
                        this.$router.push({
                            name: 'GeneralDashBoard',
                        })
                        break;
                }
            },
            toOrderNow() {
                this.$router.push({
                    name: 'OrderNow',
                })
            },
            toGeneralOrderList() {
                this.$router.push({
                    name: 'GeneralOrderList',
                })
            },
            toDeliveryOrderList() {
                this.$router.push({
                    name: 'DeliveryOrderList',
                })
            },
            toRecycleBin() {
                if (this.getUserRoleId === 'C') {
                    this.$router.push({
                        name: 'RecycleBin',
                    })
                } else if (this.getUserRoleId === 'A') {
                    this.$router.push({
                        name: 'ManagerRecycleBin',
                    })
                }
            },
            toEvaluationCenter() {
                if (this.getUserRoleId === 'C') {
                    this.$router.push({
                        name: 'GeneralEvaluation',
                    })
                } else if (this.getUserRoleId === 'B') {
                    this.$router.push({
                        name: 'DeliveryEvaluation',
                    })
                }
            },
            toPersonalCenter() {
                if (this.getUserRoleId === 'C') {
                    this.$router.push({
                        name: 'PersonalCenter',
                    })
                } else if (this.getUserRoleId === 'B') {
                    this.$router.push({
                        name: 'DeliveryPersonalCenter',
                    })
                } else if (this.getUserRoleId === 'A') {
                    this.$router.push({
                        name: 'ManagerPersonalCenter',
                    })
                }
            },
            toOrderHall() {
                this.$router.push({
                    name: 'OrderHall'
                })
            },
            toOrderManager() {
                this.$router.push({
                    name: 'OrderManager'
                })
            },
            toUserManager() {
                this.$router.push({
                    name: 'UserManager'
                })
            },
            initAjax() {
                this.loading = true;
                selectOneUserInfo().then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        //修改state里面的数据
                        this.$store.commit('updateUserInfo', {
                            'userId': rep.data.userId,
                            'userName': rep.data.userName,
                            'userRoleId': rep.data.userRoleId,
                            'userRoleName': rep.data.userRoleName
                        });
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            }
        },
        computed: {
            getUserRoleId(){
                return this.$store.getters.getUserRoleId;
            },
            getUserRoleName(){
                return this.$store.getters.getUserRoleName;
            },
            getUserId(){
                return this.$store.getters.getUserId;
            },
            showUserRoleMenu() {
                let roleId = this.getUserRoleId;
                let roleName = this.getUserRoleName;
                return roleId || roleName;
            },
            showGeneralMenu() {
                return this.getUserRoleId === 'C';
            },
            showDeliveryMenu() {
                return this.getUserRoleId === 'B';
            },
            showManagerMenu() {
                return this.getUserRoleId === 'A';
            }
        },
        mounted() {
            this.initAjax();
        }
    }
</script>

<style scoped>
    .el-aside {
        color: #333;
        text-align: center;
        border: 0;
    }

    .navigation-menu {
        width: 180px;
        height: 80vm;
    }
</style>