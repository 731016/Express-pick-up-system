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
                      :data="tableData"
                      ref="tableData"
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
                    :total="getOrderDataTotal">
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
            <div style="text-align: left" v-if="currentdialog.orderStatus == 30">
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
                        {{currentdialog.orderNumber}}
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
                        {{currentdialog.paymentInfo.paymentStatus}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets "></i>
                            订单状态
                        </template>
                        {{currentdialog.orderStatus}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-tickets"></i>
                            配送员
                        </template>
                        {{currentdialog.deliveryMan}}
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
                                v-model="rangeInfo.userRatings"
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
                                  v-model="rangeInfo.comment">
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
                    v-model="rangeInfo.successRamark">
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
                    v-model="rangeInfo.exceptionReamrk">
            </el-input>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" plain @click="commitExceptionRamark()">确 定</el-button>
                <el-button @click="closeDialog()">取 消</el-button>
            </div>
        </el-dialog>
    </div>

</template>

<script>
    // import axios from 'axios';
    import {mapState} from 'vuex'
    import mixin from '../../mixin';

    export default {
        name: "OrderList",
        mixins: [mixin],
        data() {
            return {
                currentdialog: {},
                dialogTableVisible: false,
                dialogRateVisible: false,
                dialogSuccessVisible: false,
                dialogExceptionVisible: false,
                searchConditions: {
                    orderStatus: '',
                    orderNumber: '',
                    startEndTime: [],
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                },
                rangeInfo: {
                    orderNumber: '',
                    userRatings: 5,
                    comment: '',
                    completeEvaluationFlag: 1,
                    successRamark: '',
                    exceptionReamrk: ''
                },
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
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'}),
            getOrderDataTotal() {
                return this.tableData.length;
            },
        },
        methods: {
            //显示订单完成按钮
            showOrderSuccess(row){
                return row.orderStatus == 20 || row.orderStatus == 30;
            },
            //显示订单异常按钮
            showOrderException(row){
                return row.orderStatus == 20;
            },
            showRanging(row){
                return (row.orderStatus == 40 || row.orderStatus ==30) && row.rangeInfo.completeEvaluationFlag == 0
            },
            /**
             *点击查看
             */
            orderSearch(row) {
                this.currentdialog = row
                this.dialogTableVisible = true
            },
            /**
             * 点击评价
             */
            orderRange(row) {
                this.dialogRateVisible = true;
                this.rangeInfo.orderNumber = row.orderNumber;
            },
            /**
             * 提交订单为订单完成
             */
            orderSuccess(row) {
                this.rangeInfo.orderNumber = row.orderNumber;
                this.dialogSuccessVisible = true;
            },
            /**
             * 提交订单为订单异常
             */
            orderExecption(row) {
                this.rangeInfo.orderNumber = row.orderNumber;
                this.dialogExceptionVisible = true;
            },
            /**
             * 查询条件
             */
            getFilterData() {
                // todo [用户id == 配送员id] + 查询条件
                console.log('发送ajax')
                // axios.post('xxx', {
                //     data: this.searchConditions
                // }).then(function (response) {
                //     console.log(response);
                // }).catch(function (error) {
                //     console.log(error);
                // })
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
                this.dialogRateVisible = false;
                //todo ajax根据订单id修改
                this.tableData.forEach(item => {
                    if (item.orderNumber == this.rangeInfo.orderNumber) {
                        Object.assign(item.rangeInfo, this.rangeInfo)
                    }
                })
            },
            /**
             * 订单完成
             */
            commitSuccessRamark() {
                // todo ajax 根据订单id更新，参数rangeInfo.successRamark
                //此处无须查看，不更新数据
                this.tableData.forEach(item=>{
                    if (item.orderNumber == this.rangeInfo.orderNumber){
                        item.orderStatus = 40;
                        item.orderStatus_cnName = '订单完成'
                        item.shippingReamrk = this.rangeInfo.exceptionReamrk;
                    }
                })
                this.dialogSuccessVisible = false;
                this.$message({
                    message: '操作成功',
                    type: 'success'
                });
                // this.$message.error('处理失败');
            },
            /**
             * 订单异常
             */
            commitExceptionRamark() {
                // todo ajax 根据订单id更新，参数rangeInfo.exceptionReamrk
                //此处无须查看，不更新数据
                this.tableData.forEach(item=>{
                    if (item.orderNumber == this.rangeInfo.orderNumber){
                        item.orderStatus = 30;
                        item.orderStatus_cnName = '订单异常'
                        item.shippingReamrk = this.rangeInfo.ex;
                    }
                })
                this.dialogExceptionVisible = false;
                this.$message({
                    message: '操作成功',
                    type: 'success'
                });
                // this.$message.error('处理失败');
            },
        },
        created() {
            this.getFilterData();
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