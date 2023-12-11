<template>
    <v-container class="w-75">
        <v-row class="justify-center justify-space-between">
            <v-col class="flex-grow-0" v-for="card in dashboardCards">
                <v-card height="300" width="300" class="d-flex flex-column align-center justify-center text-center" :to="card.path">
                    <v-card-title class="text-h4 mb-5 w-100"><v-icon :icon="card.icon"></v-icon>{{  card.name  }}</v-card-title>
                    <v-container class="h-75">
                        <v-row class="align-center justify-center h-75">
                            <v-col>
                                <v-card-text class="text-h5">{{ card.description }}</v-card-text>
                            </v-col>
                        </v-row>
                    </v-container>
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