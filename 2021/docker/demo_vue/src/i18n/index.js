import Vue from "vue";
import VueI18n from "vue-i18n";
Vue.use(VueI18n);

import zh_cn from "./config/zh_cn";
import zh_tw from "./config/zh_tw";
import en_us from "./config/en_us";

const i18n = new VueI18n({
  locale: localStorage.getItem("locale") || zh_cn,
  messages: {
    zh_cn,
    zh_tw,
    en_us,
  },
});

export default i18n;
