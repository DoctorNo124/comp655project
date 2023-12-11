<template>
    <v-container>
        <v-row>
            <v-btn @click="showCreateDialog = true">Create Customer</v-btn>
        </v-row>
        <v-row>
            <v-data-table :items="customers"></v-data-table>
        </v-row>
        <v-dialog v-model="showCreateDialog">
            <v-card>
                <v-card-title>Create Customer</v-card-title>
                <div class="pl-5 pr-5 pt-5 pb-5"> 
                    <v-text-field       
                    :rules="[rules.required]"
                    v-model="newCustomerName"
                    label="Name"></v-text-field>
                    <v-text-field       
                    :rules="[rules.required]"
                    v-model="newCustomerEmail"
                    label="E-mail"></v-text-field>
                    <v-text-field       
                    :rules="[rules.required]"
                    type="number"
                    v-model="newCustomerBalance"
                    label="Balance"></v-text-field>
                    <v-btn class="mr-5" @click="createCustomer" :disabled="submitDisabled">Submit</v-btn>
                    <v-btn @click="showCreateDialog = false">Close</v-btn>
                </div>
            </v-card>
        </v-dialog>
    </v-container>
</template>
<script lang="ts" setup>
import { Customer } from '@/models';
import axios from 'axios';
import { create } from 'domain';
import { computed, onMounted } from 'vue';
import { ref } from 'vue';

const rules = {
    required: (value: string) => { return !!value || 'Field is required' },
}
 
const customers = ref<Customer[]>([]);
const showCreateDialog = ref(false);
const newCustomerName = ref('');
const newCustomerEmail = ref('');
const newCustomerBalance = ref(0);

const submitDisabled = computed(() => { 
    return newCustomerName.value === '' || newCustomerEmail.value === '' || newCustomerBalance.value === 0;
});

const createCustomer = async () => { 
    await axios.post(import.meta.env.VITE_PURCHASE_URL + "/customers", { id: null, name: newCustomerName.value, email: newCustomerEmail.value, balance: newCustomerBalance.value }); 
    customers.value = (await axios.get(import.meta.env.VITE_PURCHASE_URL + "/customers")).data;
    showCreateDialog.value = false;
    newCustomerName.value = '';
    newCustomerEmail.value = ''; 
    newCustomerBalance.value = 0;
}

onMounted(async () => { 
    customers.value = (await axios.get(import.meta.env.VITE_PURCHASE_URL + "/customers")).data;
});
</script>