<template>
    <div>
        <el-descriptions title="订单信息" :column="1" direction="horizontal" border v-loading.fullscreen.lock="loading">
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-user font-weight700"></i>
                    姓名
                </template>
                {{confirmOrderInfo.pickupName}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-mobile-phone font-weight700"></i>
                    联系电话
                </template>
                {{confirmOrderInfo.contactNumber}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-document font-weight700"></i>
                    快递单号
                </template>
                {{confirmOrderInfo.trackNumber}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-location font-weight700"></i>
                    快递公司
                </template>
                {{confirmOrderInfo.trackCompanyName}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-office-building font-weight700"></i>
                    快递寄达地址
                </template>
                {{confirmOrderInfo.trackDeliveryAddress}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-location-outline font-weight700"></i>
                    收件地址
                </template>
                {{confirmOrderInfo.shipAddress}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-tickets font-weight700"></i>
                    备注
                </template>
                {{confirmOrderInfo.remark}}
            </el-descriptions-item>
        </el-descriptions>
        <div>
            <span class="info-calculate">价格计算</span>
            <i class="el-icon-warning"></i>
            <el-card shadow="always">
                <i class="weight-min">小于 500g</i>/件：2元；<br>
                <i class="weight-medium">小于 1kg</i>/件：3元；<br>
                <i class="weight-max">大于 1kg</i>/件：4元
            </el-card>
            <el-card shadow="always">
                <i class="price-label">预估价格: </i>
                <span class="price-value">{{computeTotalPrice}}</span>元
            </el-card>
            <div style="overflow: hidden">
                <ul style="list-style: none;">
                    <li v-for="(item,value) in boxList" :key="value">
                        <span>预估重量（单位：KG）</span>
                        <el-input-number :min="0.1" :step="0.5" v-model="item.weight"></el-input-number>
                    </li>
                </ul>
                <el-button type="primary" plain size="mini" @click="addBox()">
                    <i class="el-icon-plus"></i>
                    添加包裹
                </el-button>
                <el-button type="danger" plain size="mini" @click="delBox()" v-show="boxList.length != 0">
                    <i class="el-icon-delete"></i>
                    删除包裹
                </el-button>
            </div>
        </div>
        <el-footer>
            <div class="manner">支付方式</div>
            <el-button type="info" round icon="el-icon-money" @click="confirmPlaceOrder('线下支付')">线下支付</el-button>
            <el-button type="primary" round icon="el-icon-money" disabled>支付宝支付</el-button>
            <el-button type="success" round icon="el-icon-money" disabled>微信支付</el-button>
        </el-footer>
    </div>
</template>

<script>
    import {publishOrder} from '../../request/order'

    export default {
        name: "ConfirmOrder",
        data() {
            return {
                loading: false,
                confirmOrderInfo: {},
                boxWeight: 0,
                boxList: [],
                orderInfo: {
                    pickupName: '',
                    contactNumber: '',
                    trackNumber: '',
                    trackDeliveryAddress: '',
                    shipAddress: '',
                    trackCompanyId: '',
                    trackCompanyName: '',
                    remark: '',
                    paymentInfo: {
                        paymentAmount: 0.00,
                        boxNumber: 0,
                        paymentMethod: '',
                        weightTotal: 0.00,
                    }
                },
            }
        },
        computed: {
            computeTotalPrice: {
                get() {
                    return this.orderInfo.paymentInfo.paymentAmount.toFixed(2);
                },
                set() {

                }
            },
        },
        methods: {
            addBox() {
                let maxLength = this.boxList.length;
                this.boxList.push({number: maxLength, weight: 0})
            },
            delBox() {
                this.boxList.pop();
            },
            initLoad() {
                this.confirmOrderInfo = JSON.parse(this.$route.query.orderInfo);
            },
            confirmPlaceOrder(paymentMethod) {
                if (this.boxList.length <= 0) {
                    this.$message.error('请添加包裹！');
                    return false;
                }
                this.loading = true;
                let obj = this.orderInfo;
                obj.paymentInfo.paymentMethod = paymentMethod;
                publishOrder(obj).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$router.push({
                            name: 'ResultSuccess'
                        });
                        this.loading = false;
                    }
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            }
        },
        mounted() {
            this.initLoad();
        },
        watch: {
            "boxList": {
                deep: true,
                handler() {
                    let sum = 0;
                    let weight = 0.00;
                    this.boxList.forEach(item => {
                        let tmp = item.weight;
                        weight += tmp;
                        if (tmp <= 0.5) {
                            tmp = 2;
                        } else if (tmp > 1) {
                            tmp = 4;
                        } else {
                            tmp = 3;
                        }
                        sum += tmp;
                    })
                    this.orderInfo.paymentInfo.paymentAmount = sum;
                    this.orderInfo.paymentInfo.boxNumber = this.boxList.length;
                    this.orderInfo.paymentInfo.weightTotal = weight;
                }
            },
            "confirmOrderInfo": {
                deep: true,
                handler() {
                    this.orderInfo.pickupName = this.confirmOrderInfo.pickupName;
                    this.orderInfo.contactNumber = this.confirmOrderInfo.contactNumber;
                    this.orderInfo.trackNumber = this.confirmOrderInfo.trackNumber;
                    this.orderInfo.trackDeliveryAddress = this.confirmOrderInfo.trackDeliveryAddress;
                    this.orderInfo.shipAddress = this.confirmOrderInfo.shipAddress;
                    this.orderInfo.trackCompanyId = this.confirmOrderInfo.trackCompanyId;
                    this.orderInfo.trackCompanyName = this.confirmOrderInfo.trackCompanyName;
                    this.orderInfo.remark = this.confirmOrderInfo.remark;
                }
            }
        },
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

    .el-descriptions {
        float: left;
        width: 60%;
        margin-right: 10px;
    }

    .font-weight700 {
        font-weight: 700;
    }

    .el-collapse {
        border: none;
    }

    .info-calculate {
        font-weight: 700;
        color: #F67561;
    }

    .weight-min {
        font-size: 20px;
        font-style: normal;
        color: #A8C023;
    }

    .weight-medium {
        font-size: 20px;
        font-style: normal;
        color: #BD8E3E;
    }

    .weight-max {
        font-size: 20px;
        font-style: normal;
        color: #FC011A;
    }

    .price-label {
        font-style: normal;
        color: #DD5246;
    }

    .price-value {
        font-size: 18px;
        color: #108DDC;
    }

    .manner {
        font-weight: 700;
        font-size: 16px;
        margin-left: -90%;
        margin-bottom: 20px;
    }

    .el-footer {
        position: fixed;
        bottom: 20%;
    }
</style>