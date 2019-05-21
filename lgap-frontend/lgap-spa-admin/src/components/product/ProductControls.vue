<template>
    <div class="row">

        <!-- Product name filter -->
        <div class="col-md-4">
            <div class="input-group">
                <span class="input-group-addon">Product name</span>
                <input type="text" class=" form-control">
                <span class="input-group-btn">
                        <button class="btn btn-default" type="button">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>
            </div>
        </div>

        <div class="col-md-1">
            <br>
        </div>

        <!-- Add new product -->
        <div class="col-md-2 pull-right">
            <button class="btn btn-success" type="button" v-on:click="createProductAction">
                <span class="glyphicon glyphicon-plus"></span> Add new product
            </button>
        </div>

    </div>
</template>

<script>
    export default {
        methods: {
            createProductAction() {
                this.$router.push({name: 'productCreate',});
            }
        }
    }
</script>