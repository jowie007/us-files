 <template>
  <!--div
    class="
      flex flex-wrap flex-row
      justify-start
      place-items-center
      mx-auto
      items-start
      h-full
    "
  -->
  <div>
    <ui-tab-bar
      :modelValue="this.$store.state.activeTab"
      v-model="activeTab"
      class="hero-demo-tab-bar"
    >
      <ui-tab
        type="textWithIcon"
        icon="new_releases"
        stacked
        v-tooltip="'Sort the songs descending by upload date'"
        aria-describedby="tooltip-new-releases"
        >New</ui-tab
      >
      <ui-tab
        type="textWithIcon"
        icon="whatshot"
        stacked
        v-tooltip="
          'Sort the songs descending by most downloads in the last seven days'
        "
        aria-describedby="tooltip-hot-releases"
        >Top 50</ui-tab
      >
      <ui-tab
        type="textWithIcon"
        icon="chat"
        stacked
        v-tooltip="'Tell us what you think'"
        aria-describedby="tooltip-page-comments"
        >Comments</ui-tab
      >
    </ui-tab-bar>

    <div v-show="activeTab == 0">
      <div class="flex flex-wrap justify-center mb-28">
        <SongCard v-for="item in recentItems" :key="item" :id="item"></SongCard>
        <div v-for="i in 20" :key="i" style="width: 192px"></div>
      </div>
    </div>
    <div v-show="activeTab == 1">
      <div class="flex flex-wrap justify-center mb-28">
        <SongCard
          v-for="(item, index) in topItems"
          :key="item"
          :id="item"
          :chartPosition="index + 1"
        ></SongCard>
        <div v-for="i in 10" :key="i" style="width: 192px"></div>
      </div>
    </div>

    <CommentComponent
      itemid="-1"
      v-if="activeTab == 2"
      class="mb-28"
    ></CommentComponent>

    <div
      v-if="activeTab < 2"
      class="grid fixed bottom-10 right-10 justify-items-end"
    >
      <ui-fab
        extended
        v-on:click="
          this.$store.state.auth.user == null
            ? $alert('You need to be logged in')
            : navigate('/song/new')
        "
        v-tooltip="'Upload a new song'"
        aria-describedby="tooltip-create-new-song"
      >
        <template #before="{ iconClass }">
          <ui-icon :class="iconClass">add</ui-icon>
        </template>
        <span>Upload</span>
      </ui-fab>
    </div>
  </div>
</template>

<script>
import CommentComponent from "../components/elements/CommentComponent";
import SongCard from "../components/elements/SongCard.vue";
export default {
  data() {
    return {
      recentItems: [],
      topItems: [],
      initialActive: this.$store.state.activeTab,
      activeTab: 0,
    };
  },
  name: "Home",
  components: {
    CommentComponent,
    SongCard,
  },
  mounted() {
    fetch("http://192.168.178.66:8083/api/song/newest")
      .then((response) => response.json())
      .then((json) => {
        for (var jsonItem of json) {
          this.recentItems.push(jsonItem);
        }
      });
    fetch("http://192.168.178.66:8083/api/song/top")
      .then((response) => response.json())
      .then((json) => {
        for (var jsonItem of json) {
          this.topItems.push(jsonItem);
        }
      });
    document.getElementById("content-main").style.backgroundColor = "#fff";
  },
  methods: {
    navigate(route) {
      this.$router.push(route);
    },
  },
};
</script>
