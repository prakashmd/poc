import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/store'
import './../node_modules/jquery/dist/jquery.min.js';
import './../node_modules/bootstrap/dist/css/bootstrap.min.css';
import './../node_modules/bootstrap/dist/js/bootstrap.min.js';
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import VueJsModal from 'plugin'
import axios from "axios";

library.add(faUserSecret)

Vue.use(VueJsModal, {
  dialog: true,
  dynamic: true,
  dynamicDefaults: {
    foo: 'foo'
  }
})
Vue.component('font-awesome-icon', FontAwesomeIcon)
axios.defaults.headers.common['Content-Type'] = 'application/json'
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
axios.defaults.headers.common['Accept'] = 'application/json, text/plain, */*'
axios.defaults.headers.common['Access-Control-Allow-Headers'] = 'Origin, Accept, Content-Type, Authorization, Access-Control-Allow-Origin'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
