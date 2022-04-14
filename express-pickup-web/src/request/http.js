import axios from "axios";
import Vue from 'vue'
// axios.defaults.headers.post['Content-Type'] = 'application/json';
let server = axios.create({   //新创建一个axios实例
    baseURL: "http://localhost:8090",  //公共地址
    timeout: 30000, //延迟时间,
})
// 请求拦截
server.interceptors.request.use(config => {
    // console.log(config); // config是一个包含了所有请求信息的对象 在这里可以修改config对象 修改之后需要返回config对象 请求才会正常进行
    let token = localStorage.getItem('token') || ''
    config.headers['Authorization'] = token
    return config
}, err => {
    Promise.reject(err)
})
// 添加响应拦截器
server.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    if (response && response.data) {
        let statusCode = response.data.statusCode;
        let message = response.data.message;
        let code = [4000, 4001,4004, 6001,6002];
        if (code.some(item => item === statusCode)) {
            Vue.prototype.$message.error(message);
        }
    }
    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    if (error && error.response) {
        switch (error.response.status) {
            case 400:
                error.message = '请求错误(400)';
                break;
            case 401:
                error.message = '未授权，请重新登录(401)';
                break;
            case 403:
                error.message = '拒绝访问(403)';
                break;
            case 404:
                error.message = '请求出错(404)';
                break;
            case 408:
                error.message = '请求超时(408)';
                break;
            case 4003:
                error.message = 'token失效,请重新登录';
                localStorage.removeItem('token');
                location.reload();
                break;
            case 500:
                error.message = '服务器错误(500)';
                break;
            case 501:
                error.message = '服务未实现(501)';
                break;
            case 502:
                error.message = '网络错误(502)';
                break;
            case 503:
                error.message = '服务不可用(503)';
                break;
            case 504:
                error.message = '网络超时(504)';
                break;
            case 505:
                error.message = 'HTTP版本不受支持(505)';
                break;
            default:
                error.message = '连接出错' + (error.response.status);
        }
    } else {
        error.message = '连接服务器失败!'
    }
    return Promise.reject(error);
});

/**
 * 使用es6的export default导出了一个函数，导出的函数代替axios去帮我们请求数据，
 * 函数的参数及返回值如下：
 * @param {String} method  请求的方法：get、post、delete、put
 * @param {String} url     请求的url:
 * @param {Object} data    请求的参数
 * @returns {Promise}     返回一个promise对象，其实就相当于axios请求数据的返回值
 */
export default function (method, url, data = null) {
    method = method.toLowerCase();
    if (method == 'post') {
        return server.post(url, data)
    } else if (method == 'get') {
        return server.get(url, {params: data})
    } else {
        console.error('未知的method' + method)
        return false;
    }
}