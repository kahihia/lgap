<template>
    <lgap-base-component>
        <div slot="title">Auction State</div>
        <div slot="body">

            <!-- Current price -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Current price</div>
                <div class="col-md-9 col-xs-9">${{currentPrice.toFixed(2)}} per kilogram</div>
            </div>

            <hr>

            <!-- Price decrement -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Price modifier</div>
                <div class="col-md-9 col-xs-9">- ${{data.priceModifier}} per turn</div>
            </div>

            <hr>

            <!-- Price in next turn-->
            <div class="row">
                <div class="col-md-3 col-xs-3">Price in next turn</div>
                <div class="col-md-9 col-xs-9">
                    ${{priceInNextTurn.toFixed(2)}} per kilogram
                </div>
            </div>

            <hr>

            <!-- This turn started ... -->
            <div class="row">
                <div class="col-md-3 col-xs-3">This turn started</div>
                <div class="col-md-9 col-xs-9">
                    {{sinceThisTurnStarted}}, on {{beginningOfThisTurn | datetime}}
                </div>
            </div>

            <hr>

            <!-- Next turn in ... -->
            <div class="row">
                <div class="col-md-3 col-xs-3">Next turn starts</div>
                <div class="col-md-9 col-xs-9">
                    {{endOfThisTurnCountdown}}, on {{endOfThisTurn | datetime}}
                    <br>
                    <lgap-progress-bar v-if="data.turnStartTimestamp" v-bind:startTime="data.turnStartTimestamp" v-bind:endTime="data.turnEndTimestamp"></lgap-progress-bar>
                </div>
            </div>

            <!--<pre>{{JSON.stringify(data, null, 4)}}</pre>-->

        </div>
    </lgap-base-component>
</template>

<script>
    import BaseComponent from '../BaseComponent.vue';
    import ProgressBar from '../../common/TimeProgressBar.vue';
    import moment from 'moment';

    export default {
        components: {
            lgapBaseComponent: BaseComponent,
            lgapProgressBar: ProgressBar
        },
        props: {
            data: {
                type: Object,
                required: true
            }
        },
        computed: {
            turnLength() {
                return moment.duration(this.data.turnDuration, 'minutes').humanize();
            },
            currentPrice() {
                return (this.data.basePrice - (this.data.turnNumber - 1) * this.data.priceModifier);
            },
            priceInNextTurn() {
                return (this.currentPrice - this.data.priceModifier);
            },
            beginningOfThisTurn() {
                return moment(this.data.turnStartTimestamp);
            },
            sinceThisTurnStarted() {
                return this.beginningOfThisTurn.fromNow();
            },
            endOfThisTurn() {
                return moment(this.data.turnEndTimestamp);
            },
            endOfThisTurnCountdown() {
                return moment(this.endOfThisTurn).fromNow();
            }
        }
    }
</script>

<style scoped>
    hr {
        margin-top: 5px !important;
        margin-bottom: 5px !important;
    }
</style>