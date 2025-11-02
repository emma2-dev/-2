import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from './utils/request'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import markdownit from 'markdown-it'

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(mavonEditor)

// 将axios挂载到Vue原型
Vue.prototype.$axios = axios

// 将 markdown-it 挂载到 Vue 原型，供渲染 markdown 使用
const md = markdownit({
  breaks: true,
  html: true
})
Vue.prototype.$markdownit = md

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')


