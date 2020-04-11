import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/index'
import device from '@/components/device'
import login from '@/components/login'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: HelloWorld
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/device',
      name: 'device',
      component: device
    }
  ]
})
