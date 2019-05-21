<template>
    <div>
        <label>{{label}}</label>
        <div>
            <div class="dropdown">
                <button id="typeDropdown" class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                    {{ getName(index) }}
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" aria-labelledby="typeDropdown">
                    <li v-on:click="onClick(null, null)"><a>All</a></li>
                    <li v-for="(value, index) in values" v-on:click="onClick(index, value)"><a>{{getName(index)}}</a></li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            label: {
                type: String,
                required: true
            },
            values: {
                type: Array,
                required: true
            },
            formatter: {
                required: false
            }
        },
        data() {
            return {
                index: null,
                value: null
            };
        },
        methods: {
            getName(index) {
                if (index === null) {
                    return "All"
                } else if (this.formatter) {
                    return this.formatter(this.values[index]);
                } else {
                    return this.values[index];
                }
            },
            onClick(index, value) {
                this.index = index;
                this.value = value;
                this.fireEvent();
            },
            fireEvent() {
                this.$emit('filter', {
                    index: this.index,
                    value: this.value
                });
            }
        }
    }
</script>

<style scoped>
    a:hover {
        cursor: pointer;
    }
</style>