import axios from "axios";
let server = axios.create({   //新创建一个axios实例
    baseURL: "http://localhost:8090",  //公共地址
    timeout: 30000, //延迟时间,
})

// 请求拦截
server.interceptors.request.use(config => {
    // console.log(config); // config是一个包含了所有请求信息的对象 在这里可以修改config对象 修改之后需要返回config对象 请求才会正常进行
    let token = localStorage.getItem('token') || ''
    config.headers.Authorization = token
    return config
}, err => {
    Promise.reject(err)
})

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