<template>
  <div>
    <div v-if="jsonError == ''">
      <ui-tab-bar
        :modelValue="activeTab"
        v-model="activeTab"
        class="hero-demo-tab-bar"
      >
        <ui-tab type="textWithIcon" icon="info" stacked>Info</ui-tab>
        <ui-tab type="textWithIcon" icon="chat" stacked>Comments</ui-tab>
        <ui-tab
          v-show="isEditor || isCreator"
          type="textWithIcon"
          icon="subject"
          stacked
          >Songfile</ui-tab
        >
      </ui-tab-bar>
      <div v-show="activeTab == 0 && filled">
        <div class="flex flex-wrap lg:flex-nowrap items-center justify-center">
          <div
            class="
              flex flex-wrap flex-col
              w-full
              lg:w-1/2
              justify-center
              items-center
              px-4
              mt-4
              mb-20
            "
          >
            <ui-card outlined class="min-h-80 w-full">
              <ui-card-content>
                <div class="flex">
                  <img
                    id="coverId"
                    class="w-60 h-60"
                    :src="cover"
                    crossorigin="anonymous"
                  />
                  <div v-if="filled" class="text-left ml-4">
                    <div class="flex">
                      <div class="flex-shrink-0 w-40">Title:</div>
                      <div class="flex">
                        {{ selectedItem.songResponse.title }}
                      </div>
                    </div>
                    <div class="flex">
                      <div class="flex-shrink-0 w-40">Artist:</div>
                      <div class="flex">
                        {{ selectedItem.songResponse.artist }}
                      </div>
                    </div>
                    <div class="flex">
                      <div class="flex-shrink-0 w-40">Version:</div>
                      <div class="flex">
                        {{ selectedItem.songResponse.version }}
                      </div>
                    </div>
                    <div class="flex">
                      <div class="w-40">Creation date:</div>
                      {{
                        selectedItem.songResponse.finishedDate == null
                          ? "-"
                          : new Date(
                              selectedItem.songResponse.finishedDate
                            ).toLocaleDateString(undefined, dateOptions)
                      }}
                    </div>
                    <div class="flex">
                      <div class="w-40">Last update:</div>
                      {{
                        selectedItem.songResponse.updatedDate == null
                          ? "-"
                          : new Date(
                              selectedItem.songResponse.updatedDate
                            ).toLocaleDateString(undefined, dateOptions)
                      }}
                    </div>
                    <div class="flex">
                      <div class="w-40">Your last download:</div>
                      {{
                        selectedItem.downloadDate == null
                          ? "-"
                          : new Date(
                              selectedItem.downloadDate
                            ).toLocaleDateString(undefined, dateOptions)
                      }}
                    </div>
                    <div class="flex items-center">
                      <div class="w-40">Downloads:</div>
                      <div>
                        {{ selectedItem.songResponse.downloads }}
                      </div>
                    </div>
                    <div class="flex items-center">
                      <div class="w-40">Average rating:</div>
                      <div>
                        {{
                          selectedItem.songResponse.averageRating == 0
                            ? "-"
                            : selectedItem.songResponse.averageRating
                        }}
                      </div>
                    </div>
                  </div>
                </div>
                <ui-list-divider></ui-list-divider>
                <div v-if="filled" class="flex justify-center items-center">
                  <div class="flex items-center">
                    Creator:
                    <ui-icon
                      v-if="
                        profileImages[selectedItem.songResponse.creator] !=
                          undefined &&
                        profileImages[selectedItem.songResponse.creator] == ''
                      "
                      class="ml-1 cursor-pointer"
                      v-on:click="
                        navigate(`/user/${selectedItem.songResponse.creator}`)
                      "
                      v-tooltip="`${selectedItem.songResponse.creator}`"
                      :aria-describedby="`tooltip-user-${selectedItem.songResponse.creator}`"
                      >face</ui-icon
                    >
                    <img
                      v-if="
                        profileImages[selectedItem.songResponse.creator] !=
                          undefined &&
                        profileImages[selectedItem.songResponse.creator] != ''
                      "
                      v-on:click="
                        navigate(`/user/${selectedItem.songResponse.creator}`)
                      "
                      v-tooltip="`${selectedItem.songResponse.creator}`"
                      :aria-describedby="`tooltip-user-${selectedItem.songResponse.creator}`"
                      id="imageId"
                      class="ml-1 w-5 h-5 rounded-full cursor-pointer"
                      crossorigin="anonymous"
                      :src="profileImages[selectedItem.songResponse.creator]"
                    />
                  </div>
                  <div
                    v-if="selectedItem.songResponse.editors.length > 0"
                    class="flex ml-2"
                  >
                    Editors:
                    <div
                      class="flex items-center"
                      v-for="editor in selectedItem.songResponse.editors"
                      :key="editor"
                    >
                      <ui-icon
                        v-if="
                          profileImages[editor] != undefined &&
                          profileImages[editor] == ''
                        "
                        class="ml-1"
                        v-tooltip="`${editor}`"
                        :aria-describedby="`tooltip-user-${editor}`"
                        v-on:click="navigate(`/user/${editor}`)"
                        >face</ui-icon
                      >
                      <img
                        v-if="
                          profileImages[editor] != undefined &&
                          profileImages[editor] != ''
                        "
                        v-on:click="navigate(`/user/${editor}`)"
                        v-tooltip="`${editor}`"
                        :aria-describedby="`tooltip-user-${editor}`"
                        id="imageId"
                        class="ml-1 w-5 h-5 rounded-full"
                        crossorigin="anonymous"
                        :src="profileImages[editor]"
                      />
                    </div>
                  </div>
                </div>
                <ui-list-divider></ui-list-divider>
                <div class="flex justify-center items-center h-20">
                  <ui-button class="m-2" raised v-on:click="downloadTextFile()"
                    >Download</ui-button
                  >
                  <ui-button
                    class="m-2"
                    v-if="isCreator"
                    v-on:click="navigateToEdit()"
                    >Edit</ui-button
                  >
                  <ui-button class="m-2" v-if="isCreator" @click="open = true"
                    >Invite Editor</ui-button
                  >

                  <ui-button
                    class="m-2"
                    v-on:click="
                      this.$store.state.auth.user == null
                        ? $alert('You need to be logged in')
                        : navigateToNew()
                    "
                    >Upload new version</ui-button
                  >
                  <div
                    v-if="
                      !isCreator &&
                      !isEditor &&
                      this.selectedItem.songResponse != null &&
                      this.selectedItem.songResponse.percentage == 100
                    "
                    class="flex mt-1 items-center"
                  >
                    <div v-for="index in 5" :key="index">
                      <ui-icon
                        v-if="
                          (hoverRating == 0 ? userRating : hoverRating) < index
                        "
                        v-on:click="
                          this.$store.state.auth.user == null
                            ? $alert('You need to be logged in')
                            : handleRating(index)
                        "
                        class="cursor-pointer"
                        >star_border</ui-icon
                      >
                      <ui-icon
                        v-if="
                          (hoverRating == 0 ? userRating : hoverRating) >= index
                        "
                        v-on:click="
                          this.$store.state.auth.user == null
                            ? $alert('You need to be logged in')
                            : handleRating(index)
                        "
                        class="cursor-pointer"
                        >star</ui-icon
                      >
                    </div>
                  </div>
                  <ui-icon-button
                    v-if="!selectedItem.favorite"
                    v-on:click="
                      this.$store.state.auth.user == null
                        ? $alert('You need to be logged in')
                        : saveFavorite()
                    "
                    icon="favorite_border"
                  ></ui-icon-button>
                  <ui-icon-button
                    v-else
                    v-on:click="saveFavorite()"
                    icon="favorite"
                  ></ui-icon-button>
                </div>
              </ui-card-content>
            </ui-card>
          </div>
          <br />
          <div v-if="embedUrl != ''" class="w-full lg:w-1/2 px-4 mt-4 mb-20">
            <iframe
              v-if="linkType == 'spotify'"
              class="w-full rounded-md"
              style="height: 345.6px"
              :src="embedUrl"
              width="560"
              title="Spotify player"
              frameborder="0"
              allowtransparency="true"
            ></iframe>
            <iframe
              v-if="linkType == 'youtube'"
              class="w-full rounded-md"
              style="height: 345.6px"
              :src="embedUrl"
              width="560"
              title="YouTube video player"
              frameborder="0"
              allowfullscreen
            ></iframe>
          </div>
        </div>
      </div>
      <CommentComponent
        class="mb-20"
        v-show="activeTab == 1"
        :itemid="`${id}`"
      ></CommentComponent>
      <div
        class="mt-4 w-full h-full pb-20"
        v-show="(isEditor || isCreator) && activeTab == 2"
      >
        <ui-button class="m-2" raised v-on:click="markAsUpdated()"
          >Mark song as updated</ui-button
        >
        <div class="m-auto my-4 sm:w-9/12 h-full" id="examplePadBasic"></div>
      </div>
      <div class="fixed w-full bottom-0">
        <ui-select
          v-if="filled"
          id="full-func-js-select"
          v-model="selected"
          :options="options"
          class="w-full"
          @selected="onSelected($event)"
        >
          Version
        </ui-select>
      </div>
      <ui-dialog v-model="open" fullscreen>
        <ui-dialog-title>Invite Editor</ui-dialog-title>
        <ui-dialog-content>
          <ui-textfield v-model="newEditorName"> Editor Name </ui-textfield>
        </ui-dialog-content>
        <ui-dialog-actions>
          <ui-button @click="inviteEditor()">Invite</ui-button>
        </ui-dialog-actions>
      </ui-dialog>
    </div>
    <ErrorComponent
      v-else
      :code="jsonError.code"
      :message="jsonError.message"
    ></ErrorComponent>
  </div>
