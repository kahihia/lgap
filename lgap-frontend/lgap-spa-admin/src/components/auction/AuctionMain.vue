<template>
    <div>

        <!-- Header -->
        <div class="page-header">
            <h1>Auction Overview</h1>
        </div>

        <!-- Filters -->
        <lgap-auction-filter></lgap-auction-filter>

        <hr id="search">

        <!-- Content -->
        <div class="panel panel-default">
            <div class="panel-heading">
                <h1 class="panel-title">Auctions</h1>
            </div>
            <div class="panel-body">
                <lgap-auction-details v-for="auction in auctions" v-bind:key="auction.id" v-bind:auction="auction"></lgap-auction-details>
            </div>
        </div>

    </div>
</template>

<script>
    import AuctionFilter from './filter/AuctionFilter.vue';
    import AuctionDetails from './AuctionDetails.vue';

    export default {
        components: {
            lgapAuctionFilter: AuctionFilter,
            lgapAuctionDetails: AuctionDetails
        },
        data() {
            return {
                auctions: []
            };
        },
        methods: {
            createAuctionAction() {
                this.$router.push({name: 'auctionCreate',});
            }
        },
        created() {
            this.$lgap.api.auction.search({})
                .then(response => {
                    this.auctions = response.body;
                });
        }
    }
</script>

<style scoped>
    hr#search {
        margin-top: 0;
    }
</style>