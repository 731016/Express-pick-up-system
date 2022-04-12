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
                                可以接单数：{{orderOverview.canTakeOrders}}，需要派送订单数：{{orderOverview.needToBeDelivered}}
                            </div>
                            <div>
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
                            <div>
                                <a href="javascript:;">
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
    export default {
        name: "DashBoard",
        data() {
            return {
                loading: true,
                orderOverview: {
                    // 可以接单
                    canTakeOrders: 0,
                    // 需要派送
                    needToBeDelivered: 0
                },
                feedback: {
                    // 正在处理的反馈
                    processing: 0,
                    // 未处理的反馈
                    notProcessed: 0
                },
                evaluate: {
                    //收到的评价
                    receivedAReviewTotal: 0,
                    //综合评分
                    overallRating: 0
                }
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
            //todo 发送ajax查询【订单简览，反馈简览，评价简览，根据用户id查询】

            // let userId = this.$store.state.userId;
            //axios
            // this.$axios.post('http://localhost:8090/api/dashboard',).then((response) => {
            //     if (response.status === 200 && response.data.statusCode === 0) {
            //         let data = response.data.data;
            //         this.orderOverview.unPaid = data.unPaid;
            //         this.orderOverview.waitingOrder = data.waitingOrder;
            //         this.orderOverview.dispatchingOrders = data.dispatchingOrders;
            //
            //         this.feedback.feedback = data.feedback;
            //         this.feedback.processing = data.processing;
            //
            //         this.evaluate.receivedAReviewTotal = data.dispatchingOrders;
            //         this.evaluate.overallRating = data.overallRating;
            //         this.loading = false;
            //     }
            // }).catch((error) => {
            //     this.$message.error('连接超时！');
            //     this.loading = false;
            //     console.log(error);
            // });
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