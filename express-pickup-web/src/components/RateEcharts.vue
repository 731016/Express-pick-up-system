<template>
    <div class="RateEcharts">
        <div id="dashboard" style="width: 430px;height:400px;left: 25%"></div>
    </div>
</template>

<script>
    export default {
        name: "RateEcharts",
        props: ['rateData'],
        data() {
            return {
                "option": {
                    series: [
                        {
                            type: 'gauge',
                            startAngle: 180,
                            endAngle: 0,
                            min: 0,
                            max: 1,
                            splitNumber: 8,
                            axisLine: {
                                lineStyle: {
                                    width: 6,
                                    color: [
                                        [0.25, '#FF6E76'],
                                        [0.5, '#FDDD60'],
                                        [0.75, '#58D9F9'],
                                        [1, '#7CFFB2']
                                    ]
                                }
                            },
                            pointer: {
                                icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
                                length: '12%',
                                width: 20,
                                offsetCenter: [0, '-60%'],
                                itemStyle: {
                                    color: 'auto'
                                }
                            },
                            axisTick: {
                                length: 12,
                                lineStyle: {
                                    color: 'auto',
                                    width: 2
                                }
                            },
                            splitLine: {
                                length: 20,
                                lineStyle: {
                                    color: 'auto',
                                    width: 5
                                }
                            },
                            axisLabel: {
                                color: '#464646',
                                fontSize: 20,
                                distance: -60,
                                formatter: function (value) {
                                    if (value === 0.875) {
                                        return '4';
                                    } else if (value === 0.625) {
                                        return '3';
                                    } else if (value === 0.375) {
                                        return '2';
                                    } else if (value === 0.125) {
                                        return '1';
                                    }
                                    return '';
                                }
                            },
                            title: {
                                offsetCenter: [0, '-20%'],
                                fontSize: 25
                            },
                            detail: {
                                fontSize: 25,
                                offsetCenter: [0, '0%'],
                                valueAnimation: true,
                                formatter: function (value) {
                                    return Math.round(value) + '分';
                                },
                                color: 'auto'
                            },
                            data: this.rateData
                        }
                    ]
                }
            }
        },
        methods:{
            initEcharts() {
                // 基于准备好的dom，初始化echarts实例
                var myChart = this.$echarts.init(document.getElementById('dashboard'));
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(this.option);
            }
        },
        mounted() {
            this.initEcharts();
        },
        watch: {
            "rateData": {
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