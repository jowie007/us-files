<template>
  <div :id="id">
    <ui-card class="w-40 m-2 lg:m-4" v-if="Object.keys(item).length !== 0">
      <ui-card-content class="" v-on:click="showInfos">
        <div>
          <div v-if="chartPosition != null" class="relative">
            <div class="absolute mt-2 rounded-r-3xl bg-white w-10">
              {{ chartPosition }}
            </div>
          </div>
          <img class="w-40 h-40" :src="cover" crossorigin="anonymous" />
          <div class="px-1">
            <div class="font-semibold truncate">
              {{ item.songResponse.title }}
            </div>
            <div class="truncate">{{ item.songResponse.artist }}</div>
            <div class="truncate">{{ item.songResponse.version }}</div>
            <div class="truncate">
              Avg. Rating:
              {{
                item.songResponse.averageRating == 0
                  ? "-"
                  : item.songResponse.averageRating
              }}
            </div>
          </div>
        </div>
      </ui-card-content>
      <ui-card-actions>
        <ui-card-icons>
          <ui-icon-button
            icon="download"
            v-tooltip="'Download .txt file'"
            :aria-describedby="`tooltip-song-download-${id}-${chartPosition}`"
          ></ui-icon-button>
          <ui-icon-button
            icon="info"
            v-on:click="showInfos"
            v-tooltip="'Show detailed informations'"
            :aria-describedby="`tooltip-song-info-${id}-${chartPosition}`"
          ></ui-icon-button>
          <ui-icon-button
            v-if="!item.favorite"
            v-on:click="
              this.$store.state.auth.user == null
                ? $alert('You need to be logged in')
                : saveFavorite()
            "
            icon="favorite_border"
            v-tooltip="'Add song to favorites'"
            :aria-describedby="`tooltip-song-favorite-${id}-${chartPosition}`"
          ></ui-icon-button>
          <ui-icon-button
            v-else
            v-on:click="
              this.$store.state.auth.user == null
                ? $alert('You need to be logged in')
                : saveFavorite()
            "
            icon="favorite"
            v-tooltip="'Remove song from favorites'"
            :aria-describedby="`tooltip-song-unfavorite-${id}-${chartPosition}`"
          ></ui-icon-button>
        </ui-card-icons>
      </ui-card-actions>
    </ui-card>
  </div>
</template>


<script>
export default {
  data() {
    return {
      cover: "",
      item: {},
      downloaded: false,
    };
  },
  props: {
    id: Number,
    chartPosition: Number,
  },
  methods: {
    showInfos() {
      // Navigiert zur Route mit dem Pfad "/list"
      this.$router.push(`/song/${this.id}`);
    },
    showProfile() {
      // Navigiert zur Route mit dem Pfad "/list"
      this.$router.push(`/user/${this.creator}`);
    },
    saveData() {
      // https://jasonwatmore.com/post/2020/04/30/vue-fetch-http-post-request-examples
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userName:
            this.$store.state.auth.user == null
              ? ""
              : this.$store.state.auth.user.username,
          songId: this.item.songResponse.id,
          favorite: this.item.favorite,
          downloaded: this.downloaded,
          rating: this.item.rating,
        }),
      };
      fetch(
        `http://localhost:8080/api/song/${this.id}/${
          this.$store.state.auth.user == null
            ? ""
            : this.$store.state.auth.user.username
        }`,
        requestOptions
      ).then();
      this.downloaded = false;
    },
    saveFavorite() {
      this.item.favorite = !this.item.favorite;
      this.saveData();
    },
  },
  created() {
    fetch(
      `http://localhost:8080/api/song/${this.id}/${
        this.$store.state.auth.user == null
          ? ""
          : this.$store.state.auth.user.username
      }`
    )
      .then((response) => response.json())
      .then((json) => {
        if (this.$store.state.auth.user == null) {
          this.item.songResponse = json;
        } else {
          this.item = json;
        }
      })
      .catch(() => {
        console.log("Song-Data-Error");
      });
    // https://stackoverflow.com/questions/46002113/javascript-reactjs-display-image-with-readablestream-as-source
    fetch("http://localhost:8080/api/song/" + this.id + "/cover")
      .then((response) => response.blob())
      .then((blob) => {
        if (blob.size == 0) {
          this.cover = require("@/assets/cover_placeholder.png");
        } else {
          this.cover = URL.createObjectURL(blob);
        }
      })
      .catch(() => {
        console.log("Image-Error");
      });
  },
  mounted() {},
};
</script>