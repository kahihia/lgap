<template>
    <div class="row">

        <div class="col-md-4"></div>

        <div class="col-md-4 login">
            <div class="panel panel-primary">

                <div class="panel-heading">
                    <h4 class="panel-title">
                        Sign-in
                    </h4>
                </div>

                <div class="panel-body">

                    <div class="row gap">
                        <div class="col-md-12">
                            <label>User Name</label>
                            <input v-model="userName" type="text" class="form-control" placeholder="User Name">
                        </div>
                    </div>

                    <div class="row gap">
                        <div class="col-md-12">
                            <label>Password</label>
                            <input v-model="password" type="password" class="form-control" placeholder="Password">
                        </div>
                    </div>

                    <button v-on:click="doSignIn" class="btn btn-primary" style="float:right">Submit</button>

                </div>

            </div>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                userName: "",
                password: "",
            }
        },
        computed: {
            loginData() {
                return {
                    username: this.userName,
                    password: this.password
                }
            }
        },
        methods: {
            doSignIn() {
                this.$lgap.api.admin.login(this.loginData)
                    .then(response => {
                        this.$store.dispatch('login', response.body.authToken);
                        this.$router.push({name: 'home'});
                    });

            }
        }
    }
</script>

<style scoped>
    .login {
        margin-top: 10%;
    }
    .gap {
        padding-bottom: 10px;
    }
</style>