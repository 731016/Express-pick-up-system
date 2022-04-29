<template>
    <div v-loading.fullscreen.lock="loading">
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
            <div v-for="item in commitMap" :key="item.orderId">
                <el-col :span="8">
                    <div class="grid-content">
                        <el-tag>
                            来自订单
                            <span style="color: #BC7568">{{item.orderId}}</span>
                            的评价：
                        </el-tag>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="grid-content">
                        <el-tag type="success" class="el-tag-text">
                            {{item.comment}}
                        </el-tag>
                    </div>
                </el-col>
                <el-col :span="4">
                    <div class="grid-content">
                        <el-tag type="danger" style="line-height: 70px;font-weight: 700;font-size: 28px;">
                            {{getUserRating(item.userRating)}}
                        </el-tag>
                    </div>
                </el-col>
            </div>
        </el-row>
        <div v-if="showPage">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :hide-on-single-page="false"
                    :current-page="searchConditions.currentPage"
                    :page-sizes="[5, 20, 50]"
                    :page-size="searchConditions.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="searchConditions.totalPage">
            </el-pagination>
        </div>
        <div v-else>
            <el-empty description="无数据"></el-empty>
        </div>
    </div>
</template>

<script>
    import mixin from '../../mixin';
    import {selectDeliveryComment, queryDeliveryRate} from "../../request/deliveryOrder";

    export default {
        name: "EvaluationCenter",
        mixins: [mixin],
        data() {
            return {
                loading: false,
                searchConditions: {
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                    totalPage: 0,
                },
                value: 0,
                commitMap: []
            }
        },
        methods: {
            initData() {
                this.loading = true;
                selectDeliveryComment(this.searchConditions).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.commitMap = JSON.parse(JSON.stringify(rep.dataList));
                        this.updatePage(rep.currentPage, rep.totalPage);
                        this.commitMap.push({});
                        this.loading = false;
                    }
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
                queryDeliveryRate().then(response => {
                    this.loading = true;
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.value = rep.data;
                        this.loading = false;
                    }
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            updatePage(currentPage, totalPage) {
                this.searchConditions.currentPage = currentPage;
                this.searchConditions.totalPage = totalPage;
            },
            getUserRating(userRating) {
                return userRating.toFixed(2);
            },
        },
        computed: {
            //获取综合评分
            getRating() {
                let rates = this.value;
                return rates;
            },
            showPage() {
                return this.commitMap ? (this.commitMap.length !== 0) : false;
            }
        },
        mounted() {
            this.initData();
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