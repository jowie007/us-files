<template>
  <html class="h-screen">
    <head>
      <meta charset="utf-8" />
    </head>
    <body>
      <div
        class="
          flex
          text-black
          fixed
          w-full
          sm:h-16
          h-14
          z-10
          bg-white
          items-center
        "
        v-if="showSearch"
      >
        <!-- https://stackoverflow.com/questions/51642232/prevent-form-default-behavior-in-vue-js -->
        <form class="w-full" @submit.prevent="handleSearch">
          <input
            class="w-full sm:h-16 h-14 outline-none ml-10 text-xl"
            ref="inputSearch"
            v-model="searchInput"
            v-on:blur="showSearch = false"
            type="text"
            placeholder="Search songs"
            data-tooltip-id="search-bar-tooltip"
          />
          <ui-tooltip-anchor>
            <ui-tooltip id="search-bar-tooltip" rich>
              <template #title>Search songs</template>
              <template #default="{}">
                You can search with a combined string including artist, title
                and version.
                <br />
                <br />
                You can also search with parameters like "Artist: Queen, Title:
                Don't stop me now, ReleaseYear: 1978 ...".
                <br />
                <br />
                Accepted parameters: Artist, Title, Version, Genre, Language, ReleaseYear, Creator, Editor.
                <br />
                <br />
                Press enter to start the search.
              </template>
            </ui-tooltip>
          </ui-tooltip-anchor>
        </form>
        <ui-icon-button
          class="mx-3 my-2 p-3"
          icon="clear"
          v-on:click="showSearch = false"
        ></ui-icon-button>
      </div>

      <div>
        <ui-top-app-bar
          content-selector="#content-main"
          type="1"
          :title="title"
          @nav="openDrawer = true"
          style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.5)"
        >
          <div class="flex items-center">
            <ui-icon
              class="cursor-pointer"
              v-on:click="navigate('/')"
              v-tooltip="'Navigate to homepage'"
              aria-describedby="tooltip-homepage"
              >home</ui-icon
            >
            <div class="ml-4 truncate invisible md:visible">{{ title }}</div>
          </div>
          <template #toolbar="{ toolbarItemClass }">
            <div class="mx-3 mt-1 cursor-pointer">
              <ui-badge
                v-show="this.$store.state.auth.status.loggedIn && newMessages"
                overlap
                dot
              >
                <ui-icon
                  v-if="this.$store.state.auth.status.loggedIn"
                  v-on:click="navigateToProfile()"
                  :class="toolbarItemClass"
                  v-tooltip="'Navigate to your profil'"
                  aria-describedby="tooltip-profile"
                  >face</ui-icon
                >
              </ui-badge>
              <div v-show="!newMessages">
                <ui-icon
                  v-if="this.$store.state.auth.status.loggedIn"
                  v-on:click="navigateToProfile()"
                  :class="toolbarItemClass"
                  v-tooltip="'Navigate to your profil'"
                  aria-describedby="tooltip-profile"
                  >face</ui-icon
                >
              </div>
            </div>
            <ui-icon-button
              v-if="this.$store.state.auth.status.loggedIn"
              v-on:click="handleLogout"
              :class="toolbarItemClass"
              v-tooltip="'Logout'"
              aria-describedby="tooltip-logout"
              icon="logout"
            ></ui-icon-button>
            <ui-icon-button
              v-else
              v-on:click="navigate('/login')"
              :class="toolbarItemClass"
              v-tooltip="'Login'"
              aria-describedby="tooltip-login"
              icon="login"
            ></ui-icon-button>
            <ui-icon-button
              v-on:click="handleShowSearch"
              :class="toolbarItemClass"
              icon="search"
              v-tooltip="'Search songs'"
              aria-describedby="tooltip-search"
            ></ui-icon-button>
          </template>
        </ui-top-app-bar>

        <ui-drawer
          v-model="openDrawer"
          type="modal"
          v-tooltip="'Show navigation menu'"
          aria-describedby="tooltip-drawer"
        >
          <ui-drawer-header>
            <ui-drawer-title>Menu</ui-drawer-title>
          </ui-drawer-header>
          <ui-drawer-content>
            <ui-list>
              <ui-item
                v-on:click="openDrawer = false"
                :active="
                  ![
                    'Home',
                    'News',
                    'Search',
                    'Song calendar',
                    'About',
                  ].includes(routeName)
                "
              >
                <ui-item-first-content :active="false">
                  <ui-icon>arrow_back</ui-icon>
                </ui-item-first-content>
                <ui-item-text-content>Back</ui-item-text-content>
              </ui-item>
              <ui-list-divider></ui-list-divider>
              <ui-nav>
                <ui-nav-item
                  v-on:click="navigate('/')"
                  :active="routeName == 'Home'"
                  >Home</ui-nav-item
                >
                <ui-nav-item
                  v-on:click="navigate('/news')"
                  :active="routeName == 'News'"
                  >News</ui-nav-item
                >
                <ui-nav-item
                  v-if="this.$store.state.auth.status.loggedIn"
                  v-on:click="navigate('/song/new')"
                  :active="routeName == 'Create new song'"
                  >Upload</ui-nav-item
                >
                <ui-nav-item
                  v-on:click="navigate('/search')"
                  :active="routeName == 'Search'"
                  >Search</ui-nav-item
                >
                <ui-nav-item
                  v-on:click="navigate('/calendar')"
                  :active="routeName == 'Song calendar'"
                  >Song calendar</ui-nav-item
                >
                <ui-nav-item
                  v-on:click="navigate('/about')"
                  :active="routeName == 'About'"
                  >About</ui-nav-item
                >
              </ui-nav>
            </ui-list>
          </ui-drawer-content>
        </ui-drawer>
        <div class="min-h-screen h-full" id="content-main">
          <router-view></router-view>
        </div>
      </div>
    </body>
  </html>
