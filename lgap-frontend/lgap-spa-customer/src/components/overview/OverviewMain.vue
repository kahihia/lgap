<template>
    <div>
        <div class="page-header">
            <h1>Overview Page</h1>
        </div>

        <lgap-overview-filter></lgap-overview-filter>

        <hr>

        <div class="row">
            <div class="col-md-12">
                <lgap-overview-group title="Ongoing Auctions" v-bind:collapsed="false">
                    <lgap-overview-group-item v-for="auction in ongoingAuctions" v-bind:auction="auction" v-bind:key="auction.id"></lgap-overview-group-item>
                </lgap-overview-group>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <lgap-overview-group title="Upcoming Auctions">
                    <lgap-overview-group-item v-for="auction in upcomingAuctions" v-bind:auction="auction" v-bind:key="auction.id"></lgap-overview-group-item>
                </lgap-overview-group>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <lgap-overview-group title="Past Auctions">
                    <lgap-overview-group-item v-for="auction in pastAuctions" v-bind:auction="auction" v-bind:key="auction.id"></lgap-overview-group-item>
                </lgap-overview-group>
            </div>
        </div>

        <!--<pre>{{auctions}}</pre>-->

    </div>
</template>

<script>
    import OverviewFilter from './OverviewFilter.vue';
    import OverviewGroup from './OverviewGroup.vue';
    import OverviewGroupItem from './OverviewGroupItem.vue';

    export default {
        components: {
            lgapOverviewFilter: OverviewFilter,
            lgapOverviewGroup: OverviewGroup,
            lgapOverviewGroupItem: OverviewGroupItem
        },
        data() {
            return {
                auctions: []
            };
        },
        computed: {
            ongoingAuctions() {
                return this.auctions.filter(auction => { return auction.auctionPhase === 'RUNNING'; });
            },
            upcomingAuctions() {
                return this.auctions.filter(auction => { return auction.auctionPhase === 'PREVIEW'; });
            },
            pastAuctions() {
                return this.auctions.filter(auction => { return auction.auctionPhase === 'FINISHED'; });
            }
        },
        methods: {
            fetchData() {
                this.$lgap.api.auction.search({})
                    .then(response => {
                        return response.json();
                    })
                    .then(data => {
                        this.auctions = data;
                    });
            }
        },
        created() {
            this.fetchData();
        }
    }
</script>