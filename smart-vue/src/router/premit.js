
import router from '../router/index'
import store from '../store/index'
import { getToken } from '@/utils/app'
import { asnycRouterMap, defaultRouter } from '../router/index'
router.beforeEach((to, from, next) => {
    console.log(to.path)
    if (to.meta.title) {
        document.title = to.meta.title
    }
    let basePage = false
    // 判断是否是公共页面
    for (let index = 0; index < defaultRouter.length; index++) {
        let element = defaultRouter[index];
        if (element['path'] == to.path) {
            basePage = true
        }
        if (element['children']) {
            for (let j = 0; j < element['children'].length; j++) {
                let el = element['children'][j];
                if (element['path'] + "/" + el['path'] == to.path) {
                    basePage = true
                }
            }
        }
    }
    //如果登陆了
    if (getToken() !== null) {

        // 如果还没有获取到路由权限则先去获取
        if (store.getters['permission/roles'].length === 0) {
            console.log('路由权限为空')
            // 获取用户路由权限
            store.dispatch('permission/getRouters').then(role => {

                // 如果未获取到权限
                // if(role.length ==0){  
                //     next()
                //     return
                // }
                // 如果不是公共页面
                if (!basePage) {
                    let hasPermission = false
                  
                    // 判断是否有要访问路径的权限
                    for (let index = 0; index < asnycRouterMap.length; index++) {
                        const element = asnycRouterMap[index];
                        if (element['path'] == to.path && role.includes(element['meta']['role'])) {
                            hasPermission = true
                            break
                        }
                        if (element['children']) {
                            for (let j = 0; j < element['children'].length; j++) {
                                const el = element['children'][j];
                                if (element['path'] +'/'+el['path'] == to.path && role.includes(element['meta']['role'])) {
                                    hasPermission = true
                                    break
                                }
                            }
                        }
                    }
                   
                    // 如果没有权限
                    if (!hasPermission && to.path != '/404') {
                        next('/404');
                        return
                    }
                    else { 
                        next()     
                    }
                }else{
                    // 若是公共页面直接跳转
                    next() 
                    
                }

                // 保存获取到的路由
                store.dispatch('permission/createRouter', role).then(addRouter => {
                    let allRouter = store.getters['permission/allRouter']
                    router.options.routes = allRouter
                    router.addRoutes(addRouter)

                    next({ ...to })

                })
            })
        } else {
            next()
        }
    } else {
        // to.matched 会拿到该路由的所有地址，调用some方法如果有一个匹配需要登陆则返回true
        if (to.matched.some(record => record.meta.require)) {
            //如果需要登陆
            next('/login')
        } else {
            next()
        }
    }
})