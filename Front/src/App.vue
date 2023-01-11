<template>
  <v-app>
    <header-app></header-app>
    <v-alert width="20%" dismissible elevation="1" :type="alert.type" :color="alert.color"
             transition="slide-y-transition" style="position: absolute; right: 5%; top: 10%; z-index:20001;"
             v-model="alert.show" class="text-button alert-style">
      {{ alert.msg }}
    </v-alert>
    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>
<script>
import { EventBus } from '@/main';
import HeaderApp from "@/components/HeaderApp.vue";
export default {
  name: 'App',
  created() {
    EventBus.$on('showAlert', model => {
      this.showAlert(model);
    });
  },
  data: () => ({
    alert: {
      show: false,
      color: "",
      type: null,
      msg: ""
    }
  }),
  methods: {
    showAlert(model) {
      this.alert = {
        color: model.color,
        type: model.type,
        msg: model.msg,
        show: true
      }
      setTimeout(() => this.alert.show = false, 2200);
    }
  },
  components: {
    HeaderApp
  }
};
</script>
<style>
::-webkit-scrollbar{
  width: 10px;
  background-color: #F5F5F5;
}
::-webkit-scrollbar-track{
  -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.1);
  background-color: #F5F5F5;
}
::-webkit-scrollbar-thumb{
  background-color: rgba(80, 80, 80, .3);
}
</style>
