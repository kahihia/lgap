<template>
    <lgapGroupSelectFilter
            label="Regions"
            v-bind:values="regions"
            v-bind:formatter="formatter"
            v-on:filter="delegateEvent">
    </lgapGroupSelectFilter>
</template>

<script>
    import GroupSelectFilter from "../../common/filter/GroupSelectFilter.vue";

    export default {
        components: {
            lgapGroupSelectFilter: GroupSelectFilter
        },
        data() {
            return {
                regions: []
            };
        },
        methods: {
            formatter(regionCode) {
                switch (regionCode) {
                    case this.regions[0]: return "EU & US";
                    case this.regions[1]: return "North Asia";
                    case this.regions[2]: return "AMEA";
                    default: return regionCode;
                }
            },
            delegateEvent(event) {
                this.$emit('filter', event);
            }
        },
        created() {
            this.regions = this.$lgap.auctionRegions.slice();
        }
    }
</script>