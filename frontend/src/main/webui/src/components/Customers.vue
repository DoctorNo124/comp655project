<template>
<v-data-table :items="customers"></v-data-table>
</template>
<script lang="ts" setup>
import { Customer } from '@/models';
import axios from 'axios';
import { onMounted } from 'vue';
import { ref } from 'vue';


const customers = ref<Customer[]>([]);

onMounted(async () => { 
    customers.value = (await axios.get(import.meta.env.VITE_PURCHASE_URL + "/customers")).data;
});
</script>