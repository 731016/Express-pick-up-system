<template>
    <el-container>
        <Aside v-if="getUserId"></Aside>
        <el-main>
            <keep-alive :include="['OrderNow']">
                <router-view></router-view>
            </keep-alive>
        </el-main>
    </el-container>
</template>

<script>
    import Aside from "../common/Aside";
    import {mapGetters} from 'vuex';
    export default {
        name: "Content",
        data() {
            return {
                loading: true
            }
        }
        ,
        components: {Aside},
        computed:{
            ...mapGetters({getUserId:'getUserId'})
        }
    }
</script>

<style scoped>
    *{
        padding: 0;
        margin: 0;
    }
    .el-container {
        /*721*/
        min-height: 721px;
    }
</style>