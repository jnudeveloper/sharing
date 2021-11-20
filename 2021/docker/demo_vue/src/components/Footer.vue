<template>
  <div class="foorer">
    <footer>
      <div class="base">
        <p><a href="#">@ 2021 HAPPY CAT CLOUD TOOLS</a></p>
        <p><a href="#">Site Map</a></p>
        <p><a href="#">More Tools</a></p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: "Footer",
  props: {
    msg: String,
  },
};
</script>

<style lang="scss" scoped>
footer {
  height: 5rem;
  background-color: #343a40;
  color: #ffffff;
  text-align: center;
  .base {
    padding-top: 1.5rem;
    p {
      display: inline;
      padding-right: 1rem;
      a {
        color: #ffffff !important;
        text-decoration-line: revert;
      }
    }
  }
}
</style>
