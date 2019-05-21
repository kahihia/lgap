<template>
    <div>
        <div v-on:click="navigateToAuction" class="row well-sm item">
            <div class="col-md-2">
                {{auction.name}}
            </div>
            <div class="col-md-2">
                {{auction.description}}
            </div>
            <div class="col-md-2">
                {{auction.auctionType | descriptiveTypeName}}
            </div>
            <div class="col-md-3">
                From: {{startTime | datetime}}
            </div>
            <div class="col-md-3">
                Until: {{endTime | datetime}}
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
        data() {
            return {
                image: "http://d2n4wb9orp1vta.cloudfront.net/resources/images/cdn/cms/0310HPC_Recycling4.jpg"
            };
        },
        computed: {
            startTime() {
                return moment(this.auction.startDate);
            },
            endTime() {
                return moment(this.auction.endDate);
            }
        },
        methods: {
            navigateToAuction() {
                this.$router.push({
                    name: 'auction',
                    params: { auctionType: this.auction.auctionType, auctionId: this.auction.id }
                });
            }
        }
    }
</script>

<style scoped>
    .item:hover {
        background: #F3F3F3;
        cursor: pointer;
    }
    hr {
        margin-top: 0px !important;
        margin-bottom: 0px !important;
    }
    img {
        display: inline;
        max-width: 50px;
    }
</style>