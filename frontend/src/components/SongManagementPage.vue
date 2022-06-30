 <template>
  <div class="mt-2">
    <ui-form item-margin-bottom="16" label-width="80">
      <template #default="{ actionClass }">
        <!--ui-form-field class="justify-center" v-if="parentSong != ''">
          <div>{{ parentSong.artist + " - " + parentSong.title }}</div>
        </ui-form-field-->
        <ui-form-field
          v-if="this.formData.parentSongId == null"
          class="justify-center"
        >
          <ui-textfield class="w-60" required v-model="formData.artist"
            >Artist</ui-textfield
          >
        </ui-form-field>
        <ui-form-field v-else class="justify-center">
          <ui-textfield class="w-60" disabled required v-model="formData.artist"
            >Artist</ui-textfield
          >
        </ui-form-field>
        <ui-form-field
          v-if="this.formData.parentSongId == null"
          class="justify-center"
        >
          <ui-textfield class="w-60" required v-model="formData.title"
            >Title</ui-textfield
          >
        </ui-form-field>
        <ui-form-field v-else class="justify-center">
          <ui-textfield class="w-60" disabled required v-model="formData.title"
            >Title</ui-textfield
          >
        </ui-form-field>
        <ui-form-field class="justify-center">
          <ui-textfield class="w-60" required v-model="formData.version"
            >Version</ui-textfield
          >
        </ui-form-field>
        <ui-form-field class="justify-center">
          <ui-autocomplete
            class="w-60"
            v-model="formData.genre"
            :source="genres"
            >Genre</ui-autocomplete
          >
        </ui-form-field>
        <ui-form-field class="justify-center">
          <ui-autocomplete
            class="w-60"
            :source="languages"
            v-model="formData.language"
            >Language</ui-autocomplete
          >
        </ui-form-field>
        <ui-form-field class="justify-center">
          <ui-textfield
            class="w-60"
            :source="languages"
            v-model="formData.releaseYear"
            >Release year</ui-textfield
          >
        </ui-form-field>
        <ui-form-field class="justify-center">
          <ui-textfield class="w-60" v-model="formData.link">
            Link
            <template #after>
              <ui-textfield-icon data-tooltip-id="tooltip-demo-2"
                >info</ui-textfield-icon
              >
            </template>
          </ui-textfield>
        </ui-form-field>

        <label>Progress percentage:</label>
        <div class="flex justify-center">
          <div class="w-2/3">
            <ui-slider
              v-model="formData.percentage"
              type="discrete"
              :step="10"
              with-tick-marks
            ></ui-slider>
          </div>
        </div>
        <ui-form-field v-if="file != ''" class="justify-center">
          Cover:
        </ui-form-field>
        <br v-if="file != ''" />
        <ui-form-field v-if="file != ''" class="justify-center">
          <img class="w-40 h-40" :src="file" crossorigin="anonymous" />
        </ui-form-field>
        <ui-form-field class="justify-center">
          <ui-file
            accept="image/*"
            text="Upload cover"
            @change="balmUIUpload.onChange('files', $event)"
          ></ui-file>
        </ui-form-field>
        <ui-form-field
          v-show="messages.length"
          class="flex justify-center"
          style="margin-bottom: 0px"
        >
          <div class="w-80">
            <ui-alert state="error">
              <ul>
                <li v-for="(message, index) in messages" :key="index">
                  {{ message }}
                </li>
              </ul>
            </ui-alert>
          </div>
        </ui-form-field>
        <div class="justify-center">
          <ui-form-field :class="actionClass" style="padding-left: ">
            <ui-button raised @click="onSubmit">Save</ui-button>
          </ui-form-field>
        </div>
      </template>
    </ui-form>
    <ui-tooltip-anchor>
      <ui-tooltip id="tooltip-demo-2" rich>
        <template #title>Insert link</template>
        <template #default="{ linkClass }">
          To add a YouTube-Link to your song, paste an URL like this: <br />
          <a :class="linkClass">https://www.youtube.com/watch?v=HgzGwKwLmgM</a
          ><br /><br />
          To add a Spotify-Link to your song, paste an URL like this: <br />
          <a :class="linkClass"
            >https://open.spotify.com/track/5T8EDUDqKcs6OSOwEsfqG7?si=99d11d157295481a</a
          >
        </template>
      </ui-tooltip>
    </ui-tooltip-anchor>
  </div>
</template>

<script>
import { useValidator, useEvent } from "balm-ui";
// https://stackoverflow.com/questions/50715823/how-do-i-access-data-from-an-external-file-in-vue-js/56508468
import languageFile from "../js/languages.js";
import genreFile from "../js/genres.js";

const validations = {
  artist: {
    label: "Artist",
    validator: "required, artist",
  },
  title: {
    label: "Title",
    validator: "required, title",
  },
  version: {
    label: "Version",
    validator: "required, version",
  },
};

