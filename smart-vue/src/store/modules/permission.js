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

const mutations = {
    set_roles(state,value){
        state.roles = value;
    },
    set_router(state,router){
        state.addRouter = router
        state.allRouter = defaultRouter.concat(router)
       
    }

}

const actions = {
    getRouters({commit},requestData){
        return new Promise((resolve,reject) =>{
            getUserRole().then(response=>{
                let role = response.data.data
                commit('set_roles',role)
                resolve(role)
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
                // 需要权限的路由 如果在 获取的权限路由里面
                // 意思就是 只有当该用户有该路由的权限时，才去增加
                if(role.includes(item.meta.role) ){ 
                    return item
                } 
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