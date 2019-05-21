<template>
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">

            <div class="navbar-header">

                <!-- BRANDING -->
                <router-link to="/" class="navbar-brand">Auction Administration</router-link>

                <!-- RESPONSIVE MENU BUTTON -->
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#headerNavbar">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>

            </div>

            <div v-if="showMenu" id="headerNavbar" class="collapse navbar-collapse">
                <!-- AVAILABLE MENU ITEMS -->
                <ul class="nav navbar-nav navbar-right">
                    <router-link to="/"        tag="li"><a>Home</a></router-link>
                    <router-link to="/product" tag="li"><a>Products</a></router-link>
                    <router-link to="/auction" tag="li"><a>Auctions</a></router-link>
                    <router-link to="/logout"  tag="li" ><a><span class="glyphicon glyphicon-log-out"></span> Logout</a></router-link>
                </ul>
            </div>

        </div>
    </nav>
</template>

<script>
    export default {
        computed: {
            showMenu() {
                return this.$store.getters.isAuthenticated;
            }
        }
    }
</script>