<template>
    <div>
        <el-skeleton :loading="loading" animated>
            <template slot="template">
                <el-skeleton-item
                        variant="rect"
                        style="width: 100%; height: 125px;"
                />
                <div style="width: 33%;float: left;margin: 2px">
                    <el-skeleton-item variant="text" class="order-overview-top" style="height: 36px"/>
                    <el-skeleton-item variant="text" class="order-overview-middle" style="height: 36px"/>
                    <el-skeleton-item variant="text" style="height: 36px"/>
                </div>
                <div style="width: 33%;float: left;margin: 2px">
                    <el-skeleton-item variant="text" class="order-overview-top" style="height: 36px"/>
                    <el-skeleton-item variant="text" class="order-overview-middle" style="height: 36px"/>
                    <el-skeleton-item variant="text" style="height: 36px"/>
                </div>
                <div style="width: 33%;float: left;margin: 2px">
                    <el-skeleton-item variant="text" class="order-overview-top" style="height: 36px"/>
                    <el-skeleton-item variant="text" class="order-overview-middle" style="height: 36px"/>
                    <el-skeleton-item variant="text" style="height: 36px"/>
                </div>
            </template>
            <template>
                <el-row>
                    <el-col :span="24">
                        <div class="grid-content bg-purple-light main-top">
                            <span>{{getUserName}}</span>，欢迎登录！
                        </div>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <div class="grid-content order-overview main-middle">
                            <div class="order-overview-top">订单简览</div>
                            <div class="order-overview-middle">
                                今日新增订单数：{{orderOverview.newAddOrders}}，总等待接单数：{{orderOverview.allowOrderNumber}}，正在派送数：{{orderOverview.dispatchOrderNumber}}
                            </div>
                            <div>
                                <a href="javascript:;" @click="toOrderManager()">
                                    查看更多
                                    <i class="el-icon-d-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </el-col>
                    <!--                    <el-col :span="8">-->
                    <!--                        <div class="grid-content feedback-overview main-bottom">-->
                    <!--                            <div class="feedback-overview-top">反馈简览</div>-->
                    <!--                            <div class="feedback-overview-middle">-->
                    <!--                                今日新增反馈数：{{feedback.addProcess}}，等待处理数：{{feedback.notProcessed}}-->
                    <!--                            </div>-->
                    <!--                            <div>-->
                    <!--                                <a href="javascript:;">-->
                    <!--                                    查看更多-->
                    <!--                                    <i class="el-icon-d-arrow-right"></i>-->
                    <!--                                </a>-->
                    <!--                            </div>-->
                    <!--                        </div>-->
                    <!--                    </el-col>-->
                    <el-col :span="8">
                        <div class="grid-content evaluate-overview">
                            <div class="evaluate-overview-top">评价简览</div>
                            <div class="evaluate-overview-middle">
                                今日注册用户数：{{evaluate.newAddRegister}}，总用户数：{{evaluate.userTotal}},其中禁用用户数：{{evaluate.disableUser}},冻结用户数：{{evaluate.freezeUser}}
                            </div>
                            <div>
                                <a href="javascript:;" @click="toUserManager()">
                                    查看更多
                                    <i class="el-icon-d-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </template>
        </el-skeleton>
    </div>
</template>

<script>
    import {dashBoardManager} from '../../request/dashboard'

    export default {
        name: "ManageDashBoard",
        data() {
            return {
                loading: true,
                orderOverview: {
                    // 今日新增订单
                    newAddOrders: 0,
                    // 总等待接单数
                    allowOrderNumber: 0,
                    // 正在派送
                    dispatchOrderNumber: 0
                },
                evaluate: {
                    //今日新增注册
                    newAddRegister: 0,
                    //用户总数
                    userTotal: 0,
                    disableUser: 0,
                    freezeUser: 0
                }
            }
        },
        computed: {
            getUserName() {
                return this.$store.state.userName;
            },
        },
        methods: {
            toUserManager() {
                this.$router.push({
                    name: 'UserManager'
                })
            },
            toOrderManager() {
                this.$router.push({
                    name: 'OrderManager'
                })
            },
            init() {
                dashBoardManager().then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        let data = rep.data;
                        this.orderOverview.newAddOrders = data.newAddOrders == null ? 0 : data.unPaidNumber;
                        this.orderOverview.waitingOrder = data.waitOrderNumber == null ? 0 : data.waitOrderNumber;
                        this.orderOverview.dispatchOrderNumber = data.dispatchOrderNumber == null ? 0 : data.dispatchOrderNumber;

                        this.evaluate.receivedAReviewTotal = data.receivedEvaluateNumber == null ? 0 : data.receivedEvaluateNumber;
                        this.evaluate.overallRating = data.overallRate == null ? 0 : data.overallRate;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            }
        },
        mounted() {
            this.init();
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
    }

    .row-bg {
        padding: 10px 0;
        background-color: #f9fafc;
    }

    .main-top {
        min-height: 125px;
        text-align: center;
        font-size: 32px;
        line-height: 125px;
    }

    .main-top span {
        color: #BC7568;
    }

    .order-overview,
    .feedback-overview,
    .evaluate-overview {
        text-align: left;
    }

    .order-overview a:hover,
    .feedback-overview a:hover,
    .evaluate-overview a:hover {
        color: #409EFF;
    }

    .order-overview-top,
    .feedback-overview-top,
    .evaluate-overview-top {
        font-size: 28px;
    }

    .order-overview-middle,
    .feedback-overview-middle,
    .evaluate-overview-middle {
        margin: 30px 0 30px 0;
    }
</style>