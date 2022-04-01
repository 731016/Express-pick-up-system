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
                                v-model="searchConditions.orderNumber"
                                clearable>
                        </el-input>
                    </div>
                </el-col>
                <el-col :span="2">
                    <div class="grid-content">
                        <el-button icon="el-icon-search" circle @click="getOrderNotTaken"></el-button>
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
                    :data="getFilterData"
                    ref="tableData"
                    @selection-change="rowChange()"
                    style="width: 100%"
                    max-height="680">
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
                        label="收件人"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="paymentInfo.paymentStatus"
                        label="收件电话"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="orderStatus"
                        label="收件地址"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="createTime"
                        label="快递寄达地址"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="createTime"
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
                    :total="getOrderDataTotal()">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import mixin from '../../mixin'

    export default {
        name: "OrderHall",
        mixins: [mixin],
        data() {
            return {
                searchConditions: {
                    orderStatus: '',
                    orderNumber: '',
                    startEndTime: [],
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                },
                bulkOrders: [],
                tableData: [
                    {
                        id: 'qwed-34253-fsgdf-42354',
                        orderStep: 2,
                        // 订单号
                        orderNumber: '1111111111',
                        // 收件姓名
                        pickupName: '涂鏊飞',
                        // 收件短信
                        contactNumber: '17685585594',
                        // 寄达地址
                        trackDeliveryAddress: '武汉市xxx',
                        // 收件地址
                        shipAddress: '碧海花园',
                        // 快递单号
                        trackNumber: '156461564496',
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
                        orderStatus: 20,
                        orderStatus_cnName: '派送中',
                        createTime: '2022/3/12 11:34:34',
                        rangeInfo: {
                            id: '',
                            userRatings: 0,
                            comment: '',
                            completeEvaluationFlag: 0
                        },
                        paymentInfo: {
                            id: '1-1234',
                            // 支付方式
                            paymentMethod: '支付宝',
                            // 流水号
                            serialNumber: '3346587897967568689797',
                            // 支付金额
                            paymentAmount: 4.0,
                            // 支付状态
                            paymentStatus: 1,
                            paymentStatus_cnName: '支付成功'
                        }
                    },
                    {
                        id: 'd223423-edwede-d2wedwe-e2r23f-dwedf',
                        orderStep: 3,
                        // 订单号
                        orderNumber: '22222222222',
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
                        deliveryManId: '43657876876',
                        // 配送员
                        deliveryMan: '规划和',
                        // 配送备注
                        shippingReamrk: '已送达',
                        // 备注
                        remark: '放到菜鸟驿站',
                        //是否删除【1撤销，0未删除,-1删除】
                        isDel: 0,
                        //删除原因
                        delReason: '',
                        // 订单状态
                        orderStatus: 40,
                        orderStatus_cnName: '订单完成',
                        createTime: '2022/3/14 10:45:33',
                        rangeInfo: {
                            id: '2-45345',
                            userRatings: 4.5,
                            comment: '服务好',
                            completeEvaluationFlag: 1
                        },
                        paymentInfo: {
                            id: '2-43242',
                            // 支付方式
                            paymentMethod: '支付宝',
                            // 流水号
                            serialNumber: '3346587897967568689797',
                            // 支付金额
                            paymentAmount: 42.0,
                            // 支付状态
                            paymentStatus: 1,
                            paymentStatus_cnName: '支付成功'
                        }
                    },
                    {
                        id: '8989797-435-45646-54654-7574',
                        orderStep: 0,
                        // 订单号
                        orderNumber: '333333333333',
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
                        // 配送员备注
                        shippingReamrk: '',
                        // 备注
                        remark: '放到菜鸟驿站',
                        //是否删除【1撤销，0未删除,-1删除】
                        isDel: 0,
                        //删除原因
                        delReason: '',
                        // 订单状态
                        orderStatus: 10,
                        orderStatus_cnName: '等待接单',
                        createTime: '2022/3/12 14:00:34',
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
                            paymentStatus_cnName: '支付成功'
                        }
                    },
                    {
                        id: '75676586755-2432423-234324-2423',
                        orderStep: 2,
                        // 订单号
                        orderNumber: '55555555',
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
                        deliveryManId: '475678687686',
                        // 配送员
                        deliveryMan: '嘎嘎嘎',
                        // 配送备注
                        shippingReamrk: '已送达',
                        // 备注
                        remark: '放到菜鸟驿站',
                        //是否删除【1撤销，0未删除,-1删除】
                        isDel: 0,
                        //删除原因
                        delReason: '',
                        // 订单状态
                        orderStatus: 20,
                        orderStatus_cnName: '派送中',
                        createTime: '2022/3/14 16:33:00',
                        rangeInfo: {
                            id: '',
                            userRatings: 0,
                            comment: '',
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
                        }
                    },
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
            /**
             * 查询未接单的订单
             */
            getOrderNotTaken() {
                //todo 查询所有被未接单的订单，参数：【orderStatus订单状态==等待接单10 && paymentStatus==支付成功1】
                console.log(this.searchConditions)
                console.log('ajax查询')
            },
            /**
             * 表格被选中的数据发送变化 【选择的订单id】，orderStatus订单状态=‘派送中’20
             */
            rowChange() {
                this.bulkOrders = [];
                let selectedAllData = this.$refs.tableData.selection;
                let bulkOrders = [];
                selectedAllData.forEach(function (item) {
                    bulkOrders.push(item.orderNumber);
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
                this.$confirm('确认撤销订单' + this.bulkOrders.join(","), '撤销操作', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    //todo axios 修改 参数：【订单id】
                    this.tableData.forEach(item => {
                        if (this.bulkOrders.some(ele => ele == item.orderNumber)) {
                            item.orderStatus = 20;
                            item.orderStatus_cnName = '派送中';
                        }
                    })
                    this.$message({
                        message: '接单成功',
                        type: 'success'
                    });
                    this.bulkOrders = [];
                }).catch(() => {

                });
            },
            /**
             * 获取未接单的订单总数量 参数：【orderStatus订单状态==等待接单10 && paymentStatus==支付成功1】
             */
            getOrderDataTotal() {
                //axios
                return this.getFilterData.length;
            }
        },
        //过滤【等待接单、支付成功】的订单
        computed: {
            getFilterData() {
                return this.tableData.filter(item => item.orderStatus == 10 && item.paymentInfo.paymentStatus == 1)
            }
        },
        //初始化加载ajax
        mounted() {
            this.getOrderNotTaken();
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
        }
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