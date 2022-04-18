<template>
    <div class="login-wrap" v-loading.fullscreen.lock="loading">
        <div class="ms-login">
            <div class="ms-title">快递代取系统登录</div>
            <el-form status-icon :model="param" :rules="rules" ref="param" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="param.username" placeholder="账号" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="密码" v-model="param.password"
                              autocomplete="off"
                              @keyup.enter.native="submitForm('param')">
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm('param')">登录</el-button>
                </div>
                <p class="login-tips">
                    <el-link type="warning" style="font-weight: 700" @click="toRegister()">立即注册</el-link>
                </p>
            </el-form>
        </div>
    </div>
</template>

<script>
    import {login} from "../../request/user";

    export default {
        name: "Login",
        data() {
            return {
                loading: false,
                param: {
                    username: '',
                    password: ''
                },
                rules: {
                    username: [{required: true, message: "请输入用户名", trigger: "blur",}],
                    password: [{required: true, message: "请输入密码", trigger: "blur"},],
                },
            }
        },
        computed: {
            getUserId() {
                return this.$store.state.userId;
            }
        },
        methods: {
            submitForm(forName) {
                this.$refs[forName].validate((valid) => {
                    if (valid) {
                        this.checkUserLogin();
                    } else {
                        return false;
                    }
                });
            },
            checkUserLogin() {
                let userInfo = {
                    "userName": this.param.username,
                    "passWord": this.param.password
                }
                this.loading = true;
                login(userInfo).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        localStorage.setItem('token', rep.data);
                        this.$router.push({
                            name: 'Home'
                        })
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            toRegister() {
                this.$router.push({
                    name: 'Register',
                })
            }
        },
    };
</script>

<style scoped>
    .login-wrap {
        position: relative;
        width: 100%;
        height: 100%;
        background-image: url(../../assets/img/login.jpg);
        background-size: 100%;
    }

    .ms-title {
        width: 100%;
        line-height: 50px;
        text-align: center;
        font-size: 20px;
        color: #25b7ef;
        border-bottom: 1px solid #ddd;
    }

    .ms-login {
        position: absolute;
        left: 50%;
        top: 50%;
        width: 350px;
        margin: 175px 0 0 -175px;
        border-radius: 5px;
        background: rgba(255, 255, 255, 0.3);
        overflow: hidden;
    }

    .ms-content {
        padding: 30px 30px;
        background-color: #f7f1f1;
    }

    .login-btn {
        text-align: center;
    }

    .login-btn button {
        width: 100%;
        height: 36px;
        margin-bottom: 10px;
    }

    .login-tips {
        font-size: 12px;
        line-height: 30px;
        color: #fff;
    }
</style>