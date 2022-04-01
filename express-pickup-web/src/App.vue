<template>
    <div id="app">
        <Header v-if="getUserId"></Header>
        <Contenter></Contenter>
    </div>
</template>

<script>
    import Header from "./views/common/Header";
    import Contenter from "./views/deliveryUser/Contenter";
    import {mapGetters} from 'vuex';
    export default {
        name: 'App',
        components: {
            Header,
            Contenter,
        },
        computed:{
            ...mapGetters({getUserId:'getUserId'})
        }
    }
</script>

<style>
    * {
        margin: 0;
        padding: 0;
    }

    a {
        text-decoration: none;
    }

    #app {
        font-family: 'Consolas';
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
    }
</style>
