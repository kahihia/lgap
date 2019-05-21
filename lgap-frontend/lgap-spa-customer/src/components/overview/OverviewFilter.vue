<template>
    <lgap-collapsible-panel title="Search" context="primary">
        <div class="row">

            <!-- Auction name filter -->
            <div class="col-md-3">
                <lgap-text-filter label="Name, description" v-on:filter="auctionNameFilterChanged"></lgap-text-filter>
            </div>

            <!-- Auction start date filter -->
            <div class="col-md-3">
                <lgap-date-filter label="Beginning" v-on:filter="beginningFilterChanged"></lgap-date-filter>
            </div>

            <!-- Auction end date filter -->
            <div class="col-md-3">
                <lgap-date-filter label="Ending" v-on:filter="endingFilterChanged"></lgap-date-filter>
            </div>

        </div>
    </lgap-collapsible-panel>
</template>

<script>
    import CollapsiblePanel from "../common/CollapsiblePanel.vue";
    import TextFilter from "../common/filter/TextFilter.vue";
    import DateFilter from "../common/filter/DateFilter.vue";

    export default {
        components: {
            lgapCollapsiblePanel: CollapsiblePanel,
            lgapTextFilter: TextFilter,
            lgapDateFilter: DateFilter
        },
        data() {
            return {
                filter: {}
            };
        },
        methods: {
            auctionNameFilterChanged(filter) {
                console.log(filter);
            },
            beginningFilterChanged(filter) {
                console.log(filter);
            },
            endingFilterChanged(filter) {
                console.log(filter);
            }
        }
    }
</script>