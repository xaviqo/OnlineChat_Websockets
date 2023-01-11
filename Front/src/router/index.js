import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ChatView from "@/views/ChatView.vue";
import NotFound from "@/views/NotFound.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/room/:roomId',
    component: ChatView,
  },
  {
    path: '/*',
    component: NotFound,

  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

/*router.beforeEach((to, from, next) => {
  const ls = JSON.parse(localStorage.getItem('ffchat_session'));
  if (to.meta.needsAuth){
    if (ls != null && ls['tokenPayload'].accessToken && ls['tokenPayload'].refreshToken) next();
    else next("/");
  }
  next();
});*/

export default router
