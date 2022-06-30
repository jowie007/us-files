import { createApp } from 'vue'
import App from './App.vue'
import '@/styles/dist.css'
import router from './router/router.js'
import store from './store'
import './styles/styles.css'
import BalmUI from 'balm-ui' // Official Google Material Components
import BalmUIPlus from 'balm-ui-plus' // BalmJS Team Material Components
import 'balm-ui-css'

// https://github.com/balmjs/balm-ui/issues/79
createApp(App)
  .use(BalmUI, {
    $theme: {
      'background': '#fff',
      'primary' : "#000",
      'on-primary': '#fff',
      // bg-gray-600, rgb(75 85 99);
      'secondary' : "#4b5563",
      'on-secondary': '#fff',
      'surface': '#fff',
      'on-surface': '#000',
      'error': '#b00020',
      'on-error': '#fff',
    },
  })
  .use(BalmUIPlus)
  .use(router)
  .use(store)
  .mount('#app')
