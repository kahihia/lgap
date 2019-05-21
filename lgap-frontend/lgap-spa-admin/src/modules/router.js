import Vue from 'vue'
import VueRouter from 'vue-router';

import {Store} from './store';

import Home from '../components/Home.vue';
import LoginMain from '../components/login/LoginMain.vue';
import LogoutMain from '../components/logout/LogoutMain.vue';
import ProductMain from '../components/product/ProductMain.vue';
import NewProduct from '../components/product/NewProduct.vue';
import AuctionMain from '../components/auction/AuctionMain.vue';
import NewAuction from '../components/auction/new/NewAuction.vue';
import CustomerMain from '../components/customer/CustomerMain.vue';

const login_page_name = "login";

const routes = [
    { name: login_page_name, path: '/login',                  component: LoginMain },
    { name: 'logout',        path: '/logout',                 component: LogoutMain },
    { name: 'home',          path: '/',                       component: Home },
    { name: 'product',       path: '/product',                component: ProductMain },
    { name: 'productCreate', path: '/product/new',            component: NewProduct },
    { name: 'auction',       path: '/auction',                component: AuctionMain },
    { name: 'auctionCreate', path: '/auction/new/:productId', component: NewAuction, props: true },
    { name: 'customer',      path: '/customer',               component: CustomerMain },
    { name: 'default',       path: '*',                       redirect: { name: 'home' }}
];

Vue.use(VueRouter);

const Router = new VueRouter({
    mode: 'history',
    routes: routes
});

Router.beforeEach((to, from, next) => {
    if (!Store.getters.isAuthenticated) {
        if (to.name !== login_page_name) {
            next({name: login_page_name});
        } else {
            next(true);
        }
    } else {
        next(true);
    }
});

export {Router};