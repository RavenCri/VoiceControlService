import router from '@/router/index'
import {defaultRouter} from '@/router'
import store from '../store/index'
export function getToken(){
    return localStorage.getItem('token')
}
export function removeToken(){
    localStorage.clear('token');
    
    router.options.routes = []
    router.addRoutes([])
   
    router.options.routes = defaultRouter
    router.addRoutes(defaultRouter)
    store.commit('permission/set_roles',[] )
}
export function getUserInfo(){
    return JSON.parse(JSON.parse(localStorage.getItem('userInfo')))
}
export function removeUserInfo(){
    localStorage.clear('userInfo');
}