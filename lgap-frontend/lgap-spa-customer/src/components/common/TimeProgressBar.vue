<template>
    <div v-if="showProgressBar">
        <div class="progress">
            <div class="progress-bar progress-bar-striped active pb" role="progressbar"
                 v-bind:aria-valuenow="progress"
                 v-bind:aria-valuemin="startTime"
                 v-bind:aria-valuemax="endTime"
                 v-bind:style="progressBarWidth"
            >
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            startTime: {
                type: Number,
                required: true
            },
            endTime: {
                type: Number,
                required: true
            },
            update: {
                type: Number,
                default: 5
            }
        },
        data() {
            return {
                showProgressBar: false,
                progress: 0,
                timer: null
            };
        },
        computed: {
            progressPercentage() {
                let total = this.endTime - this.startTime;
                let traveled = this.progress - this.startTime;
                return (traveled * 100) / total;
            },
            progressBarWidth() {
                return {
                    width: this.progressPercentage + '%'
                };
            }
        },
        methods: {
            timerTick() {
                if (this.progress >= this.endTime) {
                    clearInterval(this.timer); // stop timer
                    this.showProgressBar = false;
                } else {
                    this.progress = new Date().getTime();
                }
            }
        },
        created() {

            if (this.startTime < this.endTime) {

                this.progress = new Date().getTime();
                this.showProgressBar = true;

                let vm = this;
                let cylce = this.update * 1000;

                this.timer = setInterval(() => {
                    vm.timerTick();
                }, cylce);

            }

        }
    }
</script>

<style scoped>
    .progress {
        margin-bottom: 0 !important;
    }

    .pb {
        background-color: lightgray;
    }
</style>