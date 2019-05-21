<template>
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">

            <div class="navbar-header">

                <!-- RESPONSIVE MENU BUTTON -->
                <button v-on:click="toggleCollapse" type="button" class="navbar-toggle">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>

                <!-- BRANDING -->
                <router-link to="/" class="navbar-brand">Lenzing Auction Portal</router-link>

            </div>

            <div v-if="showMenu" v-bind:class="collapseClassSwitch" class="collapse navbar-collapse">
                <!-- AVAILABLE MENU ITEMS -->
                <ul class="nav navbar-nav navbar-right">
                    <router-link to="/overview" tag="li" v-on:click.native="toggleCollapse" activeClass="active"><a>Overview</a></router-link>
                    <router-link to="/logout"   tag="li" ><a><span class="glyphicon glyphicon-log-out"></span> Logout</a></router-link>
                </ul>
            </div>

        </div>
    </nav>
</template>

<script>
    export default {
        data() {
            return {
                collapseClassSwitch: {
                    in: false
                }
            };
        },
        computed: {
            showMenu() {
                return this.$store.getters.isAuthenticated;
            }
        },
        methods: {
            toggleCollapse() {
                this.collapseClassSwitch.in = !this.collapseClassSwitch.in;
            }
        }
    }
</script>