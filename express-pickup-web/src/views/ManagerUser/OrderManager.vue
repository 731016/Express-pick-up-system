<template>
    <div v-if="showUserRoleMenu()">
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
                        <el-button icon="el-icon-search" circle @click="init()"></el-button>
                    </div>
                </el-col>
            </el-col>
        </div>
        <div class="alert-title">
            <el-alert
                    title="订单分配： 订单状态需为 【等待接单】，支付状态需为 【支付成功】 或 【支付结束】 完成订单： 订单状态需为 【派送中】 或 【订单异常】 异常订单： 订单状态需为 【派送中】 删除订单： 订单状态需非 【派送中】"
                    type="warning"
                    style="text-align: left"
                    show-icon>
            </el-alert>
        </div>
        <div class="order-btn">
            <el-button type="info" round size="mini" @click="assignOrder()">
                <i class="el-icon-guide"></i>
                分配订单
            </el-button>
            <el-button type="success" round size="mini" @click="successOrder()">
                <i class="el-icon-success"></i>
                完成订单
            </el-button>
            <el-button type="warning" round size="mini" @click="execptionOrder()">
                <i class="el-icon-question"></i>
                异常订单
            </el-button>
            <el-button type="danger" round size="mini" @click="deleteOrder()">
                <i class="el-icon-error"></i>
                删除订单
            </el-button>
        </div>
        <div>
            <el-table :cell-style="drawCells"
                      v-loading="loading"
                      :data="getNotDelData"
                      ref="tableData"
                      @selection-change="rowChange()"
                      style="width: 100%"
                      max-height="480">
                <el-table-column
                        fixed
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
                        width="150">
                    <template slot-scope="scope">
                        <span>
                            <el-button icon="el-icon-search" circle @click="orderSearch(scope.row)" type="text"
                                       size="small"></el-button>
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
            <div style="text-align: left" v-if="currentdialog.orderStatus != 30 ">
                <el-steps :active="currentdialog.orderStep" finish-status="success" space="33%">
                    <el-step title="等待接单"></el-step>
                    <el-step title="派送中"></el-step>
                    <el-step title="订单完成"></el-step>
                </el-steps>
            </div>
            <div style="text-align: left" v-if="currentdialog.orderStatus == 30 ">
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
        <el-dialog title="分配订单" :visible.sync="dialogAssignVisible">
            <el-form>
                <el-form-item label="配送人员">
                    <el-select v-model="deliveryId" placeholder="请选择配送人员">
                        <el-option
                                v-for="item in orderIds"
                                :key="item.userId"
                                :label="item.userName"
                                :value="item.userId"
                                :disabled="item.disabled">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="assignOrderAjax()">分 配</el-button>
                <el-button @click="closeDialog()">关 闭</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import mixin from "../../mixin";
    import {mapGetters, mapState} from "vuex";
    import {deleteOrder} from "../../request/order";
    import {
        selectAllUnDelAndRevokeOrder,
        successOrder,
        execptionOrder,
        assignOrder,
        getDeliveryPeople
    } from "../../request/managerOrder"

    export default {
        name: "OrderManager",
        mixins: [mixin],
        data() {
            return {
                loading: false,
                //弹出框订单详情数据
                currentdialog: {},
                dialogTableVisible: false,
                dialogAssignVisible: false,
                //查询条件
                searchConditions: {
                    orderStatus: 0,
                    paymentStatus: -1,
                    id: '',
                    startEndTime: [],
                    totalPage: 0,
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                },
                deliveryId: '',
                orderIds: [],
                assignIds: [],
                successIds: [],
                execptionIds: [],
                deleteIds: [],
                tableData: [],
            }
        },
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'}),
            ...mapState({paymentStatusOptions: 'paymentStatusOptions'}),
            ...mapGetters(
                {getUserRoleId: 'getUserRoleId'},
                {getUserRoleName: 'getUserRoleName'}),
            //获取当前用户，没有被删除的订单总数
            getNotDelData() {
                return this.tableData.filter(item => item.isDel == 0);
            }
        },
        methods: {
            //分配
            assignOrder() {
                if (this.assignIds.length <= 0) {
                    this.$message({
                        message: '未选择可分配的订单',
                        type: 'warning'
                    });
                    return false;
                } else {
                    this.dialogAssignVisible = true;
                }
            },
            assignOrderAjax() {
                this.$confirm('确认分配订单' + this.assignIds.join("\n"), '分配操作', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    let obj = {};
                    obj.orderIds = this.assignIds;
                    obj.deliveryId = this.deliveryId;
                    assignOrder(obj).then(response => {
                        let rep = response.data;
                        if (response.status === 200 && rep.statusCode === 2000) {
                            this.$message.success(rep.message);
                            this.initData();
                            this.dialogAssignVisible = false;
                        }
                        this.loading = false;
                    }).catch(error => {
                        this.$message.error(error);
                        this.loading = false;
                    });
                    this.assignIds = [];
                }).catch(() => {
                    this.$message.info('用户取消操作');
                });
            },
            //完成
            successOrder() {
                if (this.successIds.length <= 0) {
                    this.$message({
                        message: '未选择可完成的订单',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('确认完成订单' + this.successIds.join("\n"), '完成操作', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    successOrder(this.successIds).then(response => {
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
                    this.successIds = [];
                }).catch(() => {
                    this.$message.info('用户取消操作');
                });
            },
            //异常
            execptionOrder() {
                if (this.execptionIds.length <= 0) {
                    this.$message({
                        message: '未选择可修改为异常的订单',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('确认异常订单' + this.execptionIds.join("\n"), '异常操作', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    execptionOrder(this.execptionIds).then(response => {
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
                    this.execptionIds = [];
                }).catch(() => {
                    this.$message.info('用户取消操作');
                });
            },
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
            updatePage(currentPage, totalPage) {
                this.searchConditions.currentPage = currentPage;
                this.searchConditions.totalPage = totalPage;
            },
            init() {
                this.initData();
                this.getDeliverys();
            },
            initData() {
                //查询所有订单状态
                this.loading = true;
                selectAllUnDelAndRevokeOrder(this.searchConditions).then(response => {
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
             *点击查看
             */
            orderSearch(row) {
                this.currentdialog = row
                this.dialogTableVisible = true
            }
            ,
            /**
             * 表格被选中的数据发送变化
             * 筛选 选择的数据中，要被【分配】【删除】【异常】【完成】的订单
             */
            rowChange() {
                this.assignIds = [];
                this.successIds = [];
                this.execptionIds = [];
                this.deleteIds = [];
                let selectedAllData = this.$refs.tableData.selection;
                let assignIds = [];
                let successIds = [];
                let execptionIds = [];
                let deleteIds = [];
                selectedAllData.forEach(function (item) {
                    if (item.orderStatus == 10) {
                        assignIds.push(item.id);
                    }
                    if (item.orderStatus == 20) {
                        execptionIds.push(item.id);
                    }
                    if (item.orderStatus != 20) {
                        deleteIds.push(item.id);
                    }
                    if (item.orderStatus == 10 || item.orderStatus == 30) {
                        successIds.push(item.id);
                    }
                });
                Object.assign(this.assignIds, assignIds);
                Object.assign(this.successIds, successIds);
                Object.assign(this.execptionIds, execptionIds);
                Object.assign(this.deleteIds, deleteIds);
            },
            /**
             * 绘制单元格，判断订单状态
             */
            drawCells({row, columnIndex}) {
                //订单状态
                if (columnIndex == 9) {
                    let payStatus = row.paymentInfo.paymentStatus;
                    if (payStatus == 0) {
                        return 'color:#BD8E3E';
                    } else {
                        return 'color:green';
                    }
                }
                //支付状态
                if (columnIndex == 10) {
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
                this.dialogAssignVisible = false
            },
            showUserRoleMenu() {
                let roleId = this.getUserRoleId;
                let roleName = this.getUserRoleName;
                return roleId || roleName;
            },
            getDeliverys() {
                this.loading = true;
                getDeliveryPeople().then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.orderIds = rep.data;
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                });
            }
        },
        mounted() {
            this.init();
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