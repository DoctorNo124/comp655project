<template>
    <v-container class="justify-center align-center">
        <v-row>
            <v-col class="d-flex justify-center">
                <v-btn @click="makePurchaseCall">Make Random Purchase</v-btn>
            </v-col>
        </v-row>
        <v-row v-if="purchase">
                <v-spacer></v-spacer>
                <v-col>
                    <v-card class="text-center">
                        <v-card-title class="mb-5">Customer</v-card-title>
                        <v-card-text>Name: {{ purchase.customer.name }}</v-card-text>
                        <v-card-text>Email: {{ purchase.customer.email }}</v-card-text>
                        <v-card-text>Balance: {{ customerNewBalance }}</v-card-text>
                    </v-card>
                </v-col>
                <v-col>
                    <v-card class="text-center">
                        <v-card-title class="mb-5">Product</v-card-title>
                        <v-card-text>Name: {{ purchase.product.name }}</v-card-text>
                        <v-card-text>Email: {{ productNewQuantity }}</v-card-text>
                        <v-card-text>Balance: {{ purchase.product.price }}</v-card-text>
                    </v-card>
                </v-col>
                <v-spacer></v-spacer>
        </v-row>
    </v-container>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { Purchase } from '@/models';
import axios from 'axios';
import { computed } from 'vue';

const purchase = ref<Purchase>();
const customerNewBalance = computed(() => { 
    if(purchase.value) { 
        return purchase.value.customer.balance - purchase.value.product.price;
    }
    return 0;
})

const productNewQuantity = computed(() => { 
    if(purchase.value) { 
        return purchase.value.product.quantity - 1;
    }
    return 0;
});

const makePurchaseCall = async () => { 
    purchase.value = (await axios.post(import.meta.env.VITE_PURCHASE_URL + "/purchase")).data;
}
</script>