<template>
    <div>
        <div>
            <el-row :gutter="0">
                <el-col :span="2">
                    <div class="grid-content">
                        <span class="search-title">启用状态</span>
                    </div>
                </el-col>
                <el-col :span="2">
                    <div class="grid-content">
                        <el-select v-model="searchConditions.enableStatus" placeholder="请选择">
                            <el-option
                                    v-for="item in enableStatus"
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
                        <span class="search-title">账户状态</span>
                    </div>
                </el-col>
                <el-col :span="2">
                    <div class="grid-content">
                        <el-select v-model="accountStatus.accountStatus" placeholder="请选择">
                            <el-option
                                    v-for="item in accountStatus"
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
                        <span class="search-title">实名认证</span>
                    </div>
                </el-col>
                <el-col :span="2">
                    <div class="grid-content">
                        <el-select v-model="accountStatus.realNameStatus" placeholder="请选择">
                            <el-option
                                    v-for="item in realNameStatus"
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
                        <span class="search-title">用户权限</span>
                    </div>
                </el-col>
                <el-col :span="3">
                    <div class="grid-content">
                        <el-select v-model="accountStatus.userAuth" placeholder="请选择">
                            <el-option
                                    v-for="item in userAuth"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                    :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="1">
                    <div class="grid-content">
                        <span class="search-title">用户id</span>
                    </div>
                </el-col>
                <el-col :span="4">
                    <div class="grid-content bg-purple">
                        <el-input
                                v-model="searchConditions.userID"
                                clearable>
                        </el-input>
                    </div>
                </el-col>
                <el-col :span="1">
                    <div class="grid-content">
                        <span class="search-title">用户名</span>
                    </div>
                </el-col>
                <el-col :span="4">
                    <div class="grid-content bg-purple">
                        <el-input
                                v-model="searchConditions.userName"
                                clearable>
                        </el-input>
                    </div>
                </el-col>
                <el-col :span="1">
                    <div class="grid-content">
                        <span class="search-title">手机号</span>
                    </div>
                </el-col>
                <el-col :span="4">
                    <div class="grid-content bg-purple">
                        <el-input
                                v-model="searchConditions.phone"
                                clearable>
                        </el-input>
                    </div>
                </el-col>
                <el-col :span="1">
                    <div class="grid-content">
                        <el-button type="primary" icon="el-icon-search" plain>搜索</el-button>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div>
            <el-table :cell-style="drawCells"
                      :data="tableData"
                      ref="tableData"
                      style="width: 100%"
                      max-height="480">
                <el-table-column
                        prop="userId"
                        label="用户ID"
                        width="240">
                </el-table-column>
                <el-table-column
                        prop="userRole"
                        label="用户权限"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="userName"
                        label="用户名"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        label="手机号"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="rate"
                        label="评分"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="realNameStatus"
                        label="实名状态"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="enableStatus"
                        label="启用状态"
                        width="120">
                </el-table-column>
                <el-table-column
                        sortable
                        prop="freezeTime"
                        label="冻结时间"
                        width="200">
                </el-table-column>
                <el-table-column
                        sortable
                        prop="registerTime"
                        label="注册时间"
                        width="200">
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="170">
                    <template slot-scope="scope">
                        <span v-if="scope.row.enableStatus === '启用'" @click="disable(scope.row)">
                            <el-button icon="el-icon-error" type="danger"
                                       size="small">禁用</el-button>

                        </span>
                        <span v-else @click="enable(scope.row)">
                            <el-button type="success" icon="el-icon-success" size="small">启用</el-button>
                        </span>
                        <span v-if="scope.row.freezeTime === '未冻结'" @click="freeze(scope.row)">
                            <el-button icon="el-icon-lightning" type="danger"
                                       size="small">冻结</el-button>

                        </span>
                        <span v-else @click="unfreeze(scope.row)">
                            <el-button type="success" icon="el-icon-sunny" size="small">解冻</el-button>
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
                    :total="getOrderDataTotal()">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import {mapState} from "vuex";
    import mixin from '../../mixin';

    export default {
        name: "UserManager",
        mixins: [mixin],
        data() {
            return {
                searchConditions: {
                    enableStatus: '',
                    accountStatus: '',
                    realNameStatus: '',
                    userAuth: '',
                    orderNumber: '',
                    userID: '',
                    userName: '',
                    phone: '',
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                },
                enableStatus: [
                    {
                        value: '0',
                        label: '不筛选'
                    },
                    {
                        value: '1',
                        label: '启用'
                    },
                    {
                        value: '-1',
                        label: '禁用'
                    }
                ],
                accountStatus: [
                    {
                        value: '0',
                        label: '不筛选'
                    },
                    {
                        value: '1',
                        label: '冻结'
                    },
                    {
                        value: '-1',
                        label: '未冻结'
                    }
                ],
                realNameStatus: [
                    {
                        value: '0',
                        label: '不筛选'
                    },
                    {
                        value: '1',
                        label: '已认证'
                    },
                    {
                        value: '-1',
                        label: '未认证'
                    }
                ],
                userAuth: [
                    {
                        value: '0',
                        label: '不筛选'
                    },
                    {
                        value: '1',
                        label: '非正式用户'
                    },
                    {
                        value: '2',
                        label: '普通用户'
                    },
                    {
                        value: '3',
                        label: '配送员'
                    },
                    {
                        value: '4',
                        label: '系统管理员'
                    }
                ],
                tableData: [
                    {
                        userId: '123456',
                        userRole: '普通用户',
                        userName: 'user1',
                        phone: '17683866724',
                        rate: '5.6',
                        realNameStatus: '未实名',
                        enableStatus: '启用',
                        freezeTime: '未冻结',
                        registerTime: this.formatTime(new Date()),
                    },
                    {
                        userId: '23456',
                        userRole: '配送员',
                        userName: 'courser1',
                        phone: '4895848454',
                        rate: '8.6',
                        realNameStatus: '已实名',
                        enableStatus: '启用',
                        freezeTime: '未冻结',
                        registerTime: this.formatTime(new Date()),
                    },
                    {
                        userId: '34567',
                        userRole: '配送员',
                        userName: 'courser1',
                        phone: '4895848454',
                        rate: '',
                        realNameStatus: '已实名',
                        enableStatus: '启用',
                        freezeTime: '未冻结',
                        registerTime: this.formatTime(new Date()),
                    }
                ]
            }
        },
        methods: {
            getOrderDataTotal() {
                return this.tableData.length;
            },
            getAllUser() {
                //todo 查询当前页码用户
            },
            /**
             * 绘制单元格，判断订单状态
             */
            drawCells({row, columnIndex}) {
                //评分
                if (columnIndex === 4) {
                    let payStatus = row.rate;
                    if (payStatus > 0) {
                        return 'color:orange';
                    }
                }
                //实名状态
                if (columnIndex === 5) {
                    let realNameStatus = row.realNameStatus
                    if (realNameStatus === '未实名') {
                        return 'color:red';
                    }
                }
                //启用状态
                if (columnIndex === 6) {
                    let enableStatus = row.enableStatus
                    if (enableStatus === '禁用') {
                        return 'color:red';
                    }
                }
                //冻结时间
                if (columnIndex === 7) {
                    let freezeTime = row.freezeTime
                    if (freezeTime !== '未冻结') {
                        return 'color:red';
                    }
                }
            },
            disable(row) {
                let currentData = row;
                //todo 禁用账户
                this.tableData.forEach(item => {
                    if (item.userId == currentData.userId) {
                        item.enableStatus = '禁用'
                    }
                })
            },
            enable(row) {
                let currentData = row;
                //todo 启用账户
                this.tableData.forEach(item => {
                    if (item.userId == currentData.userId) {
                        item.enableStatus = '启用'
                    }
                })
            },
            freeze(row) {
                let currentData = row;
                //todo 冻结账户
                this.tableData.forEach(item => {
                    if (item.userId == currentData.userId) {
                        item.freezeTime = this.formatTime(new Date());
                    }
                })
            },
            unfreeze(row) {
                let currentData = row;
                //todo 解冻账户
                this.tableData.forEach(item => {
                    if (item.userId == currentData.userId) {
                        item.freezeTime = '未冻结'
                    }
                })
            }
        },
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'})
        },
        mounted() {

        }
    }
</script>

<style lang="less" scoped>
    .el-row {
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

    .row-bg {
        background-color: #f9fafc;
    }

    .search-title {
        font-weight: 700;
    }

    .grid-content {
        line-height: 36px;
        text-align: center;
        border-radius: 4px;
        min-height: 36px;
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