import axios from 'axios'
import qs from 'qs';
import router from "@/router"
import { removeToken, removeUserInfo } from '@/utils/app'
import Vue from 'vue'
//通过该属性即可调用vue的任意属性
// Vue.prototype


//配置axios
const service = axios.create({
    baseURL: 'http://localhost:8080', // api的base_url
    timeout: 5000, // 请求超时时间
    responseType: "json",

});

// 自定义的 axios 请求拦截器 
service.interceptors.request.use((config) => {
    if (typeof (localStorage.Authorization) != 'undefined') {
        config.headers['Authorization'] = localStorage.Authorization;
    }
    return config;
}, (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
});
service.interceptors.response.use(res => {
    
    if (res.headers['authorization'] != undefined) {
        console.log("已更新token："+res.headers['authorization'])
        localStorage.setItem('Authorization', res.headers['authorization'])
    }
    return res
  },
  error => {
    alert('请求失败，请稍后重试！')
    return Promise.reject(error)
  }
  )
export default {
    get(url, params, config = {}) {
        return new Promise((resolve, reject) => {
            service.get(url, { params: params }, config)
                .then(response => {
                    console.log(response.data   )
                    // 如果token过期
                    if (response.data && response.data.code && response.data.code === -1) {
                        Vue.prototype.$alert(response.data.msg, {
                            confirmButtonText: '确定',
                            callback: action => {
                                removeToken()
                                removeUserInfo()
                                router.push({ name: 'login' })
                            }
                        });
                        // 如果非正常code      
                    } else if (response.data && response.data.code && response.data.code !== 200) {
                        Vue.prototype.$message({
                            message: '错误消息:' + response.data.msg,
                            type: 'error',
                            time: 5000
                        })
                    } else {
                        resolve(response)
                        return
                    }
                    console.error('错误消息:' + response.data.msg)
                    reject('错误消息:' + response.data.msg);
                }, err => {
                    reject(err);
                })
        })
    },
    post(url, params, config = {}) {
        return new Promise((resolve, reject) => {
            service.post(url, qs.stringify(params), config).then(response => {
                // 如果token过期
                if (response.data && response.data.code && response.data.code === -1) {
                    Vue.prototype.$alert(response.data.msg, {
                        confirmButtonText: '确定',
                        callback: action => {
                            removeToken()
                            removeUserInfo()
                            router.push({ name: 'login' })
                        }
                    });
                    reject('错误消息:' + response.data.msg);
                    // 如果非正常code      
                } else if (response.data && response.data.code && response.data.code !== 200) {
                    Vue.prototype.$message({
                        message:  response.data.msg,
                        type: 'error',
                        time: 5000
                    })
                    // reject是因为有可能要回馈关闭加载框
                    reject('错误消息:' + response.data.msg);
                } else {
                    resolve(response)
                    return
                }
                console.error('错误消息:' + response.data.msg)
                reject('错误消息:' + response.data.msg);
            }, err => {
                reject(err);
            })

        })
    },
    patch(url, params = {}) {
        return new Promise((resolve, reject) => {
            Axios.patch(url, params).then(response => {
                // 如果token过期
                if (response.data && response.data.code && response.data.code === -1) {
                    Vue.prototype.$alert(response.data.msg, {
                        confirmButtonText: '确定',
                        callback: action => {
                            removeToken()
                            removeUserInfo()
                            router.push({ name: 'login' })
                        }
                    });

                    // 如果非正常code      
                } else if (response.data && response.data.code && response.data.code !== 200) {
                    Vue.prototype.$message({
                        message: '错误消息:' + response.data.msg,
                        type: 'error',
                        time: 5000
                    })

                } else {
                    resolve(response)
                    return
                }
                console.error('错误消息:' + response.data.msg)
                reject('错误消息:' + response.data.msg);
            }, err => {
                reject(err)
            })
                .catch((error) => {
                    reject(error)
                });
        })
    },

    put(url, params = {}) {
        return new Promise((resolve, reject) => {
            Axios.put(url, { params: params }).then(response => {
                // 如果token过期
                if (response.data && response.data.code && response.data.code === -1) {
                    Vue.prototype.$alert(response.data.msg, {
                        confirmButtonText: '确定',
                        callback: action => {
                            removeToken()
                            removeUserInfo()
                            router.push({ name: 'login' })
                        }
                    });
                    // 如果非正常code      
                } else if (response.data && response.data.code && response.data.code !== 200) {
                    Vue.prototype.$message({
                        message: '错误消息:' + response.data.msg,
                        type: 'error',
                        time: 5000
                    })

                } else {
                    resolve(response);
                    return
                }
                console.error('错误消息:' + response.data.msg)
                reject('错误消息:' + response.data.msg);
            }, err => {
                reject(err);
            })
                .catch((error) => {
                    reject(error)
                });
        })

    }
}