module.exports = {
    pages:{
        index:{
            //入口
            entry:'src/main.js'
        },
    },
    devServer: {
        proxy: 'http://119.3.104.52:8090',
    }
}