<template>
    <v-data-table :items="products"></v-data-table>
</template>
<script lang="ts" setup>
import { Product } from '@/models';
import axios from 'axios';
import { onMounted } from 'vue';
import { ref } from 'vue';


const products = ref<Product[]>([]);

onMounted(async () => { 
    products.value = (await axios.get(import.meta.env.VITE_PURCHASE_URL + "/products")).data;
});
</script>