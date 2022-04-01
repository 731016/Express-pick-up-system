<template>
    <el-container>
        <el-header>
            <span class="title-text">校园快递代取系统</span>
            <el-dropdown trigger="click">
                <span class="el-dropdown-link">
                    {{getUserRole}}:{{getUserName}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>个人中心</el-dropdown-item>
                    <el-dropdown-item>退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-header>
    </el-container>
</template>

<script>
    export default {
        name: "Header",
        computed: {
            getUserRole() {
                return this.$store.state.userRole;
            },
            getUserName() {
                return this.$store.state.userName;
            }

        }
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