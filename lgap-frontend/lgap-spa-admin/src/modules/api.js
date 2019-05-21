import Vue from 'vue';
import VueResource from 'vue-resource';

import {Store} from './store';

Vue.use(VueResource);

Vue.http.options.root = 'https://127.0.0.1/api/';

// Inject the authorization token into every single API call
Vue.http.interceptors.push((request, next) => {
    request.headers.set('Authorization', 'Bearer ' + Store.getters.authToken);
    next();
});

export const Api = {
    admin: Vue.resource('admin', {}, {
        login:  { method: 'POST', url: 'admin/login' },
    }),
    product: Vue.resource('product', {}, {
        getAll:  { method: 'GET', url: 'product/get/all' },
        getById: { method: 'GET', url: 'product/get{/productId}' }
    }),
    auction: Vue.resource('auction', {}, {
        getById: { method: 'GET',  url: 'auction/get{/auctionType}{/auctionId}' },
        search:  { method: 'POST', url: 'auction/search/advanced' },
        create:  { method: 'POST', url: 'auction/create' }
    })
};