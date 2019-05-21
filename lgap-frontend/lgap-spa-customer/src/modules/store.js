import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export const Store = new Vuex.Store({
    state: {
        authToken: null
    },
    getters: {
        isAuthenticated: (state) => {
            return (state.authToken !== null);
        },
        authToken: (state) => {
            return state.authToken;
        }
    },
    mutations: {
        authToken: (state, token) => {
            state.authToken = token;
        }
    },
    actions: {
        login: (context, token) => {
            context.commit('authToken', token);
        },
        logout: (context) => {
            context.commit('authToken', null);
        }
    }
});