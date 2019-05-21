<template>
    <div></div>
</template>

<script>
    export default {
        created() {
            this.$store.dispatch('logout');
            this.$router.push({name: 'home'});
        }
    }
</script>