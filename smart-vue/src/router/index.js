import Vue from 'vue'
import Router from 'vue-router'
import index from '@/components/index'
import device from '@/components/device'
import center from '@/components/center'
import login from '@/components/login'
import updateInfo from '@/components/updateInfo'
import deviceManager from '@/components/manager/deviceManager'
import userManager from '@/components/manager/userManager'
import notFound from '@/components/notFound'

Vue.use(Router)
export const defaultRouter = [
  {
    path: '/',
    name: 'index',
    component: index,
    meta: {
      title: '科睿物联',
    }
  },
  {
    path: '/login',
    name: 'login',
    component: login,
    meta: {
      title: '登陆',
    }
  },
  {
    path: "/404",
    name: "notFound",
    component: notFound
  },
  {
    path: '/center',
    name: 'center',
    component: center,
    meta: {

      require: true,
      title: '设备中心',
      icon: 'el-icon-s-ticket'
    },
    children:[
      {
        // path前面不能带/
        path: 'device',
        component: device,
        meta:{
          title:'设备中心',
          icon:'el-icon-cpu',
          
        }
      },
      {
        path: 'updateInfo',
        component: updateInfo,
        meta:{
          title:'更新信息',
          icon:'el-icon-menu'
        }
      }
    ]
  },
 

]
/**
 * 动态路由
 */
export const asnycRouterMap = [
  {
    path: '/center/manager',
    component: center,
    meta:{
      role: 'manager'
    },
    children: [
      {
        path: 'deviceManager',
        component: deviceManager,
        meta: {
          title: '设备管理',
          role: 'manager:device'
        }
      },
      {
        path: 'userManager',
        component: userManager,
        meta: {
          title: '用户管理',
          role: 'manager:user',

        }
      }
    ]
  },
  // 此处需特别注意置于最底部，由于路由是动态变化的，必须把这个放入动态添加的路由里面。因为如果404排在其他路由前面，那么会导致其他路由不能正常访问
  {
    path: "*", 
    redirect: "/404",
    meta: {
      title: '页面丢了',
    }
  }
]

export default new Router({
  routes: defaultRouter
})

