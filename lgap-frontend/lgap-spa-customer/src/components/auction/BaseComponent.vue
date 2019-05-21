<template>
    <div v-bind:class="panelContext" class="panel">
        <div class="panel-heading">
            <h4 class="panel-title">
                <slot name="title"></slot>
            </h4>
        </div>
        <div class="panel-body">
            <slot name="body"></slot>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            context: {
                type: String,
                default: 'default'
            }
        },
        computed: {
            panelContext() {
                return 'panel-' + this.context;
            }
        }
    }
</script>