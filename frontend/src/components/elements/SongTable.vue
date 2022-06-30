 <template>
  <div class="px-4 my-4 w-screen">
    <div class="flex ml-2 w-full justify-start">
      Results for: "{{ searchStringPrint }}"
    </div>
    <ui-collapse with-icon ripple>
      <template #toggle>
        <div>Filter</div>
      </template>
      <div>
        <div class="flex justify-center">
          <div class="w-full flex justify-start items-center">
            <ui-chips
              v-on:click="handleFilter()"
              v-model="selectedValue"
              type="filter"
              :options="
                this.$store.state.auth.user == null
                  ? filterListAlt
                  : filterList
              "
            ></ui-chips>
          </div>
        </div>
      </div>
    </ui-collapse>
    <ui-collapse with-icon ripple>
      <template #toggle>
        <div>Extended Search</div>
      </template>
      <div>
        <div class="flex justify-start items-center">
          <ui-textfield class="m-2" v-model="searchParams.artist">
            Artist </ui-textfield
          ><br />
          <ui-textfield class="m-2" v-model="searchParams.title">
            Title </ui-textfield
          ><br />
          <ui-textfield class="m-2" v-model="searchParams.version">
            Version </ui-textfield
          ><br />
          <ui-textfield class="m-2" v-model="searchParams.genre">
            Genre </ui-textfield
          ><br />
          <ui-textfield class="m-2" v-model="searchParams.language">
            Language </ui-textfield
          ><br />
          <ui-textfield class="m-2" v-model="searchParams.releaseYear">
            Release year </ui-textfield
          ><br />
          <ui-textfield class="m-2" v-model="searchParams.creator">
            Creator </ui-textfield
          ><br />
          <ui-textfield class="m-2" v-model="searchParams.editor">
            Editor </ui-textfield
          ><br />
          <ui-button raised v-on:click="getSearchResults">Search</ui-button>
        </div>
      </div>
    </ui-collapse>
    <ui-table
      v-show="items.length > 0"
      :data="items"
      :thead="filterUnfinished ? thead : theadAlternative"
      :tbody="filterUnfinished ? tbody : tbodyAlternative"
      sortIconAlignEnd
      class="w-full"
      :default-col-width="200"
      :scroll="{ y: 300 }"
      @update:modelValue="handleSort"
    >
      <template #cover="{ data }">
        <img class="w-16 h-16" :src="data.cover" crossorigin="anonymous" />
      </template>
      <template #actions="{ data }">
        <ui-icon
          @click="show(data)"
          class="cursor-pointer"
          v-tooltip="'Download .txt file'"
          :aria-describedby="`tooltip-song-download-${data.id}`"
          >download</ui-icon
        >
        <ui-icon
          v-on:click="navigate(`/song/${data.id}`)"
          @click="show(data)"
          class="cursor-pointer"
          v-tooltip="'Show detailed informations'"
          :aria-describedby="`tooltip-song-info-${data.id}}`"
          >info</ui-icon
        >
        <ui-icon
          class="cursor-pointer"
          v-if="!getFullItemFromItem(data).favorite"
          v-tooltip="'Add song to favorites'"
          :aria-describedby="`tooltip-song-favorite-${data.id}`"
          @click="
            this.$store.state.auth.user == null
              ? $alert('You need to be logged in')
              : saveFavorite(data)
          "
          >favorite_border</ui-icon
        >
        <ui-icon
          class="cursor-pointer"
          v-else
          @click="saveFavorite(data)"
          v-tooltip="'Remove song from favorites'"
          :aria-describedby="`tooltip-song-favorite-${data.id}`"
          >favorite</ui-icon
        >
      </template>
      <ui-pagination
        v-model="page"
        :total="total"
        show-total
        @change="onPage"
        show-jumper
      ></ui-pagination
    ></ui-table>
    <div v-show="items.length == 0">No songs found</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedValue: [], // "artist", "title", "version"
      filterList: [
        {
          label: "Include unfinished songs",
          value: "unfinished",
        },
        {
          label: "Show only favorites",
          value: "favorites",
        },
      ],
      filterListAlt: [
        {
          label: "Include unfinished songs",
          value: "unfinished",
        },
      ],
      items: [],
      fullitems: [],
      thead: [
        "Cover",
        {
          value: "Artist",
          sort: "asc",
          columnId: "artist",
        },
        {
          value: "Title",
          sort: "none",
          columnId: "title",
        },
        {
          value: "Version",
          sort: "none",
          columnId: "version",
        },
        {
          value: "Creator",
          sort: "none",
          columnId: "creator",
        },
        {
          value: "Downloads",
          sort: "none",
          columnId: "downloads",
        },
        {
          value: "Average Rating",
          sort: "none",
          columnId: "averageRating",
        },
        {
          value: "Progress",
          sort: "none",
          columnId: "percentage",
        },
        "Actions",
      ],
      theadAlternative: [],
      tbody: [
        {
          slot: "cover",
        },
        "artist",
        "title",
        "version",
        {
          field: "creator",
          fn: (data) => {
            return data.creator;
          },
        },
        {
          field: "downloads",
          align: "right",
          fn: (data) => {
            return data.downloads;
          },
        },
        {
          field: "averageRating",
          align: "right",
          fn: (data) => {
            return data.averageRating == 0
              ? "-"
              : String(data.averageRating).length == 1
              ? data.averageRating + ".0"
              : data.averageRating;
          },
        },
        {
          field: "percentage",
          align: "right",
          fn: (data) => {
            return data.percentage + "%";
          },
        },
        {
          slot: "actions",
        },
      ],
      tbodyAlternative: [],
      total: 100,
      count: 10,
      page: 1,
      finishedLoadingInfos: false,
      promises: [],
      sortColumn: "Artist",
      sortDirection: "Asc",
      filterUnfinished: false,
      filterFavorites: false,
      filterArtist: true,
      filterTitle: true,
      filterVersion: true,
      searchParams: {},
    };
  },
  watch: {
    page() {
      this.getSearchResults();
    },
    // https://stackoverflow.com/questions/60658122/fetch-in-loop-keep-result-order
    async finishedLoadingInfos() {
      if (this.finishedLoadingInfos == true) {
        var i = 0;
        await Promise.all(this.promises)
          .then((blob) => {
            //response.url.slice(-7, -6)
            for (var blib of blob) {
              if (blib.size == 0) {
                this.items[i].cover = require("@/assets/cover_placeholder.png");
              } else {
                this.items[i].cover = URL.createObjectURL(blib);
              }
              //this.total = this.items.length;
              i++;
            }
          })
          .catch(() => {
            console.log("Image-Error");
          });
      }
    },
  },
  props: {
    onlyFavorites: Boolean,
    includeUnfinished: Boolean,
    searchString: String,
  },
  created() {
    if (this.onlyFavorites) {
      this.selectedValue.push("favorites");
      this.filterFavorites = true;
    }
    if (this.includeUnfinished) {
      this.selectedValue.push("unfinished");
      this.filterUnfinished = true;
    }
    // https://stackoverflow.com/questions/7486085/copy-array-by-value
    this.theadAlternative = this.thead.slice();
    this.tbodyAlternative = this.tbody.slice();
    var index = -1;
    var count = 0;
    for (var column of this.theadAlternative) {
      if (column.value != null && column.value == "Progress") {
        index = count;
      }
      count++;
    }
    if (index > -1) {
      this.theadAlternative.splice(index, 1);
      this.tbodyAlternative.splice(index, 1);
    }
    this.getSearchResults();
  },
  methods: {
    navigate(route) {
      this.$router.push(route);
    },
    handleFilter() {
      this.filterUnfinished = this.selectedValue.includes("unfinished");
      this.filterFavorites = this.selectedValue.includes("favorites");
      //this.filterArtist = this.selectedValue.includes("artist");
      //this.filterTitle = this.selectedValue.includes("title");
      //this.filterVersion = this.selectedValue.includes("version");

      if (this.page == 1) {
        this.getSearchResults();
      } else {
        this.page = 1;
      }
    },
    handleSort() {
      var i = 0;
      var change = false;
      for (var element of document.getElementsByClassName(
        "mdc-data-table__sort-status-label"
      )) {
        if (element.firstChild != null) {
          if (i == 0 && this.sortColumn != "Artist") {
            this.sortColumn = "Artist";
            change = true;
          } else if (i == 1 && this.sortColumn != "Title") {
            this.sortColumn = "Title";
            change = true;
          } else if (i == 2 && this.sortColumn != "Version") {
            this.sortColumn = "Version";
            change = true;
          } else if (i == 3 && this.sortColumn != "Creator") {
            this.sortColumn = "Creator";
            change = true;
          } else if (i == 4 && this.sortColumn != "Downloads") {
            this.sortColumn = "Downloads";
            change = true;
          } else if (i == 5 && this.sortColumn != "AverageRating") {
            this.sortColumn = "AverageRating";
            change = true;
          } else if (i == 6 && this.sortColumn != "Percentage") {
            this.sortColumn = "Percentage";
            change = true;
          }
          if (
            element.firstChild.data == "Sorted in ascending order" &&
            this.sortDirection != "Asc"
          ) {
            this.sortDirection = "Asc";
            change = true;
          } else if (
            element.firstChild.data == "Sorted in descending order" &&
            this.sortDirection != "Desc"
          ) {
            this.sortDirection = "Desc";
            change = true;
          }
        }
        i++;
      }
      if (change) {
        this.getSearchResults();
      }
    },
    show(items) {
      items;
    },
    getFullItemFromItem(data) {
      var ret = "";
      for (var fullitem of this.fullitems) {
        if (fullitem.songResponse.id == data.id) {
          ret = fullitem;
        }
      }
      return ret;
    },
    getSearchResults() {
      this.finishedLoadingInfos = false;
      this.items = [];
      this.fullitems = [];
      this.promises = [];
      var extendedSearchString = "";
      // https://stackoverflow.com/questions/650022/how-do-i-split-a-string-with-multiple-separators-in-javascript
      var splitString = this.searchString.split(/[,:]+/);
      for (var position in splitString) {
        var modifiedpart = splitString[position].toLowerCase().trim();
        if (modifiedpart == "artist" && splitString.length > position) {
          this.searchParams.artist = splitString[parseInt(position) + 1];
        }
        if (modifiedpart == "title" && splitString.length > position) {
          this.searchParams.title = splitString[parseInt(position) + 1];
        }
        if (modifiedpart == "version" && splitString.length > position) {
          this.searchParams.version = splitString[parseInt(position) + 1];
        }
        if (modifiedpart == "editor" && splitString.length > position) {
          this.searchParams.editor = splitString[parseInt(position) + 1];
        }
        if (modifiedpart == "creator" && splitString.length > position) {
          this.searchParams.creator = splitString[parseInt(position) + 1];
        }
        if (modifiedpart == "release year" && splitString.length > position) {
          this.searchParams.releaseYear = splitString[parseInt(position) + 1];
        }
        if (modifiedpart == "language" && splitString.length > position) {
          this.searchParams.language = splitString[parseInt(position) + 1];
        }
        if (modifiedpart == "genre" && splitString.length > position) {
          this.searchParams.genre = splitString[parseInt(position) + 1];
        }
      }
      // https://stackoverflow.com/questions/684672/how-do-i-loop-through-or-enumerate-a-javascript-object
      for (var key of Object.keys(this.searchParams)) {
        if (this.searchParams[key] != null && this.searchParams[key] != "") {
          extendedSearchString +=
            key.charAt(0).toUpperCase() +
            key.substring(1) +
            ": " +
            this.searchParams[key] +
            ", ";
        }
      }
      if (extendedSearchString != "") {
        extendedSearchString = extendedSearchString.substring(
          0,
          extendedSearchString.length - 2
        );
        this.searchStringPrint = extendedSearchString;
      } else {
        this.searchStringPrint = this.searchString;
      }
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          searchString:
            this.searchString == "" || this.searchString == null
              ? " "
              : this.searchString,
          // https://stackoverflow.com/questions/679915/how-do-i-test-for-an-empty-javascript-object
          searchParams:
            Object.keys(this.searchParams).length === 0
              ? null
              : this.searchParams,
          page: this.page,
          count: this.count,
          filterUnfinished: this.filterUnfinished,
          filterFavorites: this.filterFavorites,
          userName:
            this.$store.state.auth.user == null
              ? ""
              : this.$store.state.auth.user.username,
          sortColumn: this.sortColumn,
          sortDirection: this.sortDirection,
        }),
      };
      fetch(`http://localhost:8080/api/song/search/`, requestOptions)
        .then((response) => response.json())
        .then((json) => {
          for (var jsonItem of json) {
            this.fullitems.push(jsonItem);
            this.items.push(jsonItem.songResponse);
            console.log(this.items);
            this.promises.push(
              fetch(
                "http://localhost:8080/api/song/" +
                  jsonItem.songResponse.id +
                  "/cover"
              ).then((response) => response.blob())
            );
          }
          this.finishedLoadingInfos = true;
        });
      fetch(`http://localhost:8080/api/song/search/count`, requestOptions)
        .then((response) => response.json())
        .then((json) => {
          this.total = json;
        });
    },
    saveData(data) {
      // https://jasonwatmore.com/post/2020/04/30/vue-fetch-http-post-request-examples
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userName:
            this.$store.state.auth.user == null
              ? ""
              : this.$store.state.auth.user.username,
          songId: data.songResponse.id,
          downloaded: data.downloaded,
          favorite: data.favorite,
        }),
      };
      fetch(
        `http://localhost:8080/api/song/${data.songResponse.id}/${
          this.$store.state.auth.user == null
            ? ""
            : this.$store.state.auth.user.username
        }`,
        requestOptions
      ).then();
    },
    saveFavorite(data) {
      var fullitem = this.getFullItemFromItem(data);
      fullitem.favorite = !fullitem.favorite;
      this.saveData(fullitem);
    },
  },
};
</script>

<style lang="scss">
.mdc-data-table__cell {
  min-width: 64px;
  max-width: 200px;
}
</style>