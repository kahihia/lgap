import Vue from 'vue';
import VueRouter from 'vue-router';

import {Store} from './store';

import Home from '../components/Home.vue';
import LoginMain from '../components/login/LoginMain.vue';
import LogoutMain from '../components/logout/LogoutMain.vue';
import OverviewMain from '../components/overview/OverviewMain.vue';
import AuctionMain from '../components/auction/AuctionMain.vue';

const login_page_name = "login";

const routes = [
    { name: login_page_name, path: '/login',                           component: LoginMain },
    { name: 'logout',        path: '/logout',                          component: LogoutMain },
    { name: 'home',          path: '/',                                component: Home },
    { name: 'overview',      path: '/overview',                        component: OverviewMain },
    { name: 'auction',       path: '/auction/:auctionType/:auctionId', component: AuctionMain },
    { name: 'default',       path: '*',                                redirect: { name: 'home' }}
];

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

Vue.use(VueRouter);

export {Router};