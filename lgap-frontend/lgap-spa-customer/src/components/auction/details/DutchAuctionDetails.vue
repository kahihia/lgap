<template>
    <lgap-base-component>
        <div slot="title">Auction Details</div>
        <div slot="body" v-if="data">

            <!-- Auction name -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Auction name</div>
                <div class="col-md-9 col-xs-9">{{data.name}}</div>
            </div>

            <hr>

            <!-- Auction description -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Description</div>
                <div class="col-md-9 col-xs-9">{{data.description}}</div>
            </div>

            <hr>

            <!-- Cargo quantity -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Quantity</div>
                <div class="col-md-9 col-xs-9">{{data.cargoQuantity}} tonnes</div>
            </div>

            <hr>

            <!-- Warehouse/Dispatch location -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Dispatched from</div>
                <div class="col-md-9 col-xs-9">{{data.cargoLocation}}
                    <a v-bind:href="warehouseGoogleMapsLocation" target="_blank">
                        <span class="glyphicon glyphicon-map-marker"></span>
                    </a>
                </div>
            </div>

            <hr>

            <!-- Auction start date -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Beginning of auction</div>
                <div class="col-md-9 col-xs-9">{{startTime | fromnow}}, on {{startTime | datetime}}</div>
            </div>

            <hr>

            <!-- Auction end date -->
            <div class="row">
                <div class="col-md-3 col-xs-3">End of auction</div>
                <div class="col-md-9 col-xs-9">{{endTime | fromnow}}, on {{endTime | datetime}}</div>
            </div>

            <hr>

            <!-- Initial price -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Initial price</div>
                <div class="col-md-9 col-xs-9">${{data.basePrice.toFixed(2)}} per kilogram</div>
            </div>

            <hr>

            <!-- Reserve price -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Reserve price</div>
                <div class="col-md-9 col-xs-9">${{data.reservePrice.toFixed(2)}} per kilogram</div>
            </div>

            <!--<pre>{{JSON.stringify(data, null, 4)}}</pre>-->
        </div>
    </lgap-base-component>
</template>

<script>
    import BaseComponent from '../BaseComponent.vue';
    import moment from 'moment';

    export default {
        components: {
            lgapBaseComponent: BaseComponent
        },
        props: {
            data: {
                type: Object,
                required: true
            }
        },
        data() {
            return {
                showAuctionMethodDescription: false,
            };
        },
        computed: {
            warehouseGoogleMapsLocation() {
                return 'https://www.google.com/maps/search/' + this.data.cargoLocation.replace(' ', '+');
            },
            startTime() {
                return moment(this.data.startTimestamp);
            },
            endTime() {
                return moment(this.data.endTimestamp);
            }
        }
    }
</script>

<style scoped>
    .icon-button {
        cursor: pointer;
    }
    a {
        color: inherit;
    }
    hr {
        margin-top: 5px !important;
        margin-bottom: 5px !important;
    }
</style>