import Vue from 'vue'
import Router from 'vue-router'
import index from '@/components/index'
import device from '@/components/device'
import center from '@/components/center'
import login from '@/components/login'
import updateInfo from '@/components/updateInfo'
import deviceManager from '@/components/manager/deviceManager'
import userManager from '@/components/manager/userManager'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: index
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/center',
      name: 'center',
      component: center,
      children:[
        {
          // path前面不能带/
          path: 'device',
          component: device
        },
        {
          path: 'updateInfo',
          component: updateInfo
        },
        {
          path: 'deviceManager',
          component: deviceManager
        },
        {
          path: 'userManager',
          component: userManager
        }
      ]
    },
    
  ]
})
