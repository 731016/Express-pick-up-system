<template>
    <div>
        <el-row v-if="getRating != 0">
            <el-col :span="24">
                <div class="grid-content ">
                    <el-rate v-if="getRating != 0"
                             v-model="getRating"
                             disabled
                             show-score
                             :max="10"
                             text-color="#F0A732"
                             :colors="['#DD5145','#F0A732','#1EA362']"
                             score-template="{value} 分">
                    </el-rate>
                </div>
            </el-col>
        </el-row>
        <el-row v-else>
            <el-col :span="24">
                <div class="grid-content ">
                    <el-rate
                            v-bind="getRating"
                            disabled
                            show-score
                            :max="10"
                            text-color="#F0A732"
                            :colors="['#DD5145','#F0A732','#1EA362']"
                            score-template="无评分">
                    </el-rate>
                </div>
            </el-col>
        </el-row>
        <el-row>
            <div v-for="item in commitList" :key="item.id">
                <el-col :span="8">
                    <div class="grid-content">
                        <el-tag>
                            来自订单
                            <span style="color: #BC7568">{{item.orderNumber}}</span>
                            的评价：
                        </el-tag>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="grid-content">
                        <el-tag type="success" class="el-tag-text">
                            {{item.rangeInfo.comment}}
                        </el-tag>
                    </div>
                </el-col>
                <el-col :span="4">
                    <div class="grid-content">
                        <el-tag type="danger" style="line-height: 70px;font-weight: 700;font-size: 28px;">
                            {{item.rangeInfo.userRatings}}
                        </el-tag>
                    </div>
                </el-col>
            </div>
        </el-row>
        <div v-if="commitList.length != 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :hide-on-single-page="false"
                    :current-page="currentPage"
                    :page-sizes="[20, 50, 100]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="getRateCommitTotal">
            </el-pagination>
        </div>
        <div v-else>
            <el-empty description="无数据"></el-empty>
        </div>
    </div>
</template>

<script>
    import mixin from '../../mixin';
    import {evaluateOrder} from "../../request/order";

    export default {
        name: "EvaluationCenter",
        mixins: [mixin],
        data() {
            return {
                //当前页码
                currentPage: 1,
                pageSize: 5,
                value: 6,
                commitList: [
                      {
                          'orderNumber':'3424543564',
                          'rangeInfo':{
                              'comment':'上的起飞前',
                              'userRatings':'8.65',
                          }
                      },
                ]
            }
        },
        methods: {
            getPageComment() {
                this.loading = true;
                evaluateOrder(this.searchConditions).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.commitList = JSON.parse(JSON.stringify(rep.dataList));
                        this.updatePage(rep.currentPage, rep.totalPage);
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
                //todo 根据用户id获取所有的评价信息 Map<orderNumber,rangInfo>
                this.commentList = '';
            },
            updatePage(currentPage, totalPage) {
                this.currentPage = currentPage;
                this.totalPage = totalPage;
                // this.value = value;
            },
        },
        computed: {
            //获取评价总数
            getRateCommitTotal() {
                // return this.commentMap.filter(item => item.rangeInfo.completeEvaluationFlag != 0).size;
                return 0;
            },
            //获取综合评分
            getRating() {
                let rates = 0;
                // this.commentMap.filter(item => item.rangeInfo.completeEvaluationFlag != 0).values().forEach(item => {
                //     rates += item.userRatings;
                // })
                return rates;
            },
            getFilterData() {
                return this.tableData.filter(item => item.rangeInfo.completeEvaluationFlag != 0)
            },

        },
        mounted() {
            this.getPageComment();
            // let commentByOrderNumber = [];
            // let commentByEvaluationFlagNotEmpty = this.tableData.filter(item => item.rangeInfo.completeEvaluationFlag != 0);
            // commentByEvaluationFlagNotEmpty.forEach(item => {
            //     let obj = new Object();
            //     obj.orderId = item.id;
            //     obj.rangeInfo = item.rangeInfo;
            //     commentByOrderNumber.push(obj);
            // })
            // this.commitList = commentByOrderNumber
        },
    }
</script>

<style scoped>
    /deep/ .el-rate__icon {
        font-size: 45px;
    }

    .el-rate {
        margin-bottom: 3%;
    }

    .el-tag {
        width: 100%;
        height: 70px;
        white-space: pre-wrap;
    }

    .el-tag-text {
        overflow: auto;
        text-align: left;
    }
</style>