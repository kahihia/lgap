<template>
    <lgap-select-filter
            label="Auction Status"
            v-bind:values="auctionStatus"
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
                auctionStatus: []
            };
        },
        methods: {
            formatter(typeCode) {
                switch (typeCode) {
                    case this.auctionStatus[0]: return "Preview";
                    case this.auctionStatus[1]: return "Running";
                    case this.auctionStatus[2]: return "Ended";
                    case this.auctionStatus[3]: return "Canceled";
                    default: return typeCode;
                }
            },
            delegateEvent(event) {
                this.$emit('filter', event);
            }
        },
        created() {
            this.auctionStatus = this.$lgap.auctionStates.slice();
        }
    }
</script>