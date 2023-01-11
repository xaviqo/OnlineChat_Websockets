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
const SESSION_NAME = 'ffchat_session';

axios.interceptors.request.use(
    async config => {
      const ls = JSON.parse(localStorage.getItem(SESSION_NAME));

      if (ls){
        config.headers = {
          'Authorization': `Bearer ${ls['tokenPayload'].accessToken}`,
          'Accept': 'application/json',
        }
      } else {
        config.headers = {
          'Accept': 'application/json',
        }
      }

      return config;
    },
    error => {
      Promise.reject(error)
    });

axios.defaults.baseURL = BASE_URL;

Vue.config.productionTip = false;

export const EventBus = new Vue();

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
