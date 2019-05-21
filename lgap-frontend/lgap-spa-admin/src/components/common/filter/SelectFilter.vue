<template>
    <div>
        <label>{{label}}</label>
        <div>
            <select v-model="selectedValue" class="form-control">
                <option v-bind:value="null"></option>
                <option v-for="(value, index) in values" v-bind:value="value">{{getName(index)}}</option>
            </select>
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
                selectedValue: null
            };
        },
        watch: {
            selectedValue() {
                this.fireEvent();
            }
        },
        methods: {
            getName(index) {
                if (this.formatter) {
                    return this.formatter(this.values[index]);
                } else {
                    return this.values[index];
                }
            },
            fireEvent() {
                this.$emit('filter', {
                    value: this.selectedValue
                });
            }
        }
    }
</script>