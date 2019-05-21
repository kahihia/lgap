import Vue from 'vue';

import App from './App.vue';
import {Api} from './modules/api';
import {Store} from './modules/store';
import {Router} from './modules/router';
import "./modules/filter";

Vue.prototype.$lgap = {
    api: Api
};

new Vue({
    el: '#app',
    store: Store,
    router: Router,
    render: h => h(App)
});