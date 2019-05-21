<template>
    <div>

        <!-- Header -->
        <div class="page-header">
            <h1>Product Overview</h1>
        </div>

        <!-- Controls -->
        <lgap-product-controls></lgap-product-controls>

        <hr>

        <!-- Content -->
        <div class="panel panel-default">
            <div class="panel-heading">
                <h1 class="panel-title">Products</h1>
            </div>
            <div class="panel-body">
                <lgap-product-details v-for="product in products" v-bind:key="product.id" v-bind:product="product"></lgap-product-details>
            </div>
        </div>

    </div>
</template>

<script>
    import ProductControls from "./ProductControls.vue";
    import ProductDetails from "./ProductDetails.vue";

    export default {
        components: {
            lgapProductControls: ProductControls,
            lgapProductDetails: ProductDetails
        },
        data() {
            return {
                products: []
            };
        },
        created() {
            this.$lgap.api.product.getAll()
                .then(response => {
                    this.products = response.body;
                });
        }
    }
</script>