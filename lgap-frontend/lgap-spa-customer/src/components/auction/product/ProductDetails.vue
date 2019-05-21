<template>
    <lgap-base-component>
        <div slot="title">Product Details</div>
        <div slot="body">
            <div class="row">
                <div class="col-md-6">
                    <img v-bind:src="data.imageUrl" class="img-responsive img-rounded">
                </div>
                <div class="col-md-6">
                    <h4>Product Name</h4>
                    <p>{{data.name}}</p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <h4>Product Description</h4>
                    <p>{{data.description}}</p>
                </div>
            </div>

            <!--<pre>{{JSON.stringify(data, null, 4)}}</pre>-->
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
        }
    }
</script>
