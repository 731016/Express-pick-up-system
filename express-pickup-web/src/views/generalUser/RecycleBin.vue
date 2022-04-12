<template>
    <div>
        <div>
            <el-row :gutter="24">
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
                <el-col :span="2">
                    <div class="grid-content">
                        <span class="search-title">订单号</span>
                    </div>
                </el-col>
                <el-col :span="4">
                    <div class="grid-content bg-purple">
                        <el-input
                                v-model="searchConditions.id"
                                clearable>
                        </el-input>
                    </div>
                </el-col>
                <el-col :span="2">
                    <div class="grid-content">
                        <el-button icon="el-icon-search" circle @click="getFilterData"></el-button>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div class="order-btn">
            <el-button type="warning" round size="mini" @click="recyceOrder()">
                <i class="el-icon-circle-close"></i>
                还原订单
            </el-button>
        </div>
        <div>
            <el-table :cell-style="drawCells"
                      :data="filterData()"
                      ref="tableData"
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
                        prop="paymentInfo.paymentAmount"
                        label="支付金额"
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
                    :total="getRecoverDataTotal()">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import {mapState} from "vuex";
    import mixin from '../../mixin';

    export default {
        name: "RecycleBin",
        mixins: [mixin],
        data() {
            return {
                recycelIds: [],
                searchConditions: {
                    orderStatus: '',
                    id: '',
                    startEndTime: [],
                    currentPage: 1,
                    pageSize: 5,
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
                    //todo axios 修改 参数：【订单id】
                    this.tableData.forEach(item => {
                        if (this.recycelIds.some(ele => ele == item.id)) {
                            item.isDel = 0;
                        }
                    })
                    this.$message({
                        message: '还原成功',
                        type: 'success'
                    });
                    this.recycelIds = [];
                }).catch(() => {

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
                    console.log(isDel)
                    if (isDel == 1) {
                        return 'color:blue';
                    } else if (isDel == -1) {
                        return 'color:#EA6837';
                    }
                }
                if (columnIndex == 6) {
                    let payStatus = row.paymentStatus;
                    if (payStatus == 0) {
                        return 'color:#BD8E3E';
                    } else {
                        return 'color:green';
                    }
                }
                if (columnIndex == 7) {
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
            getFilterData() {
                //todo 获取查询条件，发送ajax
                console.log(this.searchConditions)
                // axios.post('xxx', {
                //     data: this.searchConditions
                // }).then(function (response) {
                //     console.log(response);
                // }).catch(function (error) {
                //     console.log(error);
                // })
            },
            filterData() {
                return this.tableData.filter(item => item.isDel == 1 || item.isDel == -1)
            },
            getRecoverDataTotal() {
                return this.tableData.filter(item => item.isDel == 1 || item.isDel == -1).length
            }
        },
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'}),
        },
        watch: {
            'searchConditions.pageSize': {
                immediate: false, //初始化时加载handler
                deep: true,
                handler(newValue) {
                    console.log("每页大小", newValue);
                    this.getFilterData();
                },
            },
            'searchConditions.currentPage': {
                immediate: false, //初始化时加载handler
                deep: true,
                handler(newValue) {
                    console.log("当前页码", newValue);
                    this.getFilterData();
                },
            }
        }, mounted() {
            this.getFilterData();
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