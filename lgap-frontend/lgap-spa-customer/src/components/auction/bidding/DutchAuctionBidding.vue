<template>
    <lgap-base-component context="primary">
        <div slot="title">Bidding</div>
        <div slot="body">

            <table class="table table-responsive">
                <tbody>
                <tr>
                    <td>Current total price</td>
                    <td>${{currentTotalPrice.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,')}}</td>
                </tr>
                <tr>
                    <td>Your total price</td>
                    <td>${{currentTotalPriceWithDiscount.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,')}}
                        <span class="label label-info"><b>-2%</b></span>
                    </td>
                </tr>
                </tbody>
            </table>

            <button v-on:click="submitBid" class="btn btn-danger" type="button">Accept Current Price</button>
            <!--<pre>{{data}}</pre>-->
        </div>
    </lgap-base-component>
</template>

<script>
    import BaseComponent from '../BaseComponent.vue';

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
                bidButtonDisabled: false
            };
        },
        computed: {
            currentTotalPrice() { return this.data.totalPrice; },
            currentTotalPriceWithDiscount() { return this.currentTotalPrice - this.currentTotalPrice * (this.data.discount / 100); },
            bid() {
                return {
                    extCustomerId: 1,
                    auctionId: this.$route.params.auctionId,
                    value: 10,
                    timestamp: new Date().getTime()
                }
            }
        },
        methods: {
            submitBid() {
                this.$lgap.api.auction.bid(this.bid)
                    .then(response => {
                        if (response.status === 200) {
                            this.$router.push({
                                name: 'overview'
                            });
                        }
                    });
            }
        }
    }
</script>