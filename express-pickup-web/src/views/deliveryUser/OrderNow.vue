<template>
    <div>
        <el-form ref="orderInfo" :model="orderInfo" status-icon :rules="rules" @submit.native.prevent>
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content">
                            <span class="info-text">
                                取件姓名
                            </span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content">
                        <el-form-item prop="pickupName">
                            <el-input
                                    placeholder="请输入取件姓名"
                                    v-model.trim="orderInfo.pickupName"
                                    clearable>
                            </el-input>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content">
                <span class="info-text">
                    联系电话
                </span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content">
                        <el-form-item prop="contactNumber">
                            <el-input
                                    placeholder="请输入联系电话"
                                    v-model.trim="orderInfo.contactNumber"
                                    clearable>
                            </el-input>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content">
                <span class="info-text">
                    快递单号
                </span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content">
                        <el-form-item prop="trackNumber">
                            <el-input
                                    placeholder="请输入快递单号"
                                    v-model.trim="orderInfo.trackNumber"
                                    clearable>
                            </el-input>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content">
                <span class="info-text">
                    快递公司
                </span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content">
                        <el-select @change="setTrackCompanyName" v-model="orderInfo.trackCompany" filterable
                                   placeholder="请选择">
                            <el-option
                                    v-for="item in trackCompanys"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                <span style="float: left">{{ item.label }}</span>
                                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
                            </el-option>
                        </el-select>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content">
                <span class="info-text">
                    快递寄达地址
                </span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content">
                        <el-form-item prop="trackDeliveryAddress">
                            <el-input
                                    type="textarea"
                                    maxlength="120"
                                    rows="4"
                                    show-word-limit
                                    placeholder="请输入快递寄达地址"
                                    v-model.trim="orderInfo.trackDeliveryAddress"
                                    clearable>
                            </el-input>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content">
                <span class="info-text">
                    收件地址
                </span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content">
                        <el-form-item prop="shipAddress">
                            <el-input
                                    type="textarea"
                                    maxlength="120"
                                    rows="4"
                                    show-word-limit
                                    placeholder="请输入收件地址"
                                    v-model.trim="orderInfo.shipAddress"
                                    clearable>
                            </el-input>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content ">
                <span class="info-text">
                    备注（可选）
                </span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content ">
                        <el-input
                                type="textarea"
                                maxlength="120"
                                rows="4"
                                show-word-limit
                                placeholder="如有特殊要求，请填写备注"
                                v-model="orderInfo.remark"
                                clearable>
                        </el-input>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content ">
                <span class="info-text">
                    验证码
                </span>
                    </div>
                </el-col>
                <el-col :span="10">
                    <div class="grid-content ">
                        <el-form-item prop="verificationCode">
                            <el-input
                                    placeholder="请输入验证码"
                                    v-model.trim="orderInfo.verificationCode"
                                    clearable>
                            </el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="10">
                    <div class="grid-content ">
                        <el-image
                                style="width: 100px; height: 36px"
                                :src="url" v-model="orderInfo.verificationCode"></el-image>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <div class="grid-content order-sumbit">
                        <el-button type="primary" @click="orderPayment('orderInfo')">下单支付</el-button>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="grid-content order-cancel">
                        <el-button type="info">重新填写</el-button>
                    </div>
                </el-col>
            </el-row>
        </el-form>
    </div>
</template>

