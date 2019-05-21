<template>
    <div>
        <label>{{label}}</label>
        <div>
            <div v-bind:class="{'input-group': isInputGroup}">
                <span v-if="prefix" class="input-group-addon">{{prefix}}</span>
                <input v-model="text" type="text" class="form-control">
                <span v-if="postfix" class="input-group-addon">{{postfix}}</span>
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
            defaultValue: {
                type: String,
                default: ""
            },
            prefix: {
                type: String,
                default: null
            },
            postfix: {
                type: String,
                default: null
            }
        },
        data() {
            return {
                isInputGroup: false,
                text: ""
            };
        },
        watch: {
            text() {
                this.fireEvent();
            }
        },
        methods: {
            fireEvent() {
                this.$emit('filter', {
                   text: this.text
                });
            }
        },
        created() {
            this.isInputGroup = this.prefix || this.postfix;
            this.text = this.defaultValue;
        }
    }
</script>