export default {
  data() {
    return {
      balmUI: useValidator(),
      balmUIUpload: useEvent(),
      formData: {
        id: null,
        parentSongId: null,
        creatorName: "",
        artist: "",
        title: "",
        version: "",
        genre: "",
        language: "",
        link: "",
        percentage: 0,
        releaseYear: "",
      },
      originalData: {
        id: null,
        parentSongId: null,
        creatorName: "",
        artist: "",
        title: "",
        version: "",
        genre: "",
        language: "",
        link: "",
        percentage: 0,
        releaseYear: "",
      },
      parentSong: "",
      messages: [],
      validations,
      files: [],
      file: "",
      enableNavigation: false,
      genres: genreFile,
      languages: languageFile,
    };
  },
  created() {
    if (this.$route.query.parentSongId != undefined) {
      this.formData.parentSongId = this.$route.query.parentSongId;
      this.fetchParentSong();
    }
    if (this.$route.params.id != undefined) {
      this.formData.id = this.$route.params.id;
      this.fetchSong();
    }
  },
  mounted() {
    document.getElementById("content-main").style.backgroundColor = "#fff";
  },
  watch: {
    files() {
      this.file = URL.createObjectURL(this.files[0].sourceFile);
    },
  },
  methods: {
    onSubmit() {
      let result = this.balmUI.validate(this.formData);
      let { valid, messages } = result;
      this.messages = messages;

      if (valid) {
        this.saveChangesWithCheck();
      }
    },
    fetchSong() {
      if (this.formData.id != null) {
        fetch(`http://localhost:8080/api/song/${this.formData.id}`)
          .then((response) => response.json())
          .then((json) => {
            this.formData = json;
            // Objekt klonen
            this.originalData = JSON.parse(JSON.stringify(this.formData));
          })
          .catch(() => {
            console.log("Song-Data-Error");
          });
      }
    },
    fetchParentSong() {
      if (this.formData.parentSongId != null) {
        fetch(`http://localhost:8080/api/song/${this.formData.parentSongId}`)
          .then((response) => response.json())
          .then((json) => {
            this.parentSong = json;
            if (this.formData.id == null) {
              this.formData.artist = this.parentSong.artist;
              this.formData.title = this.parentSong.title;
              this.formData.genre = this.parentSong.genre;
              this.formData.language = this.parentSong.language;
              this.formData.releaseYear = this.parentSong.releaseYear;
              this.originalData.artist = this.parentSong.artist;
              this.originalData.title = this.parentSong.title;
              this.originalData.genre = this.parentSong.genre;
              this.originalData.language = this.parentSong.language;
              this.originalData.releaseYear = this.parentSong.releaseYear;
            }
          })
          .catch(() => {
            console.log("Song-Data-Error");
          });
      }
    },
    saveChangesWithCheck() {
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          artist: this.formData.artist,
          title: this.formData.title,
          version: this.formData.version,
        }),
      };
      if (this.parentSongId != undefined) {
        fetch("http://localhost:8080/api/song/version/exists", requestOptions)
          .then((response) => response.json())
          .then((json) => {
            if (json) {
              this.messages.push("Version already exists");
            } else {
              this.saveChanges();
            }
          })
          .catch(() => {
            this.error = true;
          });
      } else {
        this.saveChanges();
      }
    },
    saveChanges() {
      this.formData.creatorName = this.$store.state.auth.user.username;
      this.formData.editorNames = this.formData.editors;
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(this.formData),
      };
      console.log(this.formData);
      fetch("http://localhost:8080/api/song/save", requestOptions)
        .then((response) => response.json())
        .then((json) => {
          // Objekt klonen, damit zwischengespeichert werden kann
          this.originalData = JSON.parse(JSON.stringify(this.formData));
          this.saveCover(json);
        })
        .catch(() => {
          this.error = true;
        });
      this.$toast("Successfuly saved!");
    },
    saveCover(returnId) {
      // https://stackoverflow.com/questions/35192841/how-do-i-post-with-multipart-form-data-using-fetch
      const formData = new FormData();
      var file = this.files[0].sourceFile;
      formData.append("file", file);
      // https://stackoverflow.com/questions/49053193/nodejs-request-how-to-send-multipart-form-data-post-request
      const requestOptions = {
        method: "POST",
        body: formData,
      };
      fetch(
        `http://localhost:8080/api/song/save/${returnId}/cover/`,
        requestOptions
      )
        .then((response) => response.json())
        .then((json) => {
          json;
        })
        .catch(() => {});
    },
    compareData() {
      return (
        this.formData.artist == this.originalData.artist &&
        this.formData.title == this.originalData.title &&
        this.formData.version == this.originalData.version &&
        this.formData.genre == this.originalData.genre &&
        this.formData.language == this.originalData.language &&
        this.formData.link == this.originalData.link &&
        this.formData.progress == this.originalData.progress &&
        this.formData.releaseYear == this.originalData.releaseYear
      );
    },
  },
  beforeRouteLeave(to, from, next) {
    if (!this.enableNavigation && !this.compareData()) {
      next(false);
      this.$confirm({
        title: "Leave page?",
        message:
          "If you leave this page without saving, all changes will be lost.",
        acceptText: "Ok",
        cancelText: "Cancel",
      }).then((result) => {
        if (result) {
          this.enableNavigation = true;
          this.$router.push(to);
        }
      });
    } else {
      next();
    }
    to;
    from;
    //next();
  },
};
</script>

<style scoped>
.mdc-slider {
  margin: 0;
}
</style>