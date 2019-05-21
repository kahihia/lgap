<template>
    <div v-bind:class="contextCSS" class="panel">
        <div class="panel-heading">
            <h4 class="panel-title">
                <span v-on:click="toggleCollapse" class="panel-collapse-clickable">
                    {{title}}
                </span>
                <span v-on:click="toggleCollapse" class="pull-right panel-collapse-clickable">
                    <i v-bind:class="iconCSS" class="glyphicon"></i>
                </span>
            </h4>
        </div>
        <div v-bind:class="collapseCSS" class="panel-collapse collapse">
            <div class="panel-body">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            title: {
                type: String,
                required: true
            },
            collapsed: {
                type: Boolean,
                default: true
            },
            context: {
                type: String,
                default: "default"
            }
        },
        data() {
            return {
                isCollapsed: !this.collapsed
            };
        },
        computed: {
            collapseCSS() {
                return {
                    'in': this.isCollapsed
                };
            },
            iconCSS() {
                return {
                    'glyphicon-chevron-down': !this.isCollapsed,
                    'glyphicon-chevron-up': this.isCollapsed
                };
            },
            contextCSS() {
                return "panel-" + this.context;
            }
        },
        methods: {
            toggleCollapse() {
                this.isCollapsed = !this.isCollapsed;
            }
        }
    }
</script>

<style scoped>
    .panel-collapse-clickable {
        cursor: pointer;
    }
</style>