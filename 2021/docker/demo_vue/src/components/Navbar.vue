<template>
  <div class="navbar">
    <b-navbar toggleable="lg" type="dark" class="nav-miao">
      <b-navbar-brand href="#" @click="$router.push('/')">
        {{ $t("sms.index.title") }}
      </b-navbar-brand>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item href="#" @click="toFilter('all')">
            {{ $t("sms.navbar.allNo") }}
          </b-nav-item>
          <b-nav-item href="#" @click="toFilter('us')">
            {{ $t("sms.navbar.usNo") }}
          </b-nav-item>
          <b-nav-item href="#" @click="toFilter('uk')">
            {{ $t("sms.navbar.gbNo") }}
          </b-nav-item>
          <b-nav-item href="#" @click="toFilter('jp')">
            {{ $t("sms.navbar.jpNo") }}
          </b-nav-item>
          <b-nav-item href="#"> {{ $t("sms.navbar.contact") }}</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-item-dropdown text="Lang" right>
            <b-dropdown-item
              href="#"
              v-for="lang in langs"
              :key="lang.key"
              @click="changeLocale(lang)"
              >{{ lang.text }}</b-dropdown-item
            >
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
export default {
  name: "Navbar",
  props: {
    msg: String,
  },
  data() {
    return {
      langs: [
        {
          key: "en_us",
          text: "English",
        },
        {
          key: "zh_cn",
          text: "简体中文",
        },
        {
          key: "zh_tw",
          text: "繁體中文",
        },
        {
          key: "fr",
          text: "Français",
        },
        {
          key: "ru",
          text: "русский язык",
        },
        {
          key: "jp",
          text: "日本語",
        },
        {
          key: "th",
          text: "ภาษาไทย",
        },
      ],
    };
  },
  methods: {
    toFilter(contry) {
      this.$router.push(`/filter/${contry}`);
    },
    changeLocale(lang) {
      localStorage.setItem("locale", lang.key);
      this.$i18n.locale = lang.key;
    },
  },
};
</script>

<style lang="scss" scoped>
.navbar {
  width: 100%;
  background-color: #563d7c !important;
}
</style>