<script>
    function verifyVerificationCodeAjax(){
        console.log('发送ajax，验证验证码')
        return true;
    }
    export default {
        name: "OrderNow",
        data() {
            var verifyVerificationCode = (rule, value, callback) => {
                console.log('rule', rule)
                console.log('value', value)
                console.log('callback', callback)
                if (value !== '' && verifyVerificationCodeAjax()) {
                    console.log('验证码验证成功！！！')
                    callback();
                } else {
                    callback(new Error('验证码不能为空'))
                }
            };
            return {
                genVerificationCode: '',
                orderInfo: {
                    pickupName: '',
                    contactNumber: '',
                    trackNumber: '',
                    trackDeliveryAddress: '',
                    shipAddress: '',
                    verificationCode: '',
                    trackCompany: '',
                    trackCompanyName: '',
                    remark: '',
                },
                trackCompanys: [
                    {
                        value: 'STO',
                        label: '申通快递'
                    },
                    {
                        value: 'YTO',
                        label: '圆通快递'
                    },
                    {
                        value: 'YD',
                        label: '韵达快递'
                    },
                    {
                        value: 'YZPY',
                        label: '邮政快递包裹'
                    },
                    {
                        value: 'EMS',
                        label: 'EMS'
                    },
                    {
                        value: 'HHTT',
                        label: '天天快递'
                    },
                    {
                        value: 'JD',
                        label: '京东物流'
                    },
                    {
                        value: 'JT',
                        label: '极兔快递'
                    },
                    {
                        value: 'DBL',
                        label: '德邦物流'
                    },
                    {
                        value: 'ZJS',
                        label: '宅急送'
                    },
                    {
                        value: 'ALKJWL',
                        label: '阿里跨境电商物流'
                    },
                    {
                        value: 'AMAZON',
                        label: '亚马逊物流'
                    },
                    {
                        value: 'BDT',
                        label: '八达通'
                    },
                    {
                        value: 'BTWL',
                        label: '百世快运'
                    }, {
                        value: 'GTO',
                        label: '国通快递'
                    },
                    {
                        value: 'QFKD',
                        label: '全峰快递'
                    },
                    {
                        value: 'RFD',
                        label: '如风达'
                    },
                    {
                        value: 'RRS',
                        label: '日日顺物流'
                    },
                    {
                        value: 'SNWL',
                        label: '苏宁物流'
                    },
                    {
                        value: 'SUBIDA',
                        label: '速必达物流'
                    },
                    {
                        value: 'SDEZ',
                        label: '速递e站'
                    },
                    {
                        value: 'ZTOKY',
                        label: '中通快运'
                    }
                ],
                url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
                rules: {
                    pickupName: [
                        {required: true, message: '取件姓名不能为空', trigger: 'blur'},
                    ],
                    contactNumber: [
                        {required: true, message: '联系电话不能为空', trigger: 'blur'}
                    ],
                    trackNumber: [
                        {required: true, message: '快递单号不能为空', trigger: 'blur'}
                    ],
                    trackDeliveryAddress: [
                        {required: true, message: '快递寄达地址不能为空', trigger: 'blur'}
                    ],
                    shipAddress: [
                        {required: true, message: '收件地址不能为空', trigger: 'blur'}
                    ],
                    verificationCode: [
                        {validator: verifyVerificationCode, trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            orderPayment(orderInfo) {
                this.$refs[orderInfo].validate((valid) => {
                    if (valid) {
                        console.log('submit!');
                        this.$router.push({
                            path: '/ConfirmOrder',
                            query: {
                                orderInfo: this.orderInfo
                            }
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            setTrackCompanyName(val) {
                console.log(val)
                this.trackCompanys.forEach(item => {
                    if (item.value == val) {
                        this.orderInfo.trackCompanyName = item.label;
                    }
                });
            }
        },
        mounted() {
            this.orderInfo.trackCompany = this.trackCompanys[0].value;
            this.setTrackCompanyName(this.orderInfo.trackCompany);
        },
        destroyed() {
            console.log('被销毁')
        }
    }
</script>

<style lang="less" scoped>
    .el-row {
        margin-bottom: 20px;

        &
        :last-child {
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
    }

    .row-bg {
        padding: 10px 0;
        background-color: #f9fafc;
    }

    .info-text {
        font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
        font-weight: 700;
        line-height: 36px;
        text-align: left;
    }

    .el-select {
        width: 100%;
    }

    .el-image {
        float: left;
    }

    .order-sumbit {
        float: right;
    }

    .order-cancel {
        float: left;
    }
</style>