<template>
    <div>

        <div class="row welcome">
            <div class="col-md-12">
                <h1>Welcome to the Lenzing Global Auction Portal!</h1>
                <h3>Please sign-in to continue</h3>
            </div>
        </div>

        <div class="row">

            <div class="col-md-4"></div>

            <div class="col-md-4">
                <div class="panel panel-primary">

                    <div class="panel-heading">
                        <h4 class="panel-title">
                            Sign-in
                        </h4>
                    </div>

                    <div class="panel-body">

                        <div class="row gap">
                            <div class="col-md-12">
                                <label>Email</label>
                                <input v-model="emailAddress" type="text" class="form-control" placeholder="Portal Email Address">
                            </div>
                        </div>

                        <div class="row gap">
                            <div class="col-md-12">
                                <label>Password</label>
                                <input v-model="password" type="password" class="form-control" placeholder="Portal Password">
                            </div>
                        </div>

                        <span v-if="loginFailed" class="label label-danger">{{errorMessage}}</span>

                        <button v-on:click="doSignIn" class="btn btn-primary" style="float:right">Submit</button>

                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                emailAddress: "",
                password: "",
                loginFailed: false,
                errorMessage: null
            }
        },
        computed: {
            loginData() {
                return {
                    portalEmailAddress: this.emailAddress,
                    portalPassword: this.password
                }
            }
        },
        methods: {
            doSignIn() {
                this.$lgap.api.customer.login(this.loginData)
                    .then(response => {
                        this.loginFailed = false;
                        this.$store.dispatch('login', response.body.authToken);
                        this.$router.push({name: 'home'});
                    }, error => {
                        this.loginFailed = true;
                        if (error.status === 401) {
                            this.errorMessage = "Invalid email address and/or password";
                        } else {
                            this.errorMessage = "Something went wrong. Please try again later";
                        }
                    });
            }
        }
    }
</script>

<style scoped>
    .welcome {
        text-align: center;
        margin-bottom: 7%;
    }

    .gap {
        padding-bottom: 10px;
    }
</style>