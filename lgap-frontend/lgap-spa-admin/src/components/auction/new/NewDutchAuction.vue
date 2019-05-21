<template>
    <form>

        <div class="row">

            <!-- Base (starting) price -->
            <div class="col-md-4 form-group">
                <label>Starting price</label>
                <div class="input-group">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-usd"></span></span>
                    <input v-model.number="dutchAuction.dutchAuctionBasePrice" type="number" class="form-control">
                    <span class="input-group-addon">per kilogram</span>
                </div>
            </div>

            <!-- Price modifier -->
            <div class="col-md-4 form-group">
                <label>Price decrement</label>
                <div class="input-group">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-usd"></span></span>
                    <input v-model.number="dutchAuction.dutchAuctionPriceModifier" type="number" class="form-control">
                    <span class="input-group-addon">per turn</span>
                </div>
            </div>

        </div>

        <!--<pre>{{dutchAuction}}</pre>-->

    </form>
</template>

<script>
    export default {
        data() {
            return {
                turnDuration: 24,
                turnDurationUnit: "HOURS",
                dutchAuction: {
                    dutchAuctionBasePrice: 0.0,
                    dutchAuctionPriceModifier: 0.25,
                    dutchAuctionTurnCount: 10,
                    dutchAuctionTurnDuration: 1440,
                }
            };
        },
        watch: {
            dutchAuction: {
                handler() {
                    this.onInputChange();
                },
                deep: true
            },
            turnDuration(duration) {
                this.dutchAuction.dutchAuctionTurnDuration = this.convertTimeDurationToMinutes(duration, this.turnDurationUnit);
            },
            turnDurationUnit(durationUnit) {
                this.dutchAuction.dutchAuctionTurnDuration = this.convertTimeDurationToMinutes(this.turnDuration, durationUnit);
            }
        },
        computed: {
            turnDurationUnitDisplay() {
                switch (this.turnDurationUnit) {
                    case "HOURS": return "Hours";
                    case "DAYS": return "Days";
                    default: return this.turnDurationUnit;
                }
            }
        },
        methods: {
            onInputChange() {
                this.$emit('input', this.dutchAuction);
            },
            convertTimeDurationToMinutes(duration, durationUnit) {
                switch (durationUnit) {
                    case "HOURS": return (duration * 60);
                    case "DAYS":  return (duration * 24 * 60);
                    default: return this.turnDurationUnit;
                }
            }
        },
        created() {
            // Send the initial values
            this.$emit('input', this.dutchAuction);
        }
    }
</script>

<style scoped>
    a:hover {
        cursor: pointer;
    }
</style>