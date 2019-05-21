<template>
    <lgap-collapsible-panel title="Search" context="primary">
        <div class="row">

            <!-- Auction name filter -->
            <div class="col-md-3">
                <lgap-text-filter label="Auction name, description" v-on:filter="auctionNameFilterChanged"></lgap-text-filter>
            </div>

            <!-- Region filters -->
            <div class="col-md-3">
                <lgap-region-filter v-on:filter="regionFilterChanged"></lgap-region-filter>
            </div>

            <!-- Auction type filter -->
            <div class="col-md-3">
                <lgap-auction-type-filter v-on:filter="auctionTypeFilterChanged"></lgap-auction-type-filter>
            </div>

            <!-- Auction status filter -->
            <div class="col-md-3">
                <lgap-auction-status-filter v-on:filter="auctionStatusFilterChanged"></lgap-auction-status-filter>
            </div>

        </div>

        <br>

        <div class="row">

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
    import CollapsiblePanel from "../../common/CollapsiblePanel.vue";
    import TextFilter from "../../common/filter/TextFilter.vue";
    import DateFilter from "../../common/filter/DateFilter.vue";
    import RegionFilter from "./RegionFilter.vue";
    import AuctionTypeFilter from "./AuctionTypeFilter.vue";
    import AuctionStatusFilter from "./AuctionStatusFilter.vue";

    export default {
        components: {
            lgapCollapsiblePanel: CollapsiblePanel,
            lgapTextFilter: TextFilter,
            lgapRegionFilter: RegionFilter,
            lgapAuctionTypeFilter: AuctionTypeFilter,
            lgapAuctionStatusFilter: AuctionStatusFilter,
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
            regionFilterChanged(filter) {
                console.log(filter);
            },
            auctionTypeFilterChanged(filter) {
                console.log(filter);
            },
            auctionStatusFilterChanged(filter) {
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