 <template>
  <div>
    <div
      class="
        flex flex-wrap flex-row
        justify-center
        place-items-center
        mx-auto
        items-start
      "
    >
      <SongTable
        :key="key"
        :searchString="searchString"
        :onlyFavorites="filterFavorites"
        :includeUnfinished="includeUnfinished"
      ></SongTable>
    </div>
  </div>
</template>

<script>
import SongTable from "../components/elements/SongTable.vue";
export default {
  data() {
    return {
      items: [],
      keyId: 0,
      searchString: "",
      filterFavorites: false,
      includeUnfinished: false,
    };
  },
  name: "Search",
  components: {
    SongTable,
  },
  created() {
    this.checkData();
  },
  // https://stackoverflow.com/questions/43270159/how-to-watch-store-values-from-vuex
  computed: {
    key() {
      console.log("searchpage-" + this.keyId);
      return "searchpage-" + this.keyId;
    },
  },
  watch: {
    searchString() {
      this.keyId = this.keyId + 1;
      console.log(this.keyId);
    },
    "$route.query": {
      handler: function () {
        this.checkData();
        console.log("Checked")
      },
      deep: true,
      immediate: true,
    },
  },
  mounted() {
    document.getElementById("content-main").style.backgroundColor = "#fff";
  },
  methods: {
    checkData() {
      if (this.$route.query.searchString != null) {
        this.searchString = this.$route.query.searchString;
      }
      if (this.$route.query.filterFavorites != null) {
        this.filterFavorites = this.$route.query.filterFavorites;
      }
      if (this.$route.query.includeUnfinished != null) {
        this.includeUnfinished = this.$route.query.includeUnfinished;
      }
    },
  },
};
</script>
