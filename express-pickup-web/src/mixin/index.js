export default {
    data() {
        return {
            pickerOptions: {
                shortcuts: [
                    {
                        text: '最近3天',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
            },
        }
    },
    methods: {
        handleSizeChange(val) {
            //每页大小，当前页码
            this.searchConditions.pageSize = val;
            this.initData();
        }
        ,
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.searchConditions.currentPage = val;
            this.initData();
        },
        supplementZero(time) {
            if (time < 10) {
                return '0' + time;
            }
            return time;
        },
        formatTime(dateTime) {
            const date = new Date(dateTime);
            let year = date.getFullYear();
            let month = date.getMonth() + 1;
            let day = date.getDate();
            let hour = date.getHours();
            let minute = date.getMinutes();
            let second = date.getSeconds();
            let dateMap = new Map();
            dateMap.set('year', this.supplementZero(year));
            dateMap.set('month', this.supplementZero(month));
            dateMap.set('day', this.supplementZero(day));
            dateMap.set('hour', this.supplementZero(hour));
            dateMap.set('minute', this.supplementZero(minute));
            dateMap.set('second', this.supplementZero(second));
            return dateMap.get('year') + '-' + dateMap.get('month') + '-' + dateMap.get('day') + ' ' + dateMap.get('hour') + ':' + dateMap.get('minute') + ':' + dateMap.get('second');
        },
    },
}