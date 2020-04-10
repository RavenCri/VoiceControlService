import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/index'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: HelloWorld
    }
  ]
})
