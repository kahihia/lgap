<template>
    <div>
        <div class="row well-sm">

            <!-- Product image -->
            <div class="col-md-3">
                <img v-bind:src="product.imageUrl" class="img-responsive img-rounded">
            </div>

            <div class="col-md-9">

                <!-- Product name -->
                <div class="row">
                    <div class="col-md-12">
                        <h3 class="productName">{{product.name}}</h3>

                        <!-- Edit product -->
                        <button type="button" class="btn btn-info" v-on:click="editProductAction">
                            <span class="glyphicon glyphicon-pencil"></span> Edit
                        </button>

                        <!-- Create auction -->
                        <button type="button" class="btn btn-success" v-on:click="createAuctionAction">
                            <span class="glyphicon glyphicon-plus"></span> New auction
                        </button>

                    </div>
                </div>

                <!-- Product description -->
                <div class="row">
                    <div class="col-md-12">
                        {{product.description}}
                    </div>
                </div>

            </div>
        </div>
        <hr>
    </div>
</template>

<script>
    export default {
        props: {
            product: {
                type: Object,
                required: true
            },
        },
        methods: {
            editProductAction() {

            },
            createAuctionAction() {
                this.$router.push({
                    name: 'auctionCreate',
                    params: {
                        productId: this.product.id
                    }
                });
            }
        }
    }
</script>

<style scoped>
    button {
        padding: 2px 5px 2px 5px;
        display: inline-block;
        margin-bottom: 7px;
    }
    .productName {
        display: inline-block;
    }
</style>