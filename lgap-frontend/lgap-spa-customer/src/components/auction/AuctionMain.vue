<template>
    <div>
        <div class="page-header">
            <h1>Auction Page</h1>
        </div>
        <div class="row">
            <div class="col-md-5">
                <component v-bind:is="productDetailsComponent" v-bind:data="productDetailsData"></component>
            </div>
            <div class="col-md-7">
                <component v-bind:is="auctionDetailsComponent" v-bind:data="auctionDetailsData"></component>
                <component v-bind:is="auctionStateComponent" v-bind:data="auctionStateData"></component>
                <component v-bind:is="biddingComponent" v-bind:data="biddingData"></component>
            </div>
        </div>
    </div>
</template>

<script>
    import ProductDetails from './product/ProductDetails.vue';
    import DutchAuctionDetails from './details/DutchAuctionDetails.vue';
    import DutchAuctionState from './state/DutchAuctionState.vue';
    import DutchAuctionBidding from './bidding/DutchAuctionBidding.vue';
    import PreviewAuctionState from './state/PreviewAuctionState.vue';
    import FinishedAuctionState from './state/FinishedAuctionState.vue';
    import PreviewBidding from './bidding/PreviewBidding.vue';
    import ContentProxy from '../common/ContentProxy.vue';

    export default {
        components: {
            lgapProductDetails:      ProductDetails,
            lgapDutchAuctionDetails: DutchAuctionDetails,
            lgapDutchAuctionState:   DutchAuctionState,
            lgapDutchAuctionBidding: DutchAuctionBidding,
            lgapPreviewAuctionState: PreviewAuctionState,
            lgapFinishedAuctionState: FinishedAuctionState,
            lgapBiddingPreview:      PreviewBidding,
            lgapContentLoading:      ContentProxy
        },
        data() {
            return {
                id: this.$route.params.auctionId,
                type: this.$route.params.auctionType,
                productDetailsComponent: "lgapContentLoading",
                auctionDetailsComponent: "lgapContentLoading",
                auctionStateComponent:   "lgapContentLoading",
                biddingComponent:        "lgapContentLoading",
                auction: {},
                cargo: {},
                auctionTurn: {},
                product: {}
            };
        },
        computed: {
            productDetailsData() {
                return {
                    name:        this.product.name,
                    description: this.product.description,
                    imageUrl:    this.product.imageUrl
                };
            },
            auctionDetailsData() {
                return {
                    name:           this.auction.name,
                    description:    this.auction.description,
                    cargoQuantity:  this.cargo.quantity,
                    cargoLocation:  this.cargo.location,
                    startTimestamp: this.auction.startDate,
                    endTimestamp:   this.auction.endDate,
                    basePrice:      this.auction.basePrice,
                    reservePrice:   this.auction.reservePrice,
                };
            },
            auctionStateData() {
                return {
                    basePrice:             this.auction.basePrice,
                    priceModifier:         this.auction.priceModifier,
                    turnNumber:            this.auctionTurn.turnNumber,
                    turnDuration:          this.auctionTurn.turnDuration,
                    turnStartTimestamp:    this.auctionTurn.startDateTimestamp,
                    turnEndTimestamp:      this.auctionTurn.endDateTimestamp
                };
            },
            biddingData() {
                return {
                    discount: 2,
                    totalPrice: (this.cargo.quantity * 1000 * (this.auction.basePrice - this.auctionTurn.turnNumber * this.auction.priceModifier)),
                };
            }
        },
        methods: {
            loadProductDetailsComponent() {
                this.productDetailsComponent = "lgapProductDetails";
            },
            loadAuctionDetailsComponent() {
                switch (this.auction.auctionType) {
                    case "DUTCH":
                        this.auctionDetailsComponent = "lgapDutchAuctionDetails";
                        break;
                }
            },
            loadAuctionStateComponent() {
                if (this.auction.auctionPhase === 'PREVIEW') {
                    this.auctionStateComponent = "lgapPreviewAuctionState";
                    this.biddingComponent = "";
                }
                else if (this.auction.auctionPhase === 'FINISHED') {
                    this.auctionStateComponent = "lgapFinishedAuctionState";
                    this.biddingComponent = "";
                }
                else if (this.auction.auctionPhase === 'RUNNING') {
                    switch (this.auction.auctionType) {
                        case "DUTCH":
                            this.auctionStateComponent = "lgapDutchAuctionState";
                            this.biddingComponent = "lgapDutchAuctionBidding";
                            break;
                    }
                }
            }
        },
        created() {

            this.$lgap.api.auction.getById({auctionType: this.type, auctionId: this.id})
                .then(auctionResponse => {

                    this.auction = auctionResponse.body;
                    this.cargo = this.auction.cargo;
                    this.loadAuctionDetailsComponent();
                    this.loadAuctionStateComponent();

                    if (this.auction.auctionPhase === 'RUNNING') {
                        this.$lgap.api.auction.getTurn({auctionType: this.type, auctionId: this.id})
                            .then(turnResponse => {
                                this.auctionTurn = turnResponse.body;
                                this.loadAuctionStateComponent();
                            });
                    }

                    this.$lgap.api.product.getById({productId: this.auction.cargo.extProductId})
                        .then(productResponse => {
                            this.product = productResponse.body;
                            this.loadProductDetailsComponent();
                        });
                });
        }
    }
</script>