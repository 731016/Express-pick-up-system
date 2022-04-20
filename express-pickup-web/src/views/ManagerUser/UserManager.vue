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
                                v-model="searchConditions.userId"
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
                        <el-button type="primary" icon="el-icon-search" plain @click="initData()">搜索</el-button>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div>
            <el-table :cell-style="drawCells"
                      :data="tableData"
                      v-loading="loading"
                      ref="tableData"
                      style="width: 100%"
                      max-height="480">
                <el-table-column
                        prop="userId"
                        label="用户ID"
                        width="240">
                </el-table-column>
                <el-table-column
                        prop="userRoleName"
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
                        prop="disableName"
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
                        <span v-if="scope.row.disable === 1" @click="disable(scope.row)">
                            <el-button icon="el-icon-error" type="danger"
                                       size="small">禁用</el-button>

                        </span>
                        <span v-else @click="enable(scope.row)">
                            <el-button type="success" icon="el-icon-success" size="small">启用</el-button>
                        </span>
                        <span v-if="scope.row.freezeTime === '未冻结' || scope.row.freezeTime === null"
                              @click="freeze(scope.row)">
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
                    :total="searchConditions.totalPage">
            </el-pagination>
            <el-dialog title="冻结小时" :visible.sync="dialogVisible">
                <el-form :model="freezeTime" status-icon label-width="100px">
                    <el-input-number v-model="freezeTime" :min="1" :max="72"></el-input-number>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submitForm()">确 定</el-button>
                    <el-button @click="resetForm()">取 消</el-button>
                </div>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import {mapState} from "vuex";
    import mixin from '../../mixin';
    import {getAllUser, disableUser, enableUser, freezeUser, unfreezeUser} from "../../request/managerOrder";

    export default {
        name: "UserManager",
        mixins: [mixin],
        data() {
            return {
                loading: false,
                dialogVisible: false,
                userId: '',
                freezeTime: 0,
                searchConditions: {
                    enableStatus: 0,
                    accountStatus: 0,
                    realNameStatus: 0,
                    userAuth: '0',
                    orderNumber: '',
                    userId: '',
                    userName: '',
                    phone: '',
                    //当前页码
                    currentPage: 1,
                    pageSize: 5,
                    totalPage: 0
                },
                enableStatus: [
                    {
                        value: 0,
                        label: '不筛选'
                    },
                    {
                        value: 1,
                        label: '启用'
                    },
                    {
                        value: -1,
                        label: '禁用'
                    }
                ],
                accountStatus: [
                    {
                        value: 0,
                        label: '不筛选'
                    },
                    {
                        value: 1,
                        label: '冻结'
                    },
                    {
                        value: -1,
                        label: '未冻结'
                    }
                ],
                realNameStatus: [
                    {
                        value: 0,
                        label: '不筛选'
                    },
                    {
                        value: 1,
                        label: '已认证'
                    },
                    {
                        value: -1,
                        label: '未认证'
                    }
                ],
                userAuth: [
                    {
                        value: '0',
                        label: '不筛选'
                    },
                    {
                        value: 'C',
                        label: '普通用户'
                    },
                    {
                        value: 'B',
                        label: '配送员'
                    },
                    {
                        value: 'A',
                        label: '系统管理员'
                    }
                ],
                tableData: []
            }
        },
        methods: {
            submitForm(row) {
                let currentData = row;
                let obj = {};
                obj.userId = currentData.userId;
                obj.freezeTime = this.freezeTime;
                freezeUser(obj).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.tableData.forEach(item => {
                            if (item.userId == currentData.userId) {
                                item.freezeTime = this.formatTime(new Date());
                            }
                            this.dialogVisible = false;
                        })
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            resetForm() {
                this.dialogVisible = false;
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
                    if (realNameStatus === '未认证') {
                        return 'color:red';
                    }
                }
                //启用状态
                if (columnIndex === 6) {
                    let disable = row.disable
                    if (disable === 0) {
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
                this.loading = true;
                let currentData = row;
                disableUser({"userId": currentData.userId}).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.tableData.forEach(item => {
                            if (item.userId == currentData.userId) {
                                item.disable = 0
                                item.disableName = '禁用'
                            }
                        })
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            enable(row) {
                this.loading = true;
                let currentData = row;
                enableUser({"userId": currentData.userId}).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.tableData.forEach(item => {
                            if (item.userId == currentData.userId) {
                                item.disable = 1
                                item.disableName = '启用'
                            }
                        })
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            freeze(row) {
                this.userId = row.userId;
                this.dialogVisible = true;
            },
            unfreeze(row) {
                this.loading = true;
                let currentData = row;
                unfreezeUser({"userId": currentData.userId}).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.tableData.forEach(item => {
                            if (item.userId == currentData.userId) {
                                item.freezeTime = '未冻结'
                            }
                        })
                    }
                    this.loading = false;
                }).catch(error => {
                    this.$message.error(error);
                    this.loading = false;
                })
            },
            initData() {
                this.loading = true;
                getAllUser(this.searchConditions).then(response => {
                    let rep = response.data;
                    if (response.status === 200 && rep.statusCode === 2000) {
                        this.tableData = JSON.parse(JSON.stringify(rep.data));
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
        computed: {
            ...mapState({orderStatusOptions: 'orderStatusOptions'}),
        },
        mounted() {
            this.initData();
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