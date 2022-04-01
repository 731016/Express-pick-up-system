module.exports = {
    pages:{
        index:{
            //入口
            entry:'src/main.js'
        },
    },
    devServer: {
        proxy: 'http://localhost:8090',
    }
}