<template>
    <lgap-base-component>
        <div slot="title">Bidding</div>
        <div slot="body">
            <h3 class="text-muted">This auction is currently in preview mode</h3>
        </div>
    </lgap-base-component>
</template>

<script>
    import BaseComponent from '../BaseComponent.vue';

    export default {
        components: {
            lgapBaseComponent: BaseComponent
        },
        data() {
            return {};
        }
    }
</script>

<style scoped>
    h3 {
        text-align: center;
    }
</style>