 <template>
  <div>
    <ui-tab-bar
      v-show="this.name == this.$store.state.auth.user.username"
      :modelValue="this.$store.state.activeTab"
      v-model="activeTab"
      class="hero-demo-tab-bar"
    >
      <ui-tab
        type="textWithIcon"
        icon="info"
        stacked
        v-tooltip="'Show your profile informations'"
        aria-describedby="tooltip-profile-informations"
        >Info</ui-tab
      >
      <ui-tab
        type="textWithIcon"
        stacked
        v-tooltip="'Show your inbox'"
        aria-describedby="tooltip-show-messages"
        >Inbox
        <template #icon="{ iconClass }">
          <ui-badge v-show="newMessages" overlap dot>
            <ui-icon
              v-if="this.$store.state.auth.status.loggedIn"
              v-on:click="navigateToProfile()"
              :class="iconClass"
              v-tooltip="'Navigate to your profil'"
              aria-describedby="tooltip-profile"
              >inbox</ui-icon
            >
          </ui-badge>
          <ui-icon
            v-show="!newMessages"
            v-if="this.$store.state.auth.status.loggedIn"
            v-on:click="navigateToProfile()"
            :class="iconClass"
            v-tooltip="'Navigate to your profil'"
            aria-describedby="tooltip-profile"
            >inbox</ui-icon
          >
        </template>
      </ui-tab></ui-tab-bar
    >
    <div v-if="jsonError == ''">
      <div
        v-show="activeTab == 0"
        class="flex flex-wrap lg:flex-nowrap items-center justify-center"
      >
        <div
          v-if="filled"
          class="
            flex flex-wrap
            w-1/2
            flex-col
            justify-center
            items-center
            px-4
            mt-4
          "
        >
          <ui-card outlined class="w-1/2 py-4">
            <ui-card-content>
              <div class="w-full flex justify-center mb-4">
                <img
                  v-show="this.image != ''"
                  id="imageId"
                  class="w-40 h-40 rounded-full"
                  crossorigin="anonymous"
                />
                <div
                  v-show="this.image == ''"
                  class="bg-gray-600 w-40 h-40 rounded-full"
                >
                  <div class="absolute" style="margin: 3.5rem">
                    <ui-icon size="48" light inactive
                      >image_not_supported</ui-icon
                    >
                  </div>
                </div>
                <div
                  v-show="
                    this.image != '' &&
                    this.name == this.$store.state.auth.user.username
                  "
                  class="
                    absolute
                    mt-2
                    rounded-full
                    bg-red-600
                    w-12
                    h-12
                    text-white
                  "
                  style="margin-right: 7.5rem"
                >
                  <ui-icon-button
                    v-on:click="deleteProfileImage()"
                    icon="delete"
                    class="m-0"
                  ></ui-icon-button>
                </div>
                <div
                  v-show="this.name == this.$store.state.auth.user.username"
                  class="
                    absolute
                    mt-2
                    rounded-full
                    bg-green-600
                    w-12
                    h-12
                    text-white
                  "
                  style="margin-left: 7.5rem"
                >
                  <input
                    id="fileInput"
                    type="file"
                    @change="onFileChanged"
                    class="absolute w-12 h-12 rounded-full"
                    style="display: none"
                  /><ui-icon-button
                    v-on:click="showUploadDialog()"
                    icon="file_upload"
                    class="m-0"
                  ></ui-icon-button>
                </div>
              </div>

              <div class="text-left mx-4">
                <div class="flex">
                  <div class="flex-shrink-0 w-36">Name:</div>
                  <div class="flex w-full flex-wrap">
                    <div class="">
                      {{ this.user.name }}
                    </div>
                  </div>
                </div>
                <ui-divider></ui-divider>
                <div class="flex">
                  <div class="flex-shrink-0 w-36">Registration date:</div>
                  <div class="flex w-full flex-wrap">
                    <div class="">
                      {{ this.user.registrationDate }}
                    </div>
                  </div>
                </div>
                <ui-divider></ui-divider>
                <div class="flex">
                  <div class="flex-shrink-0 w-36">Role:</div>
                  <div class="flex w-full flex-wrap">
                    <div class="">
                      {{ this.user.role }}
                    </div>
                  </div>
                </div>
                <ui-divider></ui-divider>
              </div>
              <ui-button
                v-if="this.user.createdSongs > 0"
                class="m-2"
                raised
                v-on:click="
                  navigateToTable(`Creator: ${this.user.name}`, false, true)
                "
                >Show {{ this.user.createdSongs }} created song{{
                  this.user.createdSongs > 1 ? "s" : ""
                }}</ui-button
              >
              <div v-else>No songs created</div>
              <ui-button
                v-if="this.user.editedSongs > 0"
                class="m-2"
                raised
                v-on:click="navigateToTable(`Editor: ${this.user.name}`, false, true)"
                >Show {{ this.user.editedSongs }} edited songs{{
                  this.user.editedSongs > 1 ? "s" : ""
                }}</ui-button
              >
              <div v-else>No songs edited</div>
              <ui-button
                v-show="this.name == this.$store.state.auth.user.username"
                class="m-2"
                raised
                v-on:click="navigateToTable('', true, true)"
                >Show favorite songs</ui-button
              >
            </ui-card-content>
          </ui-card>
        </div>
      </div>
      <div v-show="activeTab == 1" class="mt-4">
        <ui-button icon="delete" v-on:click="saveMessagesStatus(true, null)"
          >Delete selected</ui-button
        >
        <ui-button icon="done_all" v-on:click="saveMessagesStatus(null, true)"
          >Mark selected as read</ui-button
        >
        <ui-button icon="select_all" v-on:click="selectAll()"
          >Select all</ui-button
        >
        <ui-button v-on:click="deselectAll()"
          >Deselect all
          <template #before>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              enable-background="new 0 0 24 24"
              height="18px"
              width="18px"
              viewBox="0 0 24 24"
              fill="#000000"
              style="margin-right: 8px"
            >
              <g><rect fill="none" height="24" width="24" /></g>
              <g>
                <path
                  d="M3,13h2v-2H3V13z M7,21h2v-2H7V21z M13,3h-2v2h2V3z M19,3v2h2C21,3.9,20.1,3,19,3z M5,21v-2H3C3,20.1,3.9,21,5,21z M3,17h2 v-2H3V17z M11,21h2v-2h-2V21z M19,13h2v-2h-2V13z M19,9h2V7h-2V9z M15,5h2V3h-2V5z M7.83,5L7,4.17V3h2v2H7.83z M19.83,17L19,16.17 V15h2v2H19.83z M21.19,21.19L2.81,2.81L1.39,4.22L4.17,7H3v2h2V7.83l2,2V17h7.17l2,2H15v2h2v-1.17l2.78,2.78L21.19,21.19z M9,15 v-3.17L12.17,15H9z M15,12.17V9h-3.17l-2-2H17v7.17L15,12.17z"
                />
              </g></svg></template
        ></ui-button>
        <div class="flex justify-center">
          <ui-list v-if="messagesLoaded && items.length > 0" class="w-full">
            <div
              v-for="item in items"
              :key="item"
              class="flex justify-center w-full"
            >
              <ui-item class="w-2/3">
                <ui-item-text-content
                  v-if="item.messageDto.read == false"
                  class="font-bold"
                  >{{ item.text }}</ui-item-text-content
                >
                <ui-item-text-content v-else>{{
                  item.text
                }}</ui-item-text-content>
                <ui-item-last-content>
                  <ui-checkbox v-model="item.checked" @click.stop></ui-checkbox>
                </ui-item-last-content>
              </ui-item>
              <ui-icon-button v-on:click="navigateToMessageLocation(item)"
                >navigate_next</ui-icon-button
              >
            </div>
          </ui-list>
          <div v-show="items.length == 0">No messages available</div>
        </div>
      </div>
    </div>
    <ErrorComponent
      v-else
      :code="jsonError.code"
      :message="jsonError.message"
    ></ErrorComponent>
  </div>
