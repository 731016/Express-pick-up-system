<template>
    <div v-loading.fullscreen.lock="loading">
        <el-row v-if="getUserName">
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
                        可以接单数：{{orderOverview.allowOrderNumber}}，需要派送订单数：{{orderOverview.dispatchOrderNumber}}
                    </div>
                    <DashBoardEcharts :dashBoardData="dashBoardData"></DashBoardEcharts>
                    <div @click="toOrder()">
                        <a href="javascript:;">
                            查看更多
                            <i class="el-icon-d-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </el-col>
            <el-col :span="8">
                <!--                        <div class="grid-content feedback-overview main-bottom">-->
                <!--                            <div class="feedback-overview-top">反馈简览</div>-->
                <!--                            <div class="feedback-overview-middle">-->
                <!--                                正在处理的反馈数：{{feedback.processing}}，未处理的反馈数：{{feedback.notProcessed}}-->
                <!--                            </div>-->
                <!--                            <div>-->
                <!--                                <a href="javascript:;">-->
                <!--                                    查看更多-->
                <!--                                    <i class="el-icon-d-arrow-right"></i>-->
                <!--                                </a>-->
                <!--                            </div>-->
                <!--                        </div>-->
            </el-col>
            <el-col :span="8">
                <div class="grid-content evaluate-overview">
                    <div class="evaluate-overview-top">评价简览</div>
                    <div class="evaluate-overview-middle">
                        您共收到：{{evaluate.receivedAReviewTotal}}条评价，您的综合评分为：{{getRateFraction}}分
                    </div>
                    <RateEcharts :rateData="rateData"></RateEcharts>
                    <div @click="toEvaluate()">
                        <a href="javascript:;">
                            查看更多
                            <i class="el-icon-d-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import {dashBoardDelivery} from '../../request/dashboard';
    import DashBoardEcharts from "../../components/DashBoardEcharts";
    import RateEcharts from "../../components/RateEcharts";

    export default {
        name: "DashBoard",
        components: {
            DashBoardEcharts: DashBoardEcharts,
            RateEcharts:RateEcharts
        },
        data() {
            return {
                loading: true,
                orderOverview: {
                    // 等待接单
                    allowOrderNumber: 0,
                    // 正在派送
                    dispatchOrderNumber: 0
                },
                evaluate: {
                    //收到的评价
                    receivedAReviewTotal: 0,
                    //综合评分
                    overallRating: 0
                },
                dashBoardData: [],
                rateData:[]
            }
        },
        methods: {
            toOrder() {
                this.$router.push({
                    name: 'DeliveryOrderList'
                })
            },
            toEvaluate() {
                this.$router.push({
                    name: 'DeliveryEvaluation'
                })
            },
            init() {
                dashBoardDelivery().then(response => {
                    this.loading = true;
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        let data = rep.data;
                        this.orderOverview.allowOrderNumber = data.allowOrderNumber ? data.allowOrderNumber : 0;
                        this.orderOverview.dispatchOrderNumber = data.dispatchOrderNumber ? data.dispatchOrderNumber : 0;

                        this.dashBoardData.push({value: this.orderOverview.allowOrderNumber, name: '可以接单'});
                        this.dashBoardData.push({value: this.orderOverview.dispatchOrderNumber, name: '需要派送'});

                        this.evaluate.receivedAReviewTotal = data.receivedEvaluateNumber ? data.receivedEvaluateNumber : 0;
                        this.evaluate.overallRating = data.overallRate ? data.overallRate : 0;

                        this.rateData.push({value: this.evaluate.overallRating, name: '综合评分'});
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            }
        },
        computed: {
            getUserName() {
                return this.$store.state.userName;
            },
            getRateFraction() {
                return this.evaluate.overallRating.toFixed(2);
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
        text-align: center;
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