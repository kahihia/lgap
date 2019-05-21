<template>
    <lgap-base-component>
        <div slot="title">Auction State</div>
        <div slot="body">
            <h3 class="text-muted">This auction has already ended</h3>
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