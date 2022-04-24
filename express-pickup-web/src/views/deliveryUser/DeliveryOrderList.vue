<template>
    <div v-loading.fullscreen.lock="loading">
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
                        <el-button icon="el-icon-search" circle @click="initData()"></el-button>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div class="alert-title">
            <el-alert
                    title="完成订单： 订单状态需为 【派送中】 或 【订单异常】 异常订单： 订单状态需为 【派送中】 订单评价： 订单状态需为 【订单完成】 或 【订单异常】"
                    type="success"
                    show-icon>
            </el-alert>
        </div>
        <div>
            <el-table :cell-style="drawCells"
                      :data="getNotDelData()"
                      :v-loading="loading"
                      ref="tableData"
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
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="150">
                    <template slot-scope="scope">
                        <span>
                            <el-button icon="el-icon-search" circle @click="orderSearch(scope.row)" type="text"
                                       size="small"></el-button>
                        </span>
                        <span v-if="showOrderSuccess(scope.row)">
                            <el-button type="success" icon="el-icon-check" circle size="mini"
                                       @click="orderSuccess(scope.row)"></el-button>
                        </span>
                        <span v-if="showOrderException(scope.row)">
                            <el-button type="danger" icon="el-icon-close" circle size="mini"
                                       @click="orderExecption(scope.row)"></el-button>
                        </span>
                        <span v-if="showRanging(scope.row)">
                            <el-button type="primary" icon="el-icon-edit" circle size="mini"
                                       @click="orderRange(scope.row)"></el-button>
                        </span>
                    </template>
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
        <el-dialog title="订单详情" :visible.sync="dialogTableVisible"
                   :modal-append-to-body='false' @close="closeDialog">
            <div style="text-align: left" v-if="currentDialog.orderStatus != 30 ">
                <el-steps :active="currentDialog.orderStep" finish-status="success" space="33%">
                    <el-step title="等待接单"></el-step>
                    <el-step title="派送中"></el-step>
                    <el-step title="订单完成"></el-step>
                </el-steps>
            </div>
            <div style="text-align: left" v-if="currentDialog.orderStatus == 30">
                <el-steps :active="currentDialog.orderStep" finish-status="error" space="33%">
                    <el-step title="等待接单"></el-step>
                    <el-step title="派送中"></el-step>
                    <el-step title="订单异常"></el-step>
                </el-steps>
            </div>
            <div>
                <el-descriptions :column="1" direction="horizontal" border>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-user "></i>
                            订单号
                        </template>
                        {{currentDialog.id}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-user "></i>
                            姓名
                        </template>
                        {{currentDialog.pickupName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-mobile-phone "></i>
                            联系电话
                        </template>
                        {{currentDialog.contactNumber}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-document "></i>
                            快递单号
                        </template>
                        {{currentDialog.trackNumber}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-location "></i>
                            快递公司
                        </template>
                        {{currentDialog.trackCompanyName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-office-building "></i>
                            快递寄达地址
                        </template>
                        {{currentDialog.trackDeliveryAddress}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-location-outline "></i>
                            收件地址
                        </template>
                        {{currentDialog.shipAddress}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            备注
                        </template>
                        {{currentDialog.remark}}
                    </el-descriptions-item>

                    <el-descriptions-item v-if="currentDialog.paymentInfo">
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            支付方式
                        </template>
                        {{currentDialog.paymentInfo.paymentMethod}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="currentDialog.paymentInfo">
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            流水号
                        </template>
                        {{currentDialog.paymentInfo.serialNumber}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="currentDialog.paymentInfo">
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            支付金额
                        </template>
                        {{currentDialog.paymentInfo.paymentAmount}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="currentDialog.paymentInfo">
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            支付状态
                        </template>
                        {{currentDialog.paymentInfo.paymentStatusName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            订单状态
                        </template>
                        {{currentDialog.orderStatusName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets"></i>
                            配送员
                        </template>
                        {{currentDialog.deliveryMainName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets"></i>
                            配送备注
                        </template>
                        {{currentDialog.shippingReamrk}}
                    </el-descriptions-item>
                </el-descriptions>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogTableVisible = false">关 闭</el-button>
            </div>
        </el-dialog>
        <el-dialog title="订单评价" :visible.sync="dialogRateVisible"
                   :modal-append-to-body='false' @close="closeDialog">
            <el-row>
                <el-col :span="24">
                    <div class="grid-content range-directions">
                        当前订单已处理完毕，如无误请对用户进行评价。
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="4">
                    <div class="grid-content range-fraction">
                        评分
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content">
                        <el-rate
                                v-model="rangeInfo.deliveryRating"
                                show-score>
                        </el-rate>
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="4">
                    <div class="grid-content range-comment">
                        评价留言
                    </div>
                </el-col>
                <el-col :span="20">
                    <div class="grid-content range-comment-main">
                        <el-input resize="none"
                                  type="textarea"
                                  :rows="4"
                                  placeholder="给用户一个中肯的评价"
                                  v-model="rangeInfo.deliveryComment">
                        </el-input>
                    </div>
                </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
                <el-button @click="evaluate()">评 价</el-button>
            </div>
        </el-dialog>
        <el-dialog title="订单完成" :visible.sync="dialogSuccessVisible" :modal-append-to-body='false'
                   @close="closeDialog()">
            <el-input
                    type="textarea"
                    :autosize="{ minRows: 3, maxRows: 5}"
                    v-model="rangeInfo.shippingReamrk">
            </el-input>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" plain @click="commitSuccessRamark()">确定</el-button>
                <el-button @click="closeDialog()">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog title="订单异常" :visible.sync="dialogExceptionVisible" :modal-append-to-body='false'
                   @close="closeDialog()">
            <el-input
                    type="textarea"
                    :autosize="{ minRows: 3, maxRows: 5}"
                    v-model="rangeInfo.shippingReamrk">
            </el-input>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" plain @click="commitExceptionRamark()">确 定</el-button>
                <el-button @click="closeDialog()">取 消</el-button>
            </div>
        </el-dialog>
    </div>

</template>

<script>
    import {mapState} from 'vuex'
    import mixin from '../../mixin';
    import {
        selectDeliveryOrder,
        orderSuccessAjax,
        orderExceptionAjax,
        orderEvaluate
    } from "../../request/deliveryOrder";

    export default {
        name: "OrderList",
        mixins: [mixin],
        data() {
            return {
                loading: false,
                currentDialog: {},
                dialogTableVisible: false,
                dialogRateVisible: false,
                dialogSuccessVisible: false,
                dialogExceptionVisible: false,
                searchConditions: {
                    orderStatus: 0,
                    id: '',
                    startEndTime: [],
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                    totalPage: 0
                },
                rangeInfo: {
                    id: '',
                    deliveryRating: 5,
                    deliveryComment: '',
                    shippingReamrk: '',
                    deliveryTime: this.formatTime(new Date())
                },
                tableData: [],
            }
        },
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'}),
        },
        methods: {
            //显示订单完成按钮
            showOrderSuccess(row) {
                return row.orderStatus == 20 || row.orderStatus == 30;
            },
            //显示订单异常按钮
            showOrderException(row) {
                return row.orderStatus == 20;
            },
            showRanging(row) {
                return ((row.orderStatus == 40 || row.orderStatus == 30) && (row.commentStatus === 2 || row.commentStatus === 0));
            },
            getNotDelData() {
                if (this.tableData !== null) {
                    return this.tableData.filter(item => item.isDel === 0);
                }
            },
            /**
             *点击查看
             */
            orderSearch(row) {
                this.currentDialog = row
                this.dialogTableVisible = true
            },
            /**
             * 点击评价
             */
            orderRange(row) {
                this.rangeInfo.id = row.id;
                this.dialogRateVisible = true;
            },
            /**
             * 提交订单为订单完成
             */
            orderSuccess(row) {
                this.rangeInfo.id = row.id;
                this.dialogSuccessVisible = true;
            },
            /**
             * 提交订单为订单异常
             */
            orderExecption(row) {
                this.rangeInfo.id = row.id;
                this.dialogExceptionVisible = true;
            },
            drawCells({row, columnIndex}) {
                if (columnIndex == 9) {
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
            closeDialog() {
                this.dialogTableVisible = false;
                this.dialogRateVisible = false;
                this.dialogSuccessVisible = false;
                this.dialogExceptionVisible = false;
            },
            /**
             * 评价
             */
            evaluate() {
                this.loading = true;
                let commentEntity = this.rangeInfo;
                commentEntity.orderId = this.rangeInfo.id;
                orderEvaluate(commentEntity).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        this.initData();
                        this.dialogRateVisible = false;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            /**
             * 订单完成
             */
            commitSuccessRamark() {
                this.loading = true;
                let orderInfoEntity = this.rangeInfo;
                orderInfoEntity.orderStatus = 40;
                orderInfoEntity.orderStatusName = '订单完成';
                orderSuccessAjax(orderInfoEntity).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        this.initData();
                        this.dialogSuccessVisible = false;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            /**
             * 订单异常
             */
            commitExceptionRamark() {
                this.loading = true;
                let orderInfoEntity = this.rangeInfo;
                orderInfoEntity.orderStatus = 30;
                orderInfoEntity.orderStatusName = '订单异常';
                orderExceptionAjax(orderInfoEntity).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        this.initData();
                        this.dialogExceptionVisible = false;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            initData() {
                //查询所有订单状态
                this.loading = true;
                selectDeliveryOrder(this.searchConditions).then(response => {
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
            updatePage(currentPage, totalPage) {
                this.searchConditions.currentPage = currentPage;
                this.searchConditions.totalPage = totalPage;
            },
        },
        mounted() {
            this.initData()
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