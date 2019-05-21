<template>
    <div>

        <form>
            <div class="row">

                <!-- Cargo (product) quantity -->
                <div class="col-md-3 form-group">
                    <label>Product quantity</label>
                    <div class="input-group">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-scale"></span></span>
                        <input v-model.number="cargo.cargoQuantity" type="number" class="form-control">
                        <span class="input-group-addon">tonnes</span>
                    </div>
                </div>

                <!-- Aprox. delivery -->
                <div class="col-md-3 form-group">
                    <label>Delivery time</label>
                    <div class="input-group">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                        <input v-model.number="cargo.cargoExpectedDeliveryDays" type="number" class="form-control">
                        <span class="input-group-addon">days</span>
                    </div>
                </div>

                <!-- Cargo (warehouse) location-->
                <div class="col-md-6 form-group">
                    <label>Warehouse location</label>
                    <div class="input-group">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-map-marker"></span></span>
                        <input v-model="cargo.cargoLocation" type="text" class="form-control" placeholder="Linz, Austria">
                    </div>
                </div>

            </div>
        </form>

        <!--<pre>{{cargo}}</pre>-->

        <label>Product</label>
        <div class="panel well">
            <div class="row">
                <!-- Product image -->
                <div class="col-md-3">
                    <img v-bind:src="product.imageUrl" class="img-responsive img-rounded">
                </div>
                <div class="col-md-9">
                    <!-- Product name -->
                    <div class="row">
                        <div class="col-md-12">
                            <h3 class="productName">{{product.name}}</h3>
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
        </div>

    </div>
</template>

<script>
    export default {
        props: {
            product: {
                type: Object,
                default: null
            }
        },
        data() {
            return {
                cargo: {
                    cargoProductId: this.$route.params.productId,
                    cargoQuantity: 5.75,
                    cargoLocation: "",
                    cargoExpectedDeliveryDays: 5,
                }
            };
        },
        watch: {
            cargo: {
                handler() {
                    this.onInputChange();
                },
                deep: true
            }
        },
        methods: {
            onInputChange() {
                this.$emit('input', this.cargo);
            }
        },
        created() {
            // Send the initial values
            this.$emit('input', this.cargo);
        }
    }
</script>