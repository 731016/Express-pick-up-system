<template>
    <div class="login-wrap" v-loading.fullscreen.lock="loading">
        <div class="ms-login">
            <div class="ms-title">快递代取系统注册</div>
            <el-form status-icon :model="param" :rules="rules" ref="param" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="param.username" placeholder="账号" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="密码" v-model="param.password"
                              autocomplete="off">
                    </el-input>
                </el-form-item>
                <el-form-item prop="confirmPwd">
                    <el-input type="password" placeholder="确认密码" v-model="param.confirmPwd"
                              autocomplete="off">
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm('param')" @keyup.enter="submitForm('param')">注册
                    </el-button>
                </div>
                <p class="login-tips">
                    <el-link type="success" @click="toLogin()">已有账号，立即登录</el-link>
                </p>
            </el-form>
        </div>
    </div>
</template>

<script>
    import {register} from '../../request/user'

    export default {
        name: "Register",
        data() {
            //验证两次密码
            var checkPwd = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.param.confirmPwd !== value) {
                        callback(new Error('两次输入的密码不一致'));
                    }
                    callback();
                }
            };
            return {
                loading: false,
                param: {
                    username: '',
                    password: '',
                    confirmPwd: ''
                },
                rules: {
                    username: [{required: true, message: "请输入用户名", trigger: "blur",}],
                    password: [{required: true, message: "请输入密码", trigger: "blur"},],
                    confirmPwd: [{validator: checkPwd, trigger: 'blur'}]
                },
            }
        },
        methods: {
            submitForm(forName) {
                this.$refs[forName].validate((valid) => {
                    if (valid) {
                        this.checkUserLogin();
                    } else {
                        //验证失败
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
                register(userInfo).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success('注册成功！，即将跳转登录');
                        window.setTimeout(() => {
                            this.loading = false;
                            this.toLogin();
                        }, 1000);
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            toLogin() {
                this.$router.push({
                    name: 'Login',
                })
            }
        },
    }
</script>

<style scoped>
    .login-wrap {
        position: relative;
        width: 100%;
        height: 710px;
        background-image: url(../../assets/img/register2.jpg);
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
        margin: -150px 0 0 -200px;
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