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
                <el-col :span="2">
                    <div class="grid-content">
                        <span class="search-title">订单号</span>
                    </div>
                </el-col>
                <el-col :span="4">
                    <div class="grid-content bg-purple">
                        <el-input
                                v-model="searchConditions.orderNumber"
                                clearable>
                        </el-input>
                    </div>
                </el-col>
                <el-col :span="2">
                    <div class="grid-content">
                        <el-button icon="el-icon-search" circle @click="getFilterData"></el-button>
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
                      :data="filterData"
                      ref="tableData"
                      @selection-change="rowChange()"
                      style="width: 100%"
                      max-height="480">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="orderNumber"
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
                        prop="paymentInfo.paymentStatus_cnName"
                        label="支付状态"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="orderStatus_cnName"
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
                    :total="getRecoverDataTotal">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import {mapState} from "vuex";
    import mixin from '../../mixin';

    export default {
        name: "ManagerRecycleBin",
        mixins: [mixin],
        data() {
            return {
                recycelIds: [],
                searchConditions: {
                    orderStatus: '',
                    paymentStatus: '',
                    orderNumber: '',
                    startEndTime: [],
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                },
                tableData: [
                    {
                        id: '45665765765-2432423-234324-5435345',
                        orderStep: 3,
                        // 订单号
                        orderNumber: '444444444',
                        // 收件姓名
                        pickupName: '涂鏊飞',
                        // 收件短信
                        contactNumber: '17685585594',
                        // 寄达地址
                        trackDeliveryAddress: '武汉市xxx',
                        // 收件地址
                        shipAddress: '碧海花园',
                        // 快递单号
                        trackNumber: '454375698709-09-90-890',
                        // 快递公司
                        trackCompany: 'JD',
                        trackCompanyName: '京东',
                        //配送员id
                        deliveryManId: '4923853458943',
                        // 配送员
                        deliveryMan: '色色色',
                        // 配送备注
                        shippingReamrk: '已送达',
                        // 备注
                        remark: '放到菜鸟驿站',
                        //是否删除【1撤销，0未删除,-1删除】
                        isDel: -1,
                        //删除原因
                        delReason: '手动删除',
                        // 订单状态
                        orderStatus: 40,
                        orderStatus_cnName: '订单完成',
                        createTime: '2022/3/14 13:00:45',
                        rangeInfo: {
                            id: '4-4534',
                            userRatings: 7.8,
                            comment: '一款具有口语',
                            completeEvaluationFlag: 0
                        },
                        paymentInfo: {
                            id: '4-433',
                            // 支付方式
                            paymentMethod: '支付宝',
                            // 流水号
                            serialNumber: '3346587897967568689797',
                            // 支付金额
                            paymentAmount: 42.0,
                            // 支付状态
                            paymentStatus: 1,
                            paymentStatus_cnName: '支付成功',
                        },
                    },
                    {
                        id: '65654645-435-45646-54654-7574',
                        orderStep: 3,
                        // 订单号
                        orderNumber: '666666666666666',
                        // 收件姓名
                        pickupName: '涂鏊飞',
                        // 收件短信
                        contactNumber: '17685585594',
                        // 寄达地址
                        trackDeliveryAddress: '武汉市xxx',
                        // 收件地址
                        shipAddress: '碧海花园',
                        // 快递单号
                        trackNumber: '454375698709-09-90-890',
                        // 快递公司
                        trackCompany: 'JD',
                        trackCompanyName: '京东',
                        //配送员id
                        deliveryManId: '4923853458943',
                        // 配送员
                        deliveryMan: '色色色',
                        // 配送备注
                        shippingReamrk: '',
                        // 备注
                        remark: '放到菜鸟驿站',
                        //是否删除【1撤销，0未删除,-1删除】
                        isDel: 0,
                        //删除原因
                        delReason: '',
                        // 订单状态
                        orderStatus: 30,
                        orderStatus_cnName: '订单异常',
                        createTime: '2022/3/12 9:34:56',
                        rangeInfo: {
                            id: '',
                            userRatings: 0,
                            comment: '',
                            completeEvaluationFlag: 0
                        },
                        paymentInfo: {
                            id: '3-4534',
                            // 支付方式
                            paymentMethod: '支付宝',
                            // 流水号
                            serialNumber: '3346587897967568689797',
                            // 支付金额
                            paymentAmount: 4.0,
                            // 支付状态
                            paymentStatus: 1,
                            paymentStatus_cnName: '支付成功',
                        }
                    }
                ],
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
                        if (this.recycelIds.some(ele => ele == item.orderNumber)) {
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
                    recycelIds.push(item.orderNumber);
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
            getFilterData() {
                //todo 获取查询条件，发送ajax
                console.log(this.searchConditions)
                console.log('获取查询条件，发送ajax')
                // axios.post('xxx', {
                //     data: this.searchConditions
                // }).then(function (response) {
                //     console.log(response);
                // }).catch(function (error) {
                //     console.log(error);
                // })
            },
        },
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'}),
            ...mapState({paymentStatusOptions: 'paymentStatusOptions'}),
            getRecoverDataTotal() {
                return this.tableData.filter(item => item.isDel == -1).length
            },
            filterData() {
                return this.tableData.filter(item => item.isDel == -1)
            }
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