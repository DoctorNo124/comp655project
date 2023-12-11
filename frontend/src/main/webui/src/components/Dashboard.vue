<template>
    <v-container>
        <v-row>
            <v-col class="d-flex justify-center" v-for="card in dashboardCards">
                <v-card :to="card.path">
                    <v-card-title class="text-h4 mb-5"><v-icon :icon="card.icon"></v-icon>{{  card.name  }}</v-card-title>
                    <v-card-text class="text-h5">{{ card.description }}</v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script lang="ts" setup>
    interface DashboardCard { 
        name: string;
        description: string;
        path: string;
        icon: string;
    }

    const dashboardCards: DashboardCard[] = [
        {
            name: "Purchase",
            description: "Make a purchase with a random customer of a random product.",
            path: "/purchase",
            icon: "mdi-currency-usd"
        },
        {
            name: "Customers",
            description: "View all the customers",
            path: "/customers",
            icon: "mdi-account"
        }, 
        {
            name: "Products",
            description: "View all the products", 
            path: "/products", 
            icon: "mdi-gift-outline"
        }
    ]
</script>

<style lang="css">
    .v-card-title { 
        background: blue;
        color: white;
    }
</style>