<template>
    <el-aside style="width: 210px">
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
            <el-menu-item index="3" @click="toOrderHall()">
                <i class="el-icon-sold-out"></i>
                <span slot="title">接单大厅</span>
            </el-menu-item>
            <el-menu-item index="4" @click="toOrderNow()">
                <i class="el-icon-sold-out"></i>
                <span slot="title">立即下单</span>
            </el-menu-item>
            <el-menu-item index="5" @click="toDeliveryOrderList()">
                <i class="el-icon-s-order"></i>
                <span slot="title">我的订单</span>
            </el-menu-item>
            <el-menu-item index="6" @click="toGeneralOrderList()">
                <i class="el-icon-s-order"></i>
                <span slot="title">派送订单</span>
            </el-menu-item>
            <el-menu-item index="7" @click="toRecycleBin()">
                <i class="el-icon-delete"></i>
                <span slot="title">回收站</span>
            </el-menu-item>
            <el-menu-item index="8" @click="toUserManager()">
                <i class="el-icon-s-custom"></i>
                <span slot="title">用户管理</span>
            </el-menu-item>
            <el-menu-item index="9">
                <i class="el-icon-s-comment"></i>
                <span slot="title">反馈建议</span>
            </el-menu-item>
            <el-menu-item index="10" @click="toEvaluationCenter()">
                <i class="el-icon-edit"></i>
                <span slot="title">评价中心</span>
            </el-menu-item>
            <el-menu-item index="11" @click="toPersonalCenter()">
                <i class="el-icon-user-solid"></i>
                <span slot="title">个人中心</span>
            </el-menu-item>
            <el-menu-item index="12">
                <i class="el-icon-remove"></i>
                <span slot="title">退出登录</span>
            </el-menu-item>
        </el-menu>
    </el-aside>
</template>

<script>
    import {mapGetters} from 'vuex'

    export default {
        name: "Aside",
        methods: {
            toDashBoard() {
                if (this.getUserRole == '管理员') {
                    this.$router.push({
                        name: 'ManageDashBoard',
                    })
                } else {
                    //参数和to里面的
                    this.$router.push({
                        name: 'DashBoard',
                    })
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
                if (this.getUserRole === '普通用户') {
                    this.$router.push({
                        name: 'RecycleBin',
                    })
                } else if (this.getUserRole === '配送员') {
                    this.$router.push({
                        name: 'RecycleBin',
                    })
                } else if (this.getUserRole === '管理员') {
                    this.$router.push({
                        name: 'ManagerRecycleBin',
                    })
                }
            },
            toEvaluationCenter() {
                this.$router.push({
                    name: 'EvaluationCenter',
                })
            },
            toPersonalCenter() {
                if (this.getUserRole === '普通用户') {
                    this.$router.push({
                        name: 'PersonalCenter',
                    })
                } else if (this.getUserRole === '配送员') {
                    this.$router.push({
                        name: 'GeneralPersonalCenter',
                    })
                } else if (this.getUserRole === '管理员') {
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
            }
        },
        computed: {
            ...mapGetters({getUserRole: 'getUserRole'})
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
    }
</style>