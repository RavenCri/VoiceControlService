import {getUserRole} from '@/common/api/user'
import {defaultRouter,asnycRouterMap} from '@/router'
function hasPermission(roles, route) {
    if (route.meta && route.meta.role) {
        return roles.some(role => route.meta.role==role)
    } else {
      return true
    }
  }
const state = {
    roles:[],
    allRouter:defaultRouter,
    addRouter:[]
}

const getters = {
    roles:state => state.roles,
    allRouter:state => state.allRouter,
    addRouter:state => state.addRouter
}
/**
 * 普通方法 store.commit
 */
const mutations = {
    set_roles(state,value){
        state.roles = value;
    },
    set_router(state,router){
        state.addRouter = router
        state.allRouter = defaultRouter.concat(router)
       
    }

}
/**
 * 处理异步请求 store.dispatch
 */
const actions = {
    getRouters({commit},requestData){
        return new Promise((resolve,reject) =>{
            getUserRole().then(response=>{
                let role = response.data.data
                commit('set_roles',role)
                resolve(role)
            }).catch(err=>{
                console.log(err)
                
            })
        })
    },
    /**
     * 创建动态路由
     * @param {*} param0 
     * @param {*} role 
     */
    createRouter({commit},data){
        return new Promise((resolve,reject) =>{
            let roles = data
            const addRouter  = asnycRouterMap.filter(item =>{
                //if(hasPermission(roles,item)){
                    if (item.children && item.children.length > 0) {
                        item.children = item.children.filter(child => {
                          if (hasPermission(roles, child)) {
                              //console.log(child)
                            return child
                          }
                          return false;
                        });
                        return item
                      } else {
                        return item
                      }
                //}
               // return false
            })
            
            // 更新路由
            commit('set_router',addRouter)
            resolve(addRouter)
        })
    }
}

export default{
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}