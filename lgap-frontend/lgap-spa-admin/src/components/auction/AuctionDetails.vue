<template>
    <div class="item">
        <div class="row well-sm">
            <div class="col-md-12">

                <!-- Auction name -->
                <div class="row">
                    <div class="col-md-12">
                        <h3>{{auction.name}}</h3>
                    </div>
                </div>

                <!-- Auction description -->
                <div class="row">
                    <div class="col-md-12">
                        <label>Description</label>
                        <p>{{auction.description}}</p>
                    </div>
                </div>

                <div class="row">

                    <!-- Regions -->
                    <div class="col-md-3">
                        <label>Regions</label>
                        <ul class="">
                            <li v-for="region in auction.regions">{{region | fullRegionName}} </li>
                        </ul>
                    </div>

                    <!-- Auction type -->
                    <div class="col-md-3">
                        <label>Auction type</label>
                        <p>{{auction.auctionType | descriptiveMethodName}}</p>
                    </div>

                    <div class="col-md-3">

                        <!-- Reserve price -->
                        <div class="row">
                            <div class="col-md-12">
                                <label>Reserve price</label>
                                <p>${{auction.reservePrice}} per kilogram</p>
                            </div>
                        </div>

                        <!-- Base price -->
                        <div v-if="auction.basePrice" class="row">
                            <div class="col-md-12">
                                <label>Base price</label>
                                <p>${{auction.basePrice}} per kilogram</p>
                            </div>
                        </div>

                    </div>

                    <!-- Auction phase -->
                    <div class="col-md-3">
                        <label>Status</label>
                        <p>{{auction.auctionPhase}}</p>
                        <button v-if="isCancelable" type="button" class="btn btn-danger">
                            <span class="glyphicon glyphicon-ban-circle"></span> Cancel
                        </button>
                    </div>

                </div>

                <div class="row">

                    <!-- Start time -->
                    <div class="col-md-6 col-sm-6">
                        <label>Beginning</label>
                        <p>{{startTime | fromnow}}, on {{startTime | datetime}}</p>
                    </div>
                </div>

                <!-- End time -->
                <div class="row">
                    <div class="col-md-6 col-sm-6">
                        <label>Ending</label>
                        <p>{{endTime | fromnow}}, on {{endTime | datetime}}</p>
                    </div>
                </div>

            </div>

        </div>
        <hr>
    </div>
</template>

<script>
    import moment from "moment";

    export default {
        props: {
            auction: {
                type: Object,
                required: true
            }
        },
        computed: {
            startTime() {
                return moment(this.auction.startDate);
            },
            endTime() {
                return moment(this.auction.endDate);
            },
            isCancelable() {
                return (this.auction.auctionPhase === "PREVIEW" || this.auction.auctionPhase === "RUNNING");
            }
        }
    }
</script>

<style scoped>
    ul.region li {
        display: inline;
    }
    h3 {
        margin-top: 10px;
    }
    hr {
        margin-top: 5px;
        margin-bottom: 5px;
    }
    button {
        padding: 2px 5px 2px 5px;
        display: inline-block;
        margin-bottom: 7px;
    }
    .item:hover {
        background: #F3F3F3;
        cursor: pointer;
    }
</style>