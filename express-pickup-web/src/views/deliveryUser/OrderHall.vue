<template>
    <div>
        <div>
            <el-row :gutter="24">
                <el-col :span="2">
                    <div class="grid-content">
                        <span class="search-title font-weight700">起始时间</span>
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
                        <span class="search-title font-weight700">订单号</span>
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
                        <el-button icon="el-icon-search" circle @click="initData()"></el-button>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div class="order-btn">
            <el-button type="success" icon="el-icon-check" round size="mini" @click="bulkOrder()">
                批量接单
            </el-button>
        </div>
        <div>
            <el-table
                    :data="getNotDelData()"
                    v-loading="loading"
                    ref="tableData"
                    @selection-change="rowChange()"
                    style="width: 100%"
                    max-height="680">
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
    import mixin from '../../mixin'
    import {selectAllowAcceptOrder, acceptOrder} from "../../request/deliveryOrder";

    export default {
        name: "OrderHall",
        mixins: [mixin],
        data() {
            return {
                loading: false,
                searchConditions: {
                    orderStatus: '',
                    id: '',
                    startEndTime: [],
                    totalPage: 0,
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                },
                bulkOrders: [],
                tableData: [],
            }
        },
        methods: {
            updatePage(currentPage, totalPage) {
                this.searchConditions.currentPage = currentPage;
                this.searchConditions.totalPage = totalPage;
            },
            /**
             * 查询未接单的订单
             */
            initData() {
                this.loading = true;
                selectAllowAcceptOrder(this.searchConditions).then(response => {
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
            /**
             * 表格被选中的数据发送变化 【选择的订单id】，orderStatus订单状态=‘派送中’20
             */
            rowChange() {
                this.bulkOrders = [];
                let selectedAllData = this.$refs.tableData.selection;
                let bulkOrders = [];
                selectedAllData.forEach(function (item) {
                    bulkOrders.push(item.id);
                });
                Object.assign(this.bulkOrders, bulkOrders);
            },
            /**
             * 修改orderStatus订单状态 = '派送中'20
             */
            bulkOrder() {
                if (this.bulkOrders.length <= 0) {
                    this.$message({
                        message: '未选中订单',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('确认接收订单' + this.bulkOrders.join(","), '撤销操作', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    acceptOrder(this.bulkOrders).then(response => {
                        let rep = response.data;
                        if (response.status === 200 && rep.statusCode === 2000) {
                            this.$message.success(rep.message);
                            this.initData();
                            this.bulkOrders = [];
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
            getNotDelData() {
                if (this.tableData) {
                    return this.tableData.filter(item => item.orderStatus === 10 && item.isDel == 0);
                }
            },
        },
        mounted() {
            this.initData();
        },
    }
</script>

<style scoped>
    .el-row {
        line-height: 36px;
    }

    .font-weight700 {
        font-weight: 700;
    }

    .order-btn {
        float: left;
    }
</style>