<template>
    <div>

        <!-- Header -->
        <div class="page-header">
            <h1>New Auction</h1>
        </div>

        <!-- Cargo Details -->
        <div class="panel panel-default">
            <!-- Header -->
            <div class="panel-heading">
                <h4 class="panel-title">Cargo Details</h4>
            </div>
            <!-- Content -->
            <div class="panel-body">
                <lgap-new-cargo v-bind:product="product" v-model="baseAuction.auctionCargo"></lgap-new-cargo>
            </div>
        </div>

        <!-- Auction details -->
        <div class="panel panel-default">

            <!-- Header -->
            <div class="panel-heading">
                <h4 class="panel-title">Auction Details</h4>
            </div>

            <!-- Content -->
            <div class="panel-body">

                <form>

                    <!-- Auction name -->
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Auction name</label>
                            <input v-model="baseAuction.auctionName" type="text" class="form-control">
                        </div>
                    </div>

                    <!-- Auction description -->
                    <div class="row">
                        <div class="col-md-12 form-group">
                            <label>Description</label>
                            <textarea v-model="baseAuction.auctionDescription" rows="5" class="form-control"></textarea>
                        </div>
                    </div>

                    <div class="row">

                        <!-- Auction start date -->
                        <div class="col-md-6 form-group">
                            <label>Auction start date</label>
                            <input v-model="tempAuctionStartDate" type="date" class="form-control" placeholder="YYYY-MM-DD">
                        </div>

                        <!-- Auction end date -->
                        <div class="col-md-6 form-group">
                            <label>Auction end date</label>
                            <input v-model="tempAuctionEndDate" type="date" class="form-control">
                        </div>

                    </div>

                    <!-- Reserve price -->
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Reserve price</label>
                            <div class="input-group">
                                <span class="input-group-addon"><span class="glyphicon glyphicon-usd"></span></span>
                                <input v-model.number="baseAuction.auctionReservePrice" type="number" class="form-control">
                                <span class="input-group-addon">per kilogram</span>
                            </div>
                        </div>
                    </div>

                    <!-- Regions -->
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Regions</label>
                            <div>
                                <input v-model="baseAuction.auctionRegions.euus" type="checkbox">
                                <label class="comboBoxLabel">Europe and the United States</label>
                            </div>
                            <div>
                                <input v-model="baseAuction.auctionRegions.northAsia" type="checkbox">
                                <label class="comboBoxLabel">North Asia</label>
                            </div>
                            <div>
                                <input v-model="baseAuction.auctionRegions.amea" type="checkbox">
                                <label class="comboBoxLabel">Asia, Middle East and Africa</label>
                            </div>
                        </div>
                    </div>

                    <!-- Auction type -->
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Auction method</label>
                            <select v-model="baseAuction.auctionType" class="form-control">
                                <option v-for="method in $lgap.auctionTypes" v-bind:value="method">{{method | descriptiveMethodName}}</option>
                            </select>
                        </div>
                    </div>

                    <!--<pre>{{baseAuction}}</pre>-->

                </form>

                <hr>

                <!-- Auction type specific form -->
                <component v-bind:is="dynamicAuctionComponent" v-model="baseAuction.extendedAuctionData"></component>

                <hr>

                <div class="row">
                    <div class="col-md-1 pull-right">
                        <button v-on:click="onSubmit" class="btn btn-success pull-right">
                            <span class="glyphicon glyphicon-check"></span> Submit
                        </button>
                    </div>
                    <div class="col-md-1 pull-right">
                        <button v-on:click="onDiscard" class="btn btn-danger pull-right">
                            <span class="glyphicon glyphicon-trash"></span> Discard
                        </button>
                    </div>
                </div>

            </div>
        </div>

    </div>
</template>

<script>
    import NewDutchAuction from "./NewDutchAuction.vue";
    import NewCargo from "./NewCargo.vue";
    import moment from "moment";

    export default {
        components: {
            lgapNewDutchAuction: NewDutchAuction,
            lgapNewCargo: NewCargo
        },
        props: {
            productId: {
                required: true
            }
        },
        data() {
            return {
                tempAuctionStartDate: moment().format("YYYY-MM-DD"),
                tempAuctionEndDate: moment().format("YYYY-MM-DD"),
                baseAuction: {
                    auctionName: "",
                    auctionDescription: "",
                    auctionStartTimestamp: new Date(moment.utc().startOf('day')).getTime(),
                    auctionEndTimestamp: new Date(moment.utc().startOf('day')).getTime(),
                    auctionReservePrice: 0.0,
                    auctionRegions: {
                        euus: false,
                        northAsia: false,
                        amea: false
                    },
                    auctionType: this.$lgap.auctionTypes[0],
                    auctionCargo: {},
                    extendedAuctionData: {}
                },
                product: {}
            };
        },
        watch: {
            tempAuctionStartDate(date) {
                this.baseAuction.auctionStartTimestamp = new Date(moment.utc(date).startOf('day')).getTime();
            },
            tempAuctionEndDate(date) {
                this.baseAuction.auctionEndTimestamp = new Date(moment.utc(date).startOf('day')).getTime();
            }
        },
        computed: {
            dynamicAuctionComponent() {
                // clear the extended data on component change
                this.baseAuction.extendedAuctionData = {};
                switch (this.baseAuction.auctionType) {
                    case "DUTCH": return "lgapNewDutchAuction";
                    default: return this.auctionType
                }
            }
        },
        methods: {
            onDiscard() {
                this.$router.back();
            },
            onSubmit() {
                const type = this.baseAuction.auctionType;
                this.$lgap.api.auction.create(this.baseAuction);
//                this.$router.back();
            }
        },
        created() {
            this.$lgap.api.product.getById({ productId: this.$route.params.productId })
                .then(response => {
                    this.product = response.body;
                });
        }
    }
</script>

<style scoped>
    label.comboBoxLabel {
        font-weight: normal;
    }
</style>