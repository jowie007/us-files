// https://www.bezkoder.com/vue-3-authentication-jwt/
import { createStore } from 'vuex'
import { auth } from './auth.module'

const store = createStore({
  modules: {
    auth,
  },
  state: {
    pageTitle: "",
    newMessages: false,
  },
  mutations: {
    setPageTitle(state, value) {
      state.pageTitle = value;
    },
    setNewMessages(state, value) {
      state.newMessages = value;
    }
  },
})

export default store
