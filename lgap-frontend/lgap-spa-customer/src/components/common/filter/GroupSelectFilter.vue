<template>
    <div>
        <label>{{label}}</label>
        <div>
            <div class="btn-group" role="group">
                <button v-for="(value, index) in values"
                        v-bind:class="toggle(index)"
                        v-on:click="onClick(index)"
                        type="button" class="btn btn-default">
                    {{ getName(index) }}
                </button>
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
                selected: new Array()
            };
        },
        methods: {
            getName(index) {
                if (this.formatter) {
                    return this.formatter(this.values[index]);
                } else {
                    return this.values[index];
                }
            },
            toggle(index) {
                return {
                    'active': this.selected[index],
                    'btn-success': this.selected[index],
                };
            },
            onClick(index) {
                this.selected.splice(index, 1, !this.selected[index]);
                this.fireEvent(index);
            },
            fireEvent(index) {
                this.$emit('filter', {
                    value: this.values[index],
                    selected: this.selected[index]
                });
            }
        },
        created() {
            for (const value in this.values) {
                this.selected.push(false);
            }
        }
    }
</script>