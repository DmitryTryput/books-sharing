<template>
    <div>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">First Name</th>
                <th scope="col">Last Name</th>
                <th scope="col">Date of Birth</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(author, index) in authors" :key="index">
                <td>{{author.id}}</td>
                <td>{{author.firstName}}</td>
                <td>{{author.lastName}}</td>
                <td>{{author.dob}}</td>

            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name: "Authors",
        data() {
            return {
                authors: []
            }
        },

        created() {
            axios.get(`/backend/api/authors/list`)
                .then(response => {
                    this.authors = response.data;
                });
        }
    }
</script>

<style scoped>

</style>