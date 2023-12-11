<template>
    <v-container>
        <v-row>
            <v-btn @click="showCreateDialog = true">Create Product</v-btn>
        </v-row>
        <v-row>
            <v-data-table :items="products"></v-data-table>
        </v-row>
        <v-dialog v-model="showCreateDialog">
            <v-card>
                <v-card-title>Create Product</v-card-title>
                <div class="pl-5 pr-5 pt-5 pb-5"> 
                    <v-text-field       
                    :rules="[rules.required]"
                    v-model="newProductName"
                    label="Name"></v-text-field>
                    <v-text-field       
                    :rules="[rules.required]"
                    v-model="newProductQuantity"
                    type="number"
                    label="Quantity"></v-text-field>
                    <v-text-field       
                    :rules="[rules.required]"
                    type="number"
                    v-model="newProductPrice"
                    label="Price"></v-text-field>
                    <v-btn class="mr-5" @click="createProduct" :disabled="submitDisabled">Submit</v-btn>
                    <v-btn @click="showCreateDialog = false">Close</v-btn>
                </div>
            </v-card>
        </v-dialog>
    </v-container>
</template>
<script lang="ts" setup>
import { Product } from '@/models';
import axios from 'axios';
import { computed, onMounted } from 'vue';
import { ref } from 'vue';

const rules = {
    required: (value: string) => { return !!value || 'Field is required' },
}

const products = ref<Product[]>([]);
const showCreateDialog = ref(false);
const newProductName = ref('');
const newProductQuantity = ref(0);
const newProductPrice = ref(0);

const submitDisabled = computed(() => { 
    return newProductName.value === '' || newProductQuantity.value === 0 || newProductPrice.value === 0;
});

const createProduct = async () => { 
    await axios.post(import.meta.env.VITE_PURCHASE_URL + "/products", { id: null, name: newProductName.value, quantity: newProductQuantity.value, price: newProductPrice.value }); 
    products.value = (await axios.get(import.meta.env.VITE_PURCHASE_URL + "/products")).data;
    showCreateDialog.value = false;
    newProductName.value = '';
    newProductQuantity.value = 0; 
    newProductPrice.value = 0;
}


onMounted(async () => { 
    products.value = (await axios.get(import.meta.env.VITE_PURCHASE_URL + "/products")).data;
});
</script>