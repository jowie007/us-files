<template>
  <div class="mt-4" :id="componentKey" :key="componentKey">
    <div class="flex justify-center">
      <div class="md:w-2/3 w-full">
        <ui-collapse with-icon ripple>
          <template #toggle>
            <div>Say something</div>
          </template>
          <div class="mb-6">
            <ui-textfield
              class="mb-2"
              input-type="textarea"
              rows="8"
              cols="100"
              v-model="inputText"
            >
              Type your comment here
            </ui-textfield>
            <br />
            <ui-button raised v-on:click="createComment">Send</ui-button>
          </div>
        </ui-collapse>

        <div v-if="!empty" class="bg-white py-4 rounded-lg">
          <div v-for="(item, index) in items" :key="item.id">
            <div class="">
              <div class="text-left">
                {{ item.text }}
              </div>
              <div class="flex text-left justify-between">
                <div class="flex">
                  <ui-icon
                    v-if="
                      profileImages[item.userName] != undefined &&
                      profileImages[item.userName] == ''
                    "
                    class="m-1 cursor-pointer"
                    v-on:click="showProfile(item.userName)"
                    v-tooltip="`${item.userName}`"
                    :aria-describedby="`tooltip-user-${item.userName}`"
                    >face</ui-icon
                  >
                  <img
                    v-if="
                      profileImages[item.userName] != undefined &&
                      profileImages[item.userName] != ''
                    "
                    v-on:click="showProfile(item.userName)"
                    v-tooltip="`${item.userName}`"
                    :aria-describedby="`tooltip-user-${item.userName}`"
                    id="imageId"
                    class="m-1 w-6 h-6 p-0.5 rounded-full cursor-pointer"
                    crossorigin="anonymous"
                    :src="profileImages[item.userName]"
                  />
                  <div
                    class="mr-2 hover:underline cursor-pointer my-auto"
                    v-on:click="showProfile(item.userName)"
                  >
                    {{ item.userName }}
                  </div>
                </div>
                <div class="my-auto w-32">{{ item.date }}</div>
              </div>
            </div>
            <!-- https://stackoverflow.com/questions/42740105/vue-check-if-you-are-on-the-last-prop-of-a-v-for-loop -->
            <ui-divider v-if="index != items.length - 1" class="my-2"></ui-divider>
          </div>
          <ui-pagination
            v-model="page"
            :total="total"
            show-total
            position="center"
            class="mt-2"
          ></ui-pagination>
        </div>
        <div v-else>No comments yet</div>
      </div>
    </div>
  </div>
</template>

<script>
import uniqueId from "lodash.uniqueid";
import moment from "moment";
export default {
  data() {
    return {
      id: uniqueId("comment-"),
      items: [],
      inputText: "",
      componentKey: 0,
      page: 1,
      total: 0,
      empty: true,
      profileImages: {},
    };
  },
  props: {
    itemid: String,
  },
  watch: {
    page() {
      this.getComments();
    },
  },
  computed: {
    componentIdentifier() {
      return "commentComponent-" + this.componentKey;
    },
    comments() {
      return [
        {
          id: this.id + "-comment",
          text: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
          user: "Johnnn",
          date: "22.10.2022 11:30",
        },
      ];
    },
  },
  mounted() {
    this.getComments();
  },
  methods: {
    getCommentsCount() {
      let url;
      if (this.itemid == "-1") {
        url = "http://localhost:8080/api/comment/page/count";
      } else {
        url = `http://localhost:8080/api/comment/${this.itemid}/count`;
      }
      fetch(url)
        .then((response) => response.json())
        .then((json) => {
          this.total = json;
        });
    },
    getComments() {
      this.getCommentsCount();
      let url;
      if (this.itemid == "-1") {
        url = "http://localhost:8080/api/comment/page";
      } else {
        url = `http://localhost:8080/api/comment/${this.itemid}`;
      }
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          page: this.page > 0 ? this.page - 1 : this.page,
          size: 10,
        }),
      };
      requestOptions;
      fetch(url, requestOptions)
        .then((response) => response.json())
        .then((json) => {
          this.items = [];
          for (var jsonItem of json) {
            var jsonData = {};
            jsonData["id"] = jsonItem.id;
            jsonData["text"] = jsonItem.text;
            jsonData["userName"] = jsonItem.userName;
            this.getProfileImage(jsonItem.userName);
            var format = "hh:mm YYYY.MM.DD";
            var append = "";
            // https://stackoverflow.com/questions/8215556/how-to-check-if-input-date-is-equal-to-todays-date
            var yesterday = new Date();
            yesterday.setDate(yesterday.getDate() - 1);
            if (
              new Date(jsonItem.date).setHours(0, 0, 0, 0) ===
              new Date().setHours(0, 0, 0, 0)
            ) {
              format = "HH:mm";
              append = " Today";
            } else if (
              new Date(jsonItem.date).setHours(0, 0, 0, 0) ===
              yesterday.setHours(0, 0, 0, 0)
            ) {
              format = "HH:mm";
              append = " Yesterday";
            }
            //2021-10-29T12:06:17.804+00:00 https://momentjs.com/
            jsonData["date"] =
              moment(String(jsonItem.date)).format(format) + append;
            this.items.push(jsonData);
          }
          if (this.items.length > 0) {
            this.empty = false;
          }
        })
        .catch((e) => {
          this.empty = true;
          console.log(e);
        });
    },
    showProfile(username) {
      // Navigiert zur Route mit dem Pfad "/list"
      this.$router.push(`/user/${username}`);
    },
    forceRerender() {
      this.componentKey += 1;
    },
    createComment() {
      if (this.inputText.trim() && this.$store.state.auth.status.loggedIn) {
        // https://jasonwatmore.com/post/2020/04/30/vue-fetch-http-post-request-examples
        const requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            userDto: {
              name: this.$store.state.auth.user.username,
            },
            songId: this.itemid,
            text: this.inputText,
          }),
        };
        fetch("http://localhost:8080/api/comment", requestOptions)
          .then((response) => response.json())
          .then((data) => {
            this.postId = data.id;
            //this.forceRerender();
            this.getComments();
            this.inputText = "";
          });
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

<style>
/* https://stackoverflow.com/questions/3671551/is-it-possible-to-give-one-css-class-priority-over-another */
div.mdc-collapse--with-icon div.mdc-collapse__content {
  padding-left: 0px;
}
</style>
