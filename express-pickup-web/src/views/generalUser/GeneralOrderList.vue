<template>
    <div v-show="showUserRoleMenu">
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
                    title="撤销订单： 订单状态需为 【等待接单】 删除订单： 订单状态需为 【订单完成】 或 【订单异常】"
                    type="warning"
                    show-icon>
            </el-alert>
        </div>
        <div class="order-btn">
            <el-button type="warning" round size="mini" @click="revokeOrder()">
                <i class="el-icon-circle-close"></i>
                撤销订单
            </el-button>
            <el-button type="danger" round size="mini" @click="deleteOrder()">
                <i class="el-icon-delete"></i>
                删除订单
            </el-button>
        </div>
        <div>
            <el-table :cell-style="drawCells"
                      v-loading="loading"
                      :data="getNotDelData()"
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
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="120">
                    <template slot-scope="scope">
                        <span>
                            <el-button icon="el-icon-search" circle @click="searchInfoDialog(scope.row)" type="text"
                                       size="small"></el-button>

                        </span>
                        <span v-if="whetherShowReviewsBtn(scope.row)">
                            <el-button type="primary" icon="el-icon-edit" circle size="small"
                                       @click="rangeClick(scope.row)"></el-button>
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
            <div style="text-align: left" v-if="currentdialog.orderStatus !== 30 ">
                <el-steps :active="currentdialog.orderStep" finish-status="success" space="33%">
                    <el-step title="等待接单"></el-step>
                    <el-step title="派送中"></el-step>
                    <el-step title="订单完成"></el-step>
                </el-steps>
            </div>
            <div style="text-align: left" v-if="currentdialog.orderStatus === 30 ">
                <el-steps :active="currentdialog.orderStep" finish-status="error" space="33%">
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
                        {{currentdialog.id}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-user "></i>
                            姓名
                        </template>
                        {{currentdialog.pickupName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-mobile-phone "></i>
                            联系电话
                        </template>
                        {{currentdialog.contactNumber}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-document "></i>
                            快递单号
                        </template>
                        {{currentdialog.trackNumber}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-location "></i>
                            快递公司
                        </template>
                        {{currentdialog.trackCompanyName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-office-building "></i>
                            快递寄达地址
                        </template>
                        {{currentdialog.trackDeliveryAddress}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-location-outline "></i>
                            收件地址
                        </template>
                        {{currentdialog.shipAddress}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            备注
                        </template>
                        {{currentdialog.remark}}
                    </el-descriptions-item>

                    <el-descriptions-item v-if="currentdialog.paymentInfo">
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            支付方式
                        </template>
                        {{currentdialog.paymentInfo.paymentMethod}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="currentdialog.paymentInfo">
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            流水号
                        </template>
                        {{currentdialog.paymentInfo.serialNumber}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="currentdialog.paymentInfo">
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            支付金额
                        </template>
                        {{currentdialog.paymentInfo.paymentAmount}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="currentdialog.paymentInfo">
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            支付状态
                        </template>
                        {{currentdialog.paymentInfo.paymentStatusName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            订单状态
                        </template>
                        {{currentdialog.orderStatusName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets"></i>
                            配送员
                        </template>
                        {{currentdialog.deliveryMainName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets"></i>
                            配送备注
                        </template>
                        {{currentdialog.shippingReamrk}}
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
                        当前订单已处理完毕，如无误请对配送员进行评价。
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
                                v-model="rangeInfo.userRating"
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
                                  placeholder="给配送员一个中肯的评价"
                                  v-model="rangeInfo.comment">
                        </el-input>
                    </div>
                </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
                <el-button @click="evaluate(currentdialog.id)">评 价</el-button>
            </div>
        </el-dialog>
    </div>

</template>

<script>
    import {selectAllOrder, revokeOrder, deleteOrder, evaluateOrder} from '../../request/order';
    import {mapGetters, mapState} from 'vuex';
    import mixin from '../../mixin';

    export default {
        name: "OrderList",
        mixins: [mixin],
        data() {
            return {
                loading: false,
                //弹出框订单详情数据
                currentdialog: {},
                dialogTableVisible: false,
                dialogRateVisible: false,
                //查询条件
                searchConditions: {
                    orderStatus: 0,
                    id: '',
                    startEndTime: [],
                    currentPage: 1,
                    pageSize: 5,
                    totalPage: 5
                },
                revokeIds: [],
                deleteIds: [],
                //评价信息
                rangeInfo: {
                    userRating: 0,
                    comment: '',
                },
                tableData: [],
            }
        },
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'}),
            ...mapGetters(
                {getUserRoleId: 'getUserRoleId'},
                {getUserRoleName: 'getUserRoleName'}),
            showUserRoleMenu() {
                let roleId = this.getUserRoleId;
                let roleName = this.getUserRoleName;
                if (roleId || roleName) {
                    return true;
                }
                return false;
            },
        },
        methods: {
            /**
             * 是否显示评价按钮 [订单异常||订单完成]
             */
            whetherShowReviewsBtn(row) {
                return ((row.orderStatus === 40 || row.orderStatus === 30) && (row.commentStatus === 0 || row.commentStatus === 2));
            },
            updatePage(currentPage, totalPage) {
                this.searchConditions.currentPage = currentPage;
                this.searchConditions.totalPage = totalPage;
            },
            //撤销订单
            revokeOrder() {
                if (this.revokeIds.length <= 0) {
                    this.$message({
                        message: '未选择可撤销的订单',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('确认撤销订单' + this.revokeIds.join("\n"), '撤销操作', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    revokeOrder(this.revokeIds).then(response => {
                        let rep = response.data;
                        if (response.status === 200 && rep.statusCode === 2000) {
                            this.$message.success(rep.message);
                            this.initData();
                            this.revokeIds = [];
                        }
                        this.loading = false;
                    }).catch(error => {
                        this.$message.error(error);
                        this.loading = false;
                    });
                }).catch(() => {
                    this.$message.info('用户取消操作');
                });
            }
            ,
            //删除订单
            deleteOrder() {
                if (this.deleteIds.length <= 0) {
                    this.$message({
                        message: '未选择可删除的订单',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('确认删除订单' + this.deleteIds.join("\n"), '删除操作', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    deleteOrder(this.deleteIds).then(response => {
                        let rep = response.data;
                        if (response.status === 200 && rep.statusCode === 2000) {
                            this.$message.success(rep.message);
                            this.initData();
                        }
                        this.loading = false;
                    }).catch(error => {
                        this.$message.error(error);
                        this.loading = false;
                    });
                    this.deleteIds = [];
                }).catch(() => {
                    this.$message.info('用户取消操作');
                });
            },
            /**
             * 弹出框
             */
            searchInfoDialog(row) {
                this.currentdialog = row
                this.dialogTableVisible = true
            },
            /**
             * 评价
             */
            rangeClick(row) {
                this.currentdialog = row;
                this.dialogRateVisible = true;
            }
            ,
            /**
             * 表格被选中的数据发送变化
             * 筛选 选择的数据中，要被【撤销】或【删除】的订单
             */
            rowChange() {
                this.revokeIds = [];
                this.deleteIds = [];
                let selectedAllData = this.$refs.tableData.selection;
                let revokeIds = [];
                let deleteIds = [];
                selectedAllData.forEach(function (item) {
                    if (item.orderStatus == 10) {
                        revokeIds.push(item.id);
                    } else if (item.orderStatus == 40 || item.orderStatus == 30) {
                        deleteIds.push(item.id);
                    }
                });
                Object.assign(this.revokeIds, revokeIds);
                Object.assign(this.deleteIds, deleteIds);
            },
            /**
             * 绘制单元格，判断订单状态
             */
            drawCells({row, columnIndex}) {
                //订单状态
                if (columnIndex == 5) {
                    let payStatus = row.paymentInfo.paymentStatus;
                    if (payStatus == 0) {
                        return 'color:#BD8E3E';
                    } else {
                        return 'color:green';
                    }
                }
                //支付状态
                if (columnIndex == 6) {
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
             *关闭弹出框
             */
            closeDialog() {
                this.dialogTableVisible = false
            },
            /**
             * 评价
             */
            evaluate(id) {
                this.dialogRateVisible = false;
                this.loading = true;
                let comment = this.rangeInfo;
                comment.orderId = id;
                evaluateOrder(comment).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.$message.success(rep.message);
                        this.initData();
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
            },
            initData() {
                //查询所有订单状态
                this.loading = true;
                selectAllOrder(this.searchConditions).then(response => {
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
            getNotDelData() {
                if (this.tableData) {
                    return this.tableData.filter(item => item.isDel === 0);
                }
            }
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