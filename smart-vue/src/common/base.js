import axios from 'axios'
import qs from 'qs';
//配置axios
const service = axios.create({
    baseURL: 'http://localhost:8080', // api的base_url
    timeout: 5000, // 请求超时时间
    responseType: "json",
   
});

// 自定义的 axios 请求拦截器 
service.interceptors.request.use((config) => {
    if (typeof (localStorage.token) != 'undefined') {
        config.headers['token'] = localStorage.token;
    }
    return config;
}, (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
});
export default {
    get(url, params, config = {}) {
        return new Promise((resolve, reject) => {
            service.get(url, {params:params}, config)
                .then(response => {
                    
                    resolve(response);
                }, err => {
                    reject(err);
                })
                .catch((error) => {
                    reject(error)
                })
        })
    },
    post(url, params, config = {}) {
        return new Promise((resolve, reject) => {
            service.post(url, qs.stringify(params), config).then(response => {
                resolve(response);
            }, err => {
                reject(err);
            })
            .catch((error) => {
                reject(error)
            })
        })
    },
    patch(url, params = {}){
        return new Promise((resolve,reject) => {
            Axios.patch(url, params).then(response => {
                resolve(response.data);
            },err => {
                reject(err)
            })
            .catch((error) => {
                reject(error)
            });
        })
    },

    put(url, params = {}) {
        return new Promise((resolve, reject) => {
            Axios.put(url, { params: params}).then(response => {
                resolve(response.data);
            }, err => {
                reject(err);
            })
            .catch((error) => {
                reject(error)
            });
        })

    }
}