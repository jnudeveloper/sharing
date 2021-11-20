<template>
  <div>
    <Navbar />

    <b-container class="bv-example-row phone-list-container">
      <div class="phone-info">
        <p>
          <label
            >{{ $t("sms.detail.phoneNumber") }}： {{ area }}{{ phone }}</label
          >
        </p>
        <p>
          <label
            >{{ $t("sms.detail.phoneCountry") }}：
            {{ phoneInfo.areaMapping.countryText }}</label
          >
        </p>
        <p>
          <label>{{ $t("sms.detail.phoneState") }}：</label>
          <b>{{ $t("sms.detail.phoneOnline") }}</b>
        </p>
        <p>
          <b-button
            v-if="loading"
            type="button"
            variant="primary"
            disabled=""
            >{{ $t("sms.detail.loadingSmsListBtnText") }}</b-button
          >
          <b-button
            v-else
            type="button"
            variant="primary"
            @click="getMessageList"
            >{{ $t("sms.detail.refreshSmsListBtnText") }}</b-button
          >
        </p>
      </div>
      <b-table
        small
        :fields="fields"
        :items="messageList"
        responsive="sm"
        class="table-msg"
      >
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>

        <template #cell(msg)="data">
          <b class="text-info">
            {{ data.item.prefix
            }}<img :src="`data:;base64,${data.item.code}`" />{{
              data.item.suffix
            }}
          </b>
        </template>
      </b-table>
    </b-container>
    <Footer />
  </div>
</template>

<script>
import Footer from "@/components/Footer";
import Navbar from "@/components/Navbar";
import AreaNumberMapping from "@/utils/AreaNumberMapping";
import i18n from "@/i18n/index";
export default {
  components: { Footer, Navbar },
  data() {
    return {
      loading: false,
      phone: "",
      area: "",
      phoneInfo: {
        areaMapping: {
          countryText: "",
        },
      },
      fields: [
        { key: "index", label: "#" },
        { key: "msg", label: i18n.tc("sms.detail.tableHeadeSmsContent") },
      ],
      messageList: [],
    };
  },

  methods: {
    getPhoneInfo() {},
    getMessageList() {
      this.messageList = [];
    },
  },
  mounted() {
    if (this.$route.params.phone) {
      this.phone = this.$route.params.phone;
    }
    if (this.$route.params.area) {
      this.area = this.$route.params.area;
    }

    this.phoneInfo.phone = this.phone;
    this.phoneInfo.area = this.area;
    // eslint-disable-next-line no-debugger
    this.phoneInfo.areaMapping = AreaNumberMapping.getAreaMapping(
      this.phoneInfo.area.trim()
    );

    this.getMessageList();
  },
};
</script>

<style lang="scss">
.phone-info {
  color: #d6d8db;
}

.b-table {
  color: #d6d8db !important;
}
.random-phone {
  font-size: 0.8rem;
}
.phone-list-container {
  padding-top: 1rem;
}
.phone-list-item {
  display: block;
  text-align: center;
}
.phone-list-item img {
  text-align: center;
  width: 10rem;
  padding-top: 1rem;
}
.phone-list-item .card-text {
  font-size: 1.5rem;
  color: #9e9e9e;
}

/*footer*/
.footer {
  height: 2rem;
  background: black;
  text-align: center;
  vertical-align: 1rem;
}
.footer a {
  margin-top: 1rem;
  color: white;
}
</style>
