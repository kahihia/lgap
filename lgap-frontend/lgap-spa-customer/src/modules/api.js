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
    customer: Vue.resource('customer', {}, {
        login:  { method: 'POST', url: 'customer/login' }
    }),
    auction: Vue.resource('auction', {}, {
        search:  { method: 'POST', url: 'auction/search' },
        getById: { method: 'GET',  url: 'auction/get{/auctionType}{/auctionId}' },
        getTurn: { method: 'GET',  url: 'auction/get/turn{/auctionType}{/auctionId}' },
        bid:     { method: 'POST', url: 'auction/bid'}

    }),
    product: Vue.resource('product', {}, {
        getById: { method: 'GET', url: 'product/get{/productId}' }
    }),
};