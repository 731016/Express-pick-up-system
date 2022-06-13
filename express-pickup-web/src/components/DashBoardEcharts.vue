<template>
    <div class="Echarts">
        <div id="rate" style="width: 430px;height:400px;left: 15%"></div>
    </div>
</template>

<script>
    export default {
        name: "DashBoardEcharts",
        props: ['dashBoardData'],
        data() {
            return {
                option: {
                    tooltip: {
                        trigger: 'item'
                    },
                    legend: {
                        top: '5%',
                        left: 'center'
                    },
                    series: [
                        {
                            name: '简介',
                            type: 'pie',
                            radius: ['40%', '70%'],
                            avoidLabelOverlap: false,
                            itemStyle: {
                                borderRadius: 10,
                                borderColor: '#fff',
                                borderWidth: 2
                            },
                            label: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                label: {
                                    show: true,
                                    fontSize: '40',
                                    fontWeight: 'bold'
                                }
                            },
                            labelLine: {
                                show: false
                            },
                            data: this.dashBoardData
                        }
                    ]
                }
            }
        },
        methods:
            {
                initEcharts() {
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = this.$echarts.init(document.getElementById('rate'));
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(this.option);
                }
            },
        mounted() {
            this.initEcharts();
        },
        watch: {
            "dashBoardData": {
                handler() {
                    this.initEcharts();
                },
                deep: true //对象内部属性的监听，关键。
            }
        }
    }
</script>

<style scoped>

</style>