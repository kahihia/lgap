<template>
    <div>
        <!-- HEADER -->
        <lgap-header></lgap-header>

        <div class="container">

            <!-- CONTENT -->
            <div class="row">
                <div class="col-md-12">
                    <router-view></router-view>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    import Header from './components/Header.vue';

    export default {
        components: {
            lgapHeader: Header
        }
    }
</script>