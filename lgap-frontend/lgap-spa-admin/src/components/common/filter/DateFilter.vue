<template>
    <div>
        <label>{{label}}</label>
        <div>
            <div class="input-group">
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        {{ actionName }}
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a v-on:click="onActionChange('BEFORE')">Before</a></li>
                        <li><a v-on:click="onActionChange('ON')">On</a></li>
                        <li><a v-on:click="onActionChange('AFTER')">After</a></li>
                    </ul>
                </div>
                <input v-model="date" type="date" class="form-control">
            </div>
        </div>
    </div>
</template>

<script>
    import moment from "moment";

    export default {
        props: {
            label: {
                type: String,
                required: true
            }
        },
        data() {
            return {
                action: "ON",
                date: moment().format("YYYY-MM-DD")
            };
        },
        watch: {
            date() {
                this.fireEvent();
            }
        },
        computed: {
            actionName() {
                switch (this.action) {
                    case "BEFORE": return "Before";
                    case "ON":     return "On";
                    case "AFTER":  return "After";
                    default:       return this.action;
                }
            }
        },
        methods: {
            onActionChange(action) {
                this.action = action;
                this.fireEvent();
            },
            fireEvent() {
                this.$emit('filter', {
                    action: this.action,
                    date: this.date
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