</template>

<script>
import $ from "jquery";
import * as etherpad from "../js/etherpad.js";
import CommentComponent from "../components/elements/CommentComponent";
import ErrorComponent from "../components/elements/ErrorComponent";
// https://lokeshdhakar.com/projects/color-thief/#getting-started
import ColorThief from "colorthief";
import colorFile from "../js/colors.js";
//var tinycolor = require("tinycolor2");
export default {
  data() {
    return {
      newEditorName: "",
      open: false,
      activeTab: 0,
      openLinkInfo: false,
      cover: "",
      colors: colorFile,
      id: this.$route.params.id,
      items: [],
      options: [],
      selected: "",
      selectedItem: "",
      linkType: "",
      filled: false,
      userRating: 0,
      hoverRating: 0,
      isEditor: false,
      isCreator: false,
      downloaded: false,
      embedUrl: "",
      dateOptions: { year: "numeric", month: "2-digit", day: "2-digit" },
      route: this.$route,
      jsonError: "",
      accentColor: "",
      profileImages: {},
    };
  },
  components: {
    CommentComponent,
    ErrorComponent,
  },
  created() {
    if (this.$route.query.initActiveTab != null) {
      this.activeTab = this.$route.query.initActiveTab;
    }
  },
  mounted() {
    this.onMounted();
  },
  watch: {
    route() {
      this.id = this.$route.params.id;
    },
    activeTab() {
      if (this.activeTab == 0) {
        if (this.accentColor != "") {
          document.getElementById("content-main").style.backgroundColor =
            this.accentColor;
        }
      } else {
        document.getElementById("content-main").style.backgroundColor = "#fff";
      }
    },
  },
  methods: {
    onMounted() {
      this.getData();
    },
    initEtherpad(position) {
      etherpad;

      // Alternativer Code um die Farbe anhand des Username-Strings zu bestimmen
      /*var username = this.$store.state.auth.user.username;
      var rgb = [0, 0, 0];
      for (var position in username) {
        console.log(username[position].charCodeAt(0));
        // https://stackoverflow.com/questions/94037/convert-character-to-ascii-code-in-javascript
        rgb[position % 3] += username[position].charCodeAt(0);
      }
      for (var i = 0; i < 3; i++) {
        rgb[i] = rgb[i] % 255;
      }
      function rgbToHex(r, g, b) {
        return (
          "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1)
        );
      }
      console.log(rgbToHex(rgb[0], rgb[1], rgb[2]));*/

      // https://github.com/ether/etherpad-lite-jquery-plugin
      $("#examplePadBasic").pad({
        padId: this.$route.params.id,
        host: "http://localhost:9001",
        userName: this.$store.state.auth.user.username,
        userColor: this.colors[position % this.colors.length],
        height: 1000,
        showLineNumbers: true,
      });
    },
    navigate(route) {
      this.$router.push(route);
    },
    navigateToNew() {
      // https://stackoverflow.com/questions/40382388/how-to-set-url-query-params-in-vue-with-vue-router
      this.$router.push({ path: "new", query: { parentSongId: this.id } });
    },
    navigateToEdit() {
      // https://stackoverflow.com/questions/40382388/how-to-set-url-query-params-in-vue-with-vue-router
      this.$router.push(`/song/edit/${this.id}`);
    },
    markAsUpdated() {
      fetch(`http://localhost:8080/api/song/${this.id}/updated`).then(() => {
        this.getData();
        this.$toast("Successfuly updated!");
      });
    },
    inviteEditor() {
      var makerequest = true;
      if (this.newEditorName.trim() == this.$store.state.auth.user.username) {
        this.$toast("You cannot invite yourself!");
        makerequest = false;
      } else if (this.newEditorName.trim() == "") {
        this.$toast("You must insert a name!");
        makerequest = false;
      } else {
        for (var editor in this.selectedItem.songResponse.editors) {
          if (
            this.selectedItem.songResponse.editors[editor] ==
            this.newEditorName.trim()
          ) {
            this.$toast(`${this.newEditorName.trim()} is already an editor!`);
            makerequest = false;
          }
        }
      }
      if (makerequest) {
        fetch(
          `http://localhost:8080/api/song/${this.id}/editor/add/${this.newEditorName}`
        )
          .then((response) => response.json())
          .then((json) => {
            if (json) {
              this.open = false;
              this.$toast(`${this.newEditorName} is now an editor!`);
            } else {
              this.$toast("User not found");
            }
          })
          .catch(() => {
            this.error = true;
          });
      }
    },
    getData() {
      this.items = [];
      this.options = [];
      this.filled = false;
      this.selectedItem = "";
      // TODO Nicht alle Daten zum Creator in Response anzeigen
      fetch(
        `http://localhost:8080/api/song/${this.id}/${
          this.$store.state.auth.user == null
            ? ""
            : this.$store.state.auth.user.username + "/"
        }versions`
      )
        .then((response) => response.json())
        .then((json) => {
          if (json.error == undefined) {
            for (var jsonItem of json) {
              this.items.push(jsonItem);
              this.options.push({
                label: jsonItem.songResponse.version,
                value: jsonItem.songResponse.version,
              });
              if (jsonItem.songResponse.id == this.id) {
                jsonItem.rating == null
                  ? (this.userRating = 0)
                  : (this.userRating = jsonItem.rating);
                this.selected = jsonItem.songResponse.version;
                this.selectedItem = jsonItem;
                this.$store.commit(
                  "setPageTitle",
                  jsonItem.songResponse.artist +
                    " - " +
                    jsonItem.songResponse.title
                );
                this.checkRights();
              }
            }
            this.filled = true;
            this.getProfileImage(this.selectedItem.songResponse.creator);
            for (let index in this.selectedItem.songResponse.editors) {
              this.getProfileImage(
                this.selectedItem.songResponse.editors[index]
              );
            }
            this.getCover();
            this.checkLink();
          } else {
            this.jsonError = json;
            this.$store.commit("setPageTitle", "Song not found");
          }
        })
        .catch((e) => {
          console.log(e);
        });
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
          songId: this.selectedItem.songResponse.id,
          rating: this.userRating,
          downloaded: this.downloaded,
          favorite: this.selectedItem.favorite,
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
    },
    saveFavorite() {
      this.selectedItem.favorite = !this.selectedItem.favorite;
      this.saveData();
    },
    handleRating(value) {
      if (this.userRating == value) {
        this.userRating = 0;
      } else {
        this.userRating = value;
      }
      this.saveData();
      this.getData();
    },
    fillColor() {
      // https://stackoverflow.com/questions/53634399/vue-js-this-is-overridden-with-image-onload-this
      var tempThis = this;
      document.getElementById("coverId").onload = function () {
        // Cross Origin Fehlende Berechtigung für Bild
        // https://stackoverflow.com/questions/62791092/colorthief-getcolor-is-not-a-function
        const colorThief = new ColorThief();
        const maincolor = colorThief.getColor(this);
        // https://stackoverflow.com/questions/47022484/in-js-find-the-color-as-if-it-had-0-5-opacity-on-a-white-background
        const rgbPieceToHexPiece = function (val) {
          var opacity = 1;
          const hex = val.toString(16);
          const res = hex.length === 1 ? "0" + hex : hex;
          return Math.floor(
            Number("0x" + res) * opacity + 0xff * (1 - opacity)
          );
        };
        var r = rgbPieceToHexPiece(maincolor[0]);
        var g = rgbPieceToHexPiece(maincolor[1]);
        var b = rgbPieceToHexPiece(maincolor[2]);
        tempThis.accentColor = "rgb(" + r + "," + b + "," + g + ")";
        if (tempThis.activeTab == 0) {
          document.getElementById("content-main").style.backgroundColor =
            tempThis.accentColor;
        }
      };
    },
    downloadTextFile() {
      window.open(
        "http://localhost:9001/p/" +
          this.selectedItem.songResponse.id +
          "/export/txt",
        "_self"
      );
      this.downloaded = true;
      this.saveData();
      this.downloaded = false;
    },
    checkRights() {
      this.isCreator = false;
      this.isEditor = false;
      if (this.$store.state.auth.user != null) {
        if (
          this.selectedItem.songResponse.creator ==
          this.$store.state.auth.user.username
        ) {
          this.isCreator = true;
          this.initEtherpad(0);
        } else {
          for (var index in this.selectedItem.songResponse.editors) {
            if (
              this.selectedItem.songResponse.editors[index] ==
              this.$store.state.auth.user.username
            ) {
              this.isEditor = true;
              this.initEtherpad(index + 1);
            }
          }
        }
      }
    },
    onSelected(selected) {
      if (this.filled == true) {
        this.selected = selected.value;
        for (var item of this.items) {
          if (item.songResponse.version == selected.value) {
            this.selectedItem = item;
          }
        }
      }
      this.navigate(`/song/${this.selectedItem.songResponse.id}`);
      this.id = this.selectedItem.songResponse.id;
      this.onMounted();
    },
    getCover() {
      // https://stackoverflow.com/questions/46002113/javascript-reactjs-display-image-with-readablestream-as-source
      fetch("http://localhost:8080/api/song/" + this.id + "/cover")
        .then((response) => response.blob())
        .then((blob) => {
          if (blob.size == 0) {
            this.cover = require("@/assets/cover_placeholder.png");
          } else {
            this.cover = URL.createObjectURL(blob);
          }
          document.getElementById("coverId").src = this.cover;
          this.fillColor();
        })
        .catch(() => {
          console.log("Image-Error");
        });
    },
    checkLink() {
      // https://stackoverflow.com/questions/6618967/php-how-to-check-whether-the-url-is-youtubes-or-vimeos
      let url = this.selectedItem.songResponse.link;
      if (
        url.includes("www.youtube.com") ||
        url.includes("youtube.com") ||
        url.includes("youtu.be")
      ) {
        this.linkType = "youtube";
        // https://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript
        const urlSearchParams = new URLSearchParams(url);
        const params = Object.fromEntries(urlSearchParams.entries());
        // https://stackoverflow.com/questions/909003/getting-the-first-index-of-an-object
        for (let param in params) {
          // https://www.youtube.com/embed/fWNaR-rxAic
          this.embedUrl = "https://www.youtube.com/embed/" + params[param];
        }
      } else if (url.includes("open.spotify.com")) {
        let urlParts = url.split("/");
        for (let urlPart in urlParts) {
          // https://open.spotify.com/embed/track/2X9fsxb6O6bYEopJYmUbNC?utm_source=generator
          this.embedUrl =
            "https://open.spotify.com/embed/track/" +
            urlParts[urlPart] +
            "?utm_source=generator&theme=0";
        }
        this.linkType = "spotify";
      } else {
        this.embedUrl = "";
      }
    },
    getProfileImage(userName) {
      // https://stackoverflow.com/questions/46002113/javascript-reactjs-display-image-with-readablestream-as-source
      fetch(`http://localhost:8080/api/user/${userName}/image`)
        .then((response) => response.blob())
        .then((blob) => {
          if (blob.size > 0) {
            this.profileImages[userName] = URL.createObjectURL(blob);
          } else {
            this.profileImages[userName] = "";
          }
        })
        .catch((e) => {
          console.log(e);
          console.log("Image-Error");
        });
    },
  },
};
</script>

<style lang="scss">
.mdc-text-field--focused.mdc-text-field:not(.mdc-text-field--disabled)
  .mdc-floating-label {
  color: black;
}
.mdc-select:not(.mdc-select--disabled).mdc-select--focused .mdc-floating-label {
  color: black;
}

.mdc-card__primary-action.mdc-ripple-upgraded {
  --mdc-ripple-fg-opacity: var(--mdc-ripple-press-opacity, 0.12);
  cursor: default;
}
</style>