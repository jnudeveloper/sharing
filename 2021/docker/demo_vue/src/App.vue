<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
  components: {},
  created() {
    if (!localStorage.getItem("locale")) {
      //根據IP分配不同的語言
      localStorage.setItem("locale", "en_us");
      this.$i18n.locale = "en_us";
    }
  },
};
</script>

<style>
body {
  background-color: #343a40 !important;
  min-height: 100vh;
}
</style>
