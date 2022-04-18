<template>
    <div v-loading.fullscreen.lock="loading">
        <el-row :gutter="20">
            <el-col :span="6">
                <div class="grid-content grid-title">
                    基础信息
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" :offset="3">
                <div class="grid-content grid-twotitle">
                    用户权限
                </div>
            </el-col>
            <el-col :span="6">
                <div class="grid-content grid-info" v-if="judgePropertyIsNotNull(userInfo.userRoleName) ">
                    {{userInfo.userRoleName}}
                </div>
                <div class="grid-content grid-info-not" v-else>
                    无权限
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" :offset="3">
                <div class="grid-content grid-twotitle">
                    用户名
                </div>
            </el-col>
            <el-col :span="6">
                <div class="grid-content grid-info" v-if="judgePropertyIsNotNull(userInfo.userName)">
                    {{userInfo.userName}}
                </div>
            </el-col>
            <el-col :span="9">
                <div class="grid-content" @click="dialogPwdVisible = true">
                    <el-button type="info" plain class="operate-btn">修改密码</el-button>
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" :offset="3">
                <div class="grid-content grid-twotitle">
                    手机号码
                </div>
            </el-col>
            <el-col :span="6">
                <div class="grid-content grid-info" v-if="judgePropertyIsNotNull(userInfo.phone)">
                    {{userInfo.phone}}
                </div>
                <div class="grid-content grid-info-not" v-else>
                    您未绑定手机号，设置后可以使用手机号登陆，以及使用找回密码功能
                </div>
            </el-col>
            <el-col :span="9">
                <div class="grid-content" v-if="judgePropertyIsNotNull(userInfo.phone)"
                     @click="dialogPhoneVisible = true">
                    <el-button type="info" plain class="operate-btn">更改绑定手机号</el-button>
                </div>
                <div class="grid-content" v-else @click="dialogPhoneVisible = true">
                    <el-button type="info" plain class="operate-btn" style="background-color:#d43f3a;color: #fff">立即绑定
                    </el-button>
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" :offset="3">
                <div class="grid-content grid-twotitle">
                    性别
                </div>
            </el-col>
            <el-col :span="6">
                <div class="grid-content grid-info" v-if="judgePropertyIsNotNull(userInfo.sex)">
                    {{userInfo.sex}}
                </div>
                <div class="grid-content grid-info-not" v-else>
                    未设置
                </div>
            </el-col>
            <el-col :span="9">
                <div class="grid-content" @click="dialogSexVisible = true">
                    <el-button type="info" plain class="operate-btn">修改性别</el-button>
                </div>
            </el-col>
        </el-row>
        <hr>
        <el-row :gutter="20">
            <el-col :span="6">
                <div class="grid-content grid-title">
                    学校信息
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" :offset="3">
                <div class="grid-content grid-twotitle">
                    学校
                </div>
            </el-col>
            <el-col :span="6">
                <div class="grid-content grid-info" v-if="judgePropertyIsNotNull(userInfo.schoolName)">
                    {{userInfo.schoolName}}
                </div>
                <div class="grid-content grid-info-not" v-else>
                    未设置
                </div>
            </el-col>
            <el-col :span="9">
                <div class="grid-content"
                     v-if="judgePropertyIsNotNull(userInfo.schoolName) || judgePropertyIsNotNull(userInfo.schoolNumber)"
                     @click="dialogSchoolVisible = true">
                    <el-button type="info" plain class="operate-btn">更改绑定</el-button>
                </div>
                <div class="grid-content" v-else @click="dialogSchoolVisible = true">
                    <el-button type="info" plain class="operate-btn" style="background-color:#d43f3a;color: #fff">
                        立即绑定
                    </el-button>
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" :offset="3">
                <div class="grid-content grid-twotitle">
                    学号
                </div>
            </el-col>
            <el-col :span="6">
                <div class="grid-content grid-info" v-if="judgePropertyIsNotNull(userInfo.schoolNumber)">
                    {{userInfo.schoolNumber}}
                </div>
                <div class="grid-content grid-info-not" v-else>
                    未设置
                </div>
            </el-col>
        </el-row>
        <hr>
        <el-row :gutter="20">
            <el-col :span="6">
                <div class="grid-content grid-title">
                    实名信息
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" :offset="3">
                <div class="grid-content grid-twotitle">
                    姓名
                </div>
            </el-col>
            <el-col :span="6">
                <div class="grid-content grid-info" v-if="judgePropertyIsNotNull(userInfo.actualName)">
                    {{userInfo.actualName}}
                </div>
                <div class="grid-content grid-info-not" v-else>
                    未实名，实名后将能够申请配送员
                </div>
            </el-col>
            <el-col :span="9">
                <div class="grid-content"
                     v-if="!judgePropertyIsNotNull(userInfo.actualName) || !judgePropertyIsNotNull(userInfo.idNumber)"
                     @click="dialogIdCardVisible = true">
                    <el-button type="info" plain style="background-color:#d43f3a;color: #fff">实名认证</el-button>
                </div>
                <!--                <div class="grid-content" v-else @click="dialogIdCardVisible = true">-->
                <!--                    <el-button type="info" plain>实名认证</el-button>-->
                <!--                </div>-->
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" :offset="3">
                <div class="grid-content grid-twotitle">
                    身份证号
                </div>
            </el-col>
            <el-col :span="6">
                <div class="grid-content grid-info" v-if="judgePropertyIsNotNull(userInfo.idNumber)">
                    {{userInfo.idNumber}}
                </div>
                <div class="grid-content grid-info-not" v-else>
                    未设置
                </div>
            </el-col>
        </el-row>
        <div>
            <el-alert
                    type="error"
                    show-icon>
                <div>
                    <h3>切换配送员</h3>
                    <div>
                        成为配送员后您将可以接单，并且进行快递配送，与此同时您将丧失下单的权利。请确保已经完成实名认证，且不存在未完成的订单。
                    </div>
                    <el-button type="danger"
                               :disabled="!judgePropertyIsNotNull(userInfo.actualName) || !judgePropertyIsNotNull(userInfo.idNumber)"
                               size="small" @click="toggleUserRole()">立即切换
                    </el-button>
                </div>
            </el-alert>
        </div>
        <el-dialog title="修改密码" :visible.sync="dialogPwdVisible">
            <el-form :model="pwd" status-icon :rules="rules" ref="pwd" label-width="100px">
                <el-form-item label="原始密码" prop="oldPwd">
                    <el-input v-model="pwd.oldPwd" autocomplete="off" show-password></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPwd">
                    <el-input v-model="pwd.newPwd" autocomplete="off" show-password></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="passWord">
                    <el-input v-model="pwd.passWord" autocomplete="off" show-password></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('pwd')">确 定</el-button>
                <el-button @click="resetForm('pwd')">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog title="设置手机号" :visible.sync="dialogPhoneVisible">
            <el-form :model="phone" status-icon :rules="rules" ref="phone" label-width="100px">
                <el-form-item label="手机号码" prop="phone">
                    <el-input v-model="phone.phone" autocomplete="off"></el-input>
                </el-form-item>
                <!--                <el-form-item label="验证码" prop="verificationCode">-->
                <!--                    <el-input style="width: 70%;" v-model="phone.verificationCode" autocomplete="off"></el-input>-->
                <!--                    <el-button style="width: 20%;margin-left: 2%" type="primary" :disabled="verificationCodeBtnStatus"-->
                <!--                               @click="sendVerificationCode()">-->
                <!--                        <span v-if="verificationCodeBtnTime != 0" v-text="verificationCodeBtnTime+'秒'"></span>-->
                <!--                        <span v-else>发送验证码</span>-->
                <!--                    </el-button>-->
                <!--                </el-form-item>-->
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('phone')">确 定</el-button>
                <el-button @click="resetForm('phone')">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog title="修改性别" status-icon :visible.sync="dialogSexVisible">
            <el-form status-icon label-width="100px">
                <el-form-item label="性别">
                    <el-radio-group v-model="sex" size="mini">
                        <el-radio-button label="男" value="1"></el-radio-button>
                        <el-radio-button label="女" value="0"></el-radio-button>
                        <el-radio-button label="保密" value="-1"></el-radio-button>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="commitSex()">确 定</el-button>
                <el-button @click="cancelSex()">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog title="设置学校信息" :visible.sync="dialogSchoolVisible">
            <el-form :model="school" status-icon :rules="rules" ref="school" label-width="100px">
                <el-form-item label="学校" prop="province">
                    <el-select v-model="school.province" placeholder="请选择省份" @change="provinceChange()">
                        <el-option
                                v-for="item in provinces"
                                :key="item.id"
                                :label="item.provinceName"
                                :value="item.id"
                                :disabled="item.disabled">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="schoolName">
                    <el-select v-model="school.schoolName" placeholder="请选择学校"
                               no-data-text="请先选择省份"
                               @change="schoolChange()">
                        <el-option
                                v-for="item in filterSchool"
                                :key="item.id"
                                :label="item.schoolName"
                                :value="item.id"
                                :disabled="item.disabled">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="学号" prop="schoolNumber">
                    <el-input style="width: 70%;" v-model="school.schoolNumber" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('school')">确 定</el-button>
                <el-button @click="resetForm('school')">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog title="实名认证" :visible.sync="dialogIdCardVisible">
            <el-form :model="idCard" status-icon :rules="rules" ref="idCard" label-width="100px">
                <el-form-item label="姓名" prop="actualName">
                    <el-input v-model="idCard.actualName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="身份证号" prop="idNumber">
                    <el-input v-model="idCard.idNumber" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('idCard')">确 定</el-button>
                <el-button @click="resetForm('idCard')">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog title="信息" :visible.sync="dialogExchangeUserOuterVisible">
            此操作具有一定危险性，即将校验密码，如未设置密码，请先设置密码。
            <el-dialog
                    width="30%"
                    title="输入密码"
                    :visible.sync="dialogExchangeUserInnerVisible"
                    append-to-body>
                <el-input type="password" v-model="exchangeUserPwd" placeholder="请输入密码"></el-input>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogExchangeUserInnerVisible = false">取 消</el-button>
                    <el-button type="primary" @click="closeAll()">确 定</el-button>
                </div>
            </el-dialog>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogExchangeUserOuterVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogExchangeUserInnerVisible = true">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {
        queryUserInfo,
        updatePwd,
        updatePhoneAjax,
        updateSexAjax,
        updateSchoolInfo,
        updateIdCard,
        verifyPwdAndChange,
    } from '../../request/user';
    import {selectAllProvince, selectOneSchool} from '../../request/selectInfo'

    export default {
        name: "PersonalCenter",
        data() {
            //验证旧密码
            var checknewPwd = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.pwd.oldPwd !== '') {
                        this.$refs.pwd.validateField('oldPwd');
                    }
                    callback();
                }
            };
            //验证两次密码
            var checkConfirmPwd = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.pwd.newPwd !== value) {
                        callback(new Error('两次输入的密码不一致'));
                    }
                    callback();
                }
            };
            //验证手机号
            var checkPhone = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入手机号'));
                } else {
                    let regExp = /^[1][3,4,5,7,8,9][0-9]{9}$/;
                    if (regExp.test(value)) {
                        callback();
                    } else {
                        callback(new Error('手机号不合法'));
                    }
                }
            };
            //检查验证码
            var checkCode = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入验证码'));
                } else {
                    // todo ajax发送验证后台验证码
                    // if(true){
                    //     callback();
                    // }else{
                    //     callback(new Error('验证码错误'));
                    // }
                    callback();
                }
            };
            //检查身份证号码
            var checkIdCard = (rule, value, callback) => {
                if (value == '') {
                    callback(new Error('请输入身份证号码'));
                } else {
                    const idcardReg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
                    if (idcardReg.test(value)) {
                        //todo 发送ajax
                        callback();
                    } else {
                        callback(new Error('身份证号码格式不正确'));
                    }
                }
            };
            return {
                loading: true,
                //密码
                dialogPwdVisible: false,
                //手机号
                dialogPhoneVisible: false,
                //验证码禁用状态
                verificationCodeBtnStatus: false,
                verificationCodeBtnTime: 0,
                //性别
                dialogSexVisible: false,
                //学校
                dialogSchoolVisible: false,
                //实名认证
                dialogIdCardVisible: false,
                dialogExchangeUserInnerVisible: false,
                dialogExchangeUserOuterVisible: false,
                userInfo: {
                    userRoleName: '',
                    userName: '',
                    phone: '',
                    sex: '',
                    schoolName: '',
                    schoolNumber: '',
                    actualName: '',
                    idNumber: ''
                },
                pwd: {
                    oldPwd: '',
                    newPwd: '',
                    passWord: ''
                },
                exchangeUserPwd: '',
                phone: {
                    phone: '',
                    verificationCode: ''
                },
                sex: '',
                school: {
                    province: '',
                    schoolName: '',
                    schoolNumber: '',
                },
                provinces:
                    [],
                schools:
                    [],
                idCard: {
                    actualName: '',
                    idNumber: ''
                },
                rules: {
                    oldPwd: [{required: true, message: "请输入密码", trigger: 'blur'}],
                    newPwd: [{validator: checknewPwd, trigger: 'blur'}],
                    passWord: [{validator: checkConfirmPwd, trigger: 'blur'}],
                    phone: [{validator: checkPhone, trigger: 'blur'}],
                    verificationCode: [{validator: checkCode, trigger: 'blur'}],
                    province: [{required: true, message: "请选择省份", trigger: 'blur'}],
                    schoolName: [{required: true, message: "请选择学校", trigger: 'blur'}],
                    schoolNumber: [{required: true, message: "请输入学号", trigger: 'blur'}],
                    actualName: [{required: true, message: "请输入姓名", trigger: 'blur'}],
                    idNumber: [{validator: checkIdCard, trigger: 'blur'}]
                }
            }
        },
        methods: {
            sendVerificationCode() {
                // todo ajax向后台请求发送验证码，根据用户id
                this.verificationCodeBtnStatus = true;
                this.verificationCodeBtnTime = 30;
                let codeTime = window.setInterval(() => {
                    console.log('定时器执行')
                    if (this.verificationCodeBtnTime <= 0) {
                        window.clearInterval(codeTime);
                        this.verificationCodeBtnTime = 0;
                        this.verificationCodeBtnStatus = false;
                    } else {
                        this.verificationCodeBtnTime -= 1;
                    }
                }, 1000)
            },
            commitSex() {
                this.loading = true;
                let sex = {"sex": this.sex};
                updateSexAjax(sex).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        this.userInfo.sex = this.sex;
                        this.dialogSexVisible = false;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
            },
            cancelSex() {
                this.dialogSexVisible = false;
            },
            submitForm(forName) {
                this.$refs[forName].validate((valid) => {
                    if (valid) {
                        switch (forName) {
                            case 'pwd':
                                this.updatePassword();
                                break;
                            case 'phone':
                                this.updatePhone();
                                break;
                            case 'school':
                                this.updateSchool();
                                break;
                            case 'idCard':
                                this.updateIdCard();
                                break;
                        }
                        this.dialogPwdVisible = false
                        this.dialogPhoneVisible = false
                        this.dialogSchoolVisible = false
                        this.dialogIdCardVisible = false
                    } else {
                        return false;
                    }
                });
            },
            updatePassword() {
                this.loading = true;
                updatePwd(this.pwd).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        setTimeout(() => {
                            this.$message.info('即将重新登录');
                            this.$router.push({
                                name: 'Login'
                            })
                        }, 2000)
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
                this.pwd.newPwd = '';
                this.pwd.oldPwd = '';
                this.pwd.passWord = '';
            },
            updatePhone() {
                this.loading = true;
                updatePhoneAjax(this.phone).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        this.userInfo.phone = this.phone.phone;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
            },
            updateSchool() {
                this.loading = true;
                updateSchoolInfo(this.school).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        this.userInfo.schoolName = rep.data.schoolName;
                        this.userInfo.schoolNumber = rep.data.schoolNumber;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });

            },
            updateIdCard() {
                this.loading = true;
                updateIdCard(this.idCard).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        this.userInfo.actualName = rep.data.actualName;
                        this.userInfo.idNumber = rep.data.idNumber;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
            },
            resetForm(forName) {
                this.dialogPwdVisible = false
                this.dialogPhoneVisible = false
                this.dialogSchoolVisible = false
                this.dialogIdCardVisible = false
                this.$refs[forName].resetFields();
            },
            senAjaxSearchUserInfo() {
                this.loading = true;
                queryUserInfo().then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.userInfo = rep.data;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
            },
            provinceChange() {
                this.loading = true;
                selectOneSchool({"provinceId": this.school.province}).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        // this.$message.success(rep.message);
                        this.schools = rep.data;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
                // this.userInfo.province = this.school.province;
            },
            schoolChange() {
                // this.userInfo.schoolName = this.school.schoolName
            },
            //切换用户角色
            toggleUserRole() {
                this.dialogExchangeUserOuterVisible = true;
            },
            closeAll() {
                this.loading = true;
                verifyPwdAndChange({"passWord": this.exchangeUserPwd}).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.dialogExchangeUserInnerVisible = false;
                        this.dialogExchangeUserOuterVisible = false;
                        this.$message.success(rep.message);
                        localStorage.removeItem("token");
                        //重新登录，进入登录页
                        setTimeout(() => {
                            this.$router.push({
                                name: 'Login',
                            })
                        }, 2000);
                    }
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
            },
            loadProvince() {
                this.loading = true;
                selectAllProvince().then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        // this.$message.success(rep.message);
                        this.provinces = rep.data;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
            },
            judgePropertyIsNotNull(param) {
                return param ? true : false;
            }
        },
        computed: {
            filterSchool() {
                return this.schools.filter(item => item.provinceId == this.school.province);
            },
        },
        mounted() {
            this.senAjaxSearchUserInfo();
            this.loadProvince();
        }
    }
</script>

<style lang="less" scoped>
    .el-row {
        margin-bottom: 20px;

        &:last-child {
            margin-bottom: 0;
        }
    }

    .el-col {
        border-radius: 4px;
    }

    .bg-purple-dark {
        background: #99a9bf;
    }

    .bg-purple {
        background: #d3dce6;
    }

    .bg-purple-light {
        background: #e5e9f2;
    }

    .grid-content {
        border-radius: 4px;
        min-height: 36px;
        text-align: center;
        line-height: 36px;
    }

    .grid-title {
        font-size: 24px;
    }

    .grid-twotitle {
        font-weight: 700;
    }

    .grid-info {
        font-weight: 400;
    }

    .grid-info-not {
        font-weight: 400;
        color: #ccc;
    }

    .row-bg {
        padding: 10px 0;
        background-color: #f9fafc;
    }

    .operate-btn {
        width: 50%;
    }
</style>