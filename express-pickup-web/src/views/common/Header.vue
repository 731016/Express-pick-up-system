<template>
    <el-container v-loading.fullscreen.lock="loading">
        <el-header>
            <span class="title-text">校园快递代取系统</span>
            <el-dropdown trigger="click" v-if="getUserId">
                <span class="el-dropdown-link">
                    {{getUserRoleName}}:{{getUserName}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="toPersonalCenter()">个人中心</el-dropdown-item>
                    <el-dropdown-item @click.native="exitLogIn()">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-header>
    </el-container>
</template>

<script>
    import {logout} from "../../request/user";

    export default {
        name: "Header",
        data() {
            return {
                loading: false
            }
        },
        computed: {
            userRoleId() {
                return this.$store.state.userRoleId
            },
            getUserName() {
                return this.$store.state.userName;
            },
            getUserId() {
                return this.$store.state.userId;
            },
            getUserRoleName() {
                return this.$store.state.userRoleName;
            }
        },
        methods: {
            exitLogIn() {
                this.loading = true;
                logout({"userId": this.getUserId}).then(response => {
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
            toPersonalCenter() {
                if (this.userRoleId === 'C') {
                    this.$router.push({
                        name: 'PersonalCenter',
                    })
                } else if (this.userRoleId === 'B') {
                    this.$router.push({
                        name: 'DeliveryPersonalCenter',
                    })
                } else if (this.userRoleId === 'A') {
                    this.$router.push({
                        name: 'ManagerPersonalCenter',
                    })
                }
            }
        },
    }
</script>

<style scoped>
    .el-header {
        background-color: #B3C0D1;
        color: #777;
        background-color: #e7e7e7;
        text-align: left;
        line-height: 60px;
        margin-bottom: 20px;
    }

    .title-text {
        font-size: 25px;
    }

    .el-dropdown {
        float: right;
    }

    .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
    }

    .el-icon-arrow-down {
        font-size: 12px;
    }
</style>