<template>
    <lgap-select-filter
            label="Auction Types"
            v-bind:values="auctionTypes"
            v-bind:formatter="formatter"
            v-on:filter="delegateEvent"
    ></lgap-select-filter>
</template>

<script>
    import SelectFilter from "../../common/filter/SelectFilter.vue";

    export default {
        components: {
            lgapSelectFilter: SelectFilter
        },
        data() {
            return {
                auctionTypes: []
            };
        },
        methods: {
            formatter(typeCode) {
                switch (typeCode) {
                    case this.auctionTypes[0]: return "Descending price auction";
                    case this.auctionTypes[1]: return "Ascending price auction";
                    default: return typeCode;
                }
            },
            delegateEvent(event) {
                this.$emit('filter', event);
            }
        },
        created() {
            this.auctionTypes = this.$lgap.auctionTypes.slice();
        }
    }
</script>