</template>

<script>
import ErrorComponent from "../components/elements/ErrorComponent";
export default {
  data() {
    return {
      name: this.$route.params.name,
      user: "",
      filled: "",
      jsonError: "",
      image: "",
      files: "",
      activeTab: 0,
      checkedMessages: [],
      items: [],
      messagesLoaded: false,
    };
  },
  components: {
    ErrorComponent,
  },
  watch: {
    // https://stackoverflow.com/questions/61083143/watch-route-params-id-does-not-trigger-re-render-of-vue-component
    "$route.params.id": {
      handler: function () {
        this.name = this.$route.params.name;
        if (this.$route.params.name != undefined) {
          this.onMounted();
        }
      },
      deep: true,
      immediate: true,
    },
  },
  computed: {
    newMessages() {
      return this.$store.state.newMessages;
    },
  },
  mounted() {
    this.onMounted();
  },
  methods: {
    navigateToMessageLocation(item) {
      if (item.messageDto.messageType == "NEW_COMMENT") {
        this.$router.push({
          path: `/song/${item.songResponse.id}`,
          query: { initActiveTab: 1 },
        });
      } else if (item.messageDto.messageType == "SONG_UPDATED") {
        this.$router.push(`/song/${item.songResponse.id}`);
      }
    },
    selectAll() {
      for (let index in this.items) {
        this.items[index].checked = true;
      }
    },
    deselectAll() {
      for (let index in this.items) {
        this.items[index].checked = false;
      }
    },
    saveMessagesStatus(deleted, read) {
      var messageIds = [];
      for (var index in this.items) {
        if (this.items[index].checked) {
          messageIds.push(this.items[index].messageDto.id);
        }
      }
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          messageIds: messageIds,
          deleted: deleted,
          read: read,
        }),
      };
      fetch(
        `http://localhost:8080/api/user/${this.user.name}/messages`,
        requestOptions
      )
        .then((response) => response.json())
        .then((json) => {
          json;
          this.getMessages();
        });
    },
    getMessages() {
      fetch(`http://localhost:8080/api/user/${this.user.name}/messages`)
        .then((response) => response.json())
        .then((json) => {
          this.items = json;
          var nothingnew = true;
          for (var index in json) {
            if (json[index].messageDto.messageType == "NEW_COMMENT") {
              json[
                index
              ].text = `Your song "${json[index].songResponse.artist} - ${json[index].songResponse.title} (${json[index].songResponse.version})" has a new comment`;
            } else if (json[index].messageDto.messageType == "SONG_UPDATED") {
              json[
                index
              ].text = `The song "${json[index].songResponse.artist} - ${json[index].songResponse.title} (${json[index].songResponse.version})" was updated`;
            }
            json[index].checked = false;
            if (json[index].messageDto.read == false) {
              nothingnew = false;
            }
            this.messagesLoaded = true;
          }
          this.$store.commit("setNewMessages", !nothingnew);
        })
        .catch((e) => {
          console.log(e);
          console.log("Image-Error");
        });
    },
    getProfileImage() {
      // https://stackoverflow.com/questions/46002113/javascript-reactjs-display-image-with-readablestream-as-source
      fetch("http://localhost:8080/api/user/" + this.user.name + "/image")
        .then((response) => response.blob())
        .then((blob) => {
          if (blob.size > 0) {
            this.image = URL.createObjectURL(blob);
            document.getElementById("imageId").src = this.image;
          } else {
            this.image = "";
          }
        })
        .catch((e) => {
          console.log(e);
          console.log("Image-Error");
        });
    },
    onMounted() {
      this.image = "";
      this.user = "";
      this.filled = "";
      this.jsonError = "";
      this.files = "";
      document.getElementById("content-main").style.backgroundColor = "#fff";
      fetch(`http://localhost:8080/api/user/${this.name}/info`)
        .then((response) => response.json())
        .then((json) => {
          if (json.error == undefined) {
            this.user = json;
            this.user.name = json.name;
            this.user.registrationDate = new Date(
              json.registrationDate
            ).toLocaleDateString();
            // https://stackoverflow.com/questions/1026069/how-do-i-make-the-first-letter-of-a-string-uppercase-in-javascript
            var role = json.role.substring(5).toLowerCase();
            role = role.charAt(0).toUpperCase() + role.substring(1);
            this.user.role = role;
            this.filled = true;
            this.getProfileImage();
            if (
              this.$store.state.auth.user.username != undefined &&
              this.name == this.$store.state.auth.user.username
            ) {
              this.getMessages();
            }
          } else {
            this.jsonError = json;
            this.$store.commit("setPageTitle", "User not found");
          }
        });
    },
    navigateToTable(searchString, filterFavorites, includeUnfinished) {
      if (filterFavorites || includeUnfinished) {
        this.$router.push({
          path: `/search`,
          query: {
            searchString: searchString,
            filterFavorites: filterFavorites,
            includeUnfinished: includeUnfinished,
          },
        });
      } else {
        this.$router.push({
          path: `/search`,
          query: { searchString: searchString },
        });
      }
    },
    saveProfileImage() {
      const formData = new FormData();
      var file = this.files[0];
      formData.append("file", file);
      // https://stackoverflow.com/questions/49053193/nodejs-request-how-to-send-multipart-form-data-post-request
      const requestOptions = {
        method: "POST",
        body: formData,
      };
      fetch(
        `http://localhost:8080/api/user/save/${this.user.name}/image/`,
        requestOptions
      )
        .then((response) => response.json())
        .then((json) => {
          json;
          this.getProfileImage();
        })
        .catch(() => {});
    },
    deleteProfileImage() {
      this.$confirm({
        message: "Do you want to delete your profile image?",
        acceptText: "Ok",
        cancelText: "Cancel",
      }).then((result) => {
        if (result) {
          fetch(
            `http://localhost:8080/api/user/delete/${this.user.name}/image/`
          )
            .then((response) => response.json())
            .then((json) => {
              json;
              this.getProfileImage();
            })
            .catch(() => {});
        }
      });
    },
    showUploadDialog() {
      // https://stackoverflow.com/questions/9400948/input-type-file-attached-to-an-onclick-event
      document.getElementById("fileInput").click();
    },
    onFileChanged(event) {
      // https://medium.com/@vipinc.007/how-to-handle-file-upload-in-vue-js-e910c63e7569
      this.files = event.target.files;
      this.saveProfileImage();
    },
  },
};
</script>

<style lang="scss">
.mdc-card__primary-action.mdc-ripple-upgraded {
  --mdc-ripple-fg-opacity: var(--mdc-ripple-press-opacity,0.12);
  cursor: default;
}
</style>