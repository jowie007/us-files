<template>
  <form>
    <ui-textfield
      class="mt-2"
      required
      v-model="email1"
      helper-text-id="em-validation-msg"
    >
      Email Address </ui-textfield
    ><br />
    <div class="flex justify-center items-center">
      <ui-textfield-helper
        v-if="!emailvalid"
        id="em-validation-msg"
        visible="true"
      >
        E-Mail is not valid
      </ui-textfield-helper>
    </div>
    <div class="" v-if="!emailvalid"></div>
    <ui-textfield
      class="mt-2"
      required
      v-model="email2"
      helper-text-id="rem-validation-msg"
    >
      Repeat Email Address </ui-textfield
    ><br />
    <div class="flex justify-center items-center">
      <ui-textfield-helper
        v-if="!emailsmatch"
        id="rem-validation-msg"
        visible="true"
      >
        E-Mails didn't match
      </ui-textfield-helper>
    </div>
    <ui-textfield
      class="mt-2"
      required
      v-model="username"
      helper-text-id="un-validation-msg"
      >Choose username</ui-textfield
    ><br />
    <div class="flex justify-center items-center">
      <ui-textfield-helper
        v-if="!usernameok"
        id="un-validation-msg"
        visible="true"
      >
        {{ usernameerror }}
      </ui-textfield-helper>
    </div>
    <ui-textfield
      class="mt-2"
      v-model="password1"
      input-type="password"
      required
      pattern=".{8,}"
      helper-text-id="pw-validation-msg"
    >
      Choose password </ui-textfield
    ><br />
    <div class="flex justify-center items-center">
      <ui-textfield-helper
        v-if="!passwordstrength"
        id="pw-validation-msg"
        visible="true"
        validMsg="false"
      >
        Password must contain at least 8 characters, a number, an uppercase and
        a lowercase letter
      </ui-textfield-helper>
    </div>
    <ui-textfield
      class="mt-2"
      v-model="password2"
      input-type="password"
      required
      pattern=".{8,}"
      helper-text-id="rpw-validation-msg"
    >
      Repeat password </ui-textfield
    ><br />
    <div class="flex justify-center items-center">
      <ui-textfield-helper
        v-if="!passwordsmatch"
        id="rpw-validation-msg"
        visible="true"
        validMsg="false"
      >
        Passwords didn't match
      </ui-textfield-helper>
    </div>
    <div class="flex justify-center items-center">
      <ui-textfield-helper
        v-if="bottomText != ''"
        visible="true"
        validMsg="false"
      >
        {{ bottomText }}
      </ui-textfield-helper>
    </div>
    <div class="flex items-center justify-center mx-auto space-x-4 pb-4 mt-4">
      <ui-button raised class="standardButton" v-on:click="register"
        >Register</ui-button
      >
      <ui-button outlined v-on:click="login">Login</ui-button>
    </div>
  </form>
</template>  

<script>
import bcrypt from "bcryptjs";
export default {
  data() {
    return {
      email1: "",
      email2: "",
      username: "",
      password1: "",
      password2: "",
      usernameerror: "",
      emailvalid: true,
      emailsmatch: true,
      usernameok: true,
      passwordstrength: true,
      passwordsmatch: true,
      bottomText: "",
    };
  },
  mounted() {
    document.getElementById("content-main").style.backgroundColor = "#fff";
  },
  methods: {
    checkemailvalid() {
      if (this.email1.length > 4) {
        this.emailvalid = true;
      } else {
        this.emailvalid = false;
      }
    },
    checkemailsmatch() {
      if (this.email1 == this.email2) {
        this.emailsmatch = true;
      } else {
        this.emailsmatch = false;
      }
    },
    checkusername() {
      if (this.username == undefined || this.username.length < 3) {
        this.usernameok = false;
        this.usernameerror = "Username must contain three characters";
      } else {
        this.usernameok = true;
      }
    },
    checkpasswordstrength() {
      // https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
      if (
        this.password1 != undefined &&
        new RegExp(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$/).test(
          this.password1
        )
      ) {
        this.passwordstrength = true;
      } else {
        this.passwordstrength = false;
      }
    },
    checkpasswords() {
      if (this.password1 == this.password2) {
        this.passwordsmatch = true;
      } else {
        this.passwordsmatch = false;
      }
    },
    encryptPassword(password, salt) {
      if (
        password != undefined &&
        password != "" &&
        salt != null &&
        salt != ""
      ) {
        return bcrypt.hashSync(password, salt);
      }
    },
    register() {
      this.checkemailvalid();
      this.checkemailsmatch();
      this.checkusername();
      this.checkpasswordstrength();
      this.checkpasswords();
      if (
        this.emailvalid &&
        this.emailsmatch &&
        this.usernameok &&
        this.passwordstrength &&
        this.passwordsmatch
      ) {
        const saltRounds = 10;
        const salt = bcrypt.genSaltSync(saltRounds);
        const user = JSON.stringify({
          name: this.username,
          email: this.email1,
          password: this.encryptPassword(this.password1, salt),
          salt: salt,
        });
        const requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: user,
        };
        fetch("http://localhost:8080/api/auth/signup", requestOptions)
          .then((response) => response.json())
          .then((json) => {
            this.bottomText = json.answer;
            if (
              this.bottomText != undefined &&
              this.bottomText.substring(0, 5) != "Error"
            ) {
              this.$router.push("/login");
            }
          })
          .catch(() => {
            this.error = true;
          });
      }
    },
    login() {
      this.$router.push("/login");
    },
  },
};
</script>