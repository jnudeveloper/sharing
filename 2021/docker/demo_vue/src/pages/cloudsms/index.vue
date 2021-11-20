<template>
  <div>
    <Navbar />

    <b-container class="bv-example-row phone-list-container">
      <b-row v-if="!phoneList" class="loading">{{
        $t("sms.index.loadingText")
      }}</b-row>
      <b-row v-else-if="phoneList.length === 0" class="fail">{{
        $t("sms.index.loadFail")
      }}</b-row>
      <b-row v-else>
        <b-col
          v-for="phone in phoneList"
          :key="phone.phone"
          sm="6"
          md="4"
          lg="3"
        >
          <b-card
            :title="`${phone.area} ${phone.phone}`"
            :img-src="phone.areaMapping.img"
            img-alt="Image"
            img-top
            tag="article"
            style="max-width: 20rem"
            class="mb-2 phone-list-item"
          >
            <b-card-text> {{ phone.areaMapping.countryText }} </b-card-text>
            <b-button @click="toDetail(phone)" variant="primary">{{
              $t("sms.index.receive")
            }}</b-button>
          </b-card></b-col
        >
      </b-row>

      <!-- <div class="pagination">
        <div>
          <b-pagination
            v-model="currentPage"
            :total-rows="rows"
            :per-page="perPage"
            aria-controls="my-table"
          ></b-pagination>
        </div>
      </div> -->
    </b-container>
    <Footer />
    <!-- <footer class="footer">
      <a href="#">版权所有@猫咪云工具</a>
    </footer> -->
  </div>
</template>

<script>
import Footer from "@/components/Footer";
import Navbar from "@/components/Navbar";
export default {
  watch: {
    $route() {
      this.$router.go(0);
    },
  },
  components: { Footer, Navbar },

  data() {
    return {
      currentPage: 1,
      rows: 100,
      perPage: 20,
      foods: [
        { value: null, text: "Choose..." },
        { value: "apple", text: "Apple" },
        { value: "orange", text: "Orange" },
      ],
      phoneList: null,
      queryForm: {
        country: null,
        page: 1,
        lmit: 16,
        sort: "random",
      },
    };
  },
  methods: {
    toDetail(phone) {
      this.$router.push(`/detail/${phone.area}/${phone.phone}`);
    },
    getList() {
      setTimeout(()=>{
        this.phoneList = [];
      },2000)
    },
  },

  mounted() {
    if (this.$route.params.country && "all" != this.$route.params.country) {
      this.queryForm.country = this.$route.params.country;
    }
    this.getList();
  },
};
</script>

<style lang="scss">
.loading {
  color: azure;
  width: 100%;
  padding: 10% 0;
  font-size: 2rem;
  text-align: center;
  display: block;
}

.fail {
  color: crimson;
  width: 100%;
  padding: 10% 0;
  font-size: 2rem;
  text-align: center;
  display: block;
}

.pagination {
  width: 100%;
  text-align: center;
}
.pagination ul {
  text-align: center;
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
/* .footer {
  height: 2rem;
  background: black;
  text-align: center;
  vertical-align: 1rem;
}
.footer a {
  margin-top: 1rem;
  color: white;
} */
</style>
