<template>
  <div>
    <div class="text-6xl mt-4">{{ code == null ? "404" : code }}</div>
    <div>{{ message == null ? "Page not found" : message }}</div>
    <div class="mt-8">
      Maybe you want to check out this randomly chosen song:
    </div>
    <div class="w-full flex justify-center">
      <SongCard v-if="randomid != ''" :id="randomid"></SongCard>
    </div>
  </div>
</template>
<script>
import SongCard from "../../components/elements/SongCard.vue";
export default {
  data() {
    return {
      randomid: "",
    };
  },
  props: {
    message: String,
    code: String,
  },
  components: {
    SongCard,
  },
  created() {
    fetch("http://localhost:8080/api/song/randomid")
      .then((response) => response.json())
      .then((json) => {
        this.randomid = json;
      })
      .catch(() => {
        console.log("Image-Error");
      });
    if (this.message == undefined && this.code == undefined) {
      this.$store.commit("setPageTitle", "Page not found");
    }
  },
};
</script>