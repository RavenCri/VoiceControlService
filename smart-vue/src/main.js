// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router/index'
import fontawesome from '@fortawesome/fontawesome'
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import solid from '@fortawesome/fontawesome-free-solid'
import regular from '@fortawesome/fontawesome-free-regular'
import brands from '@fortawesome/fontawesome-free-brands'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import './router/premit'

Vue.config.productionTip = false


/*
安装awesome
npm i --save @fortawesome/fontawesome-svg-core
npm i --save @fortawesome/vue-fontawesome
npm i --save @fortawesome/free-solid-svg-icons
npm i --save @fortawesome/free-regular-svg-icons
npm i --save @fortawesome/free-brands-svg-icons

*/
//配置fontawesome
fontawesome.library.add(solid);
fontawesome.library.add(regular);
fontawesome.library.add(brands);
Vue.component('font-awesome-icon', FontAwesomeIcon);
//配置elementUI
Vue.use(ElementUI);


new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

