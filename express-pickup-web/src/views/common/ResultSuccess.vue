<template>
    <div>
        <el-result icon="success" title="下单成功" subTitle="请根据提示进行操作">
            <template slot="extra">
                <el-button type="primary" size="medium" @click="toGeneralOrderList()">订单页面</el-button>
            </template>
        </el-result>
    </div>
</template>

<script>
    export default {
        name: "ResultSuccess",
        methods: {
            toGeneralOrderList() {
                this.$router.push({
                    name: 'GeneralOrderList'
                })
            }
        }
    }
</script>

<style scoped>

</style>