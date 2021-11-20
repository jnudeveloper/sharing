import "@babel/polyfill";
import "mutationobserver-shim";
import Vue from "vue";
import "./plugins/bootstrap-vue";
import App from "./App.vue";
import router from "./router";
import axios from "axios";
import VueAxios from "vue-axios";
import i18n from "./i18n/index";

Vue.config.productionTip = false;

Vue.use(VueAxios, axios);
new Vue({
  el: "#app",
  router,
  i18n,
  render: (h) => h(App),
});