</template>

<script>
export default {
  data() {
    return {
      searchInput: "",
      showSearch: false,
      selected: "",
      title: "USMD",
      openDrawer: false,
      newMessagesAvailable: false,
    };
  },
  name: "App",
  // https://stackoverflow.com/questions/43270159/how-to-watch-store-values-from-vuex
  computed: {
    routeName() {
      console.log(this.$route.name);
      return this.$route.name;
    },
    pageTitle() {
      return this.$store.state.pageTitle;
    },
    newMessages() {
      return this.$store.state.newMessages;
    },
    newLogIn() {
      return this.$store.state.auth.user;
    },
  },
  watch: {
    routeName() {
      this.$store.state.pageTitle = this.$route.name;
    },
    pageTitle() {
      this.title = this.$store.state.pageTitle;
    },
    newLogIn() {
      this.checkForMessagesLoop();
    },
  },
  created() {
    this.checkForMessagesLoop();
  },
  components: {},
  methods: {
    navigate(route) {
      this.$router.push(route);
    },
    navigateToProfile() {
      this.$router.push("/user/" + this.$store.state.auth.user.username);
    },
    handleLogout() {
      this.$store.dispatch("auth/logout").then(
        () => {
          this.$router.push("/");
        },
        (error) => {
          console.log(error);
        }
      );
    },
    handleShowSearch() {
      // Search Bar -> https://material.io/design/typography/the-type-system.html
      this.showSearch = true;
      // https://michaelnthiessen.com/set-focus-on-input-vue
      // https://stackoverflow.com/questions/54355375/vue-js-refs-are-undefined-even-though-this-refs-shows-theyre-there
      this.$nextTick(() => {
        this.$refs.inputSearch.focus();
      });
    },
    handleSearch() {
      this.showSearch = false;
      console.log(this.searchInput);
      this.$router.push({
        path: `/search`,
        query: { searchString: this.searchInput },
      });
    },
    checkForMessagesLoop() {
      // Alle 30 Sekunden
      // https://stackoverflow.com/questions/49849376/vue-js-triggering-a-method-function-every-x-seconds
      if (this.$store.state.auth.user != null) {
        this.checkForMessages();
      }
      window.setInterval(() => {
        if (this.$store.state.auth.user != null) {
          this.checkForMessages();
        }
      }, 30000);
    },
    checkForMessages() {
      fetch(
        `http://localhost:8080/api/user/${this.$store.state.auth.user.username}/newMessages`
      )
        .then((response) => response.json())
        .then((json) => {
          this.$store.commit("setNewMessages", json);
        });
    },
  },
};
</script>

<style lang="scss">
#app {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  background-color: #fff;
}

.mdc-tab-bar {
  background-color: white;
}
</style>
