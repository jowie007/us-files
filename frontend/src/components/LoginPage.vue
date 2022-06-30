<template>
  <form>
    <ui-textfield class="m-2" v-model="username" required>
      Username </ui-textfield
    ><br />
    <ui-textfield
      input-type="password"
      v-model="password"
      helper-text-id="my-text-field-helper-text"
      required
    >
      Password </ui-textfield
    ><br />

    <div class="flex justify-center items-center">
      <ui-textfield-helper
        v-if="error"
        id="my-text-field-helper-text"
        visible="true"
      >
        Check your username and password again!
      </ui-textfield-helper>
    </div>
    <br />
    <div class="flex items-center justify-center mx-auto space-x-4">
      <ui-button raised v-on:click="handleLogin">Login</ui-button>
      <ui-button outlined v-on:click="register">Register</ui-button>
    </div>
  </form>
</template>  

<script>
import bcrypt from "bcryptjs";
export default {
  data() {
    return {
      username: "",
      password: "",
      error: false,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  created() {
    if (this.loggedIn) {
      this.$router.push("/");
    }
  },
  mounted() {
    document.getElementById("content-main").style.backgroundColor = "#fff";
  },
  methods: {
    handleLogin() {
      if (
        this.username != undefined &&
        this.username != "" &&
        this.password != undefined &&
        this.password != ""
      ) {
        fetch(`http://localhost:8080/api/user/${this.username}/salt`)
          .then((response) => response.json())
          .then((json) => {
            let user = [];
            user.username = this.username;
            user.salt = json.salt;
            user.password = this.encryptPassword(this.password, user.salt);
            this.$store.dispatch("auth/login", user).then(
              () => {
                this.$router.push("/");
              },
              (error) => {
                this.error = true;
                this.message =
                  (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                  error.message ||
                  error.toString();
              }
            );
          })
          .catch(() => {
            this.error = true;
          });
      }
    },
    // https://stackoverflow.com/questions/64051831/vuejs-bcrypt-implementation
    // https://auth0.com/blog/adding-salt-to-hashing-a-better-way-to-store-passwords/
    encryptPassword(password, salt) {
      if (password != undefined && salt != null) {
        return bcrypt.hashSync(password, salt);
      }
    },
    register() {
      this.$router.push("/register");
    },
  },
};
</script>