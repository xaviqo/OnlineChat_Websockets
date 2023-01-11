import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import '@babel/polyfill'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios,axios);

const BASE_URL = 'http://localhost:8080'

axios.defaults.baseURL = BASE_URL;

Vue.config.productionTip = false;

export const EventBus = new Vue();

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
