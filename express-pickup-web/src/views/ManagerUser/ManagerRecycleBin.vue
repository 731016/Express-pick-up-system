<template>
    <div>
        <div>
            <el-row :gutter="10">
                <el-col :span="2">
                    <div class="grid-content">
                        <span class="search-title">订单状态</span>
                    </div>
                </el-col>
                <el-col :span="3">
                    <div class="grid-content">
                        <el-select v-model="searchConditions.orderStatus" placeholder="请选择">
                            <el-option
                                    v-for="item in orderStatusOptions"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                    :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </div>
                </el-col>
                <el-col :span="2">
                    <div class="grid-content">
                        <span class="search-title">支付状态</span>
                    </div>
                </el-col>
                <el-col :span="3">
                    <div class="grid-content">
                        <el-select v-model="searchConditions.paymentStatus" placeholder="请选择">
                            <el-option
                                    v-for="item in paymentStatusOptions"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                    :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </div>
                </el-col>
            </el-row>
            <el-col :gutter="18">
                <el-col :span="2">
                    <div class="grid-content">
                        <span class="search-title">起始时间</span>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="grid-content">
                        <el-date-picker
                                v-model="searchConditions.startEndTime"
                                type="daterange"
                                align="right"
                                unlink-panels
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                :picker-options="pickerOptions">
                        </el-date-picker>
                    </div>
                </el-col>
                <!--                <el-col :span="2">-->
                <!--                    <div class="grid-content">-->
                <!--                        <span class="search-title">订单号</span>-->
                <!--                    </div>-->
                <!--                </el-col>-->
                <!--                <el-col :span="4">-->
                <!--                    <div class="grid-content bg-purple">-->
                <!--                        <el-input-->
                <!--                                v-model="searchConditions.id"-->
                <!--                                clearable>-->
                <!--                        </el-input>-->
                <!--                    </div>-->
                <!--                </el-col>-->
                <el-col :span="2">
                    <div class="grid-content">
                        <el-button icon="el-icon-search" circle @click="initData()"></el-button>
                    </div>
                </el-col>
            </el-col>
        </div>
        <div class="order-btn">
            <el-button type="warning" round size="mini" @click="recyceOrder()">
                <i class="el-icon-refresh"></i>
                还原订单
            </el-button>
        </div>
        <div>
            <el-table :cell-style="drawCells"
                      :data="filterData()"
                      ref="tableData"
                      v-loading="loading"
                      @selection-change="rowChange()"
                      style="width: 100%"
                      max-height="480">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="订单号"
                        width="240">
                </el-table-column>
                <el-table-column
                        prop="delReason"
                        label="删除原因"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="trackNumber"
                        label="快递单号"
                        width="240">
                </el-table-column>
                <el-table-column
                        prop="trackCompanyName"
                        label="快递公司"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="pickupName"
                        label="收件人"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="contactNumber"
                        label="收件电话"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="shipAddress"
                        label="收件地址"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="trackDeliveryAddress"
                        label="快递寄达地址"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        label="下单备注"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="paymentInfo.paymentStatusName"
                        label="支付状态"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="orderStatusName"
                        label="订单状态"
                        width="120">
                </el-table-column>
                <el-table-column
                        sortable
                        prop="createTime"
                        label="下单时间"
                        width="240">
                </el-table-column>
            </el-table>
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
    </div>
</template>

<script>
    import {mapState} from "vuex";
    import mixin from '../../mixin';
    import {recyceOrder} from "../../request/order";
    import {selectAllAndRevokeOrder} from '../../request/managerOrder'

    export default {
        name: "ManagerRecycleBin",
        mixins: [mixin],
        data() {
            return {
                loading: false,
                recycelIds: [],
                searchConditions: {
                    orderStatus: 0,
                    paymentStatus: 0,
                    id: '',
                    startEndTime: [],
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                    totalPage: 5
                },
                tableData: [],
            }
        },
        methods: {
            //还原订单
            recyceOrder() {
                if (this.recycelIds.length <= 0) {
                    this.$message({
                        message: '未选中订单',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('确认还原订单' + this.recycelIds.join(","), '还原操作', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    recyceOrder(this.recycelIds).then(response => {
                        let rep = response.data;
                        if (response.status === 200 && rep.statusCode === 2000) {
                            this.$message.success(rep.message);
                            this.initData();
                            this.recycelIds = [];
                        }
                        this.loading = false;
                    }).catch(error => {
                        this.$message.error(error);
                        this.loading = false;
                    });
                }).catch(() => {
                    this.$message.info('用户取消操作');
                });
            },
            //手机所有被选中的订单的orderNumber
            rowChange() {
                this.recycelIds = [];
                let selectedAllData = this.$refs.tableData.selection;
                let recycelIds = [];
                selectedAllData.forEach(function (item) {
                    recycelIds.push(item.id);
                });
                Object.assign(this.recycelIds, recycelIds);
            },
            drawCells({row, columnIndex}) {
                if (columnIndex == 2) {
                    let isDel = row.isDel;
                    if (isDel == 1) {
                        return 'color:blue';
                    } else if (isDel == -1) {
                        return 'color:#EA6837';
                    }
                }
                if (columnIndex == 10) {
                    let payStatus = row.paymentStatus;
                    if (payStatus == 0) {
                        return 'color:#BD8E3E';
                    } else {
                        return 'color:green';
                    }
                }
                if (columnIndex == 11) {
                    let orderStatus = row.orderStatus;
                    if (orderStatus == 10) {
                        return 'color:#ccc';
                    } else if (orderStatus == 20) {
                        return 'color:#5A4331';
                    } else if (orderStatus == 40) {
                        return 'color:#6B8E23';
                    } else if (orderStatus == 30) {
                        return 'color:red';
                    }
                }
            },
            /**
             * 查询条件
             */
            initData() {
                this.loading = true;
                selectAllAndRevokeOrder(this.searchConditions).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.tableData = JSON.parse(JSON.stringify(rep.dataList));
                        this.updatePage(rep.currentPage, rep.totalPage);
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            filterData() {
                return this.tableData.filter(item => item.isDel == 1 || item.isDel == -1)
            },
            updatePage(currentPage, totalPage) {
                this.searchConditions.currentPage = currentPage;
                this.searchConditions.totalPage = totalPage;
            },
        },
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'}),
            ...mapState({paymentStatusOptions: 'paymentStatusOptions'}),
        },
        mounted() {
            this.initData();
        },
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
        background-color: #f9fafc;
    }

    .search-title {
        font-weight: 700;
    }

    .grid-content {
        line-height: 36px;
        text-align: center;
    }

    .alert-title {
        margin: 10px 0 10px 0;
    }

    .el-pagination {
        float: left;
    }

    .order-btn {
        float: left;
    }

    .range-directions {
        font-size: 18px;
    }

    .range-fraction,
    .range-comment {
        text-align: center;
        font-weight: 700;
    }
